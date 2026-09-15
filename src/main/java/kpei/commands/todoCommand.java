package kpei.commands;

import kpei.datatypes.Task;
import kpei.exceptions.MissingArgumentException;
import kpei.parser.ParsedCommand;

import java.util.List;

public class todoCommand implements Command<Task> {
    stringFlag description;
    List<String> aliases;

    public todoCommand() {
        description = new stringFlag("description",0, List.of("/d", "-d", "--description"));
        aliases = List.of("t", "todo");
    }

    public Task execute(ParsedCommand parsedCommand) throws MissingArgumentException {
        String descriptionStr = description.parse(parsedCommand);
        return new Task(descriptionStr);
    }
}
