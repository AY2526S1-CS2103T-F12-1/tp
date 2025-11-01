package seedu.address.logic.parser.group;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACT_INDEX;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_GROUP;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.group.AddMemberCommand;


public class AddMemberCommandParserTest {
    private AddMemberCommandParser parser = new AddMemberCommandParser();

    @Test
    public void parse_validArgsSinglePerson_returnsAddMemberCommand() {
        Set<Index> persons = new HashSet<>();
        persons.add(INDEX_FIRST_PERSON);

        assertParseSuccess(parser, " " + "3 " + PREFIX_CONTACT_INDEX + "1",
                new AddMemberCommand(INDEX_THIRD_GROUP, persons));
    }

    @Test
    public void parse_validArgsMultiplePersons_returnsAddMemberCommand() {
        Set<Index> persons = new HashSet<>();
        persons.add(INDEX_FIRST_PERSON);
        persons.add(INDEX_SECOND_PERSON);

        assertParseSuccess(parser, " " + "3 " + PREFIX_CONTACT_INDEX + "1 "
                        + PREFIX_CONTACT_INDEX + "2",
                new AddMemberCommand(INDEX_THIRD_GROUP, persons));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "die",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddMemberCommand.MESSAGE_USAGE));

        assertParseFailure(parser, " " + "3",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddMemberCommand.MESSAGE_USAGE));
    }

}
