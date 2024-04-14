package ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * This class launches the Habit Tracker GUI.
 *
 * @authors Phone, Sanbeer, Tania
 * @tutorial T14, T09, T10
 * Emails: tania.rizwan@ucalgary.ca, sanbeer.shafin@ucalgary.ca, phonemyat.paing@ucalgary.ca
 */

public class MainGUI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 900);
        stage.setTitle("Habit Tracker!");
        // Create controller object for initialization
        MainController controller = fxmlLoader.getController();
//        System.out.println(file);
        controller.initialize();
        controller.loadFromCMDLine(file);


        stage.setScene(scene);
        stage.show();

    }

    private static File file = null;
    public static void main(String[] args) {
        // Handle CMD arguments
        if(args.length>2){
            System.err.println("Expected one command line argument for filename to load from.");
        }
        if(args.length ==1){
            String filename = args[0];
            file = new File(args[0]);
            if(!file.exists() || !file.canRead()){
                System.err.println("Cannot load from the file "+ filename);
                System.exit(1);
            }
        }

        launch();
    }
}