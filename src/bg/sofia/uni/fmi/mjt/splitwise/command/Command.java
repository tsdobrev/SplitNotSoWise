package bg.sofia.uni.fmi.mjt.splitwise.command;

import java.util.Objects;

public class Command {
    private final CommandType type;
    private final String argument;

    public Command(CommandType type, String argument) {
        this.type = type;
        this.argument = argument;
    }

    public CommandType getType() {
        return type;
    }

    public String getArgument() {
        return argument;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Command command = (Command) object;
        return type == command.type && argument.equals(command.argument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, argument);
    }
}
