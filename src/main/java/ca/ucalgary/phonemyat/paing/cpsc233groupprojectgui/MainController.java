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
    protected void menuAddGoalsAction() {

        Dialog<String> dialog = new Dialog<>();
        dialog.setHeaderText("Add a Goal");
        dialog.show();




    }



}