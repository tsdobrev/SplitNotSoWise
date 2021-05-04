package bg.sofia.uni.fmi.mjt.splitwise.exceptions;

public class CurrentUserIsNotAFriendException extends Exception {
    private static final String MESSAGE = "This user is not a friend.";

    public CurrentUserIsNotAFriendException() {
        super(MESSAGE);
    }
}
