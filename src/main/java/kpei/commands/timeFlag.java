package kpei.commands;

import kpei.exceptions.MissingArgumentException;
import kpei.parser.ParsedCommand;

import java.time.LocalTime;
import java.util.List;

public class timeFlag extends flag<LocalTime> {

    public timeFlag(String argument, int position, List<String> aliases, boolean isRequired, LocalTime defaultValue) {
        super(argument, position, aliases, isRequired, defaultValue);
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
