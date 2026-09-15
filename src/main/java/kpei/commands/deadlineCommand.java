package kpei.commands;

import kpei.commands.flags.dateFlag;
import kpei.commands.flags.stringFlag;
import kpei.commands.flags.timeFlag;
import kpei.datatypes.Deadline;
import kpei.datatypes.Task;
import kpei.exceptions.MissingArgumentException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class deadlineCommand extends Command<Task> {
    private stringFlag descriptionFlag;
    private dateFlag byDateFlag;
    private timeFlag byTimeFlag;

    public deadlineCommand() {
        super(List.of("t", "todo"));
        descriptionFlag = new stringFlag("description",0, List.of("/d", "-d", "--description"));
        byDateFlag = new dateFlag("byDate", 1, List.of("/b", "-b", "--byDate"));
        byTimeFlag = new timeFlag("byTime", 2, List.of("/bb", "-bb", "--byTime"));
    }

    public Task execute(ParsedCommand parsedCommand) throws MissingArgumentException {
        String description = descriptionFlag.parse(parsedCommand);
        LocalDate byDate = byDateFlag.parse(parsedCommand);
        LocalTime byTime = byTimeFlag.parse(parsedCommand);
        LocalDateTime byDateTime = byDate.atTime(byTime);
        return new Deadline(description, byDateTime);
    }
}
