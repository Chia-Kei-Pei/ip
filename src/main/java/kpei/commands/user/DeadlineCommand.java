package kpei.commands.user;

import kpei.commands.parser.ParsedCommand;
import kpei.commands.flags.DateFlag;
import kpei.commands.flags.StringFlag;
import kpei.commands.flags.TimeFlag;
import kpei.datatypes.Deadline;
import kpei.datatypes.Task;
import kpei.exceptions.BertException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DeadlineCommand extends Command<Task> {
    private final StringFlag descriptionFlag;
    private final DateFlag byDateFlag;
    private final TimeFlag byTimeFlag;

    public DeadlineCommand() {
        super("deadline", List.of("d", "deadline"), 3);
        descriptionFlag = new StringFlag("description",0, List.of("/d", "-d", "--description"));
        byDateFlag = new DateFlag("byDate", 1, List.of("/b", "-b", "--byDate"));
        byTimeFlag = new TimeFlag("byTime", 2, List.of("/bb", "-bb", "--byTime"),
                LocalTime.MIDNIGHT);
    }

    public Task parse(ParsedCommand parsedCommand) throws BertException {
        checkArgCount(parsedCommand);

        String description = descriptionFlag.parse(parsedCommand);
        LocalDate byDate = byDateFlag.parse(parsedCommand);
        LocalTime byTime = byTimeFlag.parse(parsedCommand);
        return new Deadline(description, byDate, byTime);
    }
}
