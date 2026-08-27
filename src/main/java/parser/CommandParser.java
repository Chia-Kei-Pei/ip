package parser;

import exceptions.BertException;
import exceptions.UnknownCommandException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Parses raw user input strings into structured {@link ParsedCommand} objects.
 * <p>
 * Supports command words, positional arguments with or without quotation marks,
 * and named flags prefixed with {@code /}, {@code --}, {@code -}, or context-specific bare keywords.
 * </p>
 */
public class CommandParser {

    /**
     * Tokenizes a raw input string into individual tokens, taking quoted strings into account.
     * Characters enclosed in single or double quotes are treated as a single token.
     *
     * @param input The raw input line.
     * @return A list of extracted string tokens.
     */
    public static List<String> tokenize(String input) {
        List<String> tokens = new ArrayList<>();
        if (input == null || input.isBlank()) {
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
     * Supports `/flag`, `--flag`, `-flag`, and command-specific keywords like `by`, `from`, `to`.
     *
     * @param token The token string to inspect.
     * @param commandType The command context in lowercase.
     * @return The normalized flag name (without prefixes), or {@code null} if the token is not a flag.
     */
    private static String extractFlagName(String token, String commandType) {
        if (token.startsWith("/")) {
            return token.substring(1).toLowerCase();
        }
        if (token.startsWith("--")) {
            return token.substring(2).toLowerCase();
        }
        if (token.startsWith("-") && token.length() > 1 && !Character.isDigit(token.charAt(1))) {
            return token.substring(1).toLowerCase();
        }
        // Context-aware flag keywords
        if (commandType.equals("deadline") && token.equalsIgnoreCase("by")) {
            return "by";
        }
        if (commandType.equals("event") && (token.equalsIgnoreCase("from") || token.equalsIgnoreCase("to"))) {
            return token.toLowerCase();
        }
        return null;
    }

    /**
     * Parses a raw command string from the user into a {@link ParsedCommand}.
     * Validates required arguments and flags for known command types.
     *
     * @param rawInput The raw input string entered by the user.
     * @return A {@link ParsedCommand} containing the parsed command type, arguments, and flags.
     * @throws BertException If the command type is unknown or invalid.
     * @throws IllegalArgumentException If required fields or flags are missing or empty.
     */
    public static ParsedCommand parse(String rawInput) throws BertException, IllegalArgumentException {
        List<String> tokens = tokenize(rawInput);
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("Command should not be empty");
        }

        String commandType = tokens.get(0).toLowerCase();
        List<String> positionalTokens = new ArrayList<>();
        Map<String, StringBuilder> flagBuilders = new LinkedHashMap<>();
        String currentFlag = null;

        for (int i = 1; i < tokens.size(); i++) {
            String token = tokens.get(i);
            String flagName = extractFlagName(token, commandType);

            if (flagName != null) {
                currentFlag = flagName;
                flagBuilders.putIfAbsent(currentFlag, new StringBuilder());
            } else if (currentFlag == null) {
                positionalTokens.add(token);
            } else {
                StringBuilder flagBuilder = flagBuilders.get(currentFlag);
                if (!flagBuilder.isEmpty()) {
                    flagBuilder.append(" ");
                }
                flagBuilder.append(token);
            }
        }

        String argument = String.join(" ", positionalTokens).trim();
        Map<String, String> flags = new LinkedHashMap<>();
        for (Map.Entry<String, StringBuilder> entry : flagBuilders.entrySet()) {
            flags.put(entry.getKey(), entry.getValue().toString().trim());
        }

        validateCommand(commandType, argument, flags);

        return new ParsedCommand(commandType, argument, flags);
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
                throw new IllegalArgumentException("Name of todo should not be empty");
            }
            break;
        case "deadline":
            if (argument.isEmpty()) {
                throw new IllegalArgumentException("Name of deadline should not be empty");
            }
            String byDate = flags.get("by");
            if (byDate == null || byDate.isEmpty()) {
                throw new IllegalArgumentException("ByDate of deadline should not be empty");
            }
            break;
        case "event":
            if (argument.isEmpty()) {
                throw new IllegalArgumentException("Name of event should not be empty");
            }
            String fromDate = flags.get("from");
            if (fromDate == null || fromDate.isEmpty()) {
                throw new IllegalArgumentException("FromDate of event should not be empty");
            }
            String toDate = flags.get("to");
            if (toDate == null || toDate.isEmpty()) {
                throw new IllegalArgumentException("ToDate of event should not be empty");
            }
            break;
        case "mark", "unmark", "delete", "remove":
            if (argument.isEmpty()) {
                throw new IllegalArgumentException("Index must be specified for " + commandType);
            }
            break;
        case "list", "bye", "exit", "quit":
            break;
        default:
            throw new UnknownCommandException(commandType);
        }
    }
}
