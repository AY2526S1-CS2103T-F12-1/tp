package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.event.AddEventCommand;
import seedu.address.logic.commands.event.DeleteEventCommand;
import seedu.address.logic.commands.group.AddGroupCommand;
import seedu.address.logic.commands.group.AddMemberCommand;
import seedu.address.logic.commands.group.DeleteGroupCommand;
import seedu.address.logic.commands.group.DeleteMemberCommand;
import seedu.address.logic.commands.group.DeleteRepoCommand;
import seedu.address.logic.commands.group.EditGroupCommand;
import seedu.address.logic.commands.group.FindGroupCommand;
import seedu.address.logic.commands.group.GetRepoCommand;
import seedu.address.logic.commands.group.ListGroupCommand;
import seedu.address.logic.commands.group.SetRepoCommand;
import seedu.address.logic.commands.person.AddCommand;
import seedu.address.logic.commands.person.DeleteCommand;
import seedu.address.logic.commands.person.EditCommand;
import seedu.address.logic.commands.person.FindCommand;
import seedu.address.logic.commands.person.ListCommand;
import seedu.address.logic.parser.event.AddEventCommandParser;
import seedu.address.logic.parser.event.DeleteEventCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.group.AddGroupCommandParser;
import seedu.address.logic.parser.group.AddMemberCommandParser;
import seedu.address.logic.parser.group.DeleteGroupCommandParser;
import seedu.address.logic.parser.group.DeleteMemberCommandParser;
import seedu.address.logic.parser.group.DeleteRepoCommandParser;
import seedu.address.logic.parser.group.EditGroupCommandParser;
import seedu.address.logic.parser.group.FindGroupCommandParser;
import seedu.address.logic.parser.group.GetRepoCommandParser;
import seedu.address.logic.parser.group.SetRepoCommandParser;
import seedu.address.logic.parser.person.AddCommandParser;
import seedu.address.logic.parser.person.DeleteCommandParser;
import seedu.address.logic.parser.person.EditCommandParser;
import seedu.address.logic.parser.person.FindCommandParser;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Logger logger = LogsCenter.getLogger(AddressBookParser.class);

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        // Note to developers: Change the log level in config.json to enable lower level (i.e., FINE, FINER and lower)
        // log messages such as the one below.
        // Lower level log messages are used sparingly to minimize noise in the code.
        logger.fine("Command word: " + commandWord + "; Arguments: " + arguments);

        switch (commandWord) {

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case AddGroupCommand.COMMAND_WORD:
            return new AddGroupCommandParser().parse(arguments);

        case DeleteGroupCommand.COMMAND_WORD:
            return new DeleteGroupCommandParser().parse(arguments);

        case ListGroupCommand.COMMAND_WORD:
            return new ListGroupCommand();

        case FindGroupCommand.COMMAND_WORD:
            return new FindGroupCommandParser().parse(arguments);

        case EditGroupCommand.COMMAND_WORD:
            return new EditGroupCommandParser().parse(arguments);

        case AddMemberCommand.COMMAND_WORD:
            return new AddMemberCommandParser().parse(arguments);

        case DeleteMemberCommand.COMMAND_WORD:
            return new DeleteMemberCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case AddEventCommand.COMMAND_WORD:
            return new AddEventCommandParser().parse(arguments);

        case DeleteEventCommand.COMMAND_WORD:
            return new DeleteEventCommandParser().parse(arguments);

        case SetRepoCommand.COMMAND_WORD:
            return new SetRepoCommandParser().parse(arguments);

        case GetRepoCommand.COMMAND_WORD:
            return new GetRepoCommandParser().parse(arguments);

        case DeleteRepoCommand.COMMAND_WORD:
            return new DeleteRepoCommandParser().parse(arguments);

        default:
            logger.finer("This user input caused a ParseException: " + userInput);
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
