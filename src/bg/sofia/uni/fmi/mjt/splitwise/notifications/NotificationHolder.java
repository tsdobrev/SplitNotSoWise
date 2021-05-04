package bg.sofia.uni.fmi.mjt.splitwise.notifications;

import bg.sofia.uni.fmi.mjt.splitwise.utilities.DateToStringParser;

import java.time.LocalDateTime;

public class NotificationHolder { // it is not a record, because we want to use Gson.fromJson
    private final LocalDateTime dateTime;
    private final String notificationInfo;

    public NotificationHolder(LocalDateTime dateTime, String notificationInfo) {
        this.dateTime = dateTime;
        this.notificationInfo = notificationInfo;
    }

    @Override
    public String toString() {
        return String.format("[%s]: %s", DateToStringParser.parse(dateTime), notificationInfo);
    }
}
