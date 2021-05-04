package bg.sofia.uni.fmi.mjt.splitwise.exceptions;

public class UserAlreadyFriendException extends Exception {
    private static final String MESSAGE = "This user is already a friend of yours.";

    public UserAlreadyFriendException() {
        super(MESSAGE);
    }
}
