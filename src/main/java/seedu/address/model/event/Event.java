package seedu.address.model.event;

import static java.util.Objects.requireNonNull;

/**
 * Represents an Event which can be attached to a Group.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public record Event(Description description) {

    /**
     * Constructs an {@code Event}.
     *
     * @param description a valid Description.
     */
    public Event {
        requireNonNull(description);
    }

    @Override
    public String toString() {
        return this.description.toString();
    }
}
