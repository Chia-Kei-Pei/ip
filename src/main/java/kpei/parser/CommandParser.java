package kpei.parser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import kpei.exceptions.BertException;
import kpei.exceptions.UnknownCommandException;

/**
 * Parses raw user input strings into structured {@link ParsedCommand} objects.
 * <p>
 * Enforces strict single-token arguments and flag values. Arguments or flag values
 * containing spaces must be enclosed in quotation marks (e.g. {@code "wash the dishes"}).
 * Single-word arguments without spaces (or connected by underscores) do not require quotation marks.
 * </p>
 */
public class CommandParser {

    /**
     * Parses a raw command string from the user into a {@link ParsedCommand}.
     * Enforces that each argument or flag value is a single token or a quoted string.
     *
     * @param rawInput The raw input string entered by the user.
     * @return A {@link ParsedCommand} containing the parsed command type, arguments, and flags.
     * @throws BertException If the command type is unknown or invalid.
     * @throws IllegalArgumentException If arguments with spaces are not quoted, or required fields are missing.
     */
    public static ParsedCommand parse(String rawInput) throws BertException, IllegalArgumentException {
        List<String> tokens = tokenize(rawInput);
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("Command should not be empty");
        }

        String commandType = tokens.get(0).toLowerCase();
        List<String> arguments = new ArrayList<>();
        Map<String, String> flags = new LinkedHashMap<>();

        collectArgumentsAndFlags(tokens, commandType, arguments, flags);
        String argument = arguments.isEmpty() ? "" : arguments.get(0);

        validateCommand(commandType, argument, flags);

