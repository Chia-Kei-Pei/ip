package kpei.commands;

import kpei.exceptions.MissingArgumentException;
import kpei.parser.ParsedCommand;

public interface Command<T> {
    public T execute(ParsedCommand parsedCommand) throws MissingArgumentException;
}
