package kpei.commands.flags;

import kpei.commands.parser.ParsedCommand;
import kpei.exceptions.MissingArgumentException;

import java.util.List;

public abstract class Flag<T> {
    protected final String argument;
    protected final int position;
    protected List<String> aliases;
    protected final boolean isRequired;
    protected final T defaultValue;

    public Flag(String argument, int position, List<String> aliases) {
        this(argument, position, aliases, true, null);
    }

    public Flag(String argument, int position, List<String> aliases, T defaultValue) {
        this(argument, position, aliases, false, defaultValue);
    }

    private Flag(String argument, int position, List<String> aliases, boolean isRequired, T defaultValue) {
        this.argument = argument;
        this.position = position;
        this.aliases = aliases;
        this.isRequired = isRequired;
        this.defaultValue = defaultValue;
    }

    public String getValue(ParsedCommand parsedCommand) throws MissingArgumentException {
        String value = null;
        if (position < parsedCommand.getArguments().size()) {
            value = parsedCommand.getArguments().get(position);
        } else {
            for (String alias : aliases) {
                value = parsedCommand.getFlags().get(alias);
                if (value != null) {
                    break;
                }
            }
        }

        if (value == null) {
            throw new MissingArgumentException(argument);
        }

        return value;
    }

    public abstract T parse(ParsedCommand parsedCommand) throws MissingArgumentException;
}
