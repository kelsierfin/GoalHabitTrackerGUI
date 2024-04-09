package ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {

    // Status bar
    @FXML
    private Label statusLabel;

    // Menu: Edit



    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}