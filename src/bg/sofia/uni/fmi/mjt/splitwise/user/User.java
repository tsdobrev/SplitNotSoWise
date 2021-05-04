package bg.sofia.uni.fmi.mjt.splitwise.user;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.*;
import bg.sofia.uni.fmi.mjt.splitwise.notifications.NotificationCenter;
import bg.sofia.uni.fmi.mjt.splitwise.notifications.NotificationType;
import bg.sofia.uni.fmi.mjt.splitwise.payment.PaymentHistory;
import bg.sofia.uni.fmi.mjt.splitwise.status.FriendsStatus;
import bg.sofia.uni.fmi.mjt.splitwise.status.GroupsStatus;

import java.time.LocalDateTime;
import java.util.Set;

public class User extends UserBasicInfo {
    private final FriendsStatus friends;
    private final GroupsStatus groups;

    private final PaymentHistory paymentHistory; // user's payments
    private final NotificationCenter notifications; // all payments (if any) after last logout

    public User(String name, String password, String nickname) {
        super(name, password, nickname);
        friends = new FriendsStatus();
        groups = new GroupsStatus();
        notifications = new NotificationCenter();
        paymentHistory = new PaymentHistory();
    }

    // notifications commands
    public void addNotification(NotificationType type, LocalDateTime dateTime, String notification) {
        notifications.addNotification(type, dateTime, notification);
    }

    public String getNotifications() {
        return notifications.getNotifications();
    }

    public void cleatNotifications() {
        notifications.clearNotifications();
    }

    // payments commands
    public void addPayment(LocalDateTime dateTime, String paymentDescription) {
        paymentHistory.addPayment(dateTime, paymentDescription);
    }

    public String getPaymentsHistory() {
        return paymentHistory.getPayments();
    }

    public String getPaymentsWithName(String name) {
        return paymentHistory.getPaymentsWithName(name);
    }

    public String getPaymentsBefore(LocalDateTime dateTime) {
        return paymentHistory.getPaymentsBefore(dateTime);
    }

    public String getPaymentsAfter(LocalDateTime dateTime) {
        return paymentHistory.getPaymentsAfter(dateTime);
    }

    // friends commands
    public void addFriend(NameAndNicknameWrapper user) throws UserAlreadyFriendException {
        friends.addFriend(user);
    }

    public void addMoneyFriendOwes(String username, double amount) throws
            CurrentUserIsNotAFriendException {
        friends.addMoneyFriendOwes(username, amount);
    }

    public void addMoneyIOweToFriend(String username, double amount) throws CurrentUserIsNotAFriendException {
        friends.addMoneyIOweToFriend(username, amount);
    }

    public boolean hasFriend(String username) {
        return friends.hasFriend(username);
    }

    // groups commands
    public void addGroup(String groupName, Set<NameAndNicknameWrapper> members) throws AlreadyInThisGroupException {
        groups.addGroup(groupName, members);
    }

    public Set<String> getMembersOfGroup(String groupName) throws NotAMemberOfTheGroupException {
        return groups.getMembers(groupName);
    }

    public void addMoneyUserOwesMeInGroup(String groupName, String username, double amount) throws
            NotAMemberOfTheGroupException {
        groups.addMoneyUserOwesMeInGroup(groupName, username, amount);
    }

    public void addMoneyIOweToUserInGroup(String groupName, String username, double amount) throws
            NotAMemberOfTheGroupException {
        groups.addMoneyIOweToUserInGroup(groupName, username, amount);
    }

    public void addMoneyAllMembersOweMe(String groupName, double amount) throws NotAMemberOfTheGroupException {
        groups.addMoneyAllMembersOweMe(groupName, amount);
    }

    public boolean isInGroup(String groupName) {
        return groups.hasGroup(groupName);
    }

    public String getStatus() {
        return String.format("%s%n%n%s", friends.toString(), groups.toString());
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", getNickname(), getName());
    }
}