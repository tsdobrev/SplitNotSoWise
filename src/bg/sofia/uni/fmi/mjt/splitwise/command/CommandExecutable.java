package bg.sofia.uni.fmi.mjt.splitwise.command;

import java.nio.channels.SocketChannel;

public interface CommandExecutable {
    String executeCommand(String argument, SocketChannel clientChannel);
}
