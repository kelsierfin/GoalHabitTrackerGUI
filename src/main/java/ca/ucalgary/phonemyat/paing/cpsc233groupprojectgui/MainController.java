package ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.lang.reflect.Array;
import java.util.Arrays;

public class MainController {

    // Data obj
    private static Data data = new Data();

    // Status bar
    @FXML
    private Label statusLabel;

    // Global Alert obj
    Alert a = new Alert(Alert.AlertType.NONE);

    // Goals
    private ChoiceBox<String> goalsDropDown;


    // Menu: Add goals
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
                       data.createAGoal(goalName, Integer.parseInt(idealCount), null);
                       statusLabel.setText("Goal added successfully!");
                       statusLabel.setTextFill(Color.GREEN);

                       if (!data.createAGoal(goalName, Integer.parseInt(idealCount), null)) {// If false (goal already exists)
                           statusLabel.setText("This goal already exists");
                           statusLabel.setTextFill(Color.RED);
                       }
                   } else {
                       throw new NullPointerException();
                   }
               } catch (NullPointerException e){
                   statusLabel.setText("Enter valid data");
                   statusLabel.setTextFill(Color.RED);
               }
            }
            return null;
        });
        dialog.show();


    }




}