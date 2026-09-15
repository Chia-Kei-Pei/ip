package kpei.commands.user;

import kpei.commands.flags.DateFlag;
import kpei.commands.flags.StringFlag;
import kpei.commands.flags.TimeFlag;
import kpei.commands.parser.ParsedCommand;
import kpei.datatypes.Event;
import kpei.datatypes.Task;
import kpei.exceptions.BertException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EventCommand extends Command<Task> {
    private final StringFlag descriptionFlag;
    private final DateFlag fromDateFlag;
    private final TimeFlag fromTimeFlag;
    private final DateFlag toDateFlag;
    private final TimeFlag toTimeFlag;

    public EventCommand() {
        super("event", List.of("e", "event"), 5);
        descriptionFlag = new StringFlag("description",0, List.of("/d", "-d", "--description"));
        fromDateFlag = new DateFlag("fromDate", 1, List.of("/f", "-f", "--fromDate"));
        fromTimeFlag = new TimeFlag("fromTime", 2, List.of("/ff", "-ff", "--fromTime"),
                LocalTime.MIDNIGHT);
        toDateFlag = new DateFlag("toDate", 3, List.of("/t", "-t", "--toDate"));
        toTimeFlag = new TimeFlag("toTime", 4, List.of("/tt", "-tt", "--toTime"),
                LocalTime.MIDNIGHT);
    }

    public Task execute(ParsedCommand parsedCommand) throws BertException {
        checkArgCount(parsedCommand);

        String description = descriptionFlag.parse(parsedCommand);
        LocalDate fromDate = fromDateFlag.parse(parsedCommand);
        LocalTime fromTime = fromTimeFlag.parse(parsedCommand);
        LocalDate toDate = toDateFlag.parse(parsedCommand);
        LocalTime toTime = toTimeFlag.parse(parsedCommand);
        return new Event(description, fromDate, fromTime, toDate, toTime);
    }
}
