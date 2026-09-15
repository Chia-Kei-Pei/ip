package kpei.commands.user;

import kpei.commands.parser.ParsedCommand;
import kpei.exceptions.BertException;

import java.util.List;

public class ExitCommand extends Command<Boolean> {

    public ExitCommand() {
        super(List.of("q", "quit", "exit", "bye"));
    }

    public Boolean execute(ParsedCommand parsedCommand) throws BertException {
        if (!(parsedCommand.getArguments().isEmpty() && parsedCommand.getFlags().isEmpty())) {
            throw new BertException("'Exit' command does not take any arguments");
        }
        return Boolean.TRUE;
    }
}
