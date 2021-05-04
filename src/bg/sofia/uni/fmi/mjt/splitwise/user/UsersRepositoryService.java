package bg.sofia.uni.fmi.mjt.splitwise.user;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.*;

import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Set;

public interface UsersRepositoryService<T extends UserBasicInfo> {
    /**
     * Registers and logs the user
     *
     * @throws ClientAlreadyLoggedException if current user is logged he should not try to register
     * @throws UsernameTakenException       if such a username exists in system
     */
    void register(T user, SocketChannel clientChannel) throws
            ClientAlreadyLoggedException, UsernameTakenException;

    /**
     * Logins the user
     *
     * @throws ClientAlreadyLoggedException          if current user is logged
     * @throws WrongNamePasswordCombinationException if a user with current name and pass doesn't exist
     */
    <S extends UserAuthenticationInfo> void login(S user, SocketChannel clientChannel)
            throws ClientAlreadyLoggedException, WrongNamePasswordCombinationException;

    /**
     * Logouts the user
     *
     * @throws ClientNotLoggedException if client is not logged
     */
    void logout(SocketChannel clientChannel) throws ClientNotLoggedException;

    boolean isRegistered(String username);

    boolean isLogged(SocketChannel clientChannel);

    T getUser(String username) throws UserNotRegisteredException;

    T getUser(SocketChannel clientChannel) throws ClientNotLoggedException;

    Set<SocketChannel> userLoggedFrom(T user);

    Map<String, T> getAllUsers();
}
