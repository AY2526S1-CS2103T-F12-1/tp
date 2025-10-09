package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.stream.Stream;

import seedu.address.logic.commands.groups.AddGroupCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.event.UniqueEventList;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupName;
import seedu.address.model.person.UniquePersonList;

/**
 * Parses input arguments and creates a new AddGroupCommand object
 */
public class AddGroupCommandParser implements Parser<AddGroupCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddGroupCommand
     * and returns an AddGroupCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddGroupCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddGroupCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME);
        GroupName name = ParserUtil.parseGroupName(argMultimap.getValue(PREFIX_NAME).get());
        UniqueEventList events = new UniqueEventList();
        UniquePersonList persons = new UniquePersonList();

        Group group = new Group(name, events, persons);

        return new AddGroupCommand(group);
    }

    /**
    * Returns true if none of the prefixes contains empty {@code Optional} values in the given
    * {@code ArgumentMultimap}.
    */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
