package kpei.commands.user;

import kpei.commands.flags.StringFlag;
import kpei.commands.parser.ParsedCommand;
import kpei.datatypes.Task;
import kpei.exceptions.BertException;
import kpei.exceptions.MissingArgumentException;

import java.util.List;

public class FindCommand extends Command<String> {
    private StringFlag descriptionFlag;

    public FindCommand() {
        super("find", List.of("f", "find"), 1);
        descriptionFlag = new StringFlag("description",0, List.of("/d", "-d", "--description"));
    }

    public String execute(ParsedCommand parsedCommand) throws BertException {
        checkArgCount(parsedCommand);

        String description = descriptionFlag.parse(parsedCommand);
        return description;
    }
}
