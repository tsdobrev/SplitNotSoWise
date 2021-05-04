package bg.sofia.uni.fmi.mjt.splitwise.command.commands;

import bg.sofia.uni.fmi.mjt.splitwise.command.CommandsBase;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.ClientNotLoggedException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.NotAMemberOfTheGroupException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.UserNotRegisteredException;
import bg.sofia.uni.fmi.mjt.splitwise.notifications.NotificationCenter;
import bg.sofia.uni.fmi.mjt.splitwise.notifications.NotificationType;
import bg.sofia.uni.fmi.mjt.splitwise.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.user.UsersRepositoryService;

import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SplitGroupCommand extends CommandsBase {
    private static final String FAIL = "Failed to split in group.";
    private static final String SUCCESS = "You successfully split %.2f LV in %s for %s.";
    private static final String CORRECT_FORMAT =
            "Please provide amount, group name and reason for payment correctly as follows" + System.lineSeparator()
                    + "split-group amount group_name reason";
    private static final String INVALID_SUM = "The sum you have provided is invalid. Please enter number.";
    private static final String NOT_LOGGED = "You are not logged. Please login first in order to split in group.";
    private static final String EXPECTED_ARGUMENTS_COUNT = "There are expected at least 3 arguments.";
    private static final String OTHER_USER_NOTIFICATION = "%s split %.2f in %s for %s.";
    private static final String NO_GROUP = "You are not a member of %s group.";

    public SplitGroupCommand(UsersRepositoryService<User> users) {
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
            String groupName = arguments.get(1);
            String reasonForPayment = arguments.get(2);

            var allMembers = currentUser.getMembersOfGroup(groupName);

            double splitSum = amount / (allMembers.size() + 1);

            currentUser.addMoneyAllMembersOweMe(groupName, splitSum);
            updateOtherUsersOwe(groupName, splitSum, currentUser.getName(), allMembers);

            // adding payment
            String payment = String.format(SUCCESS, amount, groupName, reasonForPayment);
            LocalDateTime timeNow = LocalDateTime.now();
            currentUser.addPayment(timeNow, payment);

            // sending notifications to group members
            String notification =
                    String.format(OTHER_USER_NOTIFICATION, currentUser.getName(), amount, groupName, reasonForPayment);
            sendNotifications(groupName, allMembers, timeNow, notification);
            notifyAllLoggedUsers(allMembers);

            return payment;
        } catch (ClientNotLoggedException e) {
            return FAIL + System.lineSeparator() + NOT_LOGGED;
        } catch (NotAMemberOfTheGroupException e) {
            return FAIL + System.lineSeparator() + String.format(NO_GROUP, getArguments(argument).get(1));
        }
    }

    private void notifyAllLoggedUsers(Set<String> allMembers) {
        for (var member : allMembers) {
            try {
                User user = users.getUser(member);
                NotificationCenter.sendNotificationIfUserLoggedNow(users.userLoggedFrom(user), user);
            } catch (UserNotRegisteredException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void sendNotifications(String groupName, Set<String> allMembersWithOutMe, LocalDateTime time,
                                   String notification) {
        for (var member : allMembersWithOutMe) {
            try {
                User currentUser = users.getUser(member);
                currentUser.addNotification(NotificationType.GROUP, time, notification);
            } catch (UserNotRegisteredException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void updateOtherUsersOwe(String groupName, double amount, String username, Set<String> allMembers) {
        for (var member : allMembers) {
            if (member.equals(username)) {
                continue;
            }
            try {
                User currentUser = users.getUser(member);
                currentUser.addMoneyIOweToUserInGroup(groupName, username, amount);
            } catch (UserNotRegisteredException | NotAMemberOfTheGroupException e) {
                System.err.println(e.getMessage());
            }
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
