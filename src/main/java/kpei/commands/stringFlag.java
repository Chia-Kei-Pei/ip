package kpei.commands;

import kpei.exceptions.MissingArgumentException;
import kpei.parser.ParsedCommand;

import java.util.List;

public class stringFlag extends flag<String> {

    public stringFlag(String argument, int position, List<String> aliases) {
        super(argument, position, aliases);
    }

    public stringFlag(String argument, int position, List<String> aliases, String defaultValue) {
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
