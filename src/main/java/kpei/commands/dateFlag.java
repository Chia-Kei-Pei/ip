package kpei.commands;

import kpei.exceptions.MissingArgumentException;
import kpei.parser.ParsedCommand;

import java.time.LocalDate;
import java.util.List;

public class dateFlag extends flag<LocalDate> {

    public dateFlag(String argument, int position, List<String> aliases) {
        super(argument, position, aliases);
    }

    public dateFlag(String argument, int position, List<String> aliases, LocalDate defaultValue) {
        super(argument, position, aliases, defaultValue);
    }

    @Override
    public LocalDate parse(ParsedCommand parsedCommand) throws MissingArgumentException {
        try {
            String value = getValue(parsedCommand);
            assert !value.isBlank() : "value of argument should be initialized";
            return LocalDate.parse(value);
        } catch (MissingArgumentException e) {
            if (isRequired) {
                throw e;
            }
            return defaultValue;
        }
    }
}
