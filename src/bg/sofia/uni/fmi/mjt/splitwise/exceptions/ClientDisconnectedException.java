package bg.sofia.uni.fmi.mjt.splitwise.exceptions;

public class ClientDisconnectedException extends Exception{
    private static final String MESSAGE = "Client disconnected";

    public ClientDisconnectedException() {
        super(MESSAGE);
    }

}
