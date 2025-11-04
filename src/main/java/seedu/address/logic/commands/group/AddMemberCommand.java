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
 * Adds specified persons to the specified group.
 */
public class AddMemberCommand extends Command {

    public static final String COMMAND_WORD = "add-member";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds persons specified by index number "
            + "to the group also specified by index number\n"
            + "Parameters: "
            + "GROUP_INDEX (must be a positive integer) "
            + PREFIX_CONTACT_INDEX + "CONTACT_INDEX (must be a positive integer) "
            + "[" + PREFIX_CONTACT_INDEX + "CONTACT_INDEXES]...\n"
            + "Example: " + COMMAND_WORD + " "
            + "1 "
            + PREFIX_CONTACT_INDEX + "2 "
            + PREFIX_CONTACT_INDEX + "3 "
            + PREFIX_CONTACT_INDEX + "4 ";

    public static final String MESSAGE_SUCCESS = "New member(s): '%1$s' added to group: '%2$s'";
    public static final String MESSAGE_DUPLICATE_PERSON = "Person '%1$s' has already been added to the group";

    private final Index groupIndex;
    private final Set<Index> contactIndexes;

    /**
     * Creates an AddMemberCommand to add the specified {@code persons} to the specified {@code group}
     */
    public AddMemberCommand(Index groupIndex, Set<Index> contactIndexes) {
        requireAllNonNull(contactIndexes, groupIndex);
        this.contactIndexes = contactIndexes;
        this.groupIndex = groupIndex;
    }


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownPersonList = model.getFilteredPersonList();
        List<Group> lastShownGroupList = model.getFilteredGroupList();

        if (groupIndex.getZeroBased() >= lastShownGroupList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_GROUP_DISPLAYED_INDEX);
        }

        Group groupToAddTo = lastShownGroupList.get(groupIndex.getZeroBased());
        List<Person> personsToAdd = new ArrayList<>();
        List<String> personNames = new ArrayList<>();

        for (Index contactIndex : contactIndexes) {
            if (contactIndex.getZeroBased() >= lastShownPersonList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            Person targetPerson = lastShownPersonList.get(contactIndex.getZeroBased());

            if (groupToAddTo.containsPerson(targetPerson)) {
                throw new CommandException(String.format(MESSAGE_DUPLICATE_PERSON,
                        targetPerson.getName() + " (" + targetPerson.getEmail() + ")"));
            }

            personsToAdd.add(targetPerson);
            personNames.add(targetPerson.getNameAsString() + " (" + targetPerson.getEmail() + ")");
        }

        for (Person toAdd : personsToAdd) {
            Person modifiedPerson = model.addPersonToGroups(new HashSet<>(Set.of(groupIndex)), toAdd);
            model.setPerson(toAdd, modifiedPerson);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS,
                personNames.stream().collect(Collectors.joining(", ")),
                Messages.format(groupToAddTo)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddMemberCommand)) {
            return false;
        }

        AddMemberCommand otherAddMemberCommand = (AddMemberCommand) other;
        return groupIndex.equals(otherAddMemberCommand.groupIndex)
                && contactIndexes.equals(otherAddMemberCommand.contactIndexes);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("groupIndex", groupIndex)
                .add("contactIndex", contactIndexes)
                .toString();
    }
}
