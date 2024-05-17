package card;

import resource.ResourceType;


public record CardImpactCondition (
    ResourceType firstResourceType,
    CardImpactSide firstResourceSide,
    ResourceType secondResourceType,
    CardImpactSide secondResourceSide,
    int value,
    CardImpactConditionValue conditionValue
) {


    public CardImpactCondition(
        ResourceType firstResourceType,
        CardImpactSide firstResourceSide,
        ResourceType secondResourceType,
        CardImpactSide secondResourceSide,
        CardImpactConditionValue conditionValue
    ) {
        this(firstResourceType, firstResourceSide, secondResourceType, secondResourceSide, -1, conditionValue);
    }

    public CardImpactCondition(
        ResourceType firstResourceType,
        CardImpactSide firstResourceSide,
        int value,
        CardImpactConditionValue conditionValue
    ) {
        this(firstResourceType, firstResourceSide, null, null, value, conditionValue);
    }

    public boolean isSingleResourceCondition() {
        return secondResourceType == null && secondResourceSide == null && value >= 0;
    }

    private boolean isSinglePlayerCondition() {
        return firstResourceSide.equals(secondResourceSide) || value >= 0;
    }

    public boolean isCurrentPlayerCondition() {
        return isSinglePlayerCondition() && firstResourceSide.equals(CardImpactSide.SELF);
    }

    public boolean isOpponentPlayerCondition() {
        return isSinglePlayerCondition() && firstResourceSide.equals(CardImpactSide.OPPONENT);
    }

    public boolean getConditionResult(int firstValue, int comparisonValue) {
        return switch(conditionValue) {
            case GREATER_THAN -> firstValue > comparisonValue;
            case LESS_THAN -> firstValue < comparisonValue;
            case EQUAL -> firstValue == comparisonValue;
            case GREATER_THAN_OR_EQUAL -> firstValue >= comparisonValue;
            case LESS_THAN_OR_EQUAL -> firstValue <= comparisonValue;
        };
    }
}
