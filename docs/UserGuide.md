---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# StudyCircle User Guide

StudyCircle (SC) is a **desktop app for *NUS computing students* that helps you keep track of your project groups.** It helps you manage your contacts, sort them into groups, and track events within those groups.<br>
SC is optimised for use via a Command Line Interface (CLI) while still allowing you to see your changes in real time through a Graphical User Interface (GUI). If you can type fast, SC can get your contact and group management tasks done faster than traditional GUI-only apps.

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------
<div style="page-break-after: always;"></div>

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/AY2526S1-CS2103T-F12-1/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your StudyCircle.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar StudyCircle.jar` 
   command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. The main elements of the interface have been highlighted in the screenshot. Note how the app contains some sample data to help you get oriented.<br>
   ![Ui](images/Ui_withLabel.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list-contacts` : Lists all contacts.

   * `add-contact n/John Doe p/98765432 e/johnd@example.com` : Adds a contact named `John Doe` to the contact list.

   * `delete-contact 3` : Deletes the 3rd contact shown in the current contact list.

   * `clear` : Deletes all contacts and groups.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------
<div style="page-break-after: always;"></div>

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add-contact n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [g/GROUP_INDEX]` can be used as `n/John Doe g/1` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[g/GROUP_INDEX]…​` can be used as ` ` (i.e. 0 times), `g/1`, `g/2 g/3` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list-contacts`, 
  `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.

* All commands are case sensitive. <br>
  e.g. `list-contacts` is not the same as `List-Contacts`
</box>

