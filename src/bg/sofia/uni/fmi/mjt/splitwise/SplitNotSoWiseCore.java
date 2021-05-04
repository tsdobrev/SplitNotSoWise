package bg.sofia.uni.fmi.mjt.splitwise;

import bg.sofia.uni.fmi.mjt.splitwise.command.CommandCreator;
import bg.sofia.uni.fmi.mjt.splitwise.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.splitwise.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.command.commands.AddFriendCommand;
import bg.sofia.uni.fmi.mjt.splitwise.command.commands.CreateGroupCommand;
import bg.sofia.uni.fmi.mjt.splitwise.command.commands.GetStatusCommand;
import bg.sofia.uni.fmi.mjt.splitwise.command.commands.LoginCommand;
import bg.sofia.uni.fmi.mjt.splitwise.command.commands.LogoutCommand;
import bg.sofia.uni.fmi.mjt.splitwise.command.commands.PaidCommand;
import bg.sofia.uni.fmi.mjt.splitwise.command.commands.RegisterCommand;
import bg.sofia.uni.fmi.mjt.splitwise.command.commands.ShowPaymentsCommand;
import bg.sofia.uni.fmi.mjt.splitwise.command.commands.ShowPaymentsWithNameCommand;
import bg.sofia.uni.fmi.mjt.splitwise.command.commands.SplitCommand;
import bg.sofia.uni.fmi.mjt.splitwise.command.commands.SplitGroupCommand;
import bg.sofia.uni.fmi.mjt.splitwise.command.commands.UnknownCommand;
import bg.sofia.uni.fmi.mjt.splitwise.database.UsersDatabase;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.ClientNotLoggedException;
import bg.sofia.uni.fmi.mjt.splitwise.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.user.UsersRepository;
import bg.sofia.uni.fmi.mjt.splitwise.user.UsersRepositoryService;

import java.nio.channels.SocketChannel;

public class SplitNotSoWiseCore {
    private final UsersRepositoryService<User> users;
    private final CommandExecutor commandExecutor;
    private final UsersDatabase database;

    public SplitNotSoWiseCore() {
        database = new UsersDatabase();
        users = new UsersRepository<>(database.getSavedUsers());
        commandExecutor = new CommandExecutor();
        initializeCommands();
    }

    public void backUpUsers() {
        database.updateDatabase(users.getAllUsers());
    }

    public String handleInput(String clientInput, SocketChannel clientChannel) {
        return commandExecutor.execute(CommandCreator.getCommand(clientInput), clientChannel);
    }

    public void logoutUser(SocketChannel clientChannel) {
        try {
            users.logout(clientChannel);
        } catch (ClientNotLoggedException e) {
            // if client is not logged there is no need to log him out
        }
    }

    private void initializeCommands() {
        commandExecutor.addCommand(CommandType.UNKNOWN, new UnknownCommand());
        commandExecutor.addCommand(CommandType.REGISTER, new RegisterCommand(users));
        commandExecutor.addCommand(CommandType.LOGIN, new LoginCommand(users));
        commandExecutor.addCommand(CommandType.LOGOUT, new LogoutCommand(users));

        commandExecutor.addCommand(CommandType.ADD_FRIEND, new AddFriendCommand(users));
        commandExecutor.addCommand(CommandType.CREATE_GROUP, new CreateGroupCommand(users));

        commandExecutor.addCommand(CommandType.GET_STATUS, new GetStatusCommand(users));
        commandExecutor.addCommand(CommandType.SPLIT, new SplitCommand(users));
        commandExecutor.addCommand(CommandType.SPLIT_GROUP, new SplitGroupCommand(users));
        commandExecutor.addCommand(CommandType.PAID, new PaidCommand(users));

        commandExecutor.addCommand(CommandType.SHOW_PAYMENTS, new ShowPaymentsCommand(users));
        commandExecutor.addCommand(CommandType.SHOW_PAYMENTS_WITH_NAME, new ShowPaymentsWithNameCommand(users));
    }
}
