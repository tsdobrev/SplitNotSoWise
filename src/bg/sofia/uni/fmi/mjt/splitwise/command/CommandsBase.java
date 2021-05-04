package bg.sofia.uni.fmi.mjt.splitwise.command;

import bg.sofia.uni.fmi.mjt.splitwise.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.user.UsersRepositoryService;

public abstract class CommandsBase implements CommandExecutable {
    protected UsersRepositoryService<User> users;

    public CommandsBase(UsersRepositoryService<User> users){
        this.users = users;
    }
}
