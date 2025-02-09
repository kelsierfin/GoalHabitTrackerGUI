// Import Package

package ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui;


// Imports
import ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.objects.Goal;
import ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.objects.Habit;
import ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.util.FileLoader;
import ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.util.FileSaver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.BarChart;
import javax.management.StringValueExp;
import java.io.File;
import java.util.*;

import static ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.Data.*;

/**
 * This class provides the controller which connects the Habit Tracker to the GUI
 *
 * @authors Phone, Sanbeer, Tania
 * @tutorial T14, T09, T10
 * Emails: tania.rizwan@ucalgary.ca, sanbeer.shafin@ucalgary.ca, phonemyat.paing@ucalgary.ca
 */

public class MainController {

    // Data obj
    private static Data data = new Data();

    // Status bar
    @FXML
    private Label statusLabel;

    // Dropdowns
    @FXML
    private ChoiceBox<String> goalsDropDown;

    @FXML
    private ChoiceBox<String> habitsDropDown;

    // Choices for the categories
    @FXML
    private ChoiceBox<String> myCategoryBox;
    // Choices for the matrix Quadrants
    @FXML
    private ChoiceBox<String> myMatrixBox;
    // Buttpn to initiate the setCategory method
    @FXML
    private Button categoryButton;
    // Button to intialize the setMatrix method
    @FXML
    private Button matrixButton;
    // Show the matrix button
    @FXML
    private Button matrixShower;

    // Used to show the habitBarChart
    @FXML
    private Button habitBar;

    // GridPanes in Tracker Views
    @FXML
    private GridPane generalOverviewPane;

    @FXML
    private GridPane habitsOverviewPane;

    // PROGRAM SET-UP

    /**
     * Initialize method to pre-load (set-up GUI)
     */
    @FXML
    public void initialize() {
        populateHabitsDropDown();
    }


    /**
     * Populates the {@code habitsDropDown} {@code ChoiceBox} with the names of habits.
     * It retrieves all habit sets from the tracker and adds each habit's name to the dropdown.
     * This method should be called after initializing the tracker with data.
     *
     * @author: Phone Myat Paing
     */
    private void populateHabitsDropDown() {
        // First, clear the dropdown to avoid duplicating data
        if (habitsDropDown != null) {
            habitsDropDown.getItems().clear();

            // Create a set to hold all unique habits
            Set<String> uniqueHabits = new HashSet<>();

            // Iterate over all sets of habits in the tracker
            data.getTracker().values().forEach(habitsSet ->
                    // Add each unique habit's name to the set
                    habitsSet.forEach(habit -> uniqueHabits.add(habit.getHabit()))
            );

            // Add all unique habits to the habitsDropDown ChoiceBox
            habitsDropDown.getItems().addAll(uniqueHabits);
        }
    }

    /**
     * Goals Drop Down: lists all goals in the drop down to be used in various methods.
     * The drop down is updated anytime a goal is added/deleted.
     *
     * @author: Tania Rizwan
     */
    protected void setGoalsDropDown() {
        // First, clear the dropdown. (Prevents duplicates)
        if (goalsDropDown != null){
            goalsDropDown.getItems().clear();
            // Populate dropdown with each goal in HashSet goals (from Data.java)
            HashSet<Goal> goals = data.getGoals();
            for (Goal goal : goals) {
                if (goal != null) {
                    goalsDropDown.getItems().add(String.valueOf(goal.getGoal()));
                }
            }
        }

    }

    /**
     * Update the status label with specified message and color.
     *
     * @param message The message to display
     * @param color   The color of the text
     * @author: Phone Myat Paing
     */
    public void updateStatus(String message, String color) {
        String fontSize = "14px"; // Fixed font size
        String fontFamily = "CoolReader"; // Fixed font family
        String fontStyle = "italic"; // Fixed italic style

        Platform.runLater(() -> {
            statusLabel.setText(message);
            statusLabel.setStyle("-fx-text-fill:" + color + ";" +
                    "-fx-font-size:" + fontSize + ";" +
                    "-fx-font-family:" + fontFamily + ";" +
                    "-fx-font-style:" + fontStyle + ";");
            statusLabel.applyCss();
            statusLabel.layout();
        });
    }

