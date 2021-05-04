package bg.sofia.uni.fmi.mjt.splitwise.exceptions;

public class InvalidGroupMembersCountException extends Exception {
    private static final String MESSAGE = "Group members should be at least 3 people";

    public InvalidGroupMembersCountException() {
        super(MESSAGE);
    }
}
