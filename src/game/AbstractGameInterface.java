package game;

import card.Card;
import player.*;

import java.io.IOException;


public abstract class AbstractGameInterface {

    Player currentPlayer;
    Player opponentPlayer;

    public abstract Card getPlayerCard(Player player) throws IOException;
    public abstract PlayerCardAction getPlayerCardAction(PlayerCardAction[] permittedActions) throws IOException;
    public abstract boolean isPlayerInputValid(
        Player player, Card card, PlayerCardAction action, PlayerCardAction[] permittedActions
    ) throws PlayerResourceNotFound;
    public abstract void showPlayerCardInputIsNotValid();
    public abstract void showCardCanNotBeApplied();
    public abstract void showPlayerActionIsNotAllowed();
    public abstract void showChooseAction();
    public abstract void showGameOverMessage(Player player);

    public void setPlayers(Player firstPlayer, Player secondPlayer) {
        currentPlayer = firstPlayer;
        opponentPlayer = secondPlayer;
    }

    public void showCurrentState() throws PlayerResourceNotFound {
        showPlayerResources(currentPlayer);
        showPlayerResources(opponentPlayer);
        showPlayerCards(currentPlayer);
    }
    abstract void showCurrentPlayer();
    abstract void showPlayerResources(Player player) throws PlayerResourceNotFound;
    abstract void showPlayerCards(Player player) throws PlayerResourceNotFound;
    abstract void showGameError(Exception exc);
}
