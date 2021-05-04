package bg.sofia.uni.fmi.mjt.splitwise.status;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.AlreadyInThisGroupException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.InvalidGroupMembersCountException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.NotAMemberOfTheGroupException;
import bg.sofia.uni.fmi.mjt.splitwise.user.NameAndNicknameWrapper;
import org.junit.Before;
import org.junit.Test;

import javax.naming.InvalidNameException;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

public class GroupsStatusTest {
    private GroupsStatus groups;
    private static final NameAndNicknameWrapper user1 = new NameAndNicknameWrapper("rick6", "Rick G.");
    private static final NameAndNicknameWrapper user2 = new NameAndNicknameWrapper("pe6o", "Peter");
    private static final NameAndNicknameWrapper user3 = new NameAndNicknameWrapper("A_J", "Anthony");

    @Before
    public void setUp() {
        this.groups = new GroupsStatus();
    }

    @Test(expected = AlreadyInThisGroupException.class)
    public void testAddGroupWhenAlreadyInTheGroup() throws AlreadyInThisGroupException, InvalidNameException,
            InvalidGroupMembersCountException {
        groups.addGroup("Group-1", new HashSet<>(Arrays.asList(user1, user2)));
        groups.addGroup("Group-1", new HashSet<>(Arrays.asList(user1, user2)));
    }

    @Test
    public void testAddGroup() throws AlreadyInThisGroupException, InvalidNameException, InvalidGroupMembersCountException {
        assertFalse("In the beginning there should be no groups", groups.hasGroup("Family"));
        groups.addGroup("Family", new HashSet<>(Arrays.asList(user1, user2)));
        assertTrue("Family group is now added", groups.hasGroup("Family"));
    }

    @Test(expected = NotAMemberOfTheGroupException.class)
    public void testAddMoneyUserOwesInGroupIfNoSuchGroup() throws AlreadyInThisGroupException, InvalidNameException,
            InvalidGroupMembersCountException, NotAMemberOfTheGroupException {
        groups.addGroup("Roommates", new HashSet<>(Arrays.asList(user1, user2, user3)));
        groups.addMoneyUserOwesMeInGroup("Family", "pe6o", 7.5);
    }

    @Test(expected = NotAMemberOfTheGroupException.class)
    public void testAddMoneyUserOwesInGroupIfUserNotAMember() throws AlreadyInThisGroupException, InvalidNameException,
            InvalidGroupMembersCountException, NotAMemberOfTheGroupException {
        groups.addGroup("Roommates", new HashSet<>(Arrays.asList(user1, user2, user3)));
        groups.addMoneyUserOwesMeInGroup("Roommates", "peter", 7.5);
    }

    @Test
    public void testAddMoneyUserOwesInGroup() throws AlreadyInThisGroupException, InvalidNameException,
            InvalidGroupMembersCountException, NotAMemberOfTheGroupException {
        groups.addGroup("Roommates", new HashSet<>(Arrays.asList(user1, user2, user3)));
        groups.addMoneyUserOwesMeInGroup("Roommates", "pe6o", 7.5);
        String expected = " - Roommates :" + System.lineSeparator() +
                "Peter (pe6o): Owes you 7,50 LV";
        assertEquals("Only user pe6o owes you money", expected, groups.getStatus());
    }


    @Test
    public void testAddMoneyIOweToUserInGroup() throws NotAMemberOfTheGroupException, AlreadyInThisGroupException,
            InvalidNameException, InvalidGroupMembersCountException {
        String expected = " - Roommates :" + System.lineSeparator() +
                "Anthony (A_J): You owe 21,20 LV";
        groups.addGroup("Roommates", new HashSet<>(Arrays.asList(user1, user2, user3)));
        groups.addMoneyIOweToUserInGroup("Roommates", "A_J", 21.2);
        assertEquals("You owe A_J 21.2", expected, groups.getStatus());
    }
}