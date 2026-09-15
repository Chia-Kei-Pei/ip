package kpei.commands.flags;

import kpei.commands.parser.ParsedCommand;
import kpei.exceptions.MissingArgumentException;

import java.time.LocalDate;
import java.util.List;

public class DateFlag extends Flag<LocalDate> {

    public DateFlag(String argument, int position, List<String> aliases) {
        super(argument, position, aliases);
    }

    public DateFlag(String argument, int position, List<String> aliases, LocalDate defaultValue) {
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
