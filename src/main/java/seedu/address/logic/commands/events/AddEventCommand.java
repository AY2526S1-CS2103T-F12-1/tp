package seedu.address.logic.commands.events;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.event.Event;

/**
 * Adds an Event to the specified Group
 */
public class AddEventCommand extends Command {

    public static final String COMMAND_WORD = "add-event";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an event to the group identified "
            + "by the index number used in the displayed groups list. "
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_DESCRIPTION + "DESCRIPTION";

    public static final String MESSAGE_SUCCESS = "New event: '%1$s' added to group: %2$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This event already exists in the group";

    private final Index groupIndex;
    private final Event toAdd;

    /**
     * Creates an AddEvent to add the specified {@code Event}
     */
    public AddEventCommand(Index index, Event event) {
        requireAllNonNull(index, event);
        groupIndex = index;
        toAdd = event;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        return new CommandResult("Hello from AddEventCommand");
    }
}
