package bg.sofia.uni.fmi.mjt.splitwise.command.commands;

import bg.sofia.uni.fmi.mjt.splitwise.command.CommandsBase;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.ClientAlreadyLoggedException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.UserNotRegisteredException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.WrongNamePasswordCombinationException;
import bg.sofia.uni.fmi.mjt.splitwise.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.user.UserAuthenticationInfo;
import bg.sofia.uni.fmi.mjt.splitwise.user.UsersRepositoryService;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginCommand extends CommandsBase {
    private static final String FAIL = "Failed to login.";
    private static final String CORRECT_FORMAT =
            "Please provide username and password correctly as follows" + System.lineSeparator()
                    + "login username password";
    private static final String EXPECTED_ARGUMENTS_COUNT = "There are expected 2 arguments.";
    private static final String LOGIN_FAIL_ALREADY_LOGGED = "You are already logged.";
    private static final String SUCCESSFUL_LOGIN = "You are logged in.";
    private static final String NOT_REGISTERED = "You are not registered.";

    public LoginCommand(UsersRepositoryService<User> users) {
        super(users);
    }

    @Override
    public String executeCommand(String argument, SocketChannel clientChannel) {
        if (users.isLogged(clientChannel)) {
            return LOGIN_FAIL_ALREADY_LOGGED;
        }
        List<String> arguments = getArguments(argument);
        String error = validateArguments(arguments);
        if (error != null) {
            System.err.println(error);
            return FAIL + System.lineSeparator() + error;
        }

        String name = arguments.get(0);
        String pass = arguments.get(1);
        try {
            users.login(new UserAuthenticationInfo(name, pass), clientChannel);
            String userNotifications = users.getUser(name).getNotifications();
            users.getUser(name).cleatNotifications();
            return SUCCESSFUL_LOGIN + System.lineSeparator() + userNotifications;
        } catch (ClientAlreadyLoggedException e) {
            System.err.println(LOGIN_FAIL_ALREADY_LOGGED);
            return LOGIN_FAIL_ALREADY_LOGGED;
        } catch (WrongNamePasswordCombinationException e) {
            System.err.println(e.getMessage());
            return FAIL + System.lineSeparator() + e.getMessage();
        } catch (UserNotRegisteredException e) {
            return NOT_REGISTERED;
        }
    }

    private String validateArguments(List<String> arguments) {
        if (arguments == null) {
            return CORRECT_FORMAT;
        }
        if (arguments.size() != 2) {
            return EXPECTED_ARGUMENTS_COUNT + System.lineSeparator() + CORRECT_FORMAT;
        }
        return null;
    }

    private List<String> getArguments(String argument) {
        String[] tokens = argument.split(" ", 3);
        return new ArrayList<>(Arrays.asList(tokens));
    }
}
