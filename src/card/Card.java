package card;

import resource.ResourceType;
import static support.SupportFunctions.isNegative;


public record Card (
    ResourceType resourceType,
    String title,
    String description,
    int price,
    CardImpact[] impacts,
    CardAdditionalFeature[] additionalFeatures
) {

    public Card {
        isNegative.validate(price, "Card price cannot be less than 0");
    }

    public boolean hasAdditionalFeature(CardAdditionalFeature additionalFeature) {
        for(var feature : additionalFeatures) {
            if(feature.equals(additionalFeature)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return title + " " + description;
    }
}
