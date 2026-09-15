package kpei.commands;

import kpei.exceptions.MissingArgumentException;

public interface Command<T> {
    T execute(ParsedCommand parsedCommand) throws MissingArgumentException;
}