<div style="page-break-after: always;"></div>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`

## Contact related commands
---
### Adding a contact: `add-contact`

Adds a contact to the StudyCircle contact list.

Format: `add-contact n/NAME p/PHONE_NUMBER e/EMAIL [g/GROUP_INDEX]…​`

`NAME` Constraints:
* case-insensitive <span style="color:grey">(The system displays names in their original case but treats names like “tom” and “TOM” as the same.)</span>
* Should not be empty or contain only whitespace
* Maximum length: 50 characters
* Alphanumeric characters and spaces allowed <span style="color:grey">(no consecutive spaces)</span>
* May include _s/o_ or _d/o_ between names
* Leading and trailing spaces are automatically removed.

`PHONE_NUMBER` Constraints:
* Must contain only digits
* Minimum 8 digits
* Maximum 15 digits

`EMAIL` Constraints:
* Case-sensitive
* Must be an NUS email
* Must follow the format eXXXXXXX@u.nus.edu <span style="color:grey">(e.g., e0123456@u.nus.edu)</span>

`GROUP_INDEX` Constraints:
* Should match an index in the currently displayed group list

<box type="tip" seamless>

**Tip:** A contact can be added to multiple groups at once by specifying multiple `g/` prefixes
</box>

Examples:
* `add-contact n/John Doe p/98765432 e/e1234567@u.nus.edu` adds a contact with name: John Doe, phone: 98765432, email: e1234567@u.nus.edu to the contact list.
* `add-contact n/Betsy Crowe g/1 e/e1232567@u.nus.edu p/12345678 g/2` adds a contact with name: Betsy Crowe, phone: 12345678, email: e1232567@u.nus.edu to the contact list and add Betsy Crowe to group 1, 2.

### Deleting a contact : `delete-contact`
Deletes the specified contact from the StudyCircle contact list.

Format: `delete-contact CONTACT_INDEX`

* Deletes the contact at the specified `CONTACT_INDEX`.
* The `CONTACT_INDEX` refers to the index number shown in the displayed contact list.
* The `CONTACT_INDEX` **must be a positive integer** 1, 2, 3, …​

Examples:

* `delete-contact 1` deletes the 1st contact in the current displayed contact list.

### Editing a contact : `edit-contact`
Edits the details of the specified contact's name, phone, email and groups.

Format: `edit-contact CONTACT_INDEX [n/NAME] [p/PHONE] [e/EMAIL] [g/GROUP INDEX]...`

`CONTACT_INDEX` Constraints:
* Should match an index in the currently displayed contact list

Other parameter are the same as [add-contact](#adding-a-contact-add-contact) command

<box type="tip" seamless>

**Tip:** A contact can be added to multiple groups at once by specifying multiple `g/` prefixes
</box>
<box type="warning" seamless>

**Caution:** Using `edit-contact` with the `g/` prefix will **replace** the contact’s existing groups instead of adding to them.
</box>
Examples:

* `edit-contact 1 n/John p/12345678 e/e1234567@u.nus.edu g/1 g/2` edits the 1st contact in the current displayed contact list to name John, phone 123456, email e1234567@u.nus.edu and add John to group 1 and 2.

### Listing all contacts : `list-contacts`

Shows a list of all contacts with their name, phone number, email and groups (if any) in the current displayed contact list.

Format: `list-contacts`

### Finding a contact : `find-contact`
Finds all contacts whose names contain any of the specified keywords and displays them as a list with index numbers.

Format: `find-contact KEYWORD [MORE_KEYWORDS]…​`

* `KEYWORD` is case-insensitve <br>
  e.g. finding with `BOB` and `bob` will return the same result
* One or more keyword can be entered, returning all the contacts which contain either of the keywords <br>
  e.g. `find-contact Bob Alice` will return all contacts with either `Bob` or `Alice` in their names
* The search performs partial matches — for example, `find-contact ann` will match names like `Anna`, `Annette`, or `Joanne`.

Examples:
* `find-contact rob` will show the contact list with all people whose name contains `rob`
* `find-contact Aaron Darren` will show the contact list with all people whose name contains either `aaron` or `darren`
* `find-contact Aa Da` will match `aaron` or `darren`

## Group related commands
---
### Adding a group : `add-group`
Adds a new group to StudyCircle group list.

Format: `add-group n/GROUP_NAME`

`GROUP_NAME` Constraints:
* May contain alphanumeric characters, spaces, and the following symbols: `-`, `_`, `(`, `)`
* Must not be blank
* Maximum length: 50 characters

Examples:
* `add-group n/CS2103T` adds a group with name `CS2103T` to StudyCircle
* `add-group n/Project Group A` adds a group with name `Project Group A` to StudyCircle

### Deleting a group : `delete-group`
Deletes the specified group from StudyCircle group list.

Format: `delete-group GROUP_INDEX`

* Deletes the group at the specified `GROUP_INDEX`.
* The `GROUP_INDEX` refers to the index number shown in the displayed group list.
* The `GROUP_INDEX` **must be a positive integer** 1, 2, 3, …​

Examples:
* `delete-group 1` deletes the 1st group in the current displayed group list.

### Edit a group : `edit-group`
Edit the specified group from StudyCircle group list.

Format: `edit-group GROUP_INDEX n/GROUP_NAME`

* The `GROUP_INDEX` **must be a positive integer** 1, 2, 3, …​
* `GROUP_NAME` Constraint is same as [add-group](#adding-a-group-add-group) command

Examples:
* `edit-group 1 n/cs2103 team 1` deletes the 1st group in the current displayed group list.

### Listing all groups : `list-groups`
Shows a list of all groups with their members and events in the current displayed group list.

Format: `list-groups`

### Finding a group : `find-group`
Finds all groups whose names contain any of the specified keywords (case-insensitive) and displays them as a list with 
index numbers.

Format: `find-group KEYWORD [MORE_KEYWORDS]…​`

* `KEYWORD` is case-insensitve <br>
  e.g. finding with `tp` and `TP` will return the same result
* One or more keyword can be entered, returning all the groups which contain either of the keywords <br>
  e.g. `find-group tp CS2101` will return all groups with either `tp` or `CS2101` in their names
* The search performs partial matches — for example, `find-group tp` will match names like `tp2103`, `rentport`, or `cs2103tp`.

Examples:
* `find-group CA2` will show the group list with all groups whose name contains `CA2`
* `find-group CS 210` will show the group list with all groups whose name contains either `CS` or `210`
* `find-group ab3 2101` will match groups named `AB3` or `cs2101`

<div style="page-break-after: always;"></div>

### Adding a member to a group : `add-member`
Adds the specified contacts to the specified group.

Format: `add-member GROUP_INDEX c/CONTACT_INDEX [c/CONTACT_INDEX]…​`

* Both `GROUP_INDEX` and `CONTACT_INDEXES` **must be positive integers** 1, 2, 3, …​
* `CONTACT_INDEXES` can be one or multiple contact indexes
* `CONTACT_INDEXES` are taken from the currently displayed contact list

<box type="tip" seamless>

**Tip:** You can add more than one contact to a group by specifying each extra contact with a `c/` prefix, e.g., `c/1 c/2 c/3`.
</box>

<box type="warning" seamless>

**Caution:** Editing multiple groups at once is not supported. 
</box>

Examples:
* `add-member 1 c/2` adds the 2nd contact to the 1st group.
* `add-member 1 c/1 c/2` adds the 1st and 2nd contact to the 1st group

### Deleting a member from a group : `delete-member`
Deletes the specified contacts from the specified group.

Format: `delete-member GROUP_INDEX c/CONTACT_INDEX [c/CONTACT_INDEX]…​`

* Same as [add-member](#adding-a-member-to-a-group-add-member) command

Examples:
* `delete-member 1 c/2` deletes the 2nd contact from the 1st group.
* `delete-member 1 c/1 c/2` deletes the 1st and 2nd contacts from the 1st group

### Adding an event to a group : `add-event`
Adds an event to the specified group.

Format: `add-event GROUP_INDEX d/DESCRIPTION`

* The `GROUP_INDEX` **must be a positive integer** 1, 2, 3, …​

Examples:
* `add-event 1 d/MVP Feature Specifications` adds the event `MVP Feature Specifications` to the 1st group

### Deleting an event from a group : `delete-event`
Deletes the specified event from the specified group.

Format: `delete-event GROUP_INDEX  e/EVENT_INDEX`

* Both `GROUP_INDEX` and `EVENT_INDEX` **must be positive integers** 1, 2, 3, …​
* `EVENT_INDEX` are taken from the current displayed event list

Examples:
* `delete-event 1 e/2` deletes the 2nd event from the 1st group.

### Editing an event in a group : `edit-event`
Edits an event description in the specified group.

Format: `edit-event GROUP_INDEX e/EVENT_INDEX  d/EVENT_DESCRIPTION`

* Same as [delete-event](#deleting-an-event-from-a-group-delete-event) command

Examples:
* `edit-event 1 e/2 d/MVP Feature Specifications` edits the 2nd event in the 1st group to `MVP Feature Specifications`

### Setting repository link for a group : `set-repo`
Set repository link for the specified group.

Format: `set-repo GROUP_INDEX r/REPOSITORY_LINK`

`REPOSITORY_LINK` Constraints:
* format: \<Domain\>/\<Username or Org\>/\<Repository name\>
* \<Domain\>: Must start with `https://github.com/` (case-sensitive)
* \<Username or Org\>: Starts with a letter/digit, can include letters, digits, `-`, **max 39 chars**
* \<Repository name\>: Starts with a letter/digit, can include letters, digits, `_`, `.`, `-`, **max 100 chars**
* Does not allow consecutive `_`, `.`, or `-`.
* Does not allow Username/Org to end with `-`
* Does not allow repository name to end with `_`, `.`, `-` or `/`

Examples:
* `set-repo 2 r/https://github.com/AY2526S1-CS2103T-F12-1/tp` sets the repository link in 2nd group to 'https://github.com/AY2526S1-CS2103T-F12-1/tp'

### Getting repository link from a group : `get-repo`
Retrieves the repository link of the specified group and auto copy to your clipboard.

Format: `get-repo GROUP_INDEX`

* The `GROUP_INDEX` **must be a positive integer** 1, 2, 3, …​

Examples:
* `get-repo 2` will get the repository link in 2nd group and copy it to your clipboard.

### Deleting a repository link from a group : `delete-repo`
Deletes the specified repository link from the specified group.

Format: `delete-repo GROUP_INDEX`

* The `GROUP_INDEX` **must be a positive integer** 1, 2, 3, …​

Examples:
* `delete-repo 1` deletes repository link from the 1st group.

### Showing the dashboard of a group: `show-dashboard `
Shows the dashboard for a group which displays the group's repo link (if any), members and events. The dashboard 
also allows you to keep track of notes for a group.

Format: `show-dashboard GROUP_INDEX`

* The `GROUP_INDEX` **must be a positive integer** 1, 2, 3, …​
* The note box on the dashboard panel is editable, and auto-saves your notes on every keystroke

Examples:
* `show-dashboard 1` shows the dashboard of group 1.


