package ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class launches the Habit Tracker GUI.
 *
 * @authors Phone, Sanbeer, Tania
 */

public class MainGUI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 900);
        stage.setTitle("Habit Tracker!");
        // Create controller object for initialization
        MainController controller = new MainController();
        controller.initialize();


        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}