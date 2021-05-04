package bg.sofia.uni.fmi.mjt.splitwise.command;

public class CommandCreator {

    public static Command getCommand(String clientInput) {
        if (clientInput == null) {
            return new Command(CommandType.UNKNOWN, "");
        }
        String[] tokens = clientInput.trim().replace("\\s+", " ").split(" ", 2);
        if (tokens.length < 1) {
            return new Command(CommandType.UNKNOWN, "");
        }

        for (var type : CommandType.values()) {
            if (type.getDescription().equals(tokens[0])) {
                String argument = "";
                if (tokens.length == 2) argument = tokens[1];
                return new Command(type, argument);
            }
        }
        return new Command(CommandType.UNKNOWN, "");
    }
}
