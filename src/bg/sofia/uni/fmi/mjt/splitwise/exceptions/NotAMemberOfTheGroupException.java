package bg.sofia.uni.fmi.mjt.splitwise.exceptions;

public class NotAMemberOfTheGroupException extends Exception {
    private static final String MESSAGE = "You are not a member of this group.";

    public NotAMemberOfTheGroupException() {
        super(MESSAGE);
    }
}
