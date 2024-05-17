package game;

import card.Card;
import player.*;
import static game.PlayerCardApplier.canCardBeApplied;
import static player.PlayerCardAction.*;
import static resource.ResourceType.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;


public class CommandLineGameInterface extends AbstractGameInterface {

    PrintWriter messageDisplayer;

    public CommandLineGameInterface() {
        super();
        messageDisplayer = new PrintWriter(System.out, true);
    }

    @Override
    public Card getPlayerCard(Player player) throws IOException {
        try {
            Card card = player.getCards()[getPlayerCardInput()];
            messageDisplayer.println("Chosen card: " + card);
            return card;
        }
        catch(ArrayIndexOutOfBoundsException exc) {
            return null;
        }
    }

    @Override
    public PlayerCardAction getPlayerCardAction(PlayerCardAction[] permittedActions) throws IOException {
        var cardAction = switch(getPlayerCardActionInput(permittedActions)) {
            case "a" -> APPLY;
            case "d" -> DISCARD;
            default -> null;
        };
        messageDisplayer.println("Chosen action: " + cardAction);
        return cardAction;
    }

    @Override
    public boolean isPlayerInputValid(Player player, Card card, PlayerCardAction action, PlayerCardAction[] permittedActions)
            throws PlayerResourceNotFound {
        if(card == null) {
            showPlayerCardInputIsNotValid();
            return false;
        }
        if(action == null) {
            showChooseAction();
            return false;
        };
        if(!isActionAllowed(permittedActions, action)) {
            showPlayerActionIsNotAllowed();
            return false;
        }
        if(action.equals(APPLY) && !canCardBeApplied(card, player)) {
            showCardCanNotBeApplied();
            return false;
        }
        return true;
    }

    private int getPlayerCardInput() throws IOException {
        messageDisplayer.println("Choose card number: ");
        var playerInput = new BufferedReader(new InputStreamReader(System.in));
        String number = playerInput.readLine();
        return Integer.parseInt(number) - 1;
    }

    private String getPlayerCardActionInput(PlayerCardAction[] permittedActions) throws IOException {
        var playerInput = new BufferedReader(new InputStreamReader(System.in));
        String message = "";
        if(isActionAllowed(permittedActions, APPLY)) message += "Choose 'a' for apply. ";
        if(isActionAllowed(permittedActions, DISCARD)) message += "Choose 'd' for discard.";
        messageDisplayer.println(message);
        return playerInput.readLine();
    }

    @Override
    public void showPlayerResources(Player player) throws PlayerResourceNotFound {
        messageDisplayer.println(player.getName() + " resources: ");
        messageDisplayer.println("\tTower: " + player.getResourceByType(TOWER).getValue());
        messageDisplayer.println("\tWall: " + player.getResourceByType(WALL).getValue());
        messageDisplayer.println("\tMine: " + player.getResourceByType(MINE).getValue());
        messageDisplayer.println("\t\tOre: " + player.getResourceByType(ORE).getValue());
        messageDisplayer.println("\tMonastery: " + player.getResourceByType(MONASTERY).getValue());
        messageDisplayer.println("\t\tMana: " + player.getResourceByType(MANA).getValue());
        messageDisplayer.println("\tBarracks: " + player.getResourceByType(BARRACKS).getValue());
        messageDisplayer.println("\t\tSquads: " + player.getResourceByType(SQUADS).getValue());
        messageDisplayer.println();
    }

    @Override
    public void showPlayerCards(Player player) throws PlayerResourceNotFound {
        messageDisplayer.println(player.getName() + " cards: ");
        var playerCards = player.getCards();
        for(int i = 0; i < playerCards.length; i++ ) {
            messageDisplayer.println("Number: " + (i + 1)
                + " Resource type: " + playerCards[i].resourceType()
                + " Title: " + playerCards[i].title()
                + " Description: " + playerCards[i].description()
                + " Price: " + playerCards[i].price()
                + " Can be applied: " + canCardBeApplied(playerCards[i], player));
        }
        messageDisplayer.println();
    }

    @Override
    public void showCurrentPlayer() {
        messageDisplayer.println("It's the " + currentPlayer.getName() + "'s" + " turn");
    }

    @Override
    public void showGameOverMessage(Player player) {
        messageDisplayer.println(player.getName() + " won.");
    }

    @Override
    public void showPlayerCardInputIsNotValid() {
        messageDisplayer.println("Chosen card number is incorrect.");
    }

    @Override
    public void showCardCanNotBeApplied() {
        messageDisplayer.println("This card cannot be applied.");
    }

    @Override
    public void showPlayerActionIsNotAllowed() {
        messageDisplayer.println("Action is not allowed.");
    }

    @Override
    public void showChooseAction() {
        messageDisplayer.println("Choose 'Apply' or 'Discard'");
    }

    @Override
    public void showGameError(Exception exc) {
        messageDisplayer.println("Error was occurred: " + exc);
    }
}
