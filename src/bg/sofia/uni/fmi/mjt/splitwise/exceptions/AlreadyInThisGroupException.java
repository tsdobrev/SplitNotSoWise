package bg.sofia.uni.fmi.mjt.splitwise.exceptions;

public class AlreadyInThisGroupException extends Exception {
    private static final String MESSAGE = "Current user is already a member of this group.";

    public AlreadyInThisGroupException() {
        super(MESSAGE);
    }
}
