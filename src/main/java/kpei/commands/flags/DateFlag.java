package kpei.commands.flags;

import kpei.commands.parser.ParsedCommand;
import kpei.exceptions.BertException;
import kpei.exceptions.MissingArgumentException;
import kpei.utility.DateTimeParser;

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
    public LocalDate parse(ParsedCommand parsedCommand) throws BertException {
        try {
            String value = getValue(parsedCommand);
            assert !value.isBlank() : "value of argument should be initialized";
            return DateTimeParser.parseDate(value);
        } catch (MissingArgumentException e) {
            if (isRequired) {
                throw e;
            }
            return defaultValue;
        }
    }
}
