package kpei.commands.user;

import kpei.commands.flags.IntFlag;
import kpei.commands.parser.ParsedCommand;
import kpei.exceptions.BertException;
import kpei.exceptions.MissingArgumentException;

import java.util.List;

public class MarkCommand extends Command<Integer> {
    private IntFlag indexFlag;

    public MarkCommand() {
        super("mark", List.of("m", "mark"), 1);
        indexFlag = new IntFlag("index",0, List.of("/i", "-i", "--index"));
    }

    public Integer parse(ParsedCommand parsedCommand) throws BertException {
        checkArgCount(parsedCommand);

        Integer index = indexFlag.parse(parsedCommand);
        return index;
    }
}
