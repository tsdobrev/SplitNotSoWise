package bg.sofia.uni.fmi.mjt.splitwise.command.commands;

import bg.sofia.uni.fmi.mjt.splitwise.command.CommandExecutable;
import java.nio.channels.SocketChannel;

public class UnknownCommand implements CommandExecutable {
    private static final String MESSAGE = "Unknown command. Please check your input and try again.";

    @Override
    public String executeCommand(String argument, SocketChannel clientChannel) {
        return MESSAGE;
    }
}
