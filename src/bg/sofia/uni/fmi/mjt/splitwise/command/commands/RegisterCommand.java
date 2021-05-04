package bg.sofia.uni.fmi.mjt.splitwise.command.commands;

import bg.sofia.uni.fmi.mjt.splitwise.command.CommandsBase;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.ClientAlreadyLoggedException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.UsernameTakenException;
import bg.sofia.uni.fmi.mjt.splitwise.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.user.UsersRepositoryService;
import bg.sofia.uni.fmi.mjt.splitwise.utilities.NameValidator;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterCommand extends CommandsBase {
    private static final String FAIL = "Failed to register.";
    private static final String CORRECT_FORMAT =
            "Please provide username, password and nickname correctly as follows" + System.lineSeparator()
                    + "register username password nickname";
    private static final String EXPECTED_ARGUMENTS_COUNT = "There are expected 3 arguments.";
    private static final String PASSWORD_LENGTH =
            "Password should be at least 4 characters-long and cannot have whitespaces.";
    private static final String NICKNAME_LENGTH = "Nickname should be at least 1 characters-long.";
    private static final String REGISTRATION_FAIL_ALREADY_LOGGED =
            "You are already registered and logged. In order to make registration You have to logout first.";
    private static final String SUCCESSFUL_REGISTRATION
            = "You have been successfully registered and logged in automatically.";


    public RegisterCommand(UsersRepositoryService<User> users) {
        super(users);
    }

    @Override
    public String executeCommand(String argument, SocketChannel clientChannel) {
        if (users.isLogged(clientChannel)) {
            return REGISTRATION_FAIL_ALREADY_LOGGED;
        }

        List<String> arguments = getArguments(argument);
        String error = validateArguments(arguments);
        if (error != null) {
            System.err.println(error);
            return FAIL + System.lineSeparator() + error;
        }
        String name = arguments.get(0);
        String pass = arguments.get(1);
        String nickname = arguments.get(2);

        try {
            users.register(new User(name, pass, nickname), clientChannel);
        } catch (ClientAlreadyLoggedException e) {
            System.err.println(e.getMessage());
            return REGISTRATION_FAIL_ALREADY_LOGGED;
        } catch (UsernameTakenException e) {
            System.err.println(e.getMessage());
            return e.getMessage();
        }
        return SUCCESSFUL_REGISTRATION;
    }

    private String validateArguments(List<String> arguments) {
        if (arguments == null) {
            return CORRECT_FORMAT;
        }
        if (arguments.size() != 3) {
            return (EXPECTED_ARGUMENTS_COUNT + System.lineSeparator()
                    + CORRECT_FORMAT);
        }
        if (!NameValidator.isValid(arguments.get(0))) {
            return NameValidator.DESCRIPTION;
        }
        if (arguments.get(1).length() < 4) {
            return PASSWORD_LENGTH;
        }
        if (arguments.get(2).length() < 1) {
            return NICKNAME_LENGTH;
        }
        return null;
    }

    private List<String> getArguments(String argument) {
        String[] tokens = argument.split(" ", 3);
        return new ArrayList<>(Arrays.asList(tokens));
    }
}
