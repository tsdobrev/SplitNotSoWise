package bg.sofia.uni.fmi.mjt.splitwise.command.commands;

import bg.sofia.uni.fmi.mjt.splitwise.command.CommandsBase;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.ClientNotLoggedException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.CurrentUserIsNotAFriendException;
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

public class SplitCommand extends CommandsBase {
    private static final String FAIL = "Failed to split with friend.";
    private static final String SUCCESS = "You successfully split %.2f LV with %s for %s.";
    private static final String CORRECT_FORMAT =
            "Please provide amount, username and reason for payment correctly as follows" + System.lineSeparator()
                    + "split amount username reason";
    private static final String INVALID_SUM = "The sum you have provided is invalid. Please enter number.";
    private static final String NOT_LOGGED = "You are not logged. Please login first in order to split with friend.";
    private static final String EXPECTED_ARGUMENTS_COUNT = "There are expected at least 3 arguments.";
    private static final String OTHER_USER_NOTIFICATION = "%s split %.2f with you for %s.";
    private static final String USER_NOT_FRIEND =
            "User %s is not friend of yours. Add him first in order to split with him.";
    private final double splitTo = 2.0;

    public SplitCommand(UsersRepositoryService<User> users) {
        super(users);
    }

    @Override
    public String executeCommand(String argument, SocketChannel clientChannel) {
        try {
            User currentUser = users.getUser(clientChannel);
            List<String> arguments = getArguments(argument);
            String error = validateArguments(arguments);
            if (error != null) {
                return FAIL + System.lineSeparator() + error + System.lineSeparator() + CORRECT_FORMAT;
            }
            User otherUser = users.getUser(arguments.get(1));
            double amount = Double.parseDouble(arguments.get(0));
            double splitAmount = amount / splitTo;
            String reasonForPayment = arguments.get(2);

            currentUser.addMoneyFriendOwes(otherUser.getName(), splitAmount);
            otherUser.addMoneyIOweToFriend(currentUser.getName(), splitAmount);

            LocalDateTime timeNow = LocalDateTime.now();

            // adding to payment history
            String description = String.format(SUCCESS, amount, otherUser, reasonForPayment);
            currentUser.addPayment(timeNow, description);

            // sending notification to other
            String notification = String.format(OTHER_USER_NOTIFICATION, currentUser, amount, reasonForPayment);
            otherUser.addNotification(NotificationType.FRIEND, timeNow, notification);
            NotificationCenter.sendNotificationIfUserLoggedNow(users.userLoggedFrom(otherUser), otherUser);

            return description;
        } catch (ClientNotLoggedException e) {
            return NOT_LOGGED;
        } catch (CurrentUserIsNotAFriendException | UserNotRegisteredException e) {
            return FAIL + System.lineSeparator() + String.format(USER_NOT_FRIEND, getArguments(argument).get(1));
        }
    }

    private String validateArguments(List<String> arguments) {
        if (arguments.size() < 3) {
            return EXPECTED_ARGUMENTS_COUNT;
        }
        try {
            Double.parseDouble(arguments.get(0));
        } catch (NumberFormatException e) {
            return INVALID_SUM;
        }
        return null;
    }

    private List<String> getArguments(String argument) {
        String[] tokens = argument.split(" ", 3);
        return new ArrayList<>(Arrays.asList(tokens));
    }
}