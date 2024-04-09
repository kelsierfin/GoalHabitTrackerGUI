module ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui {
    requires javafx.controls;
    requires javafx.fxml;


    opens ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui to javafx.fxml;
    exports ca.ucalgary.phonemyat.paing.cpsc233groupprojectgui;
}