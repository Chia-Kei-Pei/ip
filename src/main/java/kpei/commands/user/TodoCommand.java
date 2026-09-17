package kpei.commands.user;

import kpei.commands.parser.ParsedCommand;
import kpei.commands.flags.StringFlag;
import kpei.datatypes.Task;
import kpei.exceptions.BertException;
import kpei.exceptions.MissingArgumentException;

import java.util.List;

public class TodoCommand extends Command<Task> {
    private final StringFlag descriptionFlag;
    private final String SYNTAX;

    public TodoCommand() {
        commandType = "todo";
        aliases = List.of("t", "todo");
        maxArgs = 1;
        descriptionFlag = new StringFlag("description",0, List.of("/d", "-d", "--description"));
        SYNTAX = "todo <description>";
    }

    public Task parse(ParsedCommand parsedCommand) throws BertException {
        checkArgCount(parsedCommand);

        try {
            String description = descriptionFlag.parse(parsedCommand);
            return new Task(description);
        } catch (MissingArgumentException e) {
            String msg = e.getMessage() + "\nSyntax: " + SYNTAX;
            throw new BertException(msg);
        }
    }
}
