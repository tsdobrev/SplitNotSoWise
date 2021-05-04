package bg.sofia.uni.fmi.mjt.splitwise.status;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.AlreadyInThisGroupException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.CurrentUserIsNotAFriendException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.NotAMemberOfTheGroupException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.UserAlreadyFriendException;
import bg.sofia.uni.fmi.mjt.splitwise.user.NameAndNicknameWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GroupsStatus {
    private final Map<String, FriendsStatus> groups; // group name to UserStatus
    public static final String NO_GROUPS = "You are not a member of any group.";
    public static final String NOTHING_TO_OWE_IN_GROUP = "Nothing to owe in any group.";

    public GroupsStatus() {
        groups = new HashMap<>();
    }

    public void addGroup(String groupName, Set<NameAndNicknameWrapper> members) throws AlreadyInThisGroupException {
        if (hasGroup(groupName)) {
            throw new AlreadyInThisGroupException();
        }
        groups.put(groupName, initializeGroupMembers(members));
    }

    public void addMoneyUserOwesMeInGroup(String groupName, String username, double amount)
            throws NotAMemberOfTheGroupException {
        if (!hasGroup(groupName)) {
            throw new NotAMemberOfTheGroupException();
        }
        try {
            groups.get(groupName).addMoneyFriendOwes(username, amount);
        } catch (CurrentUserIsNotAFriendException e) {
            throw new NotAMemberOfTheGroupException();
        }
    }

    public void addMoneyIOweToUserInGroup(String groupName, String username, double amount)
            throws NotAMemberOfTheGroupException {
        if (!hasGroup(groupName)) {
            throw new NotAMemberOfTheGroupException();
        }
        try {
            groups.get(groupName).addMoneyIOweToFriend(username, amount);
        } catch (CurrentUserIsNotAFriendException e) {
            throw new NotAMemberOfTheGroupException();
        }
    }

    public void addMoneyAllMembersOweMe(String groupName, double amount) throws NotAMemberOfTheGroupException {
        var members = getMembers(groupName);
        FriendsStatus group = groups.get(groupName);
        for (var member : members) {
            try {
                group.addMoneyFriendOwes(member, amount);
            } catch (CurrentUserIsNotAFriendException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public String getStatus() {
        if (groups.isEmpty()) {
            return NO_GROUPS;
        }
        StringBuilder status = new StringBuilder();
        groups.forEach((k, v) -> {
            if (!v.getStatus().equals(FriendsStatus.NOTHING_TO_OWE)) {
                status.append(" - ")
                        .append(k)
                        .append(" :")
                        .append(System.lineSeparator())
                        .append(v.getStatus())
                        .append(System.lineSeparator())
                        .append(System.lineSeparator());
            }
        });
        if (status.toString().isBlank()) {
            return NOTHING_TO_OWE_IN_GROUP;
        }
        status.replace(status.length() - 4, status.length(), ""); // removing last empty lines
        return status.toString();
    }

    public Set<String> getMembers(String groupName) throws NotAMemberOfTheGroupException {
        if (!hasGroup(groupName)) {
            throw new NotAMemberOfTheGroupException();
        }
        return groups.get(groupName).getFriends();
    }

    public boolean hasGroup(String name) {
        if (name == null) {
            return false;
        }
        return groups.containsKey(name);
    }

    private FriendsStatus initializeGroupMembers(Set<NameAndNicknameWrapper> members) {
        FriendsStatus result = new FriendsStatus();
        for (var member : members) {
            try {
                result.addFriend(member);
            } catch (UserAlreadyFriendException e) {
                // We don't add duplicate members
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "Groups : " + System.lineSeparator() + getStatus();
    }
}
