package kpei.commands.user;

import kpei.commands.flags.IntFlag;
import kpei.commands.parser.ParsedCommand;
import kpei.exceptions.BertException;
import kpei.exceptions.MissingArgumentException;

import java.util.List;

public class UnmarkCommand extends Command<Integer> {
    private IntFlag indexFlag;

    public UnmarkCommand() {
        commandType = "unmark";
        aliases = List.of("um", "unmark");
        maxArgs = 1;
        indexFlag = new IntFlag("index", 0, List.of("/i", "-i", "--index"), "Task index.");
    }

    public Integer parse(ParsedCommand parsedCommand) throws BertException {
        checkArgCount(parsedCommand);

        Integer index = indexFlag.parse(parsedCommand);
        return index;
    }

    @Override
    public String getSyntax() {
        return commandType + " " + indexFlag.formatArgument();
    }
}
