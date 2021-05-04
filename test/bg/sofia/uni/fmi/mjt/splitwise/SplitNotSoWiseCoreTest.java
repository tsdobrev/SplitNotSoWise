package bg.sofia.uni.fmi.mjt.splitwise;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.nio.channels.SocketChannel;

import static org.junit.Assert.assertEquals;

public class SplitNotSoWiseCoreTest {
    private SplitNotSoWiseCore core;

    @Before
    public void setUp() {
        core = new SplitNotSoWiseCore();
    }

    @Test
    public void testHandleInputNullCommand() {
        String expected = "Unknown command. Please check your input and try again.";
        String actual = core.handleInput("unknownCommand", null);
        assertEquals("This command is invalid", expected, actual);
    }

    @Test
    public void testHandleInputRegister() {
        String expected = "Failed to register." + System.lineSeparator()
                + "Password should be at least 4 characters-long and cannot have whitespaces.";
        String actual = core.handleInput("register jj 44 John J.", null);
        assertEquals("User jj should not be registered", expected, actual);
    }

    @Test
    public void testHandleInputAddFriend() {
        String expected = "Failed to add friend." + System.lineSeparator()
                + "You are not logged. Please log in first in order to add friend.";
        String actual = core.handleInput("add-friend jj", null);
        assertEquals("Cannot add friend if not logged", expected, actual);

        SocketChannel channelMock = Mockito.mock(SocketChannel.class);

        core.handleInput("register me 4444 Myself", channelMock);

        expected = "Failed to add friend." + System.lineSeparator()
                + "User with username jj is not registered.";
        actual = core.handleInput("add-friend jj", channelMock);
        assertEquals("Cannot add friend if not registered yet", expected, actual);
    }
}
