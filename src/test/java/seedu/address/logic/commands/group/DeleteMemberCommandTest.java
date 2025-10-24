package seedu.address.logic.commands.group;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.group.DeleteMemberCommand.MESSAGE_SUCCESS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_GROUP;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FOURTH_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_GROUP;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.ModelStub;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.group.Group;
import seedu.address.model.person.Person;
import seedu.address.testutil.GroupBuilder;


public class DeleteMemberCommandTest {
    private static final Group CS2103T_WA = new GroupBuilder().withName("CS2103T")
            .withPersons(BENSON)
            .build();

    private static final Group CS2103T = new GroupBuilder().withName("CS2103T")
            .withPersons(ALICE, BENSON)
            .build();

    private static final Group CS2103T_EMPTY = new GroupBuilder().withName("CS2103T")
            .withPersons()
            .build();

    @Test
    public void constructor_nullArguments_throwsNullPointerException() {
        Set<Index> persons = new HashSet<>();
        persons.add(INDEX_FIRST_PERSON);
        assertThrows(NullPointerException.class, () -> new DeleteMemberCommand(null, persons));

        assertThrows(NullPointerException.class, () -> new DeleteMemberCommand(INDEX_FIRST_GROUP, null));
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        Model modelStub = new ModelStubWithPersonAndGroup();
        Set<Index> invalidPersons = new HashSet<>();
        invalidPersons.add(INDEX_THIRD_PERSON);
        DeleteMemberCommand cmd = new DeleteMemberCommand(INDEX_FIRST_GROUP, invalidPersons);

        assertThrows(CommandException.class,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, () -> cmd.execute(modelStub));
    }

    @Test
    public void execute_invalidPersonsIndex_throwsCommandException() {
        Model modelStub = new ModelStubWithPersonAndGroup();
        Set<Index> invalidPersons = new HashSet<>();
        invalidPersons.add(INDEX_FIRST_PERSON);
        invalidPersons.add(INDEX_FOURTH_PERSON);
        DeleteMemberCommand cmd = new DeleteMemberCommand(INDEX_FIRST_GROUP, invalidPersons);

        assertThrows(CommandException.class,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, () -> cmd.execute(modelStub));
    }

    @Test
    public void execute_invalidGroupIndex_throwsCommandException() {
        Model modelStub = new ModelStubWithPersonAndGroup();
        Set<Index> persons = new HashSet<>();
        persons.add(INDEX_FIRST_PERSON);
        DeleteMemberCommand cmd = new DeleteMemberCommand(INDEX_SECOND_GROUP, persons);

        assertThrows(CommandException.class,
                Messages.MESSAGE_INVALID_GROUP_DISPLAYED_INDEX, () -> cmd.execute(modelStub));
    }

    @Test
    public void execute_memberDeletedFromGroup_success() throws CommandException {
        Model modelStub = new ModelStubAcceptingDeleteMember();
        Set<Index> persons = new HashSet<>();
        persons.add(INDEX_FIRST_PERSON);
        DeleteMemberCommand cmd = new DeleteMemberCommand(INDEX_FIRST_GROUP, persons);

        Group targetGroup = modelStub.getFilteredGroupList().get(INDEX_FIRST_GROUP.getZeroBased());

        assertEquals(String.format(MESSAGE_SUCCESS,
                targetGroup.getPersons().get(0).getNameAsString(),
                targetGroup.getName()), cmd.execute(modelStub).getFeedbackToUser());

        assertEquals(CS2103T_WA, modelStub.getFilteredGroupList().get(0));

    }

    @Test
    public void execute_multipleMembersDeletedFromGroup_success() throws CommandException {
        Model modelStub = new ModelStubAcceptingDeleteMember();
        Set<Index> persons = new HashSet<>();
        persons.add(INDEX_FIRST_PERSON);
        persons.add(INDEX_SECOND_PERSON);
        DeleteMemberCommand cmd = new DeleteMemberCommand(INDEX_FIRST_GROUP, persons);

        Group targetGroup = modelStub.getFilteredGroupList().get(INDEX_FIRST_GROUP.getZeroBased());
        ObservableList<Person> personList = targetGroup.getPersons();
        List<String> personListAsString = List.of(personList.get(0).getNameAsString(),
                personList.get(1).getNameAsString());

        assertEquals(String.format(MESSAGE_SUCCESS,
                personListAsString.stream().collect(Collectors.joining(", ")),
                targetGroup.getName()), cmd.execute(modelStub).getFeedbackToUser());

        assertEquals(CS2103T_EMPTY, modelStub.getFilteredGroupList().get(0));

    }

    @Test
    public void equalsTest() {
        Set<Index> persons1 = new HashSet<>();
        Set<Index> persons2 = new HashSet<>();
        Set<Index> persons3 = new HashSet<>();
        persons1.add(INDEX_FIRST_PERSON);
        persons2.add(INDEX_SECOND_PERSON);
        persons3.add(INDEX_THIRD_PERSON);
        DeleteMemberCommand cmd1 = new DeleteMemberCommand(INDEX_FIRST_GROUP, persons2);
        DeleteMemberCommand cmd2 = new DeleteMemberCommand(INDEX_FIRST_GROUP, persons2);
        DeleteMemberCommand cmd3 = new DeleteMemberCommand(INDEX_SECOND_GROUP, persons1);
        DeleteMemberCommand cmd4 = new DeleteMemberCommand(INDEX_FIRST_GROUP, persons3);


        assertTrue(cmd1.equals(cmd2));
        assertTrue(cmd1.equals(cmd1));
        assertFalse(cmd1.equals(cmd3));
        assertFalse(cmd1.equals(cmd4));
        assertFalse(cmd1.equals(null));
        assertFalse(cmd1.equals(1));
    }

    @Test
    public void toStringTest() {
        Set<Index> persons = new HashSet<>();
        persons.add(INDEX_SECOND_PERSON);
        DeleteMemberCommand cmd1 = new DeleteMemberCommand(INDEX_FIRST_GROUP, persons);
        String expected = DeleteMemberCommand.class.getCanonicalName() + "{groupIndex=" + INDEX_FIRST_GROUP + ", "
                + "contactIndex=[" + INDEX_SECOND_PERSON + "]}";
        assertEquals(expected, cmd1.toString());
    }




    private class ModelStubWithPersonAndGroup extends ModelStub {
        private final List<Person> persons = new ArrayList<>(List.of(ALICE));
        private final List<Group> groups = new ArrayList<>(List.of(CS2103T));

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableArrayList(persons);
        }

        @Override
        public ObservableList<Group> getFilteredGroupList() {
            return FXCollections.observableArrayList(groups);
        }
    }

    private class ModelStubAcceptingDeleteMember extends ModelStubWithPersonAndGroup {
        private final List<Group> groups = new ArrayList<>(List.of(CS2103T));

        @Override
        public void setPerson(Person target, Person editedPerson) {
        }

        @Override
        public void removePersonFromGroups(Set<Index> targetGroupIndexes, Person toRemove) {
            Group group = groups.get(0);
            List<Person> updatedMembers = new ArrayList<>(group.getPersons());
            updatedMembers.remove(toRemove);
            Group updatedGroup = new GroupBuilder(group).withPersons(updatedMembers.toArray(new Person[0])).build();
            groups.set(0, updatedGroup);
        }

        @Override
        public ObservableList<Group> getFilteredGroupList() {
            return FXCollections.observableArrayList(groups);
        }
    }
}
