package bg.sofia.uni.fmi.mjt.splitwise.exceptions;

public class UserNotRegisteredException extends Exception {
    private static final String MESSAGE = "This user is not registered";

    public UserNotRegisteredException() {
        super(MESSAGE);
    }
}
