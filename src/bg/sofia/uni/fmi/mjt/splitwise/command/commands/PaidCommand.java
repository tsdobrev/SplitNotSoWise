package bg.sofia.uni.fmi.mjt.splitwise.command.commands;

import bg.sofia.uni.fmi.mjt.splitwise.command.CommandsBase;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.ClientNotLoggedException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.CurrentUserIsNotAFriendException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.UserNotRegisteredException;
import bg.sofia.uni.fmi.mjt.splitwise.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.user.UsersRepositoryService;

import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PaidCommand extends CommandsBase {
    private static final String FAIL = "Failed to approve friend's payment.";
    private static final String SUCCESS = "You successfully approved that friend %s paid you %.2f LV.";
    private static final String CORRECT_FORMAT =
            "Please provide amount and username correctly as follows" + System.lineSeparator()
                    + "paid amount username";
    private static final String INVALID_SUM = "The sum you have provided is invalid. Please enter number.";
    private static final String NOT_LOGGED = "You are not logged. Please login first in order to approve payments.";
    private static final String EXPECTED_ARGUMENTS_COUNT = "There are expected 2 arguments.";
    private static final String OTHER_USER_PAYMENT_DESCRIPTION = "You paid %.2f LV to friend %s.";
    private static final String NO_FRIEND = "%s is not friend of yours.";

    public PaidCommand(UsersRepositoryService<User> users) {
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
            double amount = Double.parseDouble(arguments.get(0));
            String username = arguments.get(1);
            User otherUser = users.getUser(username);

            currentUser.addMoneyIOweToFriend(username, amount);
            otherUser.addMoneyFriendOwes(currentUser.getName(), amount);

            // adding payment to other user
            otherUser.addPayment(LocalDateTime.now(),
                    String.format(OTHER_USER_PAYMENT_DESCRIPTION, amount, currentUser));

            return String.format(SUCCESS, username, amount);
        } catch (ClientNotLoggedException e) {
            return FAIL + System.lineSeparator() + NOT_LOGGED;
        } catch (CurrentUserIsNotAFriendException | UserNotRegisteredException e) {
            return FAIL + System.lineSeparator() + String.format(NO_FRIEND, getArguments(argument).get(1));
        }
    }

    private String validateArguments(List<String> arguments) {
        if (arguments.size() != 2) {
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
