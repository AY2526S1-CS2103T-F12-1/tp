---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# StudyCircle Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).
* Libraries used: [JavaFX](https://openjfx.io/), [Jackson](https://github.com/FasterXML/jackson), [JUnit5](https://github.com/junit-team/junit5)


--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the section "Quick start" in the
[_User Guide_](https://ay2526s1-cs2103t-f12-1.github.io/tp/UserGuide.html).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user 
issues the command `delete-contact 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, 
`StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

<puml src="diagrams/GroupCardDiagram.puml" alt="Structure of the GroupCard component"/>

The GroupCard contains an `EventListPanel`, the list of events attached to a group, and a `MiniPersonListPanel`, 
which is the list of a group's members.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete-contact 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a contact).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` and `Group` objects (which are contained in a `UniquePersonList` and `UniqueGroupList` object respectively).
* stores the currently 'selected' `Person` and `Group` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores all `Person` and `Event` objects which belong to each `Group` (contained in a `UniquePersonList` and `UniqueEventList` object respectively).
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Edit Contact feature

The edit contact command is newly added feature in StudyCircle, extended from the original AB3 application. It supports editing various fields of the specified contact:

* `Name` — The name of the contact.
* `Phone` — The phone number of the contact.
* `Email` — The email of the contact.
* `Group names` — The group names of the groups which the contact is in.

The fields can be edited when the user use the following prefixes: `n/`, `e/`, `p/` and `g/` respectively. Additionally, the prefix `g/` can be use multiple times in a single command to add the contact into multiple groups at a time.

<box type="info" seamless>

**Note:** The prefix `g/` for editing groups when used will remove the contact from all the original groups and add the new groups based on the given command. 

</box>

Given below is an example usage scenario and how the edit-contact command behaves at each step.

Step 1. The user initiates the app with several groups:

1. CS2103T
2. CS2101 CA1
3. CS2101 CA2

and several contacts with their following information:

1. Bob - 80324084 - e1234567@u.nus.edu - groups: CS2101 CA1
2. Mary - 32404140 - e3224335@u.nus.edu - groups: CS2103T, CS2101 CA1

Step 2. The user executes the command `edit-contact 1 n/Bobby e/e7654321@u.nus.edu g/1 g/3` to change Bob's name into Bobby, email to e7654321@u.nus.edu and change Bob's groups to CS2103T and CS2101 CA2.

The command parse through each prefix to have the edited fields. The `edit-contact` command then create a new contact with the new name, email and groups fields. Sync the internal list of the groups and the contacts. Then the command sets Bob's contact to the newly created one. 

The following sequence diagram shows how an edit-contact operations goes through the `Logic` component:

<puml src="diagrams/EditContactSequenceDiagram.puml" alt="EditContactSequenceDiagram" />

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th contact in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new contact. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the contact was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
    * Pros: Easy to implement.
    * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
    * Pros: Will use less memory (e.g. for `delete`, just save the contact being deleted).
    * Cons: We must ensure that the implementation of each individual command are correct.


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](https://ay2526s1-cs2103t-f12-1.github.io/tp/Documentation.html)
* [Testing guide](https://ay2526s1-cs2103t-f12-1.github.io/tp/Testing.html)
* [Logging guide](https://ay2526s1-cs2103t-f12-1.github.io/tp/Logging.html)
* [Configuration guide](https://ay2526s1-cs2103t-f12-1.github.io/tp/Configuration.html)
* [DevOps guide](https://ay2526s1-cs2103t-f12-1.github.io/tp/DevOps.html)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* A computing student at the National University of Singapore(NUS) with multiple group projects across different modules.

**Value proposition**:
* It is hard to manage the members of each specific group\
  The app allow Creation of groups and subgroups

* With many groups at the same time, it can become difficult to know when each one needs your attention.\
  The app helps you manage your deadlines and meetings for each group



### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                                 | I want to …​                                                                            | So that…​                                                                                           |
|----------|---------------------------------------------------------|-----------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------|
| `* * *`  | user                                                    | add contacts from my school projects with their phone numbers and email into the system | I can manage the contact information of all the relevant people for the semester                    |
| `* * *`  | user                                                    | delete contacts from the contact list when I no longer need a person's contact          | my contact list is not too cluttered.                                                               |
| `* * *`  | NUS student currently doing many group projects         | create groups within my list of contacts                                                | I am able to navigate to each group based on which module I am working on.                          |
| `* * *`  | NUS student managing deadlines                          | attach project/submission deadlines to each group                                       | I can manage each deadline and I know when I should contact my various groupmates.                  |
| `* * *`  | user                                                    | delete just a group, not the contacts                                                   | I can remove a group without losing my contacts                                                     |
| `* * *`  | user                                                    | add member to a group                                                                   | I can easily manage my group                                                                        |
| `* * *`  | user                                                    | list contacts                                                                           | I can see all the contacts I have                                                                   |
| `* * *`  | user                                                    | list groups                                                                             | I can see all the groups I have                                                                     |
| `* *`    | new user                                                | view a guide                                                                            | I can easily find out what the app can do for me                                                    |
| `* *`    | expert user                                             | save my GitHub repository link                                                          | I can easily copy and paste later on when I need it                                                 |
| `* *`    | new user                                                | see a prompt telling how to execute a “help” command                                    | I can find out all the various commands that I can use in the app.                                  |
| `* *`    | user                                                    | delete old contacts by group                                                            | I do not need to spend too much time deleting users one by one                                      |
| `* *`    | user                                                    | see a confirmation of the data I added                                                  | I can catch and fix mistakes quickly                                                                |
| `* *`    | user                                                    | edit the details of a particular contact through an intuitive interface                 | I can easily tweak the information of a contact if needed.                                          |
| `* *`    | user                                                    | look for a specific contact by their name by typing it into the search bar              | I do not have to sort through the entire list of contacts to find one person.                       |
| `* *`    | user                                                    | add a number of contacts in a group at once                                             | I do not need to spend too much time adding members of a new group one by one                       |
| `* *`    | NUS student currently doing many group projects         | add a label to the contacts of people in my various groups                              | I can easily look up people based on their label.                                                   |
| `* *`    | member of a group project                               | set a meeting time/place for each of my groups                                          | I can easily know if I have a meeting for the day with my group and where to go for it.             |
| `* *`    | user                                                    | check the dashboard for nearby group assignment deadlines                               | I know which tasks to prioritize                                                                    |
| `* *`    | NUS student                                             | set a reminder for all my project/submission deadlines and group meetings               | I know when all my deadlines are without having to check them.                                      |
| `* *`    | NUS student                                             | press one button to reset and clear my entire contact list when the semester is over    | I do not have to delete those contacts one by one when I am preparing for a new semester.           |
| `* *`    | user                                                    | set profile photo for each contact                                                      | I can quickly identify the person through face, if there are similar names                          |
| `* *`    | user                                                    | Make a contact a member of multiple groups                                              | I don't have to add the same contact multiple times if I am in multiple groups with the same person |
| `* *`    | user                                                    | search my groups                                                                        | I can easily find the group I am looking for                                                        |
| `* *`    | user                                                    | set a profile picture for each of my groups                                             | I can easily find and recognize the group that I am looking for without having to look at the names |
| `* *`    | user                                                    | show a group's dashboard                                                                | I can easily see all the group's details and make notes about each group                            |
| `*`      | potential new user                                      | experiment with sample data                                                             | I can see how the app looks and behaves                                                             |
| `*`      | new user                                                | see various tooltips within the app                                                     | I am able to orientate myself with the app and its uses                                             |
| `*`      | user                                                    | access important links for my group project                                             | I can easily access my group project materials from one place                                       |
| `*`      | NUS student managing project groups                     | hide contacts from group projects which have already been completed                     | I do not have to see people in my contacts when I do not have to contact them anymore.              |
| `*`      | expert user                                             | create shortcuts for tasks                                                              | I can save time on frequently performed tasks.                                                      |
| `*`      | user who is trying to contact a group of people at once | press one button and copy all the email addresses of people in a group to my clipboard  | I can easily paste it when trying to send an email to all of these members.                         |
| `*`      | experienced user                                        | turn off the integrated tooltips                                                        | I can declutter my screen                                                                           |
| `*`      | user                                                    | receive daily/weekly summaries about the deadlines, events and tasks                    | I can plan my workload accordingly                                                                  |
| `*`      | user                                                    | Set a colour for each group which will show up on a contacts profile                    | I can easily tell what group someone belongs to visually                                            |
| `*`      | user                                                    | Find which times I am free                                                              | I can easily schedule new meetings with a group                                                     |


### Use cases

**System: StudyCircle (SC)**  
**Use Case: UC1 - Create a group**  
**Actor: User**

**MSS**

1.  User requests to create a new group with specified details.
2.  SC creates the new group and displays it.

    Use case ends.

**Extensions**

* 1a. SC detects an error in the input.

    * 1a1. SC specifies the error(s) and requests new input.
    * 1a2. User enters new input.

      Steps 1a1-1a2 are repeated until the input is valid.\
      Use case resumes from step 1.

* *a. At any point the User decides not to add the group.

  Use case ends.


**System: StudyCircle (SC)**  
**Use Case: UC2 - Create a contact**  
**Actor: User**

**MSS**

1.  User requests to create a new contact with specified details.
2.  SC creates the new contact and displays it.

    Use case ends.

**Extensions**

* 1a. SC detects an error in the input.

    * 1a1. SC specifies the error(s) and requests new input.
    * 1a2. User enters new input.

      Steps 1a1-1a2 are repeated until the input is valid.\
      Use case resumes from step 2.

* *a. At any point the user decides not to add the contact.

  Use case ends.


**System: StudyCircle (SC)**  
**Use Case: UC3 - Attach a contact to a group**  
**Actor: User**  
**Precondition: The specified group already exists within SC**

**MSS**

1.  User requests to <u>list all contacts (UC6)</u>.
2.  User requests to <u>list all groups (UC7)</u>.
3.  User requests to add specific contacts in the first list to a specific group in the second list.
4.  SC adds the contact to the group and notifies user of success of the task.

    Use case ends.

**Extensions**

* 1a. The contact the user wishes to add is not in the list or the list is empty.

    * 1a1. User requests to <u>create a new contact (UC2)</u>.

      Use case resumes from step 2.

* 3a. SC detects that the specified contact does not exist.

    * 3a1. SC shows an error message.

      Use case resumes at step 3.

* 3b. SC detects that the specified group does not exist.

    * 3b1. SC shows an error message.

      Use case resumes at step 3.

* 3c. SC detects that the contact is already in the group.

    * 3c1. SC tells user that the contact is already in the group.

      Use case ends.

* *a. At any point the user decides to stop the operation.

  Use case ends.


**System: StudyCircle (SC)**  
**Use Case: UC4 - Delete a group**  
**Actor: User**

**MSS**

1.  User requests to <u>list all groups (UC6)</u>.
2.  User chooses to delete a group from the list.
3.  SC deletes the group, keeping the member contacts, and displays the deleted group.

  Use case ends.

**Extensions**

* 2b. The specified group does not exist.

    * 2b1. SC shows an error message.

      Use case resumes from step 2.

**System: StudyCircle (SC)**  
**Use Case: UC5 - View all contacts' details**  
**Actor: User**

**MSS**

1.  User requests to list all contacts.
2.  SC display a list of all contacts and their details.

  Use case ends.

**Extensions**

* 1a. SC does not have any contacts currently saved.

    * 1a1. SC shows an error message.

      Use case ends.


**System: StudyCircle (SC)**  
**Use Case: UC6 - View all groups’ details**  
**Actor: User**

**MSS**

1.  User requests to list groups.
2.  SC shows user a list of all groups and their details.

  Use case ends.

**Extensions**

* 1a. SC does not have any groups currently saved.

    * 1a1. SC shows an error message.

      Use case ends.


**System: StudyCircle (SC)**  
**Use Case: UC7 - View specific contact’s details**  
**Actor: User**  
**Preconditions: The specified contact already exists within SC**

**MSS**

1.  User requests to <u>list all contacts (UC5)</u>.
2.  User requests to view a specific contact’s details in the list.
3.  SC shows the user the given contact’s details.

  Use case ends.

**Extensions**

* 2a. SC does not have the specified contact.

    * 2a1. SC prompts user that there is no matching contact.

      Use case resumes from step 2.

* *a. At any point the user decides to cancel the operation.

  Use case ends.


**System: StudyCircle (SC)**  
**Use Case: UC8 - View specific Group’s details**  
**Actor: User**  
**Preconditions: The specified Group already exists within SC**

**MSS**

1.  User requests to <u>list groups (UC6)</u>.
2.  User requests to view a specific group's details in the list.
3.  SC shows user the given group’s details.

    Use case ends.

**Extensions**

* 2a. SC does not have the specified group.

    * 2a1. SC prompts user that there is no matching group.

      Use case resumes from step 2.

* *a. At any point the user decides to cancel the operation.

  Use case ends.


**System: StudyCircle (SC)**  
**Use Case: UC9 - Create an event for a group (task/meeting/assignment)**  
**Actor: User**  
**Preconditions: The group which the event is to be attached to already exists**

**MSS**

1.  User requests for a <u>list of groups (UC6)</u>.
2.  User enters event details and specified group.
3.  SC adds the event to the group and displays the new event.

    Use case ends.

**Extensions**

* 2a. SC detects an error in the input.

    * 2a1. SC specifies the error(s) and requests new input.
    * 2a2. User enters valid input.

      Use case resumes from step 3.

* *a. At any point the user decides to cancel the operation.

  Use case ends.


**System: StudyCircle (SC)**  
**Use Case: UC10 - Add notes to a group**  
**Actor: User**  
**Preconditions: The specified group already exists within SC**

**MSS**

1.  User requests for a <u>list of groups (UC6)</u>.
2.  User requests to add a note to the specific group in the list.
3.  SC adds the note to the group and displays a confirmation message.

    Use case ends.

**Extensions**

* 2b. The specified group does not exist.

    * 2b1. SC shows an error message.

      Use case resumes from step 2.

* *a. At any point the user decides to cancel the operation.

  Use case ends.


**System: StudyCircle (SC)**  
**Use Case: UC11 - Edit details of a specific contact**  
**Actor: User**  
**Preconditions: The specified contact already exists within SC**

**MSS**

1.  User requests for a <u>list of contacts (UC5)</u>.
2.  User specifies a specific contact and selects which detail they would like to edit.
3.  User enters new details that they want to edit.
4.  SC edit the contact and display a confirmation message.

    Use case ends.

**Extensions**

* 2a. The detail is empty.

    * 2a1. SC shows an error message.

      Use case end.

* *a. At any point the user decides to cancel the operation.

  Use case ends.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 contacts without a noticeable sluggishness in performance for typical usage.
3.  Should be able to hold up to 1000 groups without a noticeable sluggishness in performance for typical usage
4.  user with above average typing speed for regular English text (i.e. not code, not system admin commands) should
    be able to accomplish most of the tasks faster using commands than using the mouse.
5.  Should work on any _mainstream OS_ without requiring an installer.
6.  Should work without use of any third-party libraries/services.
7.  GUI should work well for standard screen resolutions of 1920x1080 and higher.
8.  Product should be packaged into a single jar file and file size of jar file for product should not exceed 100mb.
9.  Product should work without the need for a remote server.
10. Data for a specific user should not be able to be accessed by other users even on the same device.
11. The data should be stored locally and in a human editable text file

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **GUI**: Graphical User Interface

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

    1. Download the jar file and copy into an empty folder

    1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

    1. Resize the window to an optimum size. Move the window to a different location. Close the window.

    1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

### Adding a contact

1. Add a contact 

    1. Prerequisites: none

    1. Test case: `add-contact n/John Doe p/98765432 e/e1235557@u.nus.edu`<br>
       Expected: Contact name John should appear in the contact list

### Listing all contacts

1. Listing all the contacts

    1. Test case: `list-contacts`<br>
       Expected: The status message show: `Listed all persons`, all the contacts show on the contact list panel.

### Finding contacts

1. Finding all the contacts contain some specified characters

    1. Test case: `find-contact ab`<br>
       Expected: The status message show: `x persons listed!` where x is the number of contacts that have the name contains the character `ab`, all the contacts contains the character `ab` show on the contact list panel.

   1. Test case: `find-contact ab cd`<br>
      Expected: The status message show: `x persons listed!` where x is the number of contacts that have the name contains the character `ab` or `cd`, all the contacts contains the character `ab` or `cd` show on the contact list panel.

   1. Test case: `find-contact aB CD`<br>
      Expected: The status message show: `x persons listed!` where x is the number of contacts that have the name contains the character `ab` or `cd`, all the contacts contains the character `ab` or `cd` show on the contact list panel.

   1. Test case: `find-contact`<br>
      Expected: The status message show an error message, the contact list panel remain the same.

### Deleting a contact

1. Deleting a contact while all contacts are being shown

    1. Prerequisites: List all contacts using the `list-contacts` command. Multiple contacts in the contact list.

    1. Test case: `delete-contact 1`<br>
       Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

    1. Test case: `delete-contact 0`<br>
       Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

    1. Other incorrect delete commands to try: `delete-contact`, `delete-contact x`, `...` (where x is larger than the contact list size)<br>
       Expected: Similar to previous.

### Editing a contact

1. Edit a contact while all contacts are being shown

    1. Prerequisites: must have at least one contact in the contact list

    1. Test case: `edit-contact 1 n/mary`<br>
       Expected: Fist contact name in the contact list should change to mary

### Listing all groups

1. Listing all the groups

    1. Test case: `list-groups`<br>
       Expected: The status message show: `Listed all groups`, all the groups show on the group list panel.

### Adding a group

1. Add a group

    1. Prerequisites: none

    1. Test case: `add-group n/CS2103T tp`<br>
       Expected: Group name CS2103T tp should appear in the group list

### Finding groups

1. Finding all the groups contain some specified characters

    1. Test case: `find-group ab`<br>
       Expected: The status message show: `x groups listed!` where x is the number of groups that have the name contains the character `ab`, all the groups contains the character `ab` show on the group list panel.

    1. Test case: `find-group ab cd`<br>
       Expected: The status message show: `x groups listed!` where x is the number of groups that have the name contains the character `ab` or `cd`, all the groups contains the character `ab` or `cd` show on the group list panel.

    1. Test case: `find-group aB CD`<br>
       Expected: The status message show: `x groups listed!` where x is the number of groups that have the name contains the character `ab` or `cd`, all the groups contains the character `ab` or `cd` show on the group list panel.

    1. Test case: `find-group`<br>
       Expected: The status message show an error message, the group list panel remain the same.

### Deleting a group

1. Deleting a group while all groups are being shown

    1. Prerequisites: List all groups using the `list-groups` command. Multiple groups in the group list.

    1. Test case: `delete-group 1`<br>
       Expected: First group is deleted from the list. Name of the deleted group shown in the status message. Timestamp in the status bar is updated.

    1. Test case: `delete-group 0`<br>
       Expected: No group is deleted. Error details shown in the status message. Status bar remains the same.

    1. Other incorrect delete commands to try: `delete-group`, `delete-group x`, `...` (where x is larger than the group list size)<br>
       Expected: Similar to previous.

### Adding a member to a group

1. Adding a member to a group while all contacts and groups are being shown

    1. Prerequisites: List all contacts and groups using the `list-contacts` and `list-groups` commands. The contact 
       you are trying to add must not already be in the group you are adding to. 

    1. Test case: `add-member g/1 c/1`<br>
       Expected: First contact is added to the group. Name of the contact added to the group will be shown. 
       Timestamp in the status bar is updated.

    1. Test case: `add-member g/0  c/0`<br>
       Expected: No member is added to the group. Error details shown in the status message. Status bar remains the 
       same.

    1. Other incorrect add-member commands to try: `add-member`, `add-member g/1 `, `add-member g/x c/x` (where x is 
       larger than the list size)<br>
       Expected: Similar to previous.

### Deleting a member from a group

1. Deleting a member from a group while all contacts and groups are being shown

    1. Prerequisites: List all groups using the `list-groups` commands. The contact you are trying to remove must 
       already be in the group you are removing from.

   1. Test case: `delete-member g/1 c/1`<br>
      Expected: First contact is first group's member list is removed from the group. Name of the contact removed from 
      the group will be shown.
      Timestamp in the status bar is updated.

   1. Test case: `delete-member g/0  c/0`<br>
      Expected: No member is added to the group. Error details shown in the status message. Status bar remains the
      same.

   1. Other incorrect add-member commands to try: `delete-member`, `delete-member g/1 `, `delete-member g/x c/x` (where x is larger than the list size)<br>
      Expected: Similar to previous.

### Showing and editing a group's dashboard

1. Showing a group's dashboard

    1. Prerequisites: List all groups using the `list-groups` command.

    1. Test case: `show-dashboard 1`<br>
       Expected: The dashboard panel for the selected group is shown to the user.
       Timestamp in the status bar is updated.

    1. Test case: `show-dashboard`<br>
       Expected: No member is added to the group. Error details shown in the status message. Status bar remains the
       same.

    1. Other incorrect add-member commands to try: `show-dashboard x` (where x is larger than the list size)<br>
       Expected: Similar to previous.

1. Editing a group's dashboard notes

    1. Prerequisites: The group to edit's dashboard is already shown.

    1. Test case: type "I love computing" into the dashboard panel's white note box <br>
       Expected: The notes typed into the note box will be saved and shown to the user
       Timestamp in the status bar is updated.

### Editing a contact

1. Edit a contact while all contacts are being shown

    1. Prerequisites: must have at least one contact in the contact list

    1. Test case: `edit-contact 1 n/mary`<br>
       Expected: First contact name in the contact list should change to mary

### Editing a group

1. Edit a group while all groups are being shown

    1. Prerequisites: must have at least two groups in the group list
       
    1. Test case: `edit-group 2 n/CS2101 CA4`<br>
       Expected: Second group name in the group list should change to CS2101 CA4

### Setting repository link for a group

1. Setting repository link for a group

    1. Prerequisites: must have at least one group in the group list

    1. Test case: `set-repo 1 r/https://github.com/AY2526S1-CS2103T-F12-1/tp`<br>
       Expected: You should see a green `repo link` appear in the first group.

### Getting repository link from a group

1. Getting repository link from a group

    1. Prerequisites: must have at least one group in the group list with repo link set successfully 

    1. Test case: `get-repo 1`<br>
       Expected: you should see a success message saying that "This link is copied to your clipboard, you can paste it now". you should be able to paste that link in a browser.

### Deleting a repository link from a group

1. Deleting a repository link from a group

    1. Prerequisites: must have at least one group in the group list with repo link set successfully

     1. Test case: `delete-repo 1`<br>
       Expected: You should see the green `repo link` disappear in the first group.

### Adding an event

1. Add an event to a specified group

    1. Prerequisites 1: List all groups using the `list-groups` command. Multiple groups in the group list.

    1. Test case: `add-event 1 d/abc`<br>
       Expected: The status message show: `New event: 'abc' added to group: <1st GROUP NAME>`, an event `abc` appear in the group card of the 1st group.

### Deleting an event

1. Delete a specified event from a specified group

    1. Prerequisites 1: List all groups using the `list-groups` command. Multiple groups in the group list.

    1. Test case: `delete-event 1 e/1`<br>
       Expected: The status message show: `Event: '<1st EVENT DESCRIPTION>' deleted from group: <1st GROUP NAME>`, the first event disappeared in the group card of the 1st group.

### Editing an event

1. Edit a specified event in a specified group

    1. Prerequisites 1: List all groups using the `list-groups` command. Multiple groups in the group list.

    1. Test case: `edit-event 1 e/1 d/efg`<br>
       Expected: The status message show: `Edited Event: '<1st EVENT DESCRIPTION>' in Group: <1st GROUP NAME>`, the first event change to `efg` in the group card of the 1st group.

### Saving data

1. Dealing with missing/corrupted data files

    1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
