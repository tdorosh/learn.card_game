package player;


public enum PlayerCardAction {
    APPLY, DISCARD;

    public static boolean isActionAllowed(PlayerCardAction[] permittedActions, PlayerCardAction playerCardAction) {
        for(var action : permittedActions) {
            if(action.equals(playerCardAction)) return true;
        }
        return false;
    }

    public static PlayerCardAction[] allActions() {
        return PlayerCardAction.values();
    }

    public static PlayerCardAction[] discardActions() {
        return new PlayerCardAction[] { DISCARD };
    }
}