    /**
     * * This method is part of the reset process that reinitializes the application's data to a default state.
     *
     * @author: Phone Myat Paing
     */
    private void resetAllData() {
        if (choicesArrayList2 != null) {
            choicesArrayList2.clear();
        }
        if (goals != null) {
            goals.clear();
        }
        if (matrix != null) {
            matrix.clear();
        }
        if (fields != null) {
            fields.clear();
        }
        if (matrix2 != null) {
            matrix2.clear();
        }
        if (list10 != null) {
            list10.clear();
        }
        if (list20 != null) {
            list20.clear();
        }
        if (list30 != null) {
            list30.clear();
        }
        if (list40 != null) {
            list40.clear();
        }

        if (goalsDropDown != null) {
            goalsDropDown.getItems().clear();
        }

        if (habitsDropDown != null) {
            habitsDropDown.getItems().clear();
            for (Map.Entry<Goal, HashSet<Habit>> e: tracker.entrySet()) {
                e.getValue().clear();
            }
        }

        generalOverviewPane.getChildren().clear();
        generalOverviewPane.getColumnConstraints().clear();
        generalOverviewPane.getRowConstraints().clear();
        generalOverviewPane.setGridLinesVisible(false);

    }


    // MENU ITEMS

    /**
     * Save the current tracker to a file.
     * This method is invoked when the user selects the 'Save' option from the menu.
     *
     * @author: Phone Myat Paing
     */
    @FXML
    protected void saveAction() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        // Define the initial file name
        fileChooser.setInitialFileName("tracker_data.csv");

        // Get the current window for the dialog
        Window stage = statusLabel.getScene().getWindow();

        // Show save file dialog
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            // Save the data to the file
            boolean success = FileSaver.save(file, data);

