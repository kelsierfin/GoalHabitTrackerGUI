package ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
//test

public class MainController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}