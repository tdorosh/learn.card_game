package player;

import card.Card;
import resource.Resource;
import resource.ResourceType;


public final class Player {
    private final String name;
    private Resource[] resources;
    private Card[] cards;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Resource[] getResources() {
        return resources;
    }

    public void setResources(Resource[] resources) {
        this.resources = resources;
    }

    public Card[] getCards() {
        return cards;
    }

    public void setCards(Card[] cards) {
        this.cards = cards;
    }

    public void addCard(Card card, int index) {
        cards[index] = card;
    }

    public int getCardIndex(Card card) {
        for(int i = 0; i < cards.length; i++) {
            if(cards[i] == card) return i;
        }
        return -1;
    }

    public Resource getResourceByType(ResourceType resourceType) throws PlayerResourceNotFound {
        for(var resource : resources) {
            if(resource.getType().equals(resourceType)) return resource;
        }
        throw new PlayerResourceNotFound();
    }

    @Override
    public String toString() {
        return name;
    }
}