        return new ParsedCommand(commandType, argument, flags);
    }

    /**
     * Tokenizes a raw input string into individual tokens, taking quoted strings into account.
     * Characters enclosed in single or double quotes are treated as a single token.
     *
     * @param input The raw input line.
     * @return A list of extracted string tokens.
     */
    public static List<String> tokenize(String input) {
        List<String> tokens = new ArrayList<>();
        if (input.isBlank()) {
            return tokens;
        }

        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        char quoteChar = '"';

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if ((c == '"' || c == '\'') && (!inQuotes || c == quoteChar)) {
                if (inQuotes) {
                    inQuotes = false;
                } else {
                    inQuotes = true;
                    quoteChar = c;
                }
            } else if (Character.isWhitespace(c) && !inQuotes) {
                if (!current.isEmpty()) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
            } else {
                current.append(c);
            }
        }

        if (!current.isEmpty()) {
            tokens.add(current.toString());
        }

        return tokens;
    }

    /**
     * Determines whether a token represents a flag identifier, and returns its normalized name.
     * Supports `/flag`, `--flag`, `-flag`, and context-specific bare keywords like `by`, `from`, `to`.
     *
     * @param token The token string to inspect.
     * @param commandType The command context in lowercase.
     * @return The normalized flag name, if the token is a flag.
     */
    private static Optional<String> extractFlagName(String token, String commandType) {
        if (token.startsWith("/")) {
            return Optional.of(token.substring(1).toLowerCase());
        }
        if (token.startsWith("--")) {
            return Optional.of(token.substring(2).toLowerCase());
        }
        if (token.startsWith("-") && token.length() > 1 && !Character.isDigit(token.charAt(1))) {
            return Optional.of(token.substring(1).toLowerCase());
        }
        if (commandType.equals("deadline") && token.equalsIgnoreCase("by")) {
            return Optional.of("by");
        }
        if (commandType.equals("event") && (token.equalsIgnoreCase("from")
                || token.equalsIgnoreCase("to"))) {
            return Optional.of(token.toLowerCase());
        }
        return Optional.empty();
    }

    /**
     * Collects the positional argument and named flags from command tokens.
     *
     * @param tokens The tokens in the command.
     * @param commandType The command word.
     * @param arguments The list receiving the positional argument.
     * @param flags The map receiving flag names and values.
     */
    private static void collectArgumentsAndFlags(List<String> tokens, String commandType, List<String> arguments,
                                                 Map<String, String> flags) {
        int tokenIndex = 1;
        while (tokenIndex < tokens.size()) {
            String token = tokens.get(tokenIndex);
            Optional<String> flagName = extractFlagName(token, commandType);

            if (isFlag(flagName, arguments, token)) {
                tokenIndex = addFlag(tokens, tokenIndex, token, flagName.orElseThrow(), flags);
            } else {
                addArgument(arguments, token);
                tokenIndex++;
            }
        }
    }

    /**
     * Checks whether a token should be treated as a flag in the current command context.
     *
     * @param flagName The optional normalized flag name.
     * @param arguments The positional arguments parsed so far.
     * @param token The original token.
     * @return {@code true} if the token is a flag, {@code false} otherwise.
     */
    private static boolean isFlag(Optional<String> flagName, List<String> arguments, String token) {
        return flagName.isPresent()
                && (!arguments.isEmpty() || token.startsWith("/") || token.startsWith("-"));
    }

    /**
     * Adds a flag and its value to the parsed flag map.
     *
     * @param tokens The tokens in the command.
     * @param flagIndex The index of the flag token.
     * @param token The original flag token.
     * @param flagName The normalized flag name.
     * @param flags The map receiving the flag value.
     * @return The index of the next unprocessed token.
     */
    private static int addFlag(List<String> tokens, int flagIndex, String token, String flagName,
                               Map<String, String> flags) {
        if (flagIndex + 1 >= tokens.size()) {
            throw new IllegalArgumentException("Missing value for flag: " + token);
        }

        String flagValue = tokens.get(flagIndex + 1);
        if (flagValue.startsWith("/") || flagValue.startsWith("--")) {
            throw new IllegalArgumentException("Missing value for flag: " + token);
        }

        flags.put(flagName, flagValue);
        return flagIndex + 2;
    }

    /**
     * Adds the command's only positional argument.
     *
     * @param arguments The list receiving the positional argument.
     * @param token The token to add as an argument.
     */
    private static void addArgument(List<String> arguments, String token) {
        if (!arguments.isEmpty()) {
            throw new IllegalArgumentException(
                    "Unexpected argument: \"" + token + "\"."
                    + " Arguments containing spaces must be enclosed in quotes.");
        }

        arguments.add(token);
    }

    /**
     * Validates that the parsed arguments and flags satisfy the constraints of the given command.
     *
     * @param commandType The command word.
     * @param argument The positional argument string.
     * @param flags The map of parsed flags.
     * @throws BertException If the command is unrecognized.
     * @throws IllegalArgumentException If a mandatory argument or flag is missing.
     */
    private static void validateCommand(String commandType, String argument, Map<String, String> flags)
            throws BertException, IllegalArgumentException {
        switch (commandType) {
            case "todo":
                if (argument.isEmpty()) {
                    throw new IllegalArgumentException("Task description should not be empty");
                }
                break;
            case "deadline":
                if (argument.isEmpty()) {
                    throw new IllegalArgumentException("Name of deadline should not be empty");
                }
                if (!flags.containsKey("by") || flags.get("by").isEmpty()) {
                    throw new IllegalArgumentException("ByDate of deadline should not be empty");
                }
                break;
            case "event":
                if (argument.isEmpty()) {
                    throw new IllegalArgumentException("Name of event should not be empty");
                }
                if (!flags.containsKey("from") || flags.get("from").isEmpty()) {
                    throw new IllegalArgumentException("FromDate of event should not be empty");
                }
                if (!flags.containsKey("to") || flags.get("to").isEmpty()) {
                    throw new IllegalArgumentException("ToDate of event should not be empty");
                }
                break;
            case "mark", "unmark", "delete", "remove":
                if (argument.isEmpty()) {
                    throw new IllegalArgumentException("Index must be specified for " + commandType);
                }
                break;
            case "find":
                if (argument.isEmpty()) {
                    throw new IllegalArgumentException("Keyword for find should not be empty");
                }
                break;
            case "list", "bye", "exit", "quit":
                break;
            default:
                throw new UnknownCommandException(commandType);
        }
    }
}
