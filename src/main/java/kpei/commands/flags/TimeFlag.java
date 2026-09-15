package kpei.commands.flags;

import kpei.commands.parser.ParsedCommand;
import kpei.exceptions.BertException;
import kpei.exceptions.MissingArgumentException;
import kpei.utility.DateTimeParser;

import java.time.LocalTime;
import java.util.List;

public class TimeFlag extends Flag<LocalTime> {

    public TimeFlag(String argument, int position, List<String> aliases) {
        super(argument, position, aliases);
    }

    public TimeFlag(String argument, int position, List<String> aliases, LocalTime defaultValue) {
        super(argument, position, aliases, defaultValue);
    }

    @Override
    public LocalTime parse(ParsedCommand parsedCommand) throws BertException {
        try {
            String value = getValue(parsedCommand);
            assert !value.isBlank() : "value of argument should be initialized";
            return DateTimeParser.parseTime(value);
        } catch (MissingArgumentException e) {
            if (isRequired) {
                throw e;
            }
            return defaultValue;
        }
    }
}
