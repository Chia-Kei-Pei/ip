package kpei.commands;

import kpei.exceptions.MissingArgumentException;
import kpei.parser.ParsedCommand;

public interface Command<T> {
    T execute(ParsedCommand parsedCommand) throws MissingArgumentException;
}
