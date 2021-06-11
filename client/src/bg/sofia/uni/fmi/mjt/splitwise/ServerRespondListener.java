package bg.sofia.uni.fmi.mjt.splitwise;

import java.io.BufferedReader;
import java.io.IOException;

public class ServerRespondListener implements Runnable {
    private final BufferedReader reader;

    public ServerRespondListener(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public void run() {
        String line;
        while (true) {
            try {
                if ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                System.err.println("You lost your connection with server : " + e.getMessage());
                break;
            }
        }
    }
}