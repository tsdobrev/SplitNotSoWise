package bg.sofia.uni.fmi.mjt.splitwise.exceptions;

public class WrongNamePasswordCombinationException extends Exception {
    private static final String MESSAGE = "Wrong name or password. Please, try again.";

    public WrongNamePasswordCombinationException() {
        super(MESSAGE);
    }
}
