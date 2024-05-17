package deck;

import card.*;
import org.w3c.dom.*;
import resource.ResourceType;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;


public class DeckFromFileInitializer implements DeckInitializer {

    public void initialize(Deck deck) throws DeckInitializeException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new File("src/deck/CARDS.XML"));
            NodeList cards = document.getDocumentElement().getElementsByTagName("card");
            for(int i = 0; i < cards.getLength(); i++) deck.putUnderneath(getCardFromCardItem(cards.item(i)));
        }
        catch(Exception exc) {
            throw new DeckInitializeException("Error during initializing deck from file: " + exc);
        }
    }

    private Card getCardFromCardItem(Node cardItem) {
        NamedNodeMap cardItemAttributes = cardItem.getAttributes();
        return new Card(
            ResourceType.valueOf(getItemAttributeValue(cardItemAttributes,"resource")),
            getItemAttributeValue(cardItemAttributes,"title"),
            getItemAttributeValue(cardItemAttributes,"description"),
            Integer.parseInt(getItemAttributeValue(cardItemAttributes,"price")),
            getCardImpactsFromCardItem((Element) cardItem),
            getCardFeaturesFromCardItem((Element) cardItem)
        );
    }

    private CardImpact[] getCardImpactsFromCardItem(Element cardItem) {
        NodeList impacts = cardItem.getElementsByTagName("impact");
        var cardImpacts = new CardImpact[impacts.getLength()];

        for(int i = 0; i < impacts.getLength(); i++) {
            Node impactItem = impacts.item(i);
            NamedNodeMap impactItemAttributes = impactItem.getAttributes();
            var impactType = CardImpactType.valueOf(getItemAttributeValue(impactItemAttributes, "type"));
            var impactResource = ResourceType.valueOf(getItemAttributeValue(impactItemAttributes, "resource"));
            var impactSide = CardImpactSide.valueOf(getItemAttributeValue(impactItemAttributes, "side"));
            var impactAction = CardImpactAction.valueOf(getItemAttributeValue(impactItemAttributes, "action"));
            CardImpactCondition impactCondition = getCardImpactConditionFromImpactItem((Element) impactItem);
            String impactValue = getItemAttributeValue(impactItemAttributes, "value");

            if(impactValue.isEmpty()) cardImpacts[i] = new CardImpact(
                    impactType, impactResource, impactSide, impactAction, impactCondition, 0);
            else cardImpacts[i] = new CardImpact(
                    impactType, impactResource, impactSide, impactAction, impactCondition, Integer.parseInt(impactValue));

        }
        return cardImpacts;
    }

    private CardAdditionalFeature[] getCardFeaturesFromCardItem(Element cardItem) {
        NodeList features = cardItem.getElementsByTagName("feature");
        var cardFeatures = new CardAdditionalFeature[features.getLength()];

        for(int i = 0; i < features.getLength(); i++) {
            Node featureItem = features.item(i);
            cardFeatures[i] = CardAdditionalFeature.valueOf(
                getItemAttributeValue(featureItem.getAttributes(), "name")
            );
        }
        return cardFeatures;
    }

    private CardImpactCondition getCardImpactConditionFromImpactItem(Element impactItem) {
        Node conditionItem = impactItem.getElementsByTagName("condition").item(0);
        if(conditionItem == null) return null;
        NamedNodeMap conditionItemAttributes = conditionItem.getAttributes();
        var conditionValue = CardImpactConditionValue.valueOf(
                getItemAttributeValue(conditionItemAttributes,"conditionValue"));
        String value = getItemAttributeValue(conditionItemAttributes, "value");
        ResourceType conditionFirstResource = ResourceType.valueOf(
                getItemAttributeValue(conditionItemAttributes,"firstResource"));
        CardImpactSide conditionFirstResourceSide = CardImpactSide.valueOf(
                getItemAttributeValue(conditionItemAttributes,"firstResourceSide"));
        if(value.isEmpty()) return new CardImpactCondition(
            conditionFirstResource,
            conditionFirstResourceSide,
            ResourceType.valueOf(getItemAttributeValue(conditionItemAttributes,"secondResource")),
            CardImpactSide.valueOf(getItemAttributeValue(conditionItemAttributes,"secondResourceSide")),
            conditionValue
        );
        else return new CardImpactCondition(
            conditionFirstResource, conditionFirstResourceSide, Integer.parseInt(value), conditionValue
        );
    }

    private String getItemAttributeValue(NamedNodeMap itemAttributes, String attributeName) {
        Node item = itemAttributes.getNamedItem(attributeName);
        if(item != null) return item.getNodeValue();
        return "";
    }
}
