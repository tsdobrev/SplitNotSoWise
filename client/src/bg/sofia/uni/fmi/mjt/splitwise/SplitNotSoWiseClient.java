package bg.sofia.uni.fmi.mjt.splitwise;

import bg.sofia.uni.fmi.mjt.splitwise.utilities.Menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SplitNotSoWiseClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 6666;

    public void start() {
        try (SocketChannel socketChannel = SocketChannel.open();
             BufferedReader reader = new BufferedReader(Channels.newReader(socketChannel, StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(Channels.newWriter(socketChannel, StandardCharsets.UTF_8), true);
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

            System.out.println("Connected to the server.");

            new Thread(new ServerRespondListener(reader)).start();

            while (true) {
                String message = scanner.nextLine();
                if (message.equals("help")) {
                    System.out.println(Menu.HELP);
                    continue;
                }
                if (message.equals("disconnect")) {
                    break;
                }
                writer.println(message);
            }

            System.out.println("You are disconnected.");
        } catch (IOException e) {
            System.err.println("There is a problem with the network communication. Try again later." + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new SplitNotSoWiseClient().start();
    }
}