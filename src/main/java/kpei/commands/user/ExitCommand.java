package kpei.commands.user;

import kpei.commands.parser.ParsedCommand;
import kpei.exceptions.BertException;

import java.util.List;

public class ExitCommand extends Command<Boolean> {

    public ExitCommand() {
        super("exit", List.of("q", "quit", "exit", "bye"), 0);
    }

    public Boolean parse(ParsedCommand parsedCommand) throws BertException {
        checkArgCount(parsedCommand);
        return Boolean.TRUE;
    }
}
