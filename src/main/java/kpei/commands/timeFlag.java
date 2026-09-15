package kpei.commands;

import kpei.exceptions.MissingArgumentException;

import java.time.LocalTime;
import java.util.List;

public class timeFlag extends flag<LocalTime> {

    public timeFlag(String argument, int position, List<String> aliases) {
        super(argument, position, aliases);
    }

    public timeFlag(String argument, int position, List<String> aliases, LocalTime defaultValue) {
        super(argument, position, aliases, defaultValue);
    }

    @Override
    public LocalTime parse(ParsedCommand parsedCommand) throws MissingArgumentException {
        try {
            String value = getValue(parsedCommand);
            assert !value.isBlank() : "value of argument should be initialized";
            return LocalTime.parse(value);
        } catch (MissingArgumentException e) {
            if (isRequired) {
                throw e;
            }
            return defaultValue;
        }
    }
}
