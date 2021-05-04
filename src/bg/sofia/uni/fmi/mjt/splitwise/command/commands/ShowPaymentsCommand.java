package bg.sofia.uni.fmi.mjt.splitwise.command.commands;

import bg.sofia.uni.fmi.mjt.splitwise.command.CommandsBase;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.ClientNotLoggedException;
import bg.sofia.uni.fmi.mjt.splitwise.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.user.UsersRepositoryService;

import java.nio.channels.SocketChannel;

public class ShowPaymentsCommand extends CommandsBase {
    private static final String NOT_LOGGED = "Failed to show payment history."+System.lineSeparator()
            + "You are not logged. Please log in first and try again.";

    public ShowPaymentsCommand(UsersRepositoryService<User> users) {
        super(users);
    }

    @Override
    public String executeCommand(String argument, SocketChannel clientChannel) {
        try {
            return users.getUser(clientChannel).getPaymentsHistory();
        } catch (ClientNotLoggedException e) {
            return NOT_LOGGED;
        }
    }
}
