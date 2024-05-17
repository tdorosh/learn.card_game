package deck;

import card.*;
import java.util.Collections;
import java.util.ArrayList;


public class Deck {

    private final ArrayList<Card> deck;

    public Deck() {
        this.deck = new ArrayList<>();
    }

    public void putUnderneath(Card card) {
        deck.add(card);
    }

    public Card get() {
        Card card = deck.get(0);
        deck.remove(0);
        return card;
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }
}
