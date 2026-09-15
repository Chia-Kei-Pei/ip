package kpei.commands.user;

import kpei.commands.parser.ParsedCommand;
import kpei.commands.flags.StringFlag;
import kpei.datatypes.Task;
import kpei.exceptions.MissingArgumentException;

import java.util.List;

public class TodoCommand extends Command<Task> {
    private StringFlag descriptionFlag;

    public TodoCommand() {
        super(List.of("t", "todo"));
        descriptionFlag = new StringFlag("description",0, List.of("/d", "-d", "--description"));
    }

    public Task execute(ParsedCommand parsedCommand) throws MissingArgumentException {
        String descriptionStr = descriptionFlag.parse(parsedCommand);
        return new Task(descriptionStr);
    }
}
