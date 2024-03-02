import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MainPrenotazioni extends Application {

    public static void main(String[] args) {
        HomeController.riempi();

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        HomeController.stage2 = primaryStage;
        HomeController.choice2 = new Stage();
        primaryStage.setTitle("Prenotazione Passaporti");
        try {
            Font.loadFont(MainPrenotazioni.class.getResource("Fonts/TisaSansProRegular.ttf").toExternalForm(),300);
            Font.loadFont(MainPrenotazioni.class.getResource("Fonts/TisaSansProBold.ttf").toExternalForm(),300);
            Parent root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
            Scene homePage = new Scene(root);
            primaryStage.setScene(homePage);
            primaryStage.show();
            primaryStage.setResizable(false);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informazioni");
            alert.setHeaderText("Come navigare l'applicazione");
            alert.setContentText("Per tornare al homepage, si prega di premere il logo in alto a sinistra.");
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}


