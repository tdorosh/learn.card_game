package game;

import card.*;
import deck.DeckInitializeException;
import deck.DeckInitializer;
import player.*;
import resource.*;
import deck.Deck;

import java.io.IOException;

import static card.CardAdditionalFeature.*;
import static player.PlayerCardAction.*;
import static resource.ResourceType.*;
import static resource.ResourceBoundaryValueType.*;


public final class Game {

    private Player currentPlayer;
    private final Player firstPlayer;
    private final Player secondPlayer;
    private final Deck deck;
    private final GameSettings settings;
    private final DeckInitializer deckInitializer;
    private final AbstractGameInterface playerInterface;
    private boolean isCurrentPlayerMoveCompleted;
    private boolean isCurrentPlayerActionCompleted;

    Game(AbstractGameInterface playerInterface, GameSettings settings, DeckInitializer deckInitializer,
         Player firstPlayer, Player secondPlayer) {
        this.playerInterface = playerInterface;
        this.settings = settings;
        this.deckInitializer = deckInitializer;
        this.deck = new Deck();
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    public void start() {
        try {
            initialize();
            do handlePlayerMove(); while(!isOver());
            playerInterface.showGameOverMessage(getWinnerPlayer());
        }
        catch (Exception exc) {
            playerInterface.showGameError(exc);
        }
    }

    private void initialize() throws DeckInitializeException {
        var initializer = new GameInitializer(settings, deck, deckInitializer, firstPlayer, secondPlayer);
        initializer.initialize();
        currentPlayer = firstPlayer;
        playerInterface.setPlayers(currentPlayer, getOpponentTo(currentPlayer));
    }

    private void handlePlayerMove() throws IOException, PlayerResourceNotFound {
        do { handlePlayerAction(allActions()); } while(!isCurrentPlayerMoveCompleted);
        applyMove();
    }

    private void handlePlayerAction(PlayerCardAction[] permittedActions) throws IOException, PlayerResourceNotFound {
        isCurrentPlayerMoveCompleted = false;
        isCurrentPlayerActionCompleted = false;

        playerAction: while(!isCurrentPlayerActionCompleted) {
            playerInterface.showCurrentState();
            playerInterface.showCurrentPlayer();
            Card chosenCard = playerInterface.getPlayerCard(currentPlayer);
            PlayerCardAction chosenCardAction = playerInterface.getPlayerCardAction(permittedActions);
            if(playerInterface.isPlayerInputValid(currentPlayer, chosenCard, chosenCardAction, permittedActions)) {
                switch(chosenCardAction) {
                    case APPLY:
                        applyCard(chosenCard);
                        applyAction(chosenCard);
                        if(isOver()) break playerAction;
                        handleCardAdditionalFeatures(chosenCard);
                        break;
                    case DISCARD: applyAction(chosenCard);
                }
            }
        }
    }

    private void applyMove() throws PlayerResourceNotFound {
        increasePlayerSecondaryResources(getOpponentTo(currentPlayer));
        currentPlayer = getOpponentTo(currentPlayer);
        playerInterface.setPlayers(currentPlayer, getOpponentTo(currentPlayer));
    }

    public void applyAction(Card card) {
        deck.putUnderneath(card);
        currentPlayer.addCard(deck.get(), currentPlayer.getCardIndex(card));
        isCurrentPlayerActionCompleted = true;
        isCurrentPlayerMoveCompleted = true;
    }

    private void applyCard(Card card) throws PlayerResourceNotFound {
        decreasePlayerSecondaryResources(currentPlayer, card);
        var cardApplier = new PlayerCardApplier(currentPlayer, getOpponentTo(currentPlayer));
        cardApplier.applyCard(card);
    }

    private void handleCardAdditionalFeatures(Card card) throws IOException, PlayerResourceNotFound {
        if(card.hasAdditionalFeature(DISCARD_AND_PLAY_AGAIN)) {
            handlePlayerAction(discardActions());
            handlePlayerAction(allActions());
        }
        else if(card.hasAdditionalFeature(PLAY_AGAIN)) handlePlayerAction(allActions());
    }

    private void increasePlayerSecondaryResources(Player player) throws PlayerResourceNotFound {
        for(var resource : player.getResources()) {
            ResourceType subType = getSubType(resource.getType());
            if(subType != null) player.getResourceByType(subType).increaseValue(resource.getValue());
        }
    }

    private void decreasePlayerSecondaryResources(Player player, Card card) throws PlayerResourceNotFound {
        player.getResourceByType(getSubType(card.resourceType())).decreaseValue(card.price());
    }

    private boolean isOver() {
        return playerBoundaryValuesViolated(currentPlayer)
            || playerBoundaryValuesViolated(getOpponentTo(currentPlayer));
    }

    private Player getOpponentTo(Player player) {
        if(player == firstPlayer) return secondPlayer;
        else return firstPlayer;
    }

    private Player getWinnerPlayer() {
        if(isPlayerWon(firstPlayer)) return firstPlayer;
        if(isPlayerWon(secondPlayer)) return secondPlayer;
        return null;
    }

    private boolean isPlayerWon(Player player) {
        return isPlayerResourceAchieveBoundaryValue(player, UPPER)
            || isPlayerResourceAchieveBoundaryValue(getOpponentTo(player), LOWER);
    }

    private boolean playerBoundaryValuesViolated(Player player) {
        return isPlayerResourceAchieveBoundaryValue(player, LOWER)
            || isPlayerResourceAchieveBoundaryValue(player, UPPER);
    }

    private boolean isPlayerResourceAchieveBoundaryValue(Player player, ResourceBoundaryValueType boundaryValueType) {
        for(var resource : player.getResources()) {
            for(var boundaryValue : settings.resourceBoundaryValues()) {
                if((resource.getType().equals(boundaryValue.resourceType())) && boundaryValue.type().equals(boundaryValueType)) {
                    return switch (boundaryValueType) {
                        case LOWER -> resource.getValue() <= boundaryValue.value();
                        case UPPER -> resource.getValue() >= boundaryValue.value();
                    };
                }
            }
        }
        return false;
    }
}
