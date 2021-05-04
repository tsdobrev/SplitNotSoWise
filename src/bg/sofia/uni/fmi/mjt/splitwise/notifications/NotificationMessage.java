package bg.sofia.uni.fmi.mjt.splitwise.notifications;

public enum NotificationMessage {
    NO_NOTIFICATIONS("There are not any new notifications to show.");

    private final String description;

    NotificationMessage(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
