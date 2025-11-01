package seedu.address.model.group;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;
import java.util.Objects;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.event.Event;
import seedu.address.model.event.UniqueEventList;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;

/**
 * Represents a Group in the address book.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Group {

    // Identity fields
    private final GroupName name;

    // Data fields
    private final UniqueEventList events;
    private final UniquePersonList persons;
    private final Dashboard dashboard;

    //quick link fields
    private final RepoLink repoLink;


    // To be used only by the factory method
    private Group(GroupName name, UniqueEventList events, UniquePersonList persons, RepoLink repoLink) {
        requireAllNonNull(name, events, persons, repoLink);
        this.name = name;
        this.events = events;
        this.persons = persons;
        this.repoLink = repoLink;
        this.dashboard = new Dashboard(this, "");
    }

    private Group(GroupName name, UniqueEventList events, UniquePersonList persons, RepoLink repoLink,
                  Dashboard dashboard) {
        requireAllNonNull(name, events, persons, repoLink, dashboard);
        this.name = name;
        this.events = events;
        this.persons = persons;
        this.repoLink = repoLink;
        this.dashboard = dashboard;
    }

    /**
     * Every field must be present and not null.
     */
    public Group(GroupName name) {
        requireNonNull(name);
        this.name = name;
        events = new UniqueEventList();
        persons = new UniquePersonList();
        repoLink = new RepoLink();
        dashboard = new Dashboard(this);
    }

    /**
     * Static method to create Group from storage or external data.
     **/
    public static Group fromStorage(GroupName name, UniqueEventList events, UniquePersonList persons,
                                    RepoLink repoLink, String dashboardNotes) {
        requireAllNonNull(name, events, persons, repoLink);
        Group toReturn = new Group(name, events, persons, repoLink);
        toReturn.dashboard.setNotes(dashboardNotes);
        return toReturn;
    }

    /**
     * Returns a new Group with updated name.
     */
    public Group withUpdatedName(GroupName newName) {
        requireNonNull(newName);

        return new Group(newName, events, persons, repoLink, dashboard);
    }

    public GroupName getName() {
        return name;
    }

    public ObservableList<Event> getEvents() {
        return events.asUnmodifiableObservableList();
    }

    public ObservableList<Person> getPersons() {
        return persons.asUnmodifiableObservableList();
    }

    public Dashboard getDashboard() {
        return dashboard;
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public void addPerson(Person toAdd) {
        persons.add(toAdd);
    }

    public void setEvent(Event target, Event editedEvent) {
        events.setEvent(target, editedEvent);
    }

    public void removeEvent(Event toRemove) {
        events.remove(toRemove);
    }

    public void removePerson(Person toRemove) {
        persons.remove(toRemove);
    }

    public void updatePerson(Person target, Person editedPerson) {
        persons.setPerson(target, editedPerson);
    }

    public void replacePersons(UniquePersonList replacement) {
        persons.setPersons(replacement);
    }

    public void replacePersons(List<Person> replacement) {
        persons.setPersons(replacement);
    }

    public void setDashboard(String dashboardNotes) {
        this.dashboard.setNotes(dashboardNotes);
    }

    public Group setRepoLink(RepoLink repoLink) {
        requireNonNull(repoLink);

        return new Group(name, events, persons, repoLink, dashboard);
    }

    public RepoLink getRepoLink() {
        return repoLink;
    }

    /**
     * Returns group name as a String
     */
    public String getNameAsString() {
        return getName().toString();
    }

    /**
     * Returns true if the group contains an equivalent person as the given argument.
     */
    public boolean containsPerson(Person personToCheck) {
        return persons.contains(personToCheck);
    }

    /**
     * Returns true if the group contains the given {@code Event}.
     */
    public boolean containsEvent(Event eventToCheck) {
        return events.contains(eventToCheck);
    }

    /**
     * Returns true if both groups have the same name.
     * This defines a weaker notion of equality between two groups.
     */
    public boolean isSameGroup(Group otherGroup) {
        if (otherGroup == this) {
            return true;
        }

        return otherGroup != null
                && otherGroup.getName().equals(getName());
    }

    /**
     * Returns true if both groups have the same identity and data fields.
     * This defines a stronger notion of equality between two groups.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Group)) {
            return false;
        }

        Group otherGroup = (Group) other;
        return name.equals(otherGroup.name)
                && events.equals(otherGroup.events)
                && persons.equals(otherGroup.persons)
                && repoLink.equals(otherGroup.repoLink);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, events, persons, repoLink);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("events", events)
                .add("persons", persons)
                .add("repo-link", repoLink)
                .toString();
    }

}
