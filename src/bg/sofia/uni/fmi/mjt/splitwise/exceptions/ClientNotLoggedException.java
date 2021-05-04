package bg.sofia.uni.fmi.mjt.splitwise.exceptions;

public class ClientNotLoggedException extends Exception {
    private static final String MESSAGE = "You are not logged";

    public ClientNotLoggedException() {
        super(MESSAGE);
    }
}
