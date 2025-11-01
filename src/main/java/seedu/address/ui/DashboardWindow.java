package seedu.address.ui;

import java.util.logging.Logger;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.event.Event;
import seedu.address.model.group.Group;
import seedu.address.model.person.Person;


/**
 * Controller for a dashboard page
 */
public class DashboardWindow {
    private static final Logger logger = LogsCenter.getLogger(DashboardWindow.class);

    private Stage root;
    private Label groupNameLabel;
    private Label repoLinkLabel;
    private TextArea notesTextArea;
    private ListView<String> memberList;
    private ListView<String> eventList;
    private Group group;
    private Button copyRepoButton;

    /**
     * Creates a new DashboardWindow.
     */
    public DashboardWindow() {
        initialize();
    }

    /**
     * Initializes the dashboard window.
     */
    private void initialize() {
        root = new Stage();
        root.setTitle("Group Dashboard");
        root.setMinWidth(800);
        root.setMinHeight(700);

        HBox mainContainer = createMainContainer();
        Scene scene = new Scene(mainContainer);
        root.setScene(scene);
    }

    /**
     * Creates the MainContainer for the dashboard window.
     */
    private HBox createMainContainer() {
        HBox mainContainer = new HBox();
        mainContainer.setSpacing(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: #383838;");
        mainContainer.setPrefSize(900, 750);

        VBox leftPanel = createLeftPanel();
        VBox rightPanel = createRightPanel();

        HBox.setHgrow(leftPanel, Priority.ALWAYS);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        mainContainer.getChildren().addAll(leftPanel, rightPanel);
        return mainContainer;
    }

    /**
     * Creates the left panel for the dashboard window, containing the group name, repo section and notes section
     */
    private VBox createLeftPanel() {
        VBox leftPanel = new VBox();
        leftPanel.setSpacing(15);
        leftPanel.setPrefWidth(400);

        Label titleLabel = new Label("Group Dashboard");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white; "
                + "-fx-font-family: Verdana;");

        groupNameLabel = new Label("Group: Not selected");
        groupNameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white; "
                + "-fx-font-family: Verdana;");

        VBox repoSection = createRepoLinkSection();
        VBox notesSection = createNotesArea();
        HBox clearNotesButtonContainer = createClearNotesButton();

        VBox.setVgrow(notesSection, Priority.ALWAYS);

        leftPanel.getChildren().addAll(
                titleLabel,
                new Separator(),
                groupNameLabel,
                repoSection,
                new Separator(),
                notesSection,
                clearNotesButtonContainer
        );

