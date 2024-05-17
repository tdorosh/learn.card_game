package game;

import card.*;
import player.Player;
import player.PlayerResourceNotFound;
import resource.Resource;
import resource.ResourceType;

import static resource.ResourceType.getSubType;


public class PlayerCardApplier {

    private final Player currentPlayer;
    private final Player opponentPlayer;

    public PlayerCardApplier(Player firstPlayer, Player secondPlayer) {
        currentPlayer = firstPlayer;
        opponentPlayer = secondPlayer;
    }

    public static boolean canCardBeApplied(Card card, Player player) throws PlayerResourceNotFound {
        return player.getResourceByType(getSubType(card.resourceType())).getValue() >= card.price();
    }

    public void applyCard(Card card) throws PlayerResourceNotFound {
        for(var impact : card.impacts()) {
            if(impact.hasCondition()) {
                CardImpactCondition condition = impact.condition();
                if(condition.isCurrentPlayerCondition() && cardImpactConditionToBoolean(condition, currentPlayer)) {
                    applyCardImpact(impact);
                }
                else if(condition.isOpponentPlayerCondition() && cardImpactConditionToBoolean(condition, opponentPlayer)) {
                    applyCardImpact(impact);
                }
                else if(cardImpactConditionToBoolean(condition, currentPlayer, opponentPlayer)) applyCardImpact(impact);
            }
            else applyCardImpact(impact);
        };
    }

    private void applyCardImpact(CardImpact impact) throws PlayerResourceNotFound {
        if(impact.side().equals(CardImpactSide.SELF)) handleCardImpact(impact, currentPlayer, opponentPlayer);
        else handleCardImpact(impact, opponentPlayer, currentPlayer);
    }

    private void handleCardImpact(CardImpact impact, Player firstPlayer, Player secondPlayer)
            throws PlayerResourceNotFound {
        switch (impact.type()) {
           case RESOURCE -> handleResourceCardImpact(impact, firstPlayer, secondPlayer);
           case DAMAGE -> handleDamageCardImpact(impact, firstPlayer);
        }
    }

    private void handleResourceCardImpact(CardImpact impact, Player firstPlayer, Player secondPlayer)
            throws PlayerResourceNotFound {
        Resource firstPlayerResource = firstPlayer.getResourceByType(impact.resourceType());
        Resource secondPlayerResource = secondPlayer.getResourceByType(impact.resourceType());

        switch(impact.action()) {
            case INCREASE -> firstPlayerResource.increaseValue(impact.value());
            case DECREASE -> firstPlayerResource.decreaseValue(impact.value());
            case MAKE_EQUAL_TO_OPPONENT -> firstPlayerResource.setValue(secondPlayerResource.getValue());
            case MAKE_EQUAL_TO_GREATER -> firstPlayerResource.setValue(
                Math.max(firstPlayerResource.getValue(), secondPlayerResource.getValue())
            );
            case SWAP -> {
                int firstPlayerResourceValue = firstPlayerResource.getValue();
                int secondPlayerResourceValue = secondPlayerResource.getValue();
                firstPlayerResource.setValue(secondPlayerResourceValue);
                secondPlayerResource.setValue(firstPlayerResourceValue);
            }
        }
    }

    private void handleDamageCardImpact(CardImpact impact, Player player)
            throws PlayerResourceNotFound {
        Resource wallResource = player.getResourceByType(ResourceType.WALL);
        Resource towerResource = player.getResourceByType(ResourceType.TOWER);

        if(impact.value() > wallResource.getValue()) {
            int decreaseTowerResourceValue = impact.value() - wallResource.getValue();
            wallResource.decreaseValue(impact.value());
            towerResource.decreaseValue(decreaseTowerResourceValue);
        }
        else wallResource.decreaseValue(impact.value());
    }

    public boolean cardImpactConditionToBoolean(CardImpactCondition condition, Player player)
            throws PlayerResourceNotFound {
        int comparisonValue;
        if(condition.isSingleResourceCondition()) comparisonValue = condition.value();
        else comparisonValue = player.getResourceByType(condition.secondResourceType()).getValue();
        return condition.getConditionResult(
            player.getResourceByType(condition.firstResourceType()).getValue(),
            comparisonValue
        );
    }

    public boolean cardImpactConditionToBoolean(CardImpactCondition condition, Player firstPlayer, Player secondPlayer)
            throws PlayerResourceNotFound {
        return condition.getConditionResult(
            firstPlayer.getResourceByType(condition.firstResourceType()).getValue(),
            secondPlayer.getResourceByType(condition.secondResourceType()).getValue()
        );
    }
}
