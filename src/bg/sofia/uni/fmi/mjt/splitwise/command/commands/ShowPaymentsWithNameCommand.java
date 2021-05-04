package bg.sofia.uni.fmi.mjt.splitwise.command.commands;

import bg.sofia.uni.fmi.mjt.splitwise.command.CommandsBase;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.ClientNotLoggedException;
import bg.sofia.uni.fmi.mjt.splitwise.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.user.UsersRepositoryService;

import java.nio.channels.SocketChannel;

public class ShowPaymentsWithNameCommand extends CommandsBase {
    private static final String FAIL = "Failed to show payments by name.";
    private static final String NOT_LOGGED =
            "You are not logged. Please login first in order to view payments history by name.";
    private static final String EXPECTED_ARGUMENTS_COUNT = "There are expected at least 3 characters for name.";

    public ShowPaymentsWithNameCommand(UsersRepositoryService<User> users) {
        super(users);
    }

    @Override
    public String executeCommand(String argument, SocketChannel clientChannel) {
        try {
            User currentUser = users.getUser(clientChannel);
            String error = validateArgument(argument);
            if (error != null) {
                return FAIL + System.lineSeparator() + error;
            }
            return currentUser.getPaymentsWithName(argument);
        } catch (ClientNotLoggedException e) {
            return FAIL + System.lineSeparator() + NOT_LOGGED;
        }
    }

    private String validateArgument(String argument) {
        if (argument == null || argument.length() < 3) {
            return EXPECTED_ARGUMENTS_COUNT;
        }
        return null;
    }
}
