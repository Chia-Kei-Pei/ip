package kpei.commands.user;

import kpei.commands.flags.IntFlag;
import kpei.commands.parser.ParsedCommand;
import kpei.exceptions.MissingArgumentException;

import java.util.List;

public class RemoveCommand extends Command<Integer> {
    private IntFlag indexFlag;

    public RemoveCommand() {
        super(List.of("r", "remove", "delete"));
        indexFlag = new IntFlag("index",0, List.of("/i", "-i", "--index"));
    }

    public Integer execute(ParsedCommand parsedCommand) throws MissingArgumentException {
        Integer index = indexFlag.parse(parsedCommand);
        return index;
    }
}
