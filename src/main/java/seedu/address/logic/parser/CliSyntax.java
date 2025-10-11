package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");

    // Contacts
    public static final Prefix PREFIX_CONTACT_INDEX = new Prefix("c/");

    // Groups
    public static final Prefix PREFIX_GROUP_INDEX = new Prefix("g/");

    // Events
    public static final Prefix PREFIX_DESCRIPTION = new Prefix("d/");

}
