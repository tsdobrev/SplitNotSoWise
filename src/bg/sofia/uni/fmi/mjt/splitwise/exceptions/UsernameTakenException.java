package bg.sofia.uni.fmi.mjt.splitwise.exceptions;

public class UsernameTakenException extends Exception {
    private static final String MESSAGE = "This username is already taken. Choose another one.";

    public UsernameTakenException() {
        super(MESSAGE);
    }
}
