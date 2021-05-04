package bg.sofia.uni.fmi.mjt.splitwise.notifications;

import bg.sofia.uni.fmi.mjt.splitwise.user.User;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NotificationCenter {
    private final Map<NotificationType, List<NotificationHolder>> notifications;
    private boolean hasNewNotifications = false;
    private static final int BUFFER_SIZE = 1024;
    private static final ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    public NotificationCenter() {
        this.notifications = new EnumMap<>(NotificationType.class);
        initializeAllTypes();
    }

    public void addNotification(NotificationType type, LocalDateTime dateTime, String notification) {
        notifications.get(type).add(new NotificationHolder(dateTime, notification));
        hasNewNotifications = true;
    }

    public String getNotifications() {
        if (!hasNewNotifications) {
            return "* * * Notifications * * *" + System.lineSeparator()
                    + NotificationMessage.NO_NOTIFICATIONS.getDescription();
        }

        StringBuilder result = new StringBuilder("* * * Notifications * * *");
        result.append(System.lineSeparator());

        for (var type : NotificationType.values()) {
            result.append(type.getDescription()).append(" :").append(System.lineSeparator());

            for (var notification : notifications.get(type)) {
                result.append(notification).append(System.lineSeparator());
            }
            result.append(System.lineSeparator());
        }
        result.replace(result.length() - 2, result.length(), ""); // removing last empty lines
        return result.toString();
    }

    public void clearNotifications() {
        notifications.values().forEach(List::clear);
        hasNewNotifications = false;
    }

    public static void sendNotificationIfUserLoggedNow(Set<SocketChannel> loggedFrom, User user) {
        if (loggedFrom.isEmpty()) {
            return;
        }
        String notification = user.getNotifications();
        for (var channel : loggedFrom) {
            writeClientOutput(channel, notification);
        }
        user.cleatNotifications();
    }

    private static void writeClientOutput(SocketChannel clientChannel, String output) {
        output += System.lineSeparator();
        buffer.clear();
        buffer.put(output.getBytes());
        buffer.flip();

        try {
            clientChannel.write(buffer);
        } catch (IOException e) {
            // closing the channel will be handled from the serer
        }
    }

    private void initializeAllTypes() {
        NotificationType[] types = NotificationType.values();
        for (var type : types) {
            notifications.put(type, new LinkedList<>());
        }
    }
}
