package kpei.commands;

import kpei.datatypes.Task;
import kpei.exceptions.MissingArgumentException;

import java.util.List;

public class todoCommand implements Command<Task> {
    private stringFlag description;
    private List<String> aliases;

    public todoCommand() {
        description = new stringFlag("description",0, List.of("/d", "-d", "--description"));
        aliases = List.of("t", "todo");
    }

    public Task execute(ParsedCommand parsedCommand) throws MissingArgumentException {
        String descriptionStr = description.parse(parsedCommand);
        return new Task(descriptionStr);
    }

    public boolean isMatch(ParsedCommand parsedCommand) {
        return aliases.contains(parsedCommand.getCommandType());
    }
}
