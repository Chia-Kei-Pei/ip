package kpei.commands.user;

import kpei.commands.parser.ParsedCommand;
import kpei.exceptions.BertException;

import java.util.List;

public class ListCommand extends Command<Boolean> {

    public ListCommand() {
        super("list", List.of("ls", "list", "dir"), 0);
    }

    public Boolean execute(ParsedCommand parsedCommand) throws BertException {
        checkArgCount(parsedCommand);

        return Boolean.TRUE;
    }
}
