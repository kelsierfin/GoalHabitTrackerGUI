package ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui;

import ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.objects.Goal;
import ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.objects.Habit;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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
        dialog.getDialogPane().getButtonTypes().addAll(addGoal, ButtonType.CANCEL);

        // Create TextFields
        TextField goalNameTextField = new TextField();
        TextField idealCountTextfield = new TextField();
//        goalNameTextField.setPromptText("Enter goal name");
//        idealCountTextfield.setPromptText("Enter the ideal count");

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

    protected void deleteAGoal() {
     // Create a dialog box with goal dropdown
     // User selects goal, and clicks delete
     // Call deleteAGoal method
    }

    /**
     * Goals Drop Down: lists all goals to be used in various methods
     */
    protected void setGoalsDropDown() {
        // Populate dropdown with each goal in HashSet goals (from Data.java)

        HashSet<Goal> goals = data.getGoals();
        for (Goal goal : goals) {
            goalsDropDown.getItems().add(String.valueOf(goal.getGoal()));
        }



    }






}