package bg.sofia.uni.fmi.mjt.splitwise.user;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.*;

import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UsersRepository<UT extends UserBasicInfo> implements UsersRepositoryService<UT> { // UT - user type
    private final Map<String, UT> registeredUsers; // username to User
    private final Map<SocketChannel, UT> loggedUsers; // socketChannel to User
    private final Map<UT, Set<SocketChannel>> loggedFrom; // user to socketChannels, he is logged from

    public UsersRepository() {
        this.registeredUsers = new HashMap<>();
        this.loggedUsers = new HashMap<>();
        this.loggedFrom = new HashMap<>();
    }

    public UsersRepository(Map<String, UT> registeredUser){
        this.registeredUsers = registeredUser;
        this.loggedUsers = new HashMap<>();
        this.loggedFrom = new HashMap<>();
    }

    public Map<String, UT> getAllUsers(){
        return registeredUsers;
    }

    @Override
    public void register(UT user, SocketChannel clientChannel) throws
            ClientAlreadyLoggedException, UsernameTakenException {
        if (isLogged(clientChannel)) {
            throw new ClientAlreadyLoggedException();
        }
        if (isRegistered(user.getName())) {
            throw new UsernameTakenException();
        }
        registeredUsers.put(user.getName(), user);
        loggedUsers.put(clientChannel, user);
        userLoggedFrom(user, clientChannel);
    }

    @Override
    public <T extends UserAuthenticationInfo> void login(T user, SocketChannel clientChannel) throws
            ClientAlreadyLoggedException, WrongNamePasswordCombinationException {
        if (isLogged(clientChannel)) {
            throw new ClientAlreadyLoggedException();
        }
        String username = user.getName();
        if (isRegistered(username)) {
            if (registeredUsers.get(username).passwordMatch(user)) {
                loggedUsers.put(clientChannel, registeredUsers.get(username));
                userLoggedFrom(registeredUsers.get(username), clientChannel);
                return;
            }
        }
        throw new WrongNamePasswordCombinationException();
    }

    @Override
    public void logout(SocketChannel clientChannel) throws ClientNotLoggedException {
        if (!isLogged(clientChannel)) {
            throw new ClientNotLoggedException();
        }
        userLoggedOutFrom(loggedUsers.get(clientChannel), clientChannel);
        loggedUsers.remove(clientChannel);
    }

    @Override
    public boolean isRegistered(String username) {
        if (username == null) {
            return false;
        }
        return registeredUsers.containsKey(username);
    }

    @Override
    public boolean isLogged(SocketChannel clientChannel) {
        if (clientChannel == null) {
            return false;
        }
        return loggedUsers.containsKey(clientChannel);
    }

    @Override
    public UT getUser(String username) throws UserNotRegisteredException {
        if (!isRegistered(username)) {
            throw new UserNotRegisteredException();
        }
        return registeredUsers.get(username);
    }

    @Override
    public UT getUser(SocketChannel clientChannel) throws ClientNotLoggedException {
        if (!isLogged(clientChannel)) {
            throw new ClientNotLoggedException();
        }
        return loggedUsers.get(clientChannel);
    }

    @Override
    public Set<SocketChannel> userLoggedFrom(UT user) {
        if (!loggedFrom.containsKey(user)) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(loggedFrom.get(user));
    }

    private void userLoggedFrom(UT user, SocketChannel clientChannel) {
        if (loggedFrom.containsKey(user)) {
            loggedFrom.get(user).add(clientChannel);
        } else {
            Set<SocketChannel> set = new HashSet<>();
            set.add(clientChannel);
            loggedFrom.put(user, set);
        }
    }

    private void userLoggedOutFrom(UT user, SocketChannel clientChannel) {
        if (loggedFrom.containsKey(user)) {
            loggedFrom.get(user).remove(clientChannel);
        }
    }
}
