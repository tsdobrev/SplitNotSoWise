package bg.sofia.uni.fmi.mjt.splitwise.user;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.ClientAlreadyLoggedException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.ClientNotLoggedException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.UsernameTakenException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.WrongNamePasswordCombinationException;
import org.junit.Before;
import org.junit.Test;

import javax.naming.InvalidNameException;

import java.nio.channels.SocketChannel;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class UsersRepositoryTest {
    private UsersRepository<UserBasicInfo> repository;
    private static final UserBasicInfo user1 = new UserBasicInfo("rck", "123", "Rick Gr.");
    private final SocketChannel socketChannelMock = mock(SocketChannel.class);

    @Before
    public void setUp() {
        repository = new UsersRepository<>();
    }

    @Test
    public void register() throws InvalidNameException, UsernameTakenException, ClientAlreadyLoggedException {
        repository.register(user1, socketChannelMock);
        assertTrue("User with name rck should be registered", repository.isRegistered("rck"));
        assertTrue("User with name rck should be logged", repository.isLogged(socketChannelMock));
    }

    @Test(expected = UsernameTakenException.class)
    public void registerWithTakenName() throws InvalidNameException, UsernameTakenException,
            ClientAlreadyLoggedException {
        repository.register(user1, null);
        repository.register(user1, socketChannelMock);
    }

    @Test(expected = ClientAlreadyLoggedException.class)
    public void registerIfLoggedAlready() throws InvalidNameException, UsernameTakenException,
            ClientAlreadyLoggedException {
        repository.register(user1, socketChannelMock);
        repository.register(null, socketChannelMock);
    }

    @Test(expected = ClientAlreadyLoggedException.class)
    public void loginIfLoggedAlready() throws InvalidNameException, UsernameTakenException,
            ClientAlreadyLoggedException, WrongNamePasswordCombinationException {
        repository.register(user1, socketChannelMock);
        repository.login(user1, socketChannelMock);
    }

    @Test(expected = WrongNamePasswordCombinationException.class)
    public void loginIfWrongNameAndPassword() throws InvalidNameException, UsernameTakenException,
            ClientAlreadyLoggedException, WrongNamePasswordCombinationException {
        repository.register(user1, socketChannelMock);
        repository.login(new UserAuthenticationInfo("rck", "1234"), mock(SocketChannel.class));
    }

    @Test
    public void loginIsSuccessful() throws InvalidNameException, UsernameTakenException,
            ClientAlreadyLoggedException, WrongNamePasswordCombinationException {
        repository.register(user1, socketChannelMock);
        repository.login(new UserAuthenticationInfo("rck", "123"), mock(SocketChannel.class));
    }

    @Test(expected = ClientNotLoggedException.class)
    public void logoutINotLogged() throws ClientNotLoggedException {
        repository.logout(socketChannelMock);
    }

    @Test
    public void isLogged() throws InvalidNameException, UsernameTakenException, ClientAlreadyLoggedException,
            ClientNotLoggedException {
        assertFalse("User with mockChannel should not be logged", repository.isLogged(socketChannelMock));
        repository.register(user1, socketChannelMock);
        assertTrue("User with mockChannel is now logged", repository.isLogged(socketChannelMock));
        repository.logout(socketChannelMock);
        assertFalse("User with mockChannel should not be logged", repository.isLogged(socketChannelMock));
    }
}