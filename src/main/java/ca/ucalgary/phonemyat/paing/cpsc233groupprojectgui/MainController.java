package ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MainController {

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

        Dialog<String> dialog = new Dialog<>();
        dialog.setHeaderText("Add a Goal");

        TextField goalName = new TextField();
        TextField idealCount = new TextField();

        dialog.setContentText(String.valueOf(goalName));





    }



}