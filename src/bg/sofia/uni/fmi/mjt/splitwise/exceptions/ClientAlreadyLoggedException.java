package bg.sofia.uni.fmi.mjt.splitwise.exceptions;

public class ClientAlreadyLoggedException extends Exception {
    private static final String MESSAGE = "Current user is already logged.";

    public ClientAlreadyLoggedException() {
        super(MESSAGE);
    }
}
