package bg.sofia.uni.fmi.mjt.splitwise.notifications;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class NotificationCenterTest {
    private static final LocalDateTime testTime =
            LocalDateTime.of(2021, 1, 10, 18, 33, 45);

    @Test
    public void testGetNotificationsIfNoNotifications() {
        NotificationCenter notifications = new NotificationCenter();
        String expected = "* * * Notifications * * *" + System.lineSeparator()
                + NotificationMessage.NO_NOTIFICATIONS.getDescription();
        assertEquals("Appropriate message should be shown in case there are no notifications",
                expected, notifications.getNotifications());
    }

    @Test
    public void testAddNotification() {
        NotificationCenter notifications = new NotificationCenter();
        String expected = "* * * Notifications * * *" + System.lineSeparator()
                + "Friends :" + System.lineSeparator()
                + "[10.01.2021 18:33:45]: Misho approved your payment 10 LV [Mixtape beers]." + System.lineSeparator()
                + System.lineSeparator()
                + "Groups :" + System.lineSeparator()
                + "[10.01.2021 18:33:45]: - Family: Alex split 150 LV [Surprise trip for mom and dad]"
                + System.lineSeparator();

        notifications.addNotification(NotificationType.FRIEND, testTime,
                "Misho approved your payment 10 LV [Mixtape beers].");
        notifications.addNotification(NotificationType.GROUP, testTime,
                "- Family: Alex split 150 LV [Surprise trip for mom and dad]");

        assertEquals("There should be 2 notifications in expected format",
                expected, notifications.getNotifications());
        notifications.clearNotifications();
        expected = "* * * Notifications * * *" + System.lineSeparator()
                + NotificationMessage.NO_NOTIFICATIONS.getDescription();
        assertEquals("Now there should be no new notifications", expected
                , notifications.getNotifications());
    }
}
