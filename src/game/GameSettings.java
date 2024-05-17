package game;

import resource.ResourceBoundaryValue;
import static resource.ResourceBoundaryValueType.*;
import static resource.ResourceType.*;


public record GameSettings (
    int playerCardCount,
    int initialTowerValue,
    int initialWallValue,
    int initialMainResourceValue,
    int initialSecondaryResourceValue,
    ResourceBoundaryValue[] resourceBoundaryValues
) {
    public final static int defaultPlayerCardCount = 6;
    public final static int defaultInitialTowerValue = 20;
    public final static int defaultInitialWallValue = 5;
    public final static int defaultInitialMainResourceValue = 2;
    public final static int defaultInitialSecondaryResourceValue = 5;

    GameSettings() {
        this(
            defaultPlayerCardCount,
            defaultInitialTowerValue,
            defaultInitialWallValue,
            defaultInitialMainResourceValue,
            defaultInitialSecondaryResourceValue,
            getDefaultResourceBoundaryValues()
        );
    }

    public static ResourceBoundaryValue[] getDefaultResourceBoundaryValues() {
        return new ResourceBoundaryValue[] {
            new ResourceBoundaryValue(TOWER, LOWER, 0),
            new ResourceBoundaryValue(TOWER, UPPER, 50),
        };
    }
}
