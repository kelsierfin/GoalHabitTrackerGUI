package ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui;

import ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.objects.Goal;
import ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui.objects.Habit;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
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

    /**
     * getCategoryChoice: the sets the category for the goal choice to the chosen category using a method in data
     */
    @FXML
    protected void getCategoryChoice() {
        String categoryChoice = myCategoryBox.getValue();
        String goalChoice = goalsDropDown.getValue();
        data.setCategory(categoryChoice, goalChoice);

        statusLabel.setText("You have set goal: " + goalChoice + " into Category: " + categoryChoice);
    }
    /**
     * getMatrixChoice: the sets the matrix quadrant for the goal choice to the chosen quadrant using a method in data
     */
    @FXML
    protected void getMatrixChoice() {
        String matrixChoice = myMatrixBox.getValue();
        String goalChoice2 = goalsDropDown.getValue();

        data.setMatrix(matrixChoice, goalChoice2);
        statusLabel.setText("You have set goal: " + goalChoice2 + " into Quadrant: " + matrixChoice);


    }
    /**
     * matrixShower: this uses an alert to show the current matrix;
     */

    @FXML
    protected void showMatrix(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Goals in Eisenhower Matrix");
        alert.setHeaderText("Here is your current Eisenhower Matrix");

        alert.setContentText(String.valueOf(data.matrix2));
        alert.showAndWait();




    }










    }