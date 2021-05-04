package bg.sofia.uni.fmi.mjt.splitwise.command;

import java.nio.channels.SocketChannel;
import java.util.EnumMap;
import java.util.Map;

public class CommandExecutor {
    private final Map<CommandType, CommandExecutable> commands;

    public CommandExecutor() {
        commands = new EnumMap<>(CommandType.class);
    }

    public void addCommand(CommandType type, CommandExecutable command) {
        commands.put(type, command);
    }

    public String execute(Command command, SocketChannel clientChannel) {
        return commands.get(command.getType()).executeCommand(command.getArgument(), clientChannel);
    }

}
