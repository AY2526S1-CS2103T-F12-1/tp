package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.testutil.TypicalGroups.getTypicalGroups;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.group.Group;
import seedu.address.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Person ALICE = new PersonBuilder().withName("Alice Pauline")
            .withEmail("e1234567@u.nus.edu")
            .withPhone("94351253")
            .build();
    public static final Person BENSON = new PersonBuilder().withName("Benson Meier")
            .withEmail("e0234567@u.nus.edu").withPhone("98765432").withGroups("CS2103T", "CS2101 CA3", "IS1108")
            .build();
    public static final Person CARL = new PersonBuilder().withName("Carl Kurz").withPhone("95352563")
            .withEmail("e1034567@u.nus.edu").withGroups("CS2103T", "CS2101 CA2", "IS1108").build();
    public static final Person DANIEL = new PersonBuilder().withName("Daniel Meier").withPhone("87652533")
            .withEmail("e1204567@u.nus.edu").withGroups("CS2103T", "CS2101 CA3", "IS1108").build();
    public static final Person ELLE = new PersonBuilder().withName("Elle Meyer").withPhone("94822241")
            .withEmail("e1230567@u.nus.edu").withGroups("CS2101 CA3").build();
    public static final Person FIONA = new PersonBuilder().withName("Fiona Kunz").withPhone("94824271")
            .withEmail("e1234067@u.nus.edu").withGroups("CS2103T", "CS2101 CA3", "IS1108").build();
    public static final Person GEORGE = new PersonBuilder().withName("George Best").withPhone("94824421")
            .withEmail("e1234507@u.nus.edu").build();
    public static final Person GEORGE_WITH_GROUP = new PersonBuilder().withName("George Best").withPhone("94824421")
            .withEmail("e1234507@u.nus.edu").withGroups("CS2103T").build();

    // Manually added
    public static final Person HOON = new PersonBuilder().withName("Hoon Meier").withPhone("84824241")
            .withEmail("e1234566@u.nus.edu").build();
    public static final Person IDA = new PersonBuilder().withName("Ida Mueller").withPhone("84821311")
            .withEmail("e1234557@u.nus.edu").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).build();
    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {
    } // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        for (Group group : getTypicalGroups()) {
            ab.addGroup(new GroupBuilder(group).build());
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
