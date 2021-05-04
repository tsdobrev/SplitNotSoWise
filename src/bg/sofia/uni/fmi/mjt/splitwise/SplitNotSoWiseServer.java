package bg.sofia.uni.fmi.mjt.splitwise;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.ClientDisconnectedException;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class SplitNotSoWiseServer implements Runnable {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 6666;
    private static final int BUFFER_SIZE = 1024;

    private Selector selector;
    private final ByteBuffer buffer;
    private boolean isServerActive = false;

    private final SplitNotSoWiseCore splitNotSoWise;

    public SplitNotSoWiseServer() {
        this.buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        this.splitNotSoWise = new SplitNotSoWiseCore();

    }

    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            selector = Selector.open();
            configureServerSocketChannel(serverSocketChannel, selector);
            isServerActive = true;

            while (isServerActive) {
                try {
                    int readyChannels = selector.select();
                    if (!isServerActive) {
                        break;
                    }
                    if (readyChannels == 0) {
                        continue;
                    }
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();

                        if (key.isReadable()) {
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            handleKeyIsReadable(clientChannel);
                        } else if (key.isAcceptable()) {
                            accept(selector, key);
                        }
                        keyIterator.remove();
                    }
                } catch (IOException e) {
                    System.err.println("Error occurred while processing client request: " + e.getMessage());
                }
            }
            splitNotSoWise.backUpUsers();
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to start server", e);
        }
    }

    public void stop() {
        isServerActive = false;
        if (selector.isOpen()) {
            selector.wakeup();
        }
    }

    private void configureServerSocketChannel(ServerSocketChannel channel, Selector selector) throws IOException {
        channel.bind(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private String getClientInput(SocketChannel clientChannel) throws ClientDisconnectedException {
        buffer.clear();
        try {
            int readBytes = clientChannel.read(buffer);
            if (readBytes < 0) {
                throw new ClientDisconnectedException(); // if client disconnects correctly
            }
        } catch (IOException e) {
            throw new ClientDisconnectedException(); // if client terminates his program, read throws IOException
        }

        buffer.flip();
        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8); //.replace(System.lineSeparator(), "");
    }

    private void writeClientOutput(SocketChannel clientChannel, String output) throws ClientDisconnectedException {
        buffer.clear();
        buffer.put(output.getBytes());
        buffer.flip();

        try {
            clientChannel.write(buffer);
        } catch (IOException e) {
            throw new ClientDisconnectedException();
        }
    }

    private void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();
        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
    }

    private void handleKeyIsReadable(SocketChannel clientChannel) throws IOException {
        String clientInput;
        try {
            clientInput = getClientInput(clientChannel);
            System.out.println(clientInput);

            String response = splitNotSoWise.handleInput(clientInput, clientChannel) + System.lineSeparator();

            writeClientOutput(clientChannel, response);
        } catch (ClientDisconnectedException e) { // when user ends his connection with the server
            splitNotSoWise.logoutUser(clientChannel);
            clientChannel.close();
        }
    }

    public static void main(String[] args) throws IOException {
        SplitNotSoWiseServer server = new SplitNotSoWiseServer();
        Thread thread = new Thread(server);
        thread.start();
        System.in.read();
        server.stop();
    }

    @Override
    public void run() {
        start();
    }
}
