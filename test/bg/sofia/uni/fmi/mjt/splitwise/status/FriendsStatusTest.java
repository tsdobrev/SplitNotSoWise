package bg.sofia.uni.fmi.mjt.splitwise.status;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.CurrentUserIsNotAFriendException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.UserAlreadyFriendException;
import bg.sofia.uni.fmi.mjt.splitwise.user.NameAndNicknameWrapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FriendsStatusTest {
    private FriendsStatus friends;
    private final NameAndNicknameWrapper testUser = new NameAndNicknameWrapper("peter90", "Peter");
    private final NameAndNicknameWrapper testUser2 = new NameAndNicknameWrapper("stamo", "Stamo");

    @Before
    public void setUp() {
        friends = new FriendsStatus();
    }

    @Test
    public void getFriendsCount() {
        assertEquals("In the beginning count should be 0", 0, friends.getFriendsCount());
    }

    @Test(expected = UserAlreadyFriendException.class)
    public void addFriendExistingFriend() throws UserAlreadyFriendException {
        friends.addFriend(testUser);
        friends.addFriend(testUser);
    }

    @Test
    public void addFriend() throws UserAlreadyFriendException {
        friends.addFriend(testUser);
        assertEquals("There should be one friend now.", 1, friends.getFriendsCount());
    }

    @Test(expected = CurrentUserIsNotAFriendException.class)
    public void addMoneyFriendOwesWithUserWhoIsNotInFriends() throws CurrentUserIsNotAFriendException {
        friends.addMoneyFriendOwes("testUser", 12.3);
    }

    @Test
    public void addMoneyFriendOwes() throws UserAlreadyFriendException, CurrentUserIsNotAFriendException {
        friends.addFriend(testUser);
        friends.addMoneyFriendOwes("peter90", 12.3);
        String expected = "Peter (peter90): Owes you 12,30 LV";
        assertEquals("Peter now owes 12.3", expected, friends.getStatus());
    }

    @Test(expected = CurrentUserIsNotAFriendException.class)
    public void addMoneyIOweToFriendWithNotExistingFriend() throws CurrentUserIsNotAFriendException {
        friends.addMoneyIOweToFriend("testUser", 12.3);
    }

    @Test
    public void addMoneyIOweToFriend() throws CurrentUserIsNotAFriendException, UserAlreadyFriendException {
        friends.addFriend(testUser);
        friends.addFriend(testUser2);
        friends.addMoneyIOweToFriend("peter90", 12.3);
        String expected = "Peter (peter90): You owe 12,30 LV";
        assertEquals("I owe Peter 12.3", expected, friends.getStatus());
    }

    @Test
    public void testGetStatus() throws UserAlreadyFriendException, CurrentUserIsNotAFriendException {
        friends.addFriend(testUser);
        friends.addFriend(testUser2);
        friends.addMoneyIOweToFriend("peter90", 12.3);
        friends.addMoneyFriendOwes("stamo", 11.2);
        String expected = "Peter (peter90): You owe 12,30 LV" + System.lineSeparator()
                + "Stamo (stamo): Owes you 11,20 LV";
        assertEquals("I owe Peter and Stamo owes you money", expected, friends.getStatus());
    }
}