package bg.sofia.uni.fmi.mjt.splitwise.command.commands;

import bg.sofia.uni.fmi.mjt.splitwise.command.CommandsBase;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.ClientNotLoggedException;
import bg.sofia.uni.fmi.mjt.splitwise.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.user.UsersRepositoryService;

import java.nio.channels.SocketChannel;

public class GetStatusCommand extends CommandsBase {
    private static final String NOT_LOGGED = "Filed to get the status." + System.lineSeparator()
            +  "You are not logged. Log in first in order to view your status.";

    public GetStatusCommand(UsersRepositoryService<User> users) {
        super(users);
    }

    @Override
    public String executeCommand(String argument, SocketChannel clientChannel) {
        try {
            return users.getUser(clientChannel).getStatus();
        } catch (ClientNotLoggedException e) {
            return NOT_LOGGED;
        }
    }
}
