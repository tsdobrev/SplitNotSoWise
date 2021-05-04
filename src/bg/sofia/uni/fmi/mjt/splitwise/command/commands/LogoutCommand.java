package bg.sofia.uni.fmi.mjt.splitwise.command.commands;

import bg.sofia.uni.fmi.mjt.splitwise.command.CommandsBase;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.ClientNotLoggedException;
import bg.sofia.uni.fmi.mjt.splitwise.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.user.UsersRepositoryService;

import java.nio.channels.SocketChannel;

public class LogoutCommand extends CommandsBase {
    private static final String SUCCESS = "Successfully logged out.";
    private static final String NOT_LOGGED = "You are not logged.";

    public LogoutCommand(UsersRepositoryService<User> users) {
        super(users);
    }

    @Override
    public String executeCommand(String argument, SocketChannel clientChannel) {
        try {
            users.logout(clientChannel);
            return SUCCESS;
        } catch (ClientNotLoggedException e) {
            return NOT_LOGGED;
        }
    }
}
