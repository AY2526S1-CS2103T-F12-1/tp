package seedu.address.testutil;

import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.getTypicalPersons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.group.Group;
import seedu.address.model.person.Person;

/**
 * A utility class containing a list of {@code Group} objects to be used in tests.
 */
public class TypicalGroups {

    public static final Group CS2103T = new GroupBuilder().withName("CS2103T")
            .withPersons(BENSON, DANIEL, CARL, FIONA)
            .build();
    public static final Group CS2101_CA2 = new GroupBuilder().withName("CS2101 CA2")
            .withPersons(CARL)
            .build();
    public static final Group CS2101_CA3 = new GroupBuilder().withName("CS2101 CA3")
            .withPersons(BENSON, DANIEL, ELLE, FIONA)
            .build();
    public static final Group IS1108 = new GroupBuilder().withName("IS1108")
            .withPersons(BENSON, DANIEL, CARL, FIONA)
            .build();
    public static final Group NO_MEMBER = new GroupBuilder().withName("NO MEMBER")
            .build();

    private TypicalGroups() {
    } // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical groups and typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        for (Group group : getTypicalGroups()) {
            ab.addGroup(group);
        }
        return ab;
    }

    public static List<Group> getTypicalGroups() {
        return new ArrayList<>(Arrays.asList(NO_MEMBER, CS2103T, CS2101_CA2, CS2101_CA3, IS1108));
    }
}
