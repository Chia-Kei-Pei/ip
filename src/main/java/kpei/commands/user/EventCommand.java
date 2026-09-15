package kpei.commands.user;

import kpei.commands.flags.DateFlag;
import kpei.commands.flags.StringFlag;
import kpei.commands.flags.TimeFlag;
import kpei.commands.parser.ParsedCommand;
import kpei.datatypes.Event;
import kpei.datatypes.Task;
import kpei.exceptions.MissingArgumentException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class EventCommand extends Command<Task> {
    private StringFlag descriptionFlag;
    private DateFlag fromDateFlag;
    private TimeFlag fromTimeFlag;
    private DateFlag toDateFlag;
    private TimeFlag toTimeFlag;

    public EventCommand() {
        super(List.of("e", "event"));
        descriptionFlag = new StringFlag("description",0, List.of("/d", "-d", "--description"));
        fromDateFlag = new DateFlag("fromDate", 1, List.of("/f", "-f", "--fromDate"));
        fromTimeFlag = new TimeFlag("fromTime", 2, List.of("/ff", "-ff", "--fromTime"),
                LocalTime.parse("00:00"));
        toDateFlag = new DateFlag("toDate", 3, List.of("/t", "-t", "--toDate"));
        toTimeFlag = new TimeFlag("toTime", 4, List.of("/tt", "-tt", "--toTime"),
                LocalTime.parse("00:00"));
    }

    public Task execute(ParsedCommand parsedCommand) throws MissingArgumentException {
        String description = descriptionFlag.parse(parsedCommand);
        LocalDate fromDate = fromDateFlag.parse(parsedCommand);
        LocalTime fromTime = fromTimeFlag.parse(parsedCommand);
        LocalDate toDate = toDateFlag.parse(parsedCommand);
        LocalTime toTime = toTimeFlag.parse(parsedCommand);
        LocalDateTime byDateTime = fromDate.atTime(fromTime);
        LocalDateTime toDateTime = toDate.atTime(toTime);
        return new Event(description, byDateTime, toDateTime);
    }
}
