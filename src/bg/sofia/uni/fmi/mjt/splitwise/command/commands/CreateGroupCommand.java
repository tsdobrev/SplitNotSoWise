package bg.sofia.uni.fmi.mjt.splitwise.command.commands;

import bg.sofia.uni.fmi.mjt.splitwise.command.CommandsBase;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.AlreadyInThisGroupException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.ClientNotLoggedException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.UserNotRegisteredException;
import bg.sofia.uni.fmi.mjt.splitwise.notifications.NotificationCenter;
import bg.sofia.uni.fmi.mjt.splitwise.notifications.NotificationType;
import bg.sofia.uni.fmi.mjt.splitwise.user.NameAndNicknameWrapper;
import bg.sofia.uni.fmi.mjt.splitwise.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.user.UsersRepositoryService;
import bg.sofia.uni.fmi.mjt.splitwise.utilities.NameValidator;

import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CreateGroupCommand extends CommandsBase {
    private static final String FAIL = "Failed to create group.";
    private static final String SUCCESS = "You are now in group '%s' with %s.";
    private static final String CORRECT_FORMAT =
            "Please provide group name and at least 2 valid usernames correctly as follows" + System.lineSeparator()
                    + "create-group group_name username1 username2";
    private static final String NOT_LOGGED = "You are not logged. Please log in first in order to create group.";
    private static final String EXPECTED_ARGUMENTS_COUNT = "There are expected at least 3 arguments.";
    private static final String ALREADY_IN_GROUP = "You are already in group '%s'.";
    private static final String NOT_REGISTERED_USER = "User %s does not exist.";
    private static final String INCLUDED_AS_USER =
            "Please do not include your username when writing group members. " + System.lineSeparator()
                    + "Your username will be included automatically.";

    public CreateGroupCommand(UsersRepositoryService<User> users) {
        super(users);
    }

    @Override
    public String executeCommand(String argument, SocketChannel clientChannel) {
        try {
            User currentUser = users.getUser(clientChannel);

            List<String> arguments = getArguments(argument);
            String error = validateArguments(arguments, currentUser);
            if (error != null) {
                return FAIL + System.lineSeparator() + error;
            }
            String groupName = arguments.get(0);
            NameAndNicknameWrapper currentUserWrapper =
                    new NameAndNicknameWrapper(currentUser.getName(), currentUser.getNickname());

            var allMembers = getMembers(arguments);
            allMembers.add(currentUserWrapper);
            addGroupsToAllUsers(groupName, allMembers);

            sendNotifications(groupName, allMembers, currentUserWrapper);
            allMembers.remove(currentUserWrapper);
            notifyAllLoggedUsers(allMembers);

            String usersInfo = allMembers.stream()
                    .map(NameAndNicknameWrapper::getName)
                    .collect(Collectors.joining(", "));

            return String.format(SUCCESS, groupName, usersInfo);
        } catch (ClientNotLoggedException e) {
            return NOT_LOGGED;
        } catch (AlreadyInThisGroupException e) {
            return FAIL + System.lineSeparator() + String.format(ALREADY_IN_GROUP, getArguments(argument).get(0));
        }
    }

    private void notifyAllLoggedUsers(Set<NameAndNicknameWrapper> allMembers) {
        for (var member : allMembers) {
            User user = null;
            try {
                user = users.getUser(member.getName());
            } catch (UserNotRegisteredException e) {
                System.err.println(e.getMessage());
            }
            NotificationCenter.sendNotificationIfUserLoggedNow(users.userLoggedFrom(user), user);
        }
    }

    private void sendNotifications(String groupName, Set<NameAndNicknameWrapper> allMembers,
                                   NameAndNicknameWrapper userNow) {
        LocalDateTime timeNow = LocalDateTime.now();
        var membersCopy = new HashSet<>(allMembers);
        for (var member : allMembers) {
            if (member.equals(userNow)) { // no need to send notification to current user, he will receive it now
                continue;
            }
            try {
                User currentUser = users.getUser(member.getName());
                membersCopy.remove(member);

                String usersInfo = membersCopy.stream()
                        .map(NameAndNicknameWrapper::getName)
                        .collect(Collectors.joining(", "));

                currentUser.addNotification(NotificationType.GROUP, timeNow,
                        String.format(SUCCESS, groupName, usersInfo));

                membersCopy.add(member);
            } catch (UserNotRegisteredException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void addGroupsToAllUsers(String groupName, Set<NameAndNicknameWrapper> allMembers)
            throws AlreadyInThisGroupException {
        Set<NameAndNicknameWrapper> membersCopy = new HashSet<>(allMembers);
        for (var member : allMembers) {
            try {
                User currentUser = users.getUser(member.getName());
                membersCopy.remove(member);
                currentUser.addGroup(groupName, membersCopy);
                membersCopy.add(member);
            } catch (UserNotRegisteredException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private Set<NameAndNicknameWrapper> getMembers(List<String> arguments) {
        Set<NameAndNicknameWrapper> members = new HashSet<>();
        for (int i = 1; i < arguments.size(); i++) {
            String username = arguments.get(i);
            try {
                User user = users.getUser(username);
                members.add(new NameAndNicknameWrapper(user.getName(), user.getNickname()));
            } catch (UserNotRegisteredException e) {
                // should be validated in validateArguments
            }
        }
        return members;
    }

    private String validateArguments(List<String> arguments, User currentUser) {
        if (arguments.size() < 3) {
            return EXPECTED_ARGUMENTS_COUNT + System.lineSeparator() + CORRECT_FORMAT;
        }
        String groupName = arguments.get(0);
        if (!NameValidator.isValid(groupName)) {
            return NameValidator.DESCRIPTION;
        }
        for (int i = 1; i < arguments.size(); i++) {
            String username = arguments.get(i);
            if (!users.isRegistered(username)) {
                return String.format(NOT_REGISTERED_USER, username);
            }
            if (currentUser.getName().equals(username)) {
                return INCLUDED_AS_USER;
            }
        }
        return null;
    }

    private List<String> getArguments(String argument) {
        String[] tokens = argument.split(" ");
        return new ArrayList<>(Arrays.asList(tokens));
    }
}
