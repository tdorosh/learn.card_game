package game;

import card.Card;
import deck.Deck;
import deck.DeckInitializeException;
import deck.DeckInitializer;
import player.Player;
import resource.Resource;
import static resource.ResourceType.*;


public class GameInitializer {
    private final GameSettings settings;
    private final Player firstPlayer;
    private final Player secondPlayer;
    private final Deck deck;

    private final DeckInitializer deckInitializer;

    GameInitializer(GameSettings settings, Deck deck, DeckInitializer deckInitializer, Player firstPlayer, Player secondPlayer) {
        this.settings = settings;
        this.deck = deck;
        this.deckInitializer = deckInitializer;
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    public void initialize() throws DeckInitializeException {
        initializeDeck();
        initializePlayerCards(firstPlayer);
        initializePlayerCards(secondPlayer);
        initializePlayerResources(firstPlayer, true);
        initializePlayerResources(secondPlayer, false);
    }

    private void initializeDeck() throws DeckInitializeException {
        deckInitializer.initialize(deck);
        deck.shuffle();
    }

    private void initializePlayerCards(Player player) {
        var cards = new Card[settings.playerCardCount()];
        for(int i = 0; i < cards.length; i++) {
            cards[i] = deck.get();
        }
        player.setCards(cards);
    }

    private void initializePlayerResources(Player player, boolean isFirstPlayer) {
        int secondaryResourceValue;
        int initialSecondaryResourceValue = settings.initialSecondaryResourceValue();
        int mainResourceValue = settings.initialMainResourceValue();

        if(isFirstPlayer) secondaryResourceValue = initialSecondaryResourceValue + mainResourceValue;
        else secondaryResourceValue = initialSecondaryResourceValue;

        var resources = new Resource[] {
            new Resource(MINE, mainResourceValue),
            new Resource(MONASTERY, mainResourceValue),
            new Resource(BARRACKS, mainResourceValue),
            new Resource(ORE, secondaryResourceValue),
            new Resource(MANA, secondaryResourceValue),
            new Resource(SQUADS, secondaryResourceValue),
            new Resource(TOWER, settings.initialTowerValue()),
            new Resource(WALL, settings.initialWallValue())
        };
        player.setResources(resources);
    }
}
