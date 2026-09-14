package kpei.parser;

import java.util.List;
import java.util.Map;

/**
 * Represents the structured result of parsing a raw command string.
 * Holds the command type, the primary argument, and any associated named flags.
 */
public class ParsedCommand {
    public String commandType;
    public List<String> arguments;
    public Map<String, String> flags;

    /**
     * Constructs a {@code ParsedCommand} with the given command type, positional arguments, and flags.
     *
     * @param commandType The primary command word in lowercase (e.g., "deadline", "list").
     * @param positionalParameters The primary positional argument (e.g., task description or index).
     * @param flaggedParameters A map of flag names to their corresponding values.
     */
    public ParsedCommand(String commandType, List<String> positionalParameters, Map<String, String> flaggedParameters) {
        this.commandType = commandType;
        this.arguments = positionalParameters;
        this.flags = flaggedParameters;
    }
}
