package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Names are case-insensitive, should not be blank and should only contain:\n"
                    + "- Maximum 50 characters\n"
                    + "- alphanumeric characters\n"
                    + "- spaces (no consecutive spaces)\n"
                    + "- s/o or d/o (optional, between names)\n"
                    + "- Leading and trailing spaces are automatically removed.";

    /*
     * Name have following constrain:
     * should not be blank " " (a blank string)
     * allow for alphanumeric characters, spaces, s/o or d/o (optional)(must be between two names.)
     * max 50 characters
     * do not allow trailing spaces
     */
    public static final String VALIDATION_REGEX =
            "^(?=.{1,50}$)[A-Za-z0-9]+(?: [A-Za-z0-9]+)*(?: [sd]\\/o [A-Za-z0-9]+(?: [A-Za-z0-9]+)*)?$";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        fullName = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return fullName.toLowerCase().equals(otherName.fullName.toLowerCase());
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