        return leftPanel;
    }

    /**
     * Creates the editable notes area for the left panel of the dashboard
     */
    private VBox createNotesArea() {
        VBox notesAreaSection = new VBox();

        Label notesLabel = new Label("Notes:");
        notesLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white; "
                + "-fx-font-family: Verdana;");

        notesTextArea = new TextArea();
        notesTextArea.setPromptText("Enter your notes here...");
        notesTextArea.setWrapText(true);
        notesTextArea.setPrefRowCount(8);
        notesTextArea.setStyle(
                "-fx-background-color: white;"
                        + "-fx-text-fill: black;"
                        + "-fx-border-color: #cccccc;"
                        + "-fx-border-radius: 5;"
                        + "-fx-background-radius: 5;"
                        + "-fx-padding: 10;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-family: Verdana;"
                        + "-fx-control-inner-background: white;"
                        + "-fx-prompt-text-fill: #888888;"
        );

        notesTextArea.textProperty()
                .addListener(((observable, oldValue, newValue) -> handleSaveNotes()));
        VBox.setVgrow(notesTextArea, Priority.ALWAYS);
        notesAreaSection.getChildren().addAll(notesLabel, notesTextArea);
        return notesAreaSection;
    }

    /**
     * Creates the clear notes button for the left panel of the dashboard
     */
    private HBox createClearNotesButton() {
        HBox notesButtonContainer = new HBox();
        notesButtonContainer.setSpacing(12);
        notesButtonContainer.setAlignment(Pos.CENTER_RIGHT);

        Button clearNotesButton = new Button("Clear Notes");
        clearNotesButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-padding: 8 16; "
                + "-fx-font-weight: bold; -fx-font-family: Verdana;");
        clearNotesButton.setOnAction(e -> handleClearNotes());

        notesButtonContainer.getChildren().addAll(clearNotesButton);
        return notesButtonContainer;
    }

    /**
     * Creates the repo link section for the left panel of the dashboard
     */
    private VBox createRepoLinkSection() {
        Label repoTitleLabel = new Label("Repository Link:");
        repoTitleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white; "
                + "-fx-font-family: Verdana;");

        repoLinkLabel = new Label("No repository link set");
        repoLinkLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #B0BEC5; -fx-font-family: Verdana;");

        copyRepoButton = createCopyRepoButton();

        HBox repoLinkContainer = new HBox();
        repoLinkContainer.setSpacing(10);
        repoLinkContainer.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(repoLinkLabel, Priority.ALWAYS);
        repoLinkContainer.getChildren().addAll(repoLinkLabel, copyRepoButton);

        VBox repoSection = new VBox();
        repoSection.setSpacing(5);
        repoSection.getChildren().addAll(repoTitleLabel, repoLinkContainer);

        return repoSection;
    }

    /**
     * Creates the copy button for the repo link section of the dashboard
     */
    private Button createCopyRepoButton() {
        copyRepoButton = new Button("Copy");
        copyRepoButton.setMinWidth(50);
        copyRepoButton.setPrefWidth(50);

        copyRepoButton.setStyle("-fx-background-color: #757575; -fx-text-fill: #cccccc; -fx-padding: 4 8; "
                + "-fx-font-weight: bold; -fx-font-family: Verdana; -fx-font-size: 12px;");

        copyRepoButton.setOnMouseEntered(e -> {
            if (!copyRepoButton.isDisabled()) {
                copyRepoButton.setStyle("-fx-background-color: #1976D2; -fx-text-fill: white; -fx-padding: 4 8; "
                        + "-fx-font-weight: bold; -fx-font-family: Verdana; -fx-font-size: 11px; "
                        + "-fx-cursor: hand;");
            }
        });
        copyRepoButton.setOnMouseExited(e -> {
            if (!copyRepoButton.isDisabled()) {
                copyRepoButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-padding: 4 8; "
                        + "-fx-font-weight: bold; -fx-font-family: Verdana; -fx-font-size: 11px; "
                        + "-fx-cursor: hand;");
            }
        });
        copyRepoButton.setOnMousePressed(e -> {
            if (!copyRepoButton.isDisabled()) {
                copyRepoButton.setStyle("-fx-background-color: #0D47A1; -fx-text-fill: white; -fx-padding: 4 8; "
                        + "-fx-font-weight: bold; -fx-font-family: Verdana; -fx-font-size: 11px; "
                        + "-fx-cursor: hand;");
            }
        });

        copyRepoButton.setOnMouseReleased(e -> {
            if (!copyRepoButton.isDisabled()) {
                copyRepoButton.setStyle("-fx-background-color: #1976D2; -fx-text-fill: white; -fx-padding: 4 8; "
                        + "-fx-font-weight: bold; -fx-font-family: Verdana; -fx-font-size: 11px; "
                        + "-fx-cursor: hand;");
            }
        });

        copyRepoButton.setOnAction(e -> handleCopyRepoLink());
        copyRepoButton.setDisable(true);
        return copyRepoButton;
    }

    /**
     * Creates the right panel for the dashboard window, containing the group member list and event list
     */
    private VBox createRightPanel() {
        VBox rightPanel = new VBox();
        rightPanel.setSpacing(15);
        rightPanel.setPrefWidth(400);

        VBox memberListSection = createMemberListSection();
        VBox eventListSection = createEventListSection();

        rightPanel.getChildren().addAll(
                memberListSection,
                eventListSection
        );

        return rightPanel;
    }

    /**
     * Creates the member list section for the right panel of the dashboard
     */
    private VBox createMemberListSection() {
        VBox memberListContainer = new VBox();

        Label membersLabel = new Label("Members:");
        membersLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white; "
                + "-fx-font-family: Verdana;");

        memberList = new ListView<>();
        Label memberPlaceholder = new Label("No members in this group");
        memberPlaceholder.setStyle("-fx-text-fill: white; -fx-font-family: Verdana; -fx-font-size: 16px;");
        memberList.setPlaceholder(memberPlaceholder);
        memberList.setStyle(
                "-fx-background-color: #2b2b2b;"
                        + "-fx-text-fill: white;"
                        + "-fx-border-color: #cccccc;"
                        + "-fx-border-radius: 5;"
                        + "-fx-background-radius: 5;"
                        + "-fx-padding: 5;"
                        + "-fx-font-family: Verdana;"
                        + "-fx-control-inner-background: #2b2b2b;"
        );

        VBox.setVgrow(memberList, Priority.ALWAYS);
        memberListContainer.getChildren().addAll(membersLabel, memberList);
        return memberListContainer;
    }

    /**
     * Creates the event list section for the right panel of the dashboard
     */
    private VBox createEventListSection() {
        VBox eventListContainer = new VBox();

        Label eventsLabel = new Label("Events:");
        eventsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white; "
                + "-fx-font-family: Verdana;");

        eventList = new ListView<>();
        Label eventPlaceholder = new Label("No events in this group");
        eventPlaceholder.setStyle("-fx-text-fill: white; -fx-font-family: Verdana; -fx-font-size: 16px;");
        eventList.setPlaceholder(eventPlaceholder);
        eventList.setStyle(
                "-fx-background-color: #2b2b2b;"
                        + "-fx-text-fill: white;"
                        + "-fx-border-color: #cccccc;"
                        + "-fx-border-radius: 5;"
                        + "-fx-background-radius: 5;"
                        + "-fx-padding: 5;"
                        + "-fx-font-family: Verdana;"
                        + "-fx-control-inner-background: #2b2b2b;"

        );

        VBox.setVgrow(eventList, Priority.ALWAYS);
        eventListContainer.getChildren().addAll(eventsLabel, eventList);
        return eventListContainer;
    }

    /**
     * Shows the dashboard window with the specified group data.
     */
    public void show(Group group) {
        if (group == null) {
            logger.warning("Attempted to show dashboard with null group");
            return;
        }

        if (root.isShowing()) {
            root.close();
        }

        root = new Stage();
        initialize();

        this.group = group;
        updateDashboardContent();
        updateMemberAndEventList();
        loadNotes();
        logger.fine("Showing dashboard for group: " + group.getName());

        root.show();
        root.centerOnScreen();
        root.requestFocus();
        root.setTitle("Dashboard - " + group.getName());
    }

    /**
     * Updates the dashboard content with the current group name and its repo link.
     */
    private void updateDashboardContent() {
        if (group != null) {
            groupNameLabel.setText("Group: " + group.getName());
            String repoLink = group.getRepoLink().toString();

            if (group.getRepoLink() != null && !repoLink.isEmpty() && !repoLink.equals("none")) {
                repoLinkLabel.setText(repoLink);
                repoLinkLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-font-family: Verdana; "
                        + "-fx-text-overrun: ellipsis; -fx-wrap-text: false;");
                copyRepoButton.setDisable(false);
                copyRepoButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-padding: 4 8; "
                        + "-fx-font-weight: bold; -fx-font-family: Verdana; -fx-font-size: 11px;");
            }

            root.setTitle("Dashboard - " + group.getName());
        }
    }

    /**
     * Copies a group's repository link to the user's clipboard
     */
    private void handleCopyRepoLink() {
        if (group != null && group.getRepoLink() != null && !group.getRepoLink().toString().isEmpty()) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(group.getRepoLink().toString());
            clipboard.setContent(content);
        }
    }

    /**
     * Updates the member and event lists with the current group data.
     */
    private void updateMemberAndEventList() {
        if (group != null) {
            memberList.getItems().clear();
            eventList.getItems().clear();
            int currentMemberIndex = 1;
            int currentEventIndex = 1;

            for (Person person : group.getPersons()) {
                String name = currentMemberIndex + ") " + person.getNameAsString() + " (" + person.getEmail() + ")";
                memberList.getItems().add(name);
                currentMemberIndex++;
            }

            for (Event event : group.getEvents()) {
                String description = currentEventIndex + ") " + event.toString();
                eventList.getItems().add(description);
                currentEventIndex++;
            }

        }
    }

    /**
     * Loads saved notes for the current group.
     */
    private void loadNotes() {
        if (group != null && group.getDashboard() != null) {
            String savedNotes = group.getDashboard().getNotes();
            notesTextArea.setText(savedNotes != null ? savedNotes : "");
        }
    }

    /**
     * Handles saving of notes.
     */
    private void handleSaveNotes() {
        if (group != null && group.getDashboard() != null) {
            String notes = notesTextArea.getText();
            group.getDashboard().setNotes(notes);

            logger.info("Notes saved for group: " + group.getName());
        }
    }

    /**
     * Handles clearing of notes.
     */
    private void handleClearNotes() {
        notesTextArea.clear();
        if (group != null) {
            logger.info("Notes cleared for group: " + group.getName());
        }
    }

    /**
     * Hides the dashboard window.
     */
    public void hide() {
        root.hide();
    }

}

