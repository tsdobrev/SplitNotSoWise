package bg.sofia.uni.fmi.mjt.splitwise.command.commands;

import bg.sofia.uni.fmi.mjt.splitwise.command.CommandsBase;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.ClientNotLoggedException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.UserAlreadyFriendException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.UserNotRegisteredException;
import bg.sofia.uni.fmi.mjt.splitwise.notifications.NotificationCenter;
import bg.sofia.uni.fmi.mjt.splitwise.notifications.NotificationType;
import bg.sofia.uni.fmi.mjt.splitwise.user.NameAndNicknameWrapper;
import bg.sofia.uni.fmi.mjt.splitwise.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.user.UsersRepositoryService;

import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddFriendCommand extends CommandsBase {
    private static final String FAIL = "Failed to add friend.";
    private static final String CORRECT_FORMAT =
            "Please provide valid username correctly as follows" + System.lineSeparator()
                    + "add-friend username";
    private static final String EXPECTED_ARGUMENTS_COUNT = "It is expected 1 argument.";
    private static final String NOT_LOGGED = "You are not logged. Please log in first in order to add friend.";
    private static final String USER_NOT_REGISTERED = "User with username %s is not registered.";
    private static final String ALREADY_FRIEND = "User %s is already a friend of yours.";
    private static final String ADDING_YOURSELF = "You are not allowed to add yourself as friend.";
    private static final String SUCCESSFUL_ADD = "User %s is now your friend.";

    public AddFriendCommand(UsersRepositoryService<User> users) {
        super(users);
    }

    @Override
    public String executeCommand(String argument, SocketChannel clientChannel) {
        List<String> arguments = getArguments(argument);
        String error = validateArguments(arguments, clientChannel);
        if (error != null) {
            System.err.println(error);
            return FAIL + System.lineSeparator() + error;
        }

        String name = arguments.get(0);
        User currentUser, otherUser;
        try {
            currentUser = users.getUser(clientChannel);
            otherUser = users.getUser(name);
        } catch (ClientNotLoggedException e) {
            return NOT_LOGGED;
        } catch (UserNotRegisteredException e) {
            System.err.printf((USER_NOT_REGISTERED) + "%n", name);
            return FAIL + System.lineSeparator() + String.format(USER_NOT_REGISTERED, name);
        }
        if (currentUser.getName().equals(otherUser.getName())) {
            return FAIL + System.lineSeparator() + ADDING_YOURSELF;
        }

        try {
            currentUser.addFriend(new NameAndNicknameWrapper(otherUser.getName(), otherUser.getNickname()));
            otherUser.addFriend(new NameAndNicknameWrapper(currentUser.getName(), currentUser.getNickname()));
        } catch (UserAlreadyFriendException e) {
            return FAIL + System.lineSeparator() + String.format(ALREADY_FRIEND, name);
        }
        otherUser.addNotification(NotificationType.FRIEND, LocalDateTime.now(),
                String.format(SUCCESSFUL_ADD, currentUser.getName()));
        NotificationCenter.sendNotificationIfUserLoggedNow(users.userLoggedFrom(otherUser), otherUser);

        return String.format(SUCCESSFUL_ADD, name);
    }

    private String validateArguments(List<String> arguments, SocketChannel clientChannel) {
        if (!users.isLogged(clientChannel)) {
            return NOT_LOGGED;
        }
        if (arguments.size() != 1) {
            return EXPECTED_ARGUMENTS_COUNT + System.lineSeparator() + CORRECT_FORMAT;
        }
        return null;
    }

    private List<String> getArguments(String argument) {
        String[] tokens = argument.split(" ", 2);
        return new ArrayList<>(Arrays.asList(tokens));
    }
}
