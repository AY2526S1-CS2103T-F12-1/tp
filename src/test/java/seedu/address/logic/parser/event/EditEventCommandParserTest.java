package seedu.address.logic.parser.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.event.AddEventCommandParserTest.DOUBLE_DESCRIPTION_PREFIX_EVENT;
import static seedu.address.model.event.DescriptionTest.VALID_DESCRIPTION;
import static seedu.address.model.event.DescriptionTest.VALID_DESCRIPTION_STRING;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.event.EditEventCommand;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;

public class EditEventCommandParserTest {
    public static final String VALID_EDIT_EVENT_COMMAND_ARGS = "1 " + PREFIX_EVENT_INDEX + "1 "
            + PREFIX_DESCRIPTION + VALID_DESCRIPTION_STRING;

    private final EditEventCommandParser parser = new EditEventCommandParser();
    private final String validIndex = "1";
    private final EditEventCommand validCommand = new EditEventCommand(Index.fromOneBased(1),
            Index.fromOneBased(1), VALID_DESCRIPTION);
    private final String expectedError = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditEventCommand.MESSAGE_USAGE);

    @Test
    public void parse_validInput_givesCorrectCommand() throws Exception {
        EditEventCommand command = parser.parse(VALID_EDIT_EVENT_COMMAND_ARGS);
        assertEquals(command, validCommand);
    }

    // We will leave correctly parsing the separate arguments to ParserUtil.

    @Test
    public void parse_missingGroupIndex_throwsParseException() {
        String input = PREFIX_EVENT_INDEX + validIndex + " " + PREFIX_DESCRIPTION + VALID_DESCRIPTION_STRING;
        assertThrows(ParseException.class, expectedError, () -> parser.parse(input));
    }

    @Test
    public void parse_missingEventIndex_throwsParseException() {
        String input = validIndex + " " + PREFIX_DESCRIPTION + VALID_DESCRIPTION_STRING;
        assertThrows(ParseException.class, expectedError, () -> parser.parse(input));
    }

    @Test
    public void parse_missingDescription_throwsParseException() {
        String input = validIndex + " " + PREFIX_EVENT_INDEX + validIndex;
        assertThrows(ParseException.class, expectedError, () -> parser.parse(input));
    }

    @Test
    public void parse_duplicateEventIndexPrefix_throwsParseException() {
        String input = validIndex + " "
                + PREFIX_EVENT_INDEX + validIndex + " "
                + PREFIX_EVENT_INDEX + validIndex + " "
                + PREFIX_DESCRIPTION + VALID_DESCRIPTION_STRING;
        assertThrows(ParseException.class,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EVENT_INDEX), () -> parser.parse(input));
    }

    @Test
    public void parse_duplicateDescriptionPrefix_succeeds() throws Exception {
        String input = validIndex + " "
                + PREFIX_EVENT_INDEX + validIndex + " "
                + PREFIX_DESCRIPTION + VALID_DESCRIPTION_STRING + " "
                + PREFIX_DESCRIPTION + VALID_DESCRIPTION_STRING;
        Index index = ParserUtil.parseIndex(validIndex);
        EditEventCommand expected = new EditEventCommand(index, index,
                DOUBLE_DESCRIPTION_PREFIX_EVENT.description());
        assertEquals(expected, parser.parse(input));
    }

    @Test
    public void parse_splitDuplicateDescriptionPrefix_throwsParseException() {
        String input = validIndex + " "
                + PREFIX_DESCRIPTION + VALID_DESCRIPTION_STRING + " "
                + PREFIX_EVENT_INDEX + validIndex + " "
                + PREFIX_DESCRIPTION + VALID_DESCRIPTION_STRING;
        assertThrows(ParseException.class,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DESCRIPTION), () -> parser.parse(input));
    }

    @Test
    public void parse_wrongPrefix_throwsParseException() {
        String input = validIndex + " " + PREFIX_NAME + validIndex + " " + PREFIX_NAME + VALID_DESCRIPTION_STRING;
        assertThrows(ParseException.class, expectedError, () -> parser.parse(input));
    }

    @Test
    public void parse_emptyInput_throwsParseException() {
        String input = "";
        assertThrows(ParseException.class, expectedError, () -> parser.parse(input));
    }
}
