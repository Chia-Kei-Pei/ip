package kpei.commands.user;

import kpei.commands.parser.ParsedCommand;
import kpei.exceptions.BertException;
import kpei.exceptions.MissingArgumentException;

import java.util.List;

public abstract class Command<T> {
    private List<String> aliases;

    public Command(List<String> aliases) {
        this.aliases = aliases;
    }

    public abstract T execute(ParsedCommand parsedCommand) throws BertException;

    public boolean isMatch(ParsedCommand parsedCommand) {
        return aliases.contains(parsedCommand.getCommandType());
    }
}
