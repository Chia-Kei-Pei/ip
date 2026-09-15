package kpei.commands.user;

import kpei.commands.flags.dateFlag;
import kpei.commands.flags.stringFlag;
import kpei.commands.flags.timeFlag;
import kpei.commands.parser.ParsedCommand;
import kpei.datatypes.Event;
import kpei.datatypes.Task;
import kpei.exceptions.MissingArgumentException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class eventCommand extends Command<Task> {
    private stringFlag descriptionFlag;
    private dateFlag fromDateFlag;
    private timeFlag fromTimeFlag;
    private dateFlag toDateFlag;
    private timeFlag toTimeFlag;

    public eventCommand() {
        super(List.of("e", "event"));
        descriptionFlag = new stringFlag("description",0, List.of("/d", "-d", "--description"));
        fromDateFlag = new dateFlag("fromDate", 1, List.of("/f", "-f", "--fromDate"));
        fromTimeFlag = new timeFlag("fromTime", 2, List.of("/ff", "-ff", "--fromTime"),
                LocalTime.parse("00:00"));
        toDateFlag = new dateFlag("toDate", 3, List.of("/t", "-t", "--toDate"));
        toTimeFlag = new timeFlag("toTime", 4, List.of("/tt", "-tt", "--toTime"),
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
