package game;

import deck.DeckFromFileInitializer;
import player.Player;


public class RunGame {
    public static void main(String[] args) {
        var settings = new GameSettings();
        var firstPlayer = new Player("Player 1");
        var secondPlayer = new Player("Player 2");
        var playerInterface = new CommandLineGameInterface();
        var deckInitializer = new DeckFromFileInitializer();
        var game = new Game(playerInterface, settings, deckInitializer, firstPlayer, secondPlayer);
        game.start();
    }
}
