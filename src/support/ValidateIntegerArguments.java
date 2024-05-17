package support;


@FunctionalInterface
public interface ValidateIntegerArguments {
    void validate(int number, String message) throws IllegalArgumentException;
}
