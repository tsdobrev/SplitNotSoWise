package bg.sofia.uni.fmi.mjt.splitwise.status;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.CurrentUserIsNotAFriendException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.UserAlreadyFriendException;
import bg.sofia.uni.fmi.mjt.splitwise.payment.PaymentStatus;
import bg.sofia.uni.fmi.mjt.splitwise.user.NameAndNicknameWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FriendsStatus {
    private final Map<String, PaymentStatus> friends; // username to paymentStatus
    private final Map<String, String> nicknames; // username to nickname
    public static final String NOTHING_TO_OWE = "You owe nothing and no one owes you.";

    public FriendsStatus() {
        friends = new HashMap<>();
        nicknames = new HashMap<>();
    }

    public int getFriendsCount() {
        return friends.size();
    }

    public void addFriend(NameAndNicknameWrapper user) throws UserAlreadyFriendException {
        if (hasFriend(user.getName())) {
            throw new UserAlreadyFriendException();
        }
        friends.put(user.getName(), new PaymentStatus());
        nicknames.put(user.getName(), user.getNickname());
    }

    // when current user buys something, username owes money
    public void addMoneyFriendOwes(String username, double amount) throws CurrentUserIsNotAFriendException {
        if (!hasFriend(username)) {
            throw new CurrentUserIsNotAFriendException();
        }
        friends.get(username).addMoney(amount);
    }

    // when current user owes money
    public void addMoneyIOweToFriend(String username, double amount) throws CurrentUserIsNotAFriendException {
        if (!hasFriend(username)) {
            throw new CurrentUserIsNotAFriendException();
        }
        friends.get(username).getMoney(amount);
    }

    public String getStatus() {
        StringBuilder status = new StringBuilder();
        friends.forEach((k, v) -> {
            if (!v.isEven()) {
                status.append(nicknames.get(k))
                        .append(" (")
                        .append(k)
                        .append("): ")
                        .append(v.toString())
                        .append(System.lineSeparator());
            }
        });
        if (status.toString().isBlank()) return NOTHING_TO_OWE;
        status.replace(status.length() - 2, status.length(), ""); // removing last empty lines
        return status.toString();
    }

    public Set<String> getFriends() {
        return friends.keySet();
    }

    public String getNickname(String username) throws CurrentUserIsNotAFriendException {
        if (!hasFriend(username)) {
            throw new CurrentUserIsNotAFriendException();
        }
        return nicknames.get(username);
    }

    public boolean hasFriend(String username) {
        return friends.containsKey(username);
    }

    @Override
    public String toString() {
        return String.format("Friends : %n%s", getStatus());
    }
}
