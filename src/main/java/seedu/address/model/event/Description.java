package seedu.address.model.event;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents an Event's Description
 */
public record Description(String description) {
    public static final String MESSAGE_CONSTRAINTS =
            "Descriptions should not be empty";

    /**
     * Constructs a {@code Description}.
     *
     * @param description A valid description.
     */
    public Description {
        requireNonNull(description);
        checkArgument(isValidDescription(description), MESSAGE_CONSTRAINTS);
    }

    /**
     * Returns true if a given string is a valid description.
     */
    public static boolean isValidDescription(String test) {
        return !test.isBlank();
    }

    @Override
    public String toString() {
        return description;
    }
}
