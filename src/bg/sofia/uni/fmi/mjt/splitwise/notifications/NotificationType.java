package bg.sofia.uni.fmi.mjt.splitwise.notifications;

public enum NotificationType {
    FRIEND("Friends"),
    GROUP("Groups");

    private final String description;

    NotificationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
