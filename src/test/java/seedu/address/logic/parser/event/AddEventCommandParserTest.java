package seedu.address.logic.parser.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.model.event.DescriptionTest.VALID_DESCRIPTION;
import static seedu.address.model.event.DescriptionTest.VALID_DESCRIPTION_2;
import static seedu.address.model.event.EventTest.VALID_EVENT;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.event.AddEventCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class AddEventCommandParserTest {
    private final AddEventCommandParser parser = new AddEventCommandParser();
    private final String validIndex = "1";
    private final AddEventCommand validCommand = new AddEventCommand(Index.fromOneBased(1), VALID_EVENT);
    private final String expectedError = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE);

    @Test
    public void parse_validInput_givesCorrectCommand() throws Exception {
        AddEventCommand command = parser.parse(validIndex + " " + PREFIX_DESCRIPTION + VALID_DESCRIPTION);
        assertEquals(command, validCommand);
    }

    @Test
    public void trims_whiteSpace_fromIndex() throws Exception {
        AddEventCommand command = parser.parse(" " + validIndex + "  " + PREFIX_DESCRIPTION + VALID_DESCRIPTION);
        assertEquals(command, validCommand);
    }

    @Test
    public void trims_whiteSpace_fromDescription() throws Exception {
        AddEventCommand command = parser.parse(validIndex + " " + PREFIX_DESCRIPTION
                + " " + VALID_DESCRIPTION + " ");
        assertEquals(command, validCommand);
    }

    // We will leave correctly parsing the index to ParserUtil.
    @Test
    public void parse_missingIndex_throwsParseException() {
        String input = PREFIX_DESCRIPTION.toString() + VALID_DESCRIPTION;
        assertThrows(ParseException.class, expectedError, () -> parser.parse(input));
    }

    @Test
    public void parse_missingDescription_throwsParseException() {
        String input = validIndex;
        assertThrows(ParseException.class, expectedError, () -> parser.parse(input));
    }

    @Test
    public void parse_duplicateDescriptionPrefix_throwsParseException() {
        String input = validIndex + " "
                + PREFIX_DESCRIPTION + VALID_DESCRIPTION + " "
                + PREFIX_DESCRIPTION + VALID_DESCRIPTION_2;
        assertThrows(ParseException.class,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DESCRIPTION), () -> parser.parse(input));
    }

    @Test
    public void parse_wrongPrefix_throwsParseException() {
        String input = validIndex + " " + PREFIX_NAME + VALID_DESCRIPTION;
        assertThrows(ParseException.class, expectedError, () -> parser.parse(input));
    }

    @Test
    public void parse_emptyInput_throwsParseException() {
        String input = "";
        assertThrows(ParseException.class, expectedError, () -> parser.parse(input));
    }
}
