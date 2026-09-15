package kpei.commands.user;

import kpei.commands.parser.ParsedCommand;
import kpei.commands.flags.StringFlag;
import kpei.datatypes.Task;
import kpei.exceptions.BertException;
import kpei.exceptions.MissingArgumentException;

import java.util.List;

public class TodoCommand extends Command<Task> {
    private StringFlag descriptionFlag;

    public TodoCommand() {
        super("todo", List.of("t", "todo"), 1);
        descriptionFlag = new StringFlag("description",0, List.of("/d", "-d", "--description"));
    }

    public Task execute(ParsedCommand parsedCommand) throws BertException {
        checkArgCount(parsedCommand);

        String description = descriptionFlag.parse(parsedCommand);
        return new Task(description);
    }
}
