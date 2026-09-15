package kpei.commands;

import kpei.datatypes.Task;
import kpei.exceptions.MissingArgumentException;
import kpei.parser.ParsedCommand;

import java.util.List;

public class todoCommand implements Command<Task> {
    stringFlag description;

    public todoCommand() {
        this.description = new stringFlag("description",0, List.of("/d", "-d", "--description"));
    }

    public Task execute(ParsedCommand parsedCommand) throws MissingArgumentException {
        String descriptionStr = description.parse(parsedCommand);
        return new Task(descriptionStr);
    }
}
