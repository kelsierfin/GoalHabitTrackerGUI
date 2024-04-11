package ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui;

import ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.objects.Goal;
import ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.objects.Habit;
import ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.util.FileLoader;
import ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.util.FileSaver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.lang.reflect.Array;
import java.util.*;

public class MainController {

    // Data obj
    private static Data data = new Data();

    // Status bar
    @FXML
    private Label statusLabel;

    // Global Alert obj
    Alert a = new Alert(Alert.AlertType.NONE);

    // Goals
    @FXML
    private ChoiceBox<String> goalsDropDown;

    /**
     * Initialize method to pre-load (set-up GUI)
     */
    @FXML
    public void initialize() {
        populateHabitsDropDown();
    }


    @FXML
    private ChoiceBox<String> habitsDropDown;

    private void populateHabitsDropDown() {
        Data.getTracker().values().forEach(habitsSet -> habitsSet.forEach(habit -> {
            habitsDropDown.getItems().add(habit.getHabit());
        }));
    }

    /**
     * Update the status label with specified message and color.
     *
     * @author: Phone Myat Paing
     * @param message The message to display
     * @param color   The color of the text
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
     * Save the current tracker to a file.
     * This method is invoked when the user selects the 'Save' option from the menu.
     *
     * @author: Phone Myat Paing
     */
    @FXML
    private void saveAction() {
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
    private void loadAction() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter for CSV files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        // Get the current window for the dialog
        Window stage = statusLabel.getScene().getWindow();

        // Show open file dialog
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            // Load the data from the file
            Data loadedData = FileLoader.load(file);

            if (loadedData != null) {
                data = loadedData; // Update the data model
                updateStatus("Data loaded successfully from " + file.getName(), "green");
            } else {
                updateStatus("Failed to load data from file.", "red");
            }
        }
    }

    // Member variables that hold the data
    protected static ArrayList<Integer> choicesArrayList2;
    protected static HashSet<Goal> goals;
    protected static HashSet<Habit> habits;
    protected static HashMap<String, ArrayList<String>> matrix;
    protected static HashMap<String, ArrayList<String>> fields;
    protected static HashMap<Goal, HashSet<Habit>> tracker;
    protected static HashMap<String, Integer> habitAndICounts;
    protected static HashMap<String, Integer> habitAndECounts;


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
            updateStatus("Resetting process cancelled.", "blue");
        }
    }

    /**
     *  * This method is part of the reset process that reinitializes the application's data to a default state.
     *
     * @author: Phone Myat Paing
     */
    private void resetAllData() {
        // Clear all data collections
        if (choicesArrayList2 != null) {
            choicesArrayList2.clear();}
        if (goals != null) {
            goals.clear();
        }
        if (habits != null) {
            habits.clear();
        }
        if (matrix != null) {
            matrix.clear();
        }
        if (fields != null) {
            fields.clear();
        }
        if (tracker != null) {
            tracker.clear();
        }
        if (habitAndICounts != null) {
            habitAndICounts.clear();
        }
        if (habitAndECounts != null) {
            habitAndECounts.clear();
        }


        // thinking about resetting the CSV file here
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




    // Menu Items: Edit

    /**
     * Create goal objects using data.java
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
        idealCountTextfield.setPromptText("Enter a digit (within 1-7)");

        // Add the fields to a grid
        GridPane addGoalsGrid = new GridPane();
        addGoalsGrid.add(new Label("Goal Name: "), 0, 0);
        addGoalsGrid.add(goalNameTextField, 0, 1);
        addGoalsGrid.add(new Label("Ideal Count: "), 1, 0);
        addGoalsGrid.add(idealCountTextfield, 1, 1);

        // Add grid pane to dialog pane
        dialog.getDialogPane().setContent(addGoalsGrid);


        // Convert result to string when "Add" button is clicked. Result Converter processes user input whe the add button is clicked.
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
                           statusLabel.setText("Goal added successfully!");
                           statusLabel.setTextFill(Color.GREEN);
                           setGoalsDropDown();
                       }
                   } else {
                       throw new NullPointerException();
                   }
               } catch (NullPointerException e){
                   statusLabel.setText("Enter valid data!");
                   statusLabel.setTextFill(Color.RED);
               } catch (NumberFormatException e){
                   statusLabel.setText("Enter valid data!");
                   statusLabel.setTextFill(Color.RED);
               }
            }


            return "";
        });

        dialog.show();

    }

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

        // Link goalsDropDown
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);

        dialog.getDialogPane().setContent(goalsDropDown);



        dialog.show();




    }

    /**
     * Goals Drop Down: lists all goals in the drop down to be used in various methods
     */
    protected void setGoalsDropDown() {
        // Populate dropdown with each goal in HashSet goals (from Data.java)
        HashSet<Goal> goals = data.getGoals();
        for (Goal goal : goals) {
            goalsDropDown.getItems().add(String.valueOf(goal.getGoal()));
        }
    }

    // Menu Help/ About
    /**
     * Display information about the application.
     * This method is invoked when the user selects the 'About' option from the menu.
     *
     * @author: Phone Myat Paing
     */

    @FXML
    private void aboutAction(){
        //Create an alert dialog to display information about the application
        Alert aboutAlert = new Alert(Alert.AlertType.CONFIRMATION);
        aboutAlert.setTitle("About");
        aboutAlert.setHeaderText("Habits And Goals Tracker v1.0");
        aboutAlert.setContentText("Author: Tania, Sanbeer, Phone\nVersion: 1.0\nThis application is designed to track the user's goals, habits and their related important information.");

        //Show the alert dialog and wait for user response
        aboutAlert.showAndWait();

        //Update the status label to indicate that the about action was checked
        updateStatus("Successfully Checking the About Action on from the menu bar.","green");
    }


    // Count Increasing Process
    @FXML
    private void increaseHabitCount() {
        // Check if habitsDropDown itself is not null
//        if (habitsDropDown == null) {
//            updateStatus("Habit dropdown is not initialized.", "red");
//            return;
//        }

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


//        @FXML
//        private void increaseHabitCount() {
//            String selectedHabitName = habitChoiceBox.getValue();
//            if (selectedHabitName == null || selectedHabitName.isEmpty()) {
//                updateStatus("Please select a habit.", "red");
//                return;
//            }
//
//            // Assuming 'updateHabitCompletion' updates the count and returns true if successful
//            boolean success = data.updateHabitCompletion(selectedHabitName);
//            if (success) {
//                updateStatus("Habit '" + selectedHabitName + "' count increased successfully.", "green");
//            } else {
//                updateStatus("Failed to increase count for habit '" + selectedHabitName + "'.", "red");
//            }
//        }

}