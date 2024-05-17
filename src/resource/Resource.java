package resource;

import static support.SupportFunctions.isNegative;


public final class Resource {
    private final ResourceType type;
    private int value;

    public Resource(ResourceType type, int value) {
        isNegative.validate(value, "Resource value cannot be less than 0");
        this.type = type;
        this.value = value;
    }

    public ResourceType getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void decreaseValue(int value) {
        isNegative.validate(value, "Decrease value cannot be less than 0");
        int result = this.value - value;
        this.value = Math.max(result, 0);
    }

    public void increaseValue(int value) {
        isNegative.validate(value, "Increase value cannot be less than 0");
        this.value += value;
    }

    @Override
    public String toString() {
        return type + ": " + value;
    }
}
