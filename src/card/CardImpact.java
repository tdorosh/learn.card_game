package card;

import resource.ResourceType;
import static support.SupportFunctions.isNegative;


public record CardImpact (
    CardImpactType type,
    ResourceType resourceType,
    CardImpactSide side,
    CardImpactAction action,
    CardImpactCondition condition,
    int value
) {

    public CardImpact {
        isNegative.validate(value, "Card impact value cannot be less than 0");
    }

    public boolean hasCondition() {
        return condition != null;
    }
}
