package kpei.commands.flags;

import kpei.commands.parser.ParsedCommand;
import kpei.exceptions.MissingArgumentException;

import java.util.List;

public class IntFlag extends Flag<Integer> {

    public IntFlag(String argument, int position, List<String> aliases) {
        super(argument, position, aliases);
    }

    public IntFlag(String argument, int position, List<String> aliases, Integer defaultValue) {
        super(argument, position, aliases, defaultValue);
    }

    @Override
    public Integer parse(ParsedCommand parsedCommand) throws MissingArgumentException {
        try {
            String value = getValue(parsedCommand);
            assert !value.isBlank() : "value of argument should be initialized";
            Integer integerValue = Integer.valueOf(value);
            return integerValue;
        } catch (MissingArgumentException e) {
            if (isRequired) {
                throw e;
            }
            return defaultValue;
        }
    }
}
