package kpei.commands.user;

import kpei.commands.parser.ParsedCommand;
import kpei.exceptions.BertException;

import java.util.List;

public class ListCommand extends Command<Boolean> {

    public ListCommand() {
        super(List.of("ls", "list", "dir"));
    }

    public Boolean execute(ParsedCommand parsedCommand) throws BertException {
        if (!(parsedCommand.getArguments().isEmpty() && parsedCommand.getFlags().isEmpty())) {
            throw new BertException("'list' command does not take any arguments");
        }
        return Boolean.TRUE;
    }
}
