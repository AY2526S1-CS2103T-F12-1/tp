package seedu.address.logic.parser.event;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_INDEX;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.event.EditEventCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.event.Description;

/**
 * Parses input arguments and creates a new EditEventCommand object.
 */
public class EditEventCommandParser implements Parser<EditEventCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the EditEventCommand
     * and returns a EditEventCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditEventCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_EVENT_INDEX, PREFIX_DESCRIPTION.once());

        Index groupIndex;
        Index eventIndex;
        Description description;

        try {
            groupIndex = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditEventCommand.MESSAGE_USAGE), pe);
        }
        if (!ParserUtil.arePrefixesPresent(argMultimap, PREFIX_EVENT_INDEX, PREFIX_DESCRIPTION)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditEventCommand.MESSAGE_USAGE));
        }
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_EVENT_INDEX, PREFIX_DESCRIPTION);

        eventIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_EVENT_INDEX).get());
        description = ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION).get());

        return new EditEventCommand(groupIndex, eventIndex, description);
    }
}
