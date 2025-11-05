package seedu.address.logic.parser;

/**
 * A prefix that marks the beginning of an argument in an arguments string.
 * E.g. 't/' in 'add James t/ friend'.
 */
public class Prefix {
    private final String prefix;
    private final boolean isUnique;

    private Prefix(String prefix, boolean isUnique) {
        this.prefix = prefix;
        this.isUnique = isUnique;
    }

    public Prefix(String prefix) {
        this(prefix, false);
    }

    /**
     * Returns a copy of this prefix with the isUnique flag set to true.
     */
    public Prefix once() {
        return new Prefix(prefix, true);
    }

    public boolean isUnique() {
        return isUnique;
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public String toString() {
        return getPrefix();
    }

    @Override
    public int hashCode() {
        return prefix == null ? 0 : prefix.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Prefix)) {
            return false;
        }

        Prefix otherPrefix = (Prefix) other;
        return prefix.equals(otherPrefix.prefix);
    }
}
