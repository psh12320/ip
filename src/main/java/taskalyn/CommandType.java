package taskalyn;

/**
 * Manages the type of commands available in this bot.
 */
public enum CommandType {
    BYE("bye"),
    LIST("list"),
    SORT("sort"),
    FIND("find"),
    DELETE("delete"),
    MARK("mark"),
    UNMARK("unmark"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event");

    private final String command;

    /**
     * Constructs a CommandType object with a given String command.
     *
     * @param command The command as a String.
     */
    CommandType(String command) {
        this.command = command;
    }

    /**
     * Returns a CommandType object from String stating the command.
     *
     * @param commandString User input stating the command as a String.
     * @return CommandType object with the command.
     */
    public static CommandType fromString(String commandString) {
        for (CommandType command : CommandType.values()) {
            if (command.command.equalsIgnoreCase(commandString)) {
                return command;
            }
        }
        return null;
    }
}
