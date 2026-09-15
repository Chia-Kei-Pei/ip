package kpei.commands.user;

import kpei.commands.parser.ParsedCommand;
import kpei.exceptions.BertException;
import kpei.exceptions.MissingArgumentException;

import java.util.List;

public abstract class Command<T> {
    private String commandType;
    private int maxArgs;
    private List<String> aliases;

    public Command(String commandType, List<String> aliases, int maxArgs) {
        this.commandType = commandType;
        this.aliases = aliases;
        this.maxArgs = maxArgs;
    }

    public abstract T execute(ParsedCommand parsedCommand) throws BertException;

    public boolean isMatch(ParsedCommand parsedCommand) {
        return aliases.contains(parsedCommand.getCommandType());
    }

    public void checkArgCount(ParsedCommand parsedCommand) throws BertException {
        int totalargs = parsedCommand.getArguments().size() + parsedCommand.getFlags().size();
        if (totalargs > maxArgs) {
            throw new BertException(String.format("'%s' command does not take more than %d arguments.", commandType, maxArgs));
        }
    }
}
