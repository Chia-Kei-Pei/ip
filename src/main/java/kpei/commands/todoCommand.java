package kpei.commands;

import kpei.commands.flags.stringFlag;
import kpei.datatypes.Task;
import kpei.exceptions.MissingArgumentException;

import java.util.List;

public class todoCommand extends Command<Task> {
    private stringFlag descriptionFlag;

    public todoCommand() {
        super(List.of("t", "todo"));
        descriptionFlag = new stringFlag("description",0, List.of("/d", "-d", "--description"));
    }

    public Task execute(ParsedCommand parsedCommand) throws MissingArgumentException {
        String descriptionStr = descriptionFlag.parse(parsedCommand);
        return new Task(descriptionStr);
    }
}
