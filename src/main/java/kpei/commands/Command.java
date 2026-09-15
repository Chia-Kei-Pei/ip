package kpei.commands;

import kpei.exceptions.MissingArgumentException;

import java.util.List;

public abstract class Command<T> {
    private List<String> aliases;

    public Command(List<String> aliases) {
        this.aliases = aliases;
    }

    public abstract T execute(ParsedCommand parsedCommand) throws MissingArgumentException;

    public boolean isMatch(ParsedCommand parsedCommand) {
        return aliases.contains(parsedCommand.getCommandType());
    }
}
