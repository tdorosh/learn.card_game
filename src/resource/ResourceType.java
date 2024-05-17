package resource;


public enum ResourceType {
    MINE, MONASTERY, BARRACKS, ORE, MANA, SQUADS, TOWER, WALL;


    public static ResourceType getSubType(ResourceType resourceType) {
        return switch(resourceType) {
            case MINE -> ORE;
            case MONASTERY -> MANA;
            case BARRACKS -> SQUADS;
            default -> null;
        };
    }
}
