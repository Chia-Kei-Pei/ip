package kpei.commands.flags;

import kpei.commands.parser.ParsedCommand;
import kpei.exceptions.MissingArgumentException;

import java.util.List;

public class StringFlag extends Flag<String> {

    public StringFlag(String argument, int position, List<String> aliases) {
        super(argument, position, aliases);
    }

    public StringFlag(String argument, int position, List<String> aliases, String defaultValue) {
        super(argument, position, aliases, defaultValue);
    }

    @Override
    public String parse(ParsedCommand parsedCommand) throws MissingArgumentException {
        try {
            String value = getValue(parsedCommand);
            assert !value.isBlank() : "value of argument should be initialized";
            return value;
        } catch (MissingArgumentException e) {
            if (isRequired) {
                throw e;
            }
            return defaultValue;
        }
    }
}
