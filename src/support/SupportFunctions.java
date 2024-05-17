package support;


public class SupportFunctions {
    public static ValidateIntegerArguments isNegative = (number, message) -> {
        if(number < 0) throw new IllegalArgumentException(message);
    };
}