### Clearing the StudyCircle contact book : `clear`
Clears the contact book of all groups and contacts.

Format: `clear`

### Exiting the program : `exit`
Exits the program.

Format: `exit`


<div style="page-break-after: always;"></div>

### Saving the data

StudyCircle data is saved in the hard disk automatically after any command that changes the data. There is no need to 
save manually.

### Editing the data file

StudyCircle data is saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are 
welcome to update data directly by editing that data file.

<box type="warning" seamless>

**Caution:**
If your changes to the data file makes its format invalid, StudyCircle will discard all data and start with an empty 
data file at the next run.  Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause StudyCIrcle to behave in unexpected ways (e.g., if a value entered is outside the 
acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</box>

### Linking and redirecting to GitHub Repo / Canvas course website `[coming in v0.1.4]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that 
contains the data of your previous StudyCircle home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues
1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **When resizing the window of StudyCircle**, if the window gets too small and the description of a group's 
   event or member name is too long, it will cause the box displaying that group to stop automatically resizing. 
   This leads to the need to use a scroll bar to scroll through that group's events/members if there are too many 
   items in either category.
3. **If you minimize the Help Window** and then run the help command (or use the Help menu, or the keyboard shortcut F1) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.


--------------------------------------------------------------------------------------------------------------------
<div style="page-break-after: always;"></div>

## Command summary

Action                | Format, Examples
----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------
**Add contact**       | `add-contact n/NAME p/PHONE_NUMBER e/EMAIL [g/GROUP_INDEX]…​` <br> e.g., `add-contact n/John Doe p/98765432 e/e1234567@u.nus.edu g/1 g/2`
**Delete contact**    | `delete-contact CONTACT_INDEX`<br> e.g., `delete-contact 3`
**Edit contact** | `edit-contact CONTACT_INDEX [n/NAME] [p/PHONE] [e/EMAIL] [g/GROUP INDEX]...` <br> e.g., `edit-contact 1 n/John p/12345678 e/e1234567@u.nus.edu g/1 g/2`
**Find contact**      | `find-contact KEYWORD [MORE_KEYWORDS]…​`<br> e.g., `find-contact James Jake`
**List contacts**     | `list-contacts`
**Add group**       | `add-group n/NAME` <br> e.g., `add-group n/2103T`
**Delete group**    | `delete-group GROUP_INDEX`<br> e.g., `delete-group 3`
**Edit group** | `edit-group GROUP_INDEX n/GROUP_NAME`<br> e.g., `edit-group 1 n/cs2103 team 1`
**Find group**      | `find-group KEYWORD [MORE_KEYWORDS]…​`<br> e.g., `find-group CA1 CA2`
**List groups**     | `list-groups`
**Add members to a group**       | `add-member GROUP_INDEX c/CONTACT_INDEX [c/CONTACT_INDEX]…​` <br> e.g., `add-member 1 c/1`, `add-member 1 c/1 c/2`
**Delete members from a group**  | `delete-member GROUP_INDEX c/CONTACT_INDEX [c/CONTACT_INDEX]…​` <br> e.g., `delete-member 1 c/1`, `delete-member 1 c/1 c/2`
**Add an event to a group**     | `add-event GROUP_INDEX d/DESCRIPTION`<br> e.g., `add-event 2 d/do project work`
**Delete an event from a group** | `delete-event GROUP_INDEX e/EVENT_INDEX` <br> e.g., `delete-event 1 e/2`
**Edit an event in a group** | `edit-event GROUP_INDEX e/EVENT_INDEX d/EVENT_DESCRIPTION` <br> e.g., `edit-event 1 e/2 d/MVP Feature Specifications`
**Set repository link for a group** | `set-repo GROUP_INDEX r/REPOSITORY_LINK` <br> e.g., `set-repo 2 r/https://github.com/AY2526S1-CS2103T-F12-1/tp`
**Get repository link from a group** | `get-repo GROUP_INDEX` <br> e.g., `get-repo 2`
**Delete a repository link from a group** | `delete-repo GROUP_INDEX` <br> e.g., `delete-repo 1`
**Show a group's dsahboard** | `show-dashboard GROUP_INDEX`<br> e.g., `show-dashboard 1`
**Clear StudyCircle contacts and groups**| `clear`
**Help**              | `help`