            if (success) {
                updateStatus("File saved successfully.", "green");
            } else {
                updateStatus("Failed to save file.", "red");
            }
        }
    }

    /**
     * Load the existing process from a file.
     * This method is invoked when the user selects the 'Load' option form the menu.
     *
     * @author: Phone Myat Paing
     */
    @FXML
    protected void loadAction() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter for CSV files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        // Get the current window for the dialog
        Window stage = statusLabel.getScene().getWindow();

        // Show open file dialog
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {

            //clear the current existing data, in case there is some unnecessary data
            habitsDropDown.getItems().clear();
            goalsDropDown.getItems().clear();
            // Load the data from the file
            Data loadedData = FileLoader.load(file);


            if (loadedData != null) {
                data = loadedData; // Update the data model
                populateHabitsDropDown();
                setGoalsDropDown();
                updateStatus("Data loaded successfully from " + file.getName(), "green");
            } else {
                updateStatus("Failed to load data from file.", "red");
            }
            // Update general tracker
            setTrackerGeneralView();
            // Update habitsGeneralView
            setHabitsGeneralView();
        }

    }

    /**
     * This method takes in an argument from the command-line to allow GUI to run with pre-loaded data.
     * It is separate to the loadAction method, since that method cannot use an input (as it is an EventHandler method)
     * @param file - The file to be loaded in
     * @author Tania Rizwan, Johnathan Hudson (source code from Demo3 video)
     */
    protected void loadFromCMDLine(File file) {
        Data data = FileLoader.load(file);
        if (data == null) {
            updateStatus("Failed to load data from file.", "red");
        } else {
            MainController.data = data;
            updateStatus("Data loaded successfully from " + file.getName(), "green");
        }
        populateHabitsDropDown();
        setGoalsDropDown();
            // Update general tracker
        setTrackerGeneralView();
            // Update habitsGeneralView
        setHabitsGeneralView();
    }

    /**
     * This method allows a user to create goal objects using data.java
     *
     * @author: Tania Rizwan
     */
    @FXML
    protected void menuAddGoalsAction() {

        // Set-up Dialog to add Goal Name and Ideal Count
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Add a Goal");

        // Add Buttons (OK and Cancel)
        ButtonType addGoal = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(addGoal, cancel);

        // Get the button bar to configure buttons
        ButtonBar buttonBar = (ButtonBar) dialog.getDialogPane().lookup(".button-bar");
        buttonBar.setButtonOrder(ButtonBar.BUTTON_ORDER_NONE);

        // Create TextFields
        TextField goalNameTextField = new TextField();
        TextField idealCountTextfield = new TextField();
        goalNameTextField.setPromptText("Enter a string");
        idealCountTextfield.setPromptText("Enter a digit (within 0-7)");

        // Add the fields to a grid
        GridPane addGoalsGrid = new GridPane();
        addGoalsGrid.add(new Label("Goal Name: "), 0, 0);
        addGoalsGrid.add(goalNameTextField, 0, 1);
        addGoalsGrid.add(new Label("Ideal Count: "), 1, 0);
        addGoalsGrid.add(idealCountTextfield, 1, 1);

        // Add grid pane to dialog pane
        dialog.getDialogPane().setContent(addGoalsGrid);


        // Convert result to string when "Add" button is clicked. Result Converter processes user input when the "Add" button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addGoal) {
                try {
                    // Create vars
                    String goalName = goalNameTextField.getText().toLowerCase();
                    String idealCount = idealCountTextfield.getText(); // Process as String first to check if empty later

                    if (!goalName.isEmpty() || !idealCount.isEmpty()) {
                        Integer idealCountAsInt = Integer.parseInt(idealCount);
                        if (idealCountAsInt <= 0 || idealCountAsInt > 7) {
                            throw new NullPointerException(); // Process idealCount as null;
                        } else if (data.createAGoal(goalName, Integer.parseInt(idealCount), null)) {
                            updateStatus("Goal added successfully!", "green");
                            setGoalsDropDown();
                            setTrackerGeneralView();
                            // Update habitsGeneralView
                            setHabitsGeneralView();
                        }
                    } else {
                        throw new NullPointerException();
                    }
                } catch (NullPointerException e) {
                    updateStatus("Enter valid data!", "red");
                } catch (NumberFormatException e) {
                    updateStatus("Enter valid data!", "red");
                }
            }


            return "";
        });

        dialog.show();

    }

    /**
     * This method allows a user to delete a Goal object
     *
     * @author: Tania Rizwan
     */
    @FXML
    protected void menuDeleteAGoalAction() {
        // Create a dialog box with goal dropdown
        // User selects goal, and clicks delete
        // Call deleteAGoal method

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Delete a Goal");
        dialog.setHeaderText("Select the name of the goal to delete");


        // Add buttons (Delete and Cancel)
        ButtonType delete = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(delete, cancel);
        // Get the ButtonBar and configure button order
        ButtonBar buttonBar = (ButtonBar) dialog.getDialogPane().lookup(".button-bar");
        buttonBar.setButtonOrder(ButtonBar.BUTTON_ORDER_NONE);

        // Create a copy of goalsDropDown
        ChoiceBox<String> goalsDropDownCopy = new ChoiceBox<>();
        goalsDropDownCopy.setItems(goalsDropDown.getItems());
        goalsDropDownCopy.setValue(goalsDropDown.getValue());


        // Create a VBox so we can store the dropdown with padding around
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.getChildren().add(goalsDropDownCopy); // Add dropdown to the vbox
        dialog.getDialogPane().setContent(container); // Add container to dialog

        // Use ResultConverter to get result when Delete/Cancel button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == delete) {

                // Get the name of the goal from dropdown
                String selectedValue = goalsDropDownCopy.getValue();

                if (selectedValue == null) {
                    updateStatus("Select a goal", "red");  // Tell user to select a goal before clicking Delete button
                } else {
                    if (data.goalDelete(selectedValue)) {
                        updateStatus("Goal deleted successfully", "green");
                        // Update goalsDropDown
                        setGoalsDropDown();
                        // Update habitsDropDown
                        populateHabitsDropDown();
                        // Update general tracker
                        setTrackerGeneralView();
                        // Update habitsGeneralView
                        setHabitsGeneralView();
                    } else {
                        updateStatus("Error deleting goal", "red");
                    }
                }

                return goalsDropDown.getValue();
            }
            return null; // Return null if Cancel button is clicked
        });

        dialog.show();
    }



    /**
     * This method prompts the user to select a Goal and enter a list of habits for it.
     * The user must enter habits separated by commas.
     * The data input by the user is then processed in data.java
     *
     * @author: Tania Rizwan
     */
    @FXML
    protected void menuAddAHabitAction() {

        // Create a dialog that allows user to select goal
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Add a Habit");

        // Add buttons (Add and Cancel)
        ButtonType add = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(add, cancel);
        // Get the ButtonBar and configure button order
        ButtonBar buttonBar = (ButtonBar) dialog.getDialogPane().lookup(".button-bar");
        buttonBar.setButtonOrder(ButtonBar.BUTTON_ORDER_NONE);

        // Create a copy of goalsDropDown
        ChoiceBox<String> goalsDropDownCopy = new ChoiceBox<>();
        goalsDropDownCopy.setItems(goalsDropDown.getItems());
        goalsDropDownCopy.setValue(goalsDropDown.getValue());

        // Create labels
        Label addGoalHeader = new Label("Select a Goal");
        Label addHabitsListHeader = new Label("Enter Habits (separated by a comma)");

        // Create TextField to populate with list of habits
        TextField habitsInput = new TextField();

        // Create a VBox so we can store the dropdown with padding around, and place different elements
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(addGoalHeader, goalsDropDownCopy, addHabitsListHeader, habitsInput); // Add dropdown to the vbox
        dialog.getDialogPane().setContent(container); // Add container to dialog

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == add) {

                // Get the user-input
                String goalName = goalsDropDownCopy.getValue();
                String habitsInputAsString = habitsInput.getText().trim(); // Remove all whitespace (leading/trailing)
                String[] habitsInputSplit = habitsInputAsString.split(",");
                List<String> habitsInputList = Arrays.asList(habitsInputSplit);
                ArrayList<String> habitsList = new ArrayList<>(habitsInputList);

                // Input error handling
                // Handle if user clicks habits textfield, but doesn't enter anything
                if (habitsInputAsString.equals("") || habitsInputAsString.equals(" ")) {
                    habitsList.clear(); //Make list empty
                }

                if (habitsList.isEmpty()) {
                    updateStatus("Enter a valid list of habits and re-try", "red");
                }
                if (goalName == null) {
                    updateStatus("Select a valid goal and re-try", "red");
                }

                // Send data to data.java
                data.addHabits(goalName, habitsList, 0);
                updateStatus("Habits added successfully!", "green");
                // Update habitsDropDown
                populateHabitsDropDown();
                // Update habitsGeneralView
                setHabitsGeneralView();

            }
            return "";
        });

        dialog.show();

    }

    /**
     * This method allows a user to see the list of all habits, and delete one at a time.
     *
     * @author Tania Rizwan
     */
    @FXML
    protected void menuDeleteHabitsAction() {
        // Open a dialog which presents the habits dropdown
        // Have user select a habit to delete

        // Create dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Delete a Habit");
        dialog.setHeaderText("Select the name of the habit to delete");


        // Add buttons (Delete and Cancel)
        ButtonType delete = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(delete, cancel);
        // Get the ButtonBar and configure button order
        ButtonBar buttonBar = (ButtonBar) dialog.getDialogPane().lookup(".button-bar");
        buttonBar.setButtonOrder(ButtonBar.BUTTON_ORDER_NONE);

        // Create a copy of habitsDropDown
        ChoiceBox<String> habitsDropDownCopy = new ChoiceBox<>();
        habitsDropDownCopy.setItems(habitsDropDown.getItems());
        habitsDropDownCopy.setValue(habitsDropDown.getValue());


        // Create a VBox so we can store the dropdown with padding around
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.getChildren().add(habitsDropDownCopy); // Add dropdown to the vbox
        dialog.getDialogPane().setContent(container); // Add container to dialog


        // Use ResultConverter to get result when Delete/Cancel button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == delete) {

                // Get the name of the habit from dropdown
                String selectedValue = habitsDropDownCopy.getValue();
                // Initialize goalname to map to habit
                String goalName = null;

                if (selectedValue == null) {
                    updateStatus("Select a habit", "red");// Tell user to select a goal before clicking Delete button
                } else {
                    // Find the habit object
                    for (Map.Entry<Goal, HashSet<Habit>> e : data.tracker.entrySet()) {
                        HashSet<Habit> set = e.getValue(); // Get the habits hashset

                        for (Habit habit : set) {
                            if (habit.getHabit().equals(selectedValue)) { // If the hashset contains our selected Habit
                                goalName = e.getKey().getGoal(); // Get goal name
                                break;
                            }
                        }
                    }

                    if (data.deleteHabitsFromGoal(goalName, selectedValue)) {
                        updateStatus("Habit deleted successfully", "green");
                        // Update habitsDropDown
                        populateHabitsDropDown();
                        // Update habitsGeneralView
                        setHabitsGeneralView();
                    } else {
                        updateStatus("Error deleting Habit", "Red");
                    }
                }

                return goalsDropDown.getValue();
            }
            return null; // Return null if cancel option is clicked
        });

        dialog.show();
    }


    // Menu Help/ About

    /**
     * Display information about the application.
     * This method is invoked when the user selects the 'About' option from the menu.
     *
     * @author: Phone Myat Paing
     */

    @FXML
    private void aboutAction() {
        //Create an alert dialog to display information about the application
        Alert aboutAlert = new Alert(Alert.AlertType.CONFIRMATION);
        aboutAlert.setTitle("About");
        aboutAlert.setHeaderText("Habits And Goals Tracker v1.0");
        aboutAlert.setContentText("Author: Tania, Sanbeer, Phone\nVersion: 1.0\nThis application is designed to track the user's goals, habits and their related important information.");

        //Show the alert dialog and wait for user response
        aboutAlert.showAndWait();

        //Update the status label to indicate that the about action was checked
        updateStatus("Successfully Checking the About Action on from the menu bar.", "green");
    }


    /**
     * Invoked when the user selects the 'Reset' option from the menu.
     * This method displays a confirmation dialog to the user. If the user
     * confirms the action, it attempts to reset all application data and the CSV file.
     * The method will update the GUI with the result of the reset operation.
     * If the user cancels the operation, no changes are made, and a status update is displayed indicating that the reset has been canceled.
     *
     * @author: Phone Myat Paing
     */
    @FXML
    private void resetDataAction() {
        // Show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reset Confirmation");
        alert.setHeaderText("Reset All Data");
        alert.setContentText("Are you sure you want to reset all data? This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Perform the reset
            try {
                resetAllData();
                updateStatus("All data have been reset successfully.", "green");
            } catch (Exception e) {
                updateStatus("Failed to reset data: " + e.getMessage(), "red");
            }
        } else {
            // User chose cancel
            updateStatus("Resetting process cancelled.", "red");
        }

        // Update general tracker
        setTrackerGeneralView();
        // Update habitsGeneralView
        setHabitsGeneralView();
    }


    /**
     * Quit the application.
     * This method is invoked when the user selects the 'Quit' option from the menu.
     *
     * @author: Phone Myat Paing
     */
    @FXML
    public void closeAction() {
        // Close the application
        Platform.exit();
    }


    // GUI FUNCTIONALITY

    /**
     * This method increments the habit count for a user-selected habit.
     *
     * @author Phone Myat Paing
     */
    @FXML
    private void increaseHabitCount() {
        // Check if habitsDropDown itself is not null
        if (habitsDropDown == null) {
            updateStatus("Habit dropdown is not initialized.", "red");
            return;
        }

        String selectedHabitName = habitsDropDown.getValue();
        if (selectedHabitName == null || selectedHabitName.isEmpty()) {
            updateStatus("Please select a habit.", "red");
            return;
        }

        Habit habit = findHabitByName(selectedHabitName);
        if (habit == null) {
            updateStatus("Selected habit not found.", "red");
            return;
        }

        habit.incrementCurrentCount();
        updateStatus("Habit '" + selectedHabitName + "' count increased to " + habit.getCurrentCount() + ".", "green");
        // Update habitsGeneralView
        setHabitsGeneralView();
    }

    public Habit findHabitByName(String habitName) {
        if (habitName == null) return null;

        for (HashSet<Habit> habits : tracker.values()) {
            for (Habit habit : habits) {
                if (habit.getHabit().equalsIgnoreCase(habitName)) {
                    return habit;
                }
            }
        }
        return null;
    }


    /**
     * Handles the action triggered by clicking the "View the Weekly Completion" button.
     * It asks the user if they want to see their weekly completion percentage for each habit.
     * If confirmed, it displays another alert showing the completion rates for all habits.
     *
     * @author Phone Myat Paing
     */
    @FXML
    private void viewWeeklyCompletionAction() {
        // Create a confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Action");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Do you want to see your weekly completion percentage for each habit?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Calculate and display completion rates
            Map<String, Double> completionRates = calculateWeeklyCompletionRates();
            StringBuilder contentText = new StringBuilder();
            completionRates.forEach((habit, rate) ->
                    contentText.append(String.format("Habit: %s, Completion Rate: %.0f%%\n", habit, rate)));

            // Show the completion rates in another alert dialog
            Alert ratesAlert = new Alert(Alert.AlertType.INFORMATION);
            ratesAlert.setTitle("Weekly Completion Rates");
            ratesAlert.setHeaderText("Here are your weekly completion rates:");
            ratesAlert.setContentText(contentText.toString());
            updateStatus("Successfully checking the weekly completion rate","green");
            ratesAlert.showAndWait();
        } else {
            // Handle cancellation
            updateStatus("Viewing weekly completion rates cancelled.", "red");
        }
    }

    /**
     * This statistical method allows a user to see which habits are most productive.
     * The Top 3 Habits are calculated using the completion rate.
     * The completion rate is calculated using habit counts and the ideal count.
     *
     * @author Phone Myat Paing
     */
    @FXML
    private void viewTop3HabitsAction() {
        try {
            List<Habit> topHabits = getTop3HabitsByCompletionRate();

            if (topHabits.isEmpty()) {
                updateStatus("No habit in the database.", "red");
                return; // Exit if no habits are found
            }

            StringBuilder contentText = new StringBuilder();
            // Determine the message based on the number of habits retrieved
            if (topHabits.size() >= 3) {
                contentText.append("Top 3 habits for this week are: \n");
            } else {
                contentText.append("Mostly done habits are: \n");
            }

            // Construct a message listing the habits and their completion rates
            for (int i = 0; i < topHabits.size(); i++) {
                Habit habit = topHabits.get(i);
                contentText.append(String.format("%s (%.2f%% complete)\n", habit.getHabit(), habit.getWeeklyCompletionRate()));
            }

            // Show the completion rates in an alert dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Top 3 Habits");
            alert.setHeaderText("Weekly Completion Rates");
            alert.setContentText(contentText.toString());
            alert.showAndWait();
            updateStatus("Successfully checking on Top Habits", "green");
        } catch (Error e) {
            updateStatus("Something went wrong", "Red");
        }
    }

    /**
     * getCategoryChoice: the sets the category for the goal choice to the chosen category using a method in data
     * @author: Sanbeer
     */
    @FXML
    protected void getCategoryChoice() {
        String categoryChoice = myCategoryBox.getValue();
        String goalChoice = goalsDropDown.getValue();
        data.setCategory(categoryChoice, goalChoice);
        String t = "Voila! You have set goal: " + goalChoice + " into Category: " + categoryChoice;
        updateStatus(t, "green");

        // Update general tracker
        setTrackerGeneralView();
        // Update habitsGeneralView
        setHabitsGeneralView();
    }

    /**
     * getMatrixChoice: the sets the matrix quadrant for the goal choice to the chosen quadrant using a method in data
     * @author: Sanbeer
     */
    @FXML
    protected void getMatrixChoice() {
        String matrixChoice = myMatrixBox.getValue();
        String goalChoice2 = goalsDropDown.getValue();

        data.setMatrix(matrixChoice, goalChoice2);
        String s = "Hi, You have set goal: " + goalChoice2 + " into Quadrant: " + matrixChoice;
        updateStatus(s, "green");
    }

    /**
     * matrixShower: this uses an alert to show the current matrix;
     * @author: Sanbeer
     */

    @FXML
    protected void showMatrix() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Goals in Eisenhower Matrix");
        if (data.matrix2.containsKey("Urgent and Important")) {
            alert.setHeaderText("Here is your current Eisenhower Matrix");

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);

            int row = 0;
            for (Map.Entry<String, ArrayList<String>> entry : data.matrix2.entrySet()) {
                Label keyLabel = new Label(entry.getKey() + ":");
                String combinedValues = String.join(", ", entry.getValue());
                Label valueLabel = new Label(combinedValues);
                gridPane.add(keyLabel, 0, row);
                gridPane.add(valueLabel, 1, row);
                row++;
            }
            alert.getDialogPane().setContent(gridPane);

            alert.showAndWait();
            updateStatus("Here is your Eisenhower Matrix", "green");

        } else {
            alert.setHeaderText("Looks like you have can't created the matrix yet!");
            alert.showAndWait();
            updateStatus("Please create the Eisenhower Matrix", "red");
        }

    }

    /**
     * showHabitBar: this  triggers the program to create a bar chart to display current habit counts
     * @author: Sanbeer
     */

    @FXML
    protected void showHabitBar() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("CurrentHabits");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Current Counts");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Current Habit Counts");

        XYChart.Series<String, Number > series = new XYChart.Series<>();
        series.setName("Current Standing");

        for (Map.Entry<Goal, HashSet<Habit>> e : data.tracker.entrySet()) {
            HashSet<Habit> set = e.getValue();

            for(Habit habit: set){
                series.getData().add(new XYChart.Data<>(habit.getHabit(), habit.getCurrentCount()));
            }
        }

        barChart.getData().add(series);
        Dialog<BarChart> dialog = new Dialog();
        dialog.getDialogPane().setContent(barChart);

        // Set the button types
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

        // Here, cast the lookup result to Button before setting the action
        Button closeButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.setOnAction(e -> dialog.close());

        dialog.showAndWait();

    }

    // TRACKER VIEWS

    /**
     * This method updates the General Overview tracker with the Goal objects and their properties.
     * It is called anytime the Goal object is modified.
     *
     * @author: Tania Rizwan
     */
    @FXML
    protected void setTrackerGeneralView() {
        // Clear grid
        generalOverviewPane.getChildren().clear();
        generalOverviewPane.getColumnConstraints().clear();
        generalOverviewPane.getRowConstraints().clear();
        generalOverviewPane.setGridLinesVisible(false);

        // Define rows and cols
        Integer rows = goalsDropDown.getItems().size(); // Number of goals defines rows
        Integer cols = 3; // Fixed

        // Set constraints
        for (int i = 0; i < rows; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setValignment(VPos.CENTER);
            rowConstraints.setVgrow(Priority.ALWAYS);
            generalOverviewPane.getRowConstraints().add(rowConstraints);
        }

        for (int i = 0; i < cols; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHalignment(HPos.CENTER);
            columnConstraints.setHgrow(Priority.NEVER);
            columnConstraints.setPrefWidth(200);
            columnConstraints.setMaxWidth(200);
            generalOverviewPane.getColumnConstraints().add(columnConstraints);
        }

        // Set-up fields for grid

        HashSet<Goal> goalHashSet = data.getGoals();
        Object[] goalstoArray = goalHashSet.toArray(); // Convert to Array so we can access each element


        // Set-up the grid
        for (int i = 0; i < rows; i++) {

            Goal goal = (Goal) goalstoArray[i];
            Label goalName = new Label(goal.getGoal());
            Label category = new Label(goal.getCategory());
            Label idealCount = new Label(String.valueOf(goal.getIdealCount()));

            generalOverviewPane.add(goalName, 0, i);
            generalOverviewPane.add(category, 1, i);
            generalOverviewPane.add(idealCount, 2, i);

        }
    }

    /**
     * This method updates the Habits Overview tracker with the Habit objects and their properties.
     * It is called anytime the Habit object is modified.
     *
     * @author: Tania Rizwan
     */
    @FXML
    protected void setHabitsGeneralView() {

        // Clear grid
        habitsOverviewPane.getChildren().clear();
        habitsOverviewPane.getColumnConstraints().clear();
        habitsOverviewPane.getRowConstraints().clear();
        habitsOverviewPane.setGridLinesVisible(false);

        // Define rows and cols
        Integer rows = habitsDropDown.getItems().size(); // Number of goals defines rows
        Integer cols = 4; // Fixed

        // Set constraints
        for (int i = 0; i < rows; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setValignment(VPos.CENTER);
            rowConstraints.setVgrow(Priority.ALWAYS);
            habitsOverviewPane.getRowConstraints().add(rowConstraints);
        }

        for (int i = 0; i < cols; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHalignment(HPos.CENTER);
            columnConstraints.setHgrow(Priority.NEVER);
            columnConstraints.setPrefWidth(200);
            columnConstraints.setMaxWidth(200);
            habitsOverviewPane.getColumnConstraints().add(columnConstraints);
        }

        // Set-up fields for grid
        ArrayList<Habit> habits = new ArrayList<>();
        ArrayList<Goal> goals = new ArrayList<>();
        for (Map.Entry<Goal, HashSet<Habit>> e : data.getTracker().entrySet()) {
            // Access habits hashset for specific goal
            HashSet<Habit> habitsSet = e.getValue();
            habits.addAll(habitsSet); // Add all habits to arraylist
            goals.add(e.getKey());
        }

        // Set-up the grid
        for (int i = 0; i < rows; i++) {

            Habit habit = (Habit) habits.get(i);
            Label habitName = new Label(habit.getHabit());
            Label goalName = new Label(habit.getGoal());
            Label category = new Label(habit.getCategory());
            Label currentCount = new Label(String.valueOf(habit.getCurrentCount()));

            habitsOverviewPane.add(goalName, 0, i);
            habitsOverviewPane.add(habitName, 1, i);
            habitsOverviewPane.add(category, 2, i);
            habitsOverviewPane.add(currentCount, 3, i);
        }


    }




}

