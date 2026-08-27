package parser;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents the structured result of parsing a raw command string.
 * Holds the command type, the primary argument, and any associated named flags.
 */
public class ParsedCommand {
    private final String commandType;
    private final String argument;
    private final Map<String, String> flags;

    /**
     * Constructs a {@code ParsedCommand} with the given command type, positional argument, and flags.
     *
     * @param commandType The primary command word in lowercase (e.g., "todo", "deadline", "list").
     * @param argument The primary positional argument (e.g., task description or index).
     * @param flags A map of flag names to their corresponding values.
     */
    public ParsedCommand(String commandType, String argument, Map<String, String> flags) {
        this.commandType = commandType;
        this.argument = argument;
        this.flags = Collections.unmodifiableMap(new LinkedHashMap<>(flags));
    }

    /**
     * Returns the command type in lowercase.
     *
     * @return The command name (e.g., "todo", "deadline", "mark").
     */
    public String getCommandType() {
        return commandType;
    }

    /**
     * Returns the primary positional argument.
     *
     * @return The argument string, or empty string if none was provided.
     */
    public String getArgument() {
        return argument;
    }

    /**
     * Returns an unmodifiable map of all flags and their values.
     *
     * @return Map of flag name to flag value.
     */
    public Map<String, String> getFlags() {
        return flags;
    }

    /**
     * Returns the value associated with the specified flag name.
     *
     * @param flagName The name of the flag (case-insensitive).
     * @return The flag value, or {@code null} if the flag is not present.
     */
    public String getFlag(String flagName) {
        return flags.get(flagName.toLowerCase());
    }

    /**
     * Checks whether the specified flag was supplied in the command.
     *
     * @param flagName The name of the flag (case-insensitive).
     * @return {@code true} if the flag exists, {@code false} otherwise.
     */
    public boolean hasFlag(String flagName) {
        return flags.containsKey(flagName.toLowerCase());
    }

    /**
     * Parses the positional argument as an integer index.
     *
     * @return The integer value of the positional argument.
     * @throws IllegalArgumentException If the argument is empty or cannot be parsed as an integer.
     */
    public int getArgumentAsInt() throws IllegalArgumentException {
        if (argument == null || argument.isBlank()) {
            throw new IllegalArgumentException("Index must be specified for " + commandType);
        }
        try {
            return Integer.parseInt(argument.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid index format: " + argument);
        }
    }

    /**
     * Checks if this command signals termination of the application.
     *
     * @return {@code true} if the command is "bye", "exit", or "quit"; {@code false} otherwise.
     */
    public boolean isExitCommand() {
        return commandType.equals("bye") || commandType.equals("exit") || commandType.equals("quit");
    }
}
