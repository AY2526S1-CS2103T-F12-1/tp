package seedu.address.logic.commands.group;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_GROUPS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalGroups.CS2101_CA2;
import static seedu.address.testutil.TypicalGroups.CS2101_CA3;
import static seedu.address.testutil.TypicalGroups.CS2103T;
import static seedu.address.testutil.TypicalGroups.IS1108;
import static seedu.address.testutil.TypicalGroups.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.group.GroupNameContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindGroupCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        GroupNameContainsKeywordsPredicate firstPredicate =
                new GroupNameContainsKeywordsPredicate(Collections.singletonList("first"));
        GroupNameContainsKeywordsPredicate secondPredicate =
                new GroupNameContainsKeywordsPredicate(Collections.singletonList("second"));

        FindGroupCommand findFirstCommand = new FindGroupCommand(firstPredicate);
        FindGroupCommand findSecondCommand = new FindGroupCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindGroupCommand findFirstCommandCopy = new FindGroupCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different group -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noGroupFound() {
        String expectedMessage = String.format(MESSAGE_GROUPS_LISTED_OVERVIEW, 0);
        GroupNameContainsKeywordsPredicate predicate = preparePredicate(" ");
        FindGroupCommand command = new FindGroupCommand(predicate);
        expectedModel.updateFilteredGroupList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredGroupList());
    }

    @Test
    public void execute_multipleKeywords_multipleGroupsFound() {
        String expectedMessage = String.format(MESSAGE_GROUPS_LISTED_OVERVIEW, 4);
        GroupNameContainsKeywordsPredicate predicate = preparePredicate("cs cs is");
        FindGroupCommand command = new FindGroupCommand(predicate);
        expectedModel.updateFilteredGroupList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CS2103T, CS2101_CA2, CS2101_CA3, IS1108), model.getFilteredGroupList());
    }

    @Test
    public void toStringMethod() {
        GroupNameContainsKeywordsPredicate predicate = new GroupNameContainsKeywordsPredicate(Arrays.asList("keyword"));
        FindGroupCommand findCommand = new FindGroupCommand(predicate);
        String expected = FindGroupCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code GroupNameContainsKeywordsPredicate}.
     */
    private GroupNameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new GroupNameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
