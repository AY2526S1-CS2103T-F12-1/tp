package seedu.address.logic.commands.group;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACT_INDEX;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.group.Group;
import seedu.address.model.person.Person;


/**
 * Deletes specified persons from the specified group.
 */
public class DeleteMemberCommand extends Command {

    public static final String COMMAND_WORD = "delete-member";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Delete persons specified by index numbers"
            + "from the specified group's member list\n"
            + "Parameters: "
            + "GROUP_INDEX (must be a positive integer) "
            + PREFIX_CONTACT_INDEX + "CONTACT_INDEX (must be a positive integer of contacts in group's member list) "
            + "[" + PREFIX_CONTACT_INDEX + "CONTACT_INDEXES]...\n"
            + "Example: " + COMMAND_WORD + " "
            + "1 "
            + PREFIX_CONTACT_INDEX + "2 "
            + PREFIX_CONTACT_INDEX + "3 "
            + PREFIX_CONTACT_INDEX + "4 ";

    public static final String MESSAGE_SUCCESS = "Member(s): '%1$s' deleted from group: '%2$s'";

    private final Index groupIndex;
    private final Set<Index> contactIndexes;

    /**
     * Creates a DeleteMemberCommand to add the specified {@code persons} to the specified {@code group}
     */
    public DeleteMemberCommand(Index groupIndex, Set<Index> contactIndexes) {
        requireAllNonNull(contactIndexes, groupIndex);
        this.contactIndexes = contactIndexes;
        this.groupIndex = groupIndex;
    }


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownMiniPersonList;
        List<Group> lastShownGroupList = model.getFilteredGroupList();

        if (groupIndex.getZeroBased() >= lastShownGroupList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_GROUP_DISPLAYED_INDEX);
        }

        Group groupToRemoveFrom = lastShownGroupList.get(groupIndex.getZeroBased());
        lastShownMiniPersonList = groupToRemoveFrom.getPersons();
        List<Person> personsToRemove = new ArrayList<>();
        List<String> personNames = new ArrayList<>();

        for (Index contactIndex: contactIndexes) {
            if (contactIndex.getZeroBased() >= lastShownMiniPersonList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            Person targetPerson = lastShownMiniPersonList.get(contactIndex.getZeroBased());
            personsToRemove.add(targetPerson);
            personNames.add(targetPerson.getNameAsString());
        }

        for (Person toRemove : personsToRemove) {
            model.removePersonFromGroups(new HashSet<>(Set.of(groupIndex)), toRemove);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS,
                personNames.stream().collect(Collectors.joining(", ")),
                Messages.format(groupToRemoveFrom)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteMemberCommand)) {
            return false;
        }

        DeleteMemberCommand otherDeleteMemberCommand = (DeleteMemberCommand) other;
        return groupIndex.equals(otherDeleteMemberCommand.groupIndex)
                && contactIndexes.equals(otherDeleteMemberCommand.contactIndexes);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("groupIndex", groupIndex)
                .add("contactIndex", contactIndexes)
                .toString();
    }

}
