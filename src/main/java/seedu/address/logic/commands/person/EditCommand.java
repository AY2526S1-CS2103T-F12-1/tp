package seedu.address.logic.commands.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupName;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit-contact";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_GROUP_INDEX + "GROUP INDEX]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "e1234567 "
            + PREFIX_GROUP_INDEX + "1";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person's email already exists in the address book.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        //Retrieve current data
        List<Person> lastShownPersonList = model.getFilteredPersonList();
        List<Group> lastShownGroupList = model.getFilteredGroupList();

        //Validate person index
        if (index.getZeroBased() >= lastShownPersonList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        //Validate group indexes
        Set<Index> groupsIndexes = editPersonDescriptor.getGroups().orElse(null);
        if (groupsIndexes != null && groupsIndexes.stream()
                .anyMatch(idx -> idx.getZeroBased() >= lastShownGroupList.size())) {
            throw new CommandException(Messages.MESSAGE_INVALID_GROUP_DISPLAYED_INDEX);
        }

        //Identify the target person and prepare updated field
        Person personToEdit = lastShownPersonList.get(index.getZeroBased());
        Set<GroupName> groupNames = convertIndexToGroupName(lastShownGroupList, groupsIndexes);
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor, groupNames);

        //Check for person duplicates
        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        //Apply updates
        model.setPerson(personToEdit, editedPerson);
        syncGroupWithEditedPerson(groupsIndexes, model, editedPerson);

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Synchronizes the group memberships of the given {@code editedPerson} in the {@code model}
     * based on the specified {@code groupsIndexes}.
     *
     * <p>This method updates the groups that {@code editedPerson} belongs to according to user input:
     * <ul>
     *   <li>If {@code groupsIndexes} is {@code null}, it means the {@code /g} flag was not used
     *       in the command, so no group changes are made.</li>
     *   <li>If {@code groupsIndexes} is empty, it means the {@code /g} flag was used with no
     *       arguments, so the person is removed from all existing groups.</li>
     *   <li>If {@code groupsIndexes} contains one or more indexes, the person is first removed
     *       from all current groups, then added to the specified groups.</li>
     * </ul>
     *
     * @param groupsIndexes the set of group indexes specified by the user input;
     *                      may be {@code null} or empty
     * @param model         the {@code Model} managing persons and groups
     * @param editedPerson  the {@code Person} whose group associations should be synchronized
     */
    private void syncGroupWithEditedPerson(Set<Index> groupsIndexes, Model model, Person editedPerson) {
        // groupsIndexes == null means /g is not used in the user input
        if (groupsIndexes == null) {
            return;
        }

        // /g keyword is used — remove the person from all current groups first
        // If the user provided non-empty group indexes, add them to the person
        model.removePersonFromAllGroups(editedPerson);
        if (!groupsIndexes.isEmpty()) {
            model.addPersonToGroups(groupsIndexes, editedPerson);
        }
    }

    private Set<GroupName> convertIndexToGroupName(List<Group> lastShownGroupList, Set<Index> targetGroupIndex) {
        if (targetGroupIndex == null) {
            return null;
        }

        Set<GroupName> targetGroupNames = new HashSet<>();
        for (Index index : targetGroupIndex) {
            Group groupToAddTo = lastShownGroupList.get(index.getZeroBased());
            targetGroupNames.add(groupToAddTo.getName());
        }
        return targetGroupNames;
    }

    /**
     * Creates and returns a new {@code Person} with the details of {@code personToEdit}
     * updated according to the given {@code EditPersonDescriptor} and {@code groupNames}.
     *
     * <p>This method preserves the existing values of {@code personToEdit} for any fields
     * not specified in {@code editPersonDescriptor}. The group membership of the edited person
     * is determined based on the {@code groupNames} parameter:
     * <ul>
     *   <li>If {@code groupNames} is {@code null}, the existing groups of {@code personToEdit} are retained.</li>
     *   <li>If {@code groupNames} is empty, the resulting {@code Person} will have no group associations.</li>
     *   <li>Otherwise, {@code groupNames} will replace the existing groups.</li>
     * </ul>
     *
     * @param personToEdit         the original {@code Person} to edit; must not be {@code null}
     * @param editPersonDescriptor the descriptor containing the fields to edit
     * @param groupNames           the new set of group names, or {@code null} to keep existing groups
     * @return a new {@code Person} instance with the updated details
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor,
                                             Set<GroupName> groupNames) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Set<GroupName> updatedGroups;
        if (groupNames == null) {
            updatedGroups = personToEdit.getGroups();
        } else if (groupNames.isEmpty()) {
            updatedGroups = Collections.emptySet();
        } else {
            updatedGroups = groupNames;
        }
        return new Person(updatedName, updatedPhone, updatedEmail, updatedGroups);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Set<Index> groups;

        public EditPersonDescriptor() {
        }

        /**
         * Copy constructor.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setGroups(toCopy.groups);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, groups);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        /**
         * Sets {@code groups} to this object's {@code groups}.
         * A defensive copy of {@code groups} is used internally.
         */
        public void setGroups(Set<Index> groups) {
            this.groups = (groups != null) ? new HashSet<>(groups) : null;
        }

        /**
         * Returns an unmodifiable group set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code groups} is null.
         */
        public Optional<Set<Index>> getGroups() {
            return (groups != null) ? Optional.of(Collections.unmodifiableSet(groups)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(groups, otherEditPersonDescriptor.groups);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("groups", groups)
                    .toString();
        }
    }
}
