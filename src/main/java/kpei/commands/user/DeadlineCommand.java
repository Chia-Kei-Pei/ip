package kpei.commands.user;

import kpei.commands.parser.ParsedCommand;
import kpei.commands.flags.DateFlag;
import kpei.commands.flags.StringFlag;
import kpei.commands.flags.TimeFlag;
import kpei.datatypes.Deadline;
import kpei.datatypes.Task;
import kpei.exceptions.BertException;
import kpei.exceptions.MissingArgumentException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class DeadlineCommand extends Command<Task> {
    private StringFlag descriptionFlag;
    private DateFlag byDateFlag;
    private TimeFlag byTimeFlag;

    public DeadlineCommand() {
        super("deadline", List.of("d", "deadline"), 3);
        descriptionFlag = new StringFlag("description",0, List.of("/d", "-d", "--description"));
        byDateFlag = new DateFlag("byDate", 1, List.of("/b", "-b", "--byDate"));
        byTimeFlag = new TimeFlag("byTime", 2, List.of("/bb", "-bb", "--byTime"),
                LocalTime.parse("00:00"));
    }

    public Task execute(ParsedCommand parsedCommand) throws BertException {
        checkArgCount(parsedCommand);

        String description = descriptionFlag.parse(parsedCommand);
        LocalDate byDate = byDateFlag.parse(parsedCommand);
        LocalTime byTime = byTimeFlag.parse(parsedCommand);
        LocalDateTime byDateTime = byDate.atTime(byTime);
        return new Deadline(description, byDateTime);
    }
}
