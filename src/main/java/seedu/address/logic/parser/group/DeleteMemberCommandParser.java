package seedu.address.logic.parser.group;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACT_INDEX;

import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.group.DeleteMemberCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteMemberCommand object
 */
public class DeleteMemberCommandParser implements Parser<DeleteMemberCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the DeleteMemberCommand
     * and returns a DeleteMemberCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public DeleteMemberCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CONTACT_INDEX);

        Set<Index> personIndexes;
        Index groupIndex;

        if (!ParserUtil.arePrefixesPresent(argMultimap, PREFIX_CONTACT_INDEX) || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteMemberCommand.MESSAGE_USAGE));
        }

        try {
            groupIndex = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteMemberCommand.MESSAGE_USAGE));
        }
        personIndexes = ParserUtil.parseContactIndexes(argMultimap.getAllValues(PREFIX_CONTACT_INDEX));


        return new DeleteMemberCommand(groupIndex, personIndexes);
    }

}
