import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController  extends HomeController{

    Cittadino cittadino;

    DAO Dao = new DAO();

    @FXML TextField nomeField;
    @FXML TextField cognomeField;
    @FXML TextField cd_FiscaleField;
    @FXML TextField nTesseraField;
    @FXML DatePicker dataNascitaDatePicker;
    @FXML TextField luogoNascitaField;
    @FXML TextField mailField;
    @FXML PasswordField passwordField;


    @Override
    public void switchHomePage(ActionEvent event) throws IOException {
        super.switchHomePage(event);
    }


    public void registerCittadino(ActionEvent event) throws IOException {
        boolean checkDao = false;
        try {
            cittadino = new Cittadino(cd_FiscaleField.getText().toUpperCase(), nTesseraField.getText().toUpperCase(), nomeField.getText().toUpperCase(), cognomeField.getText().toUpperCase(), luogoNascitaField.getText().toUpperCase(), new Date(dataNascitaDatePicker.getValue().getDayOfMonth(), dataNascitaDatePicker.getValue().getMonthValue(), dataNascitaDatePicker.getValue().getYear()));
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Informazioni mancanti");
            alert.setHeaderText("Errore, dati mancanti");
            alert.setContentText("Il modulo di registrazione non è stato completato. Si prega di compilare tutti i campi");
            checkDao = true;
            alert.showAndWait();
        }
        if(!checkDao) {
            boolean check = Dao.checkPerson(cittadino);
            stage2.setUserData(cittadino);
            if (check) {
                root = FXMLLoader.load(getClass().getResource("PopupRegister.fxml"));
                Scene scene1 = new Scene(root);
                Stage stageMail = new Stage();
                stageMail.setTitle("Mail and password");
                stageMail.initModality(Modality.APPLICATION_MODAL);
                stageMail.setScene(scene1);
                stageMail.show();
                stageMail.setResizable(false);

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Informazioni non corrette");
                alert.setHeaderText("Errore nei dati inseriti");
                alert.setContentText("Non sono stati rilevati i dati inseriti nel nostro database dell'anagrafe.\nSi prega di controllare che i dati siano corretti e nel caso di errore continuo, si prega di inviare una mail a: \n questura.italia@questura.com");

                alert.showAndWait();
            }
        }
    }

    public void mailPasswordFill(ActionEvent event) throws IOException{
        try {
            Dao.addRegistrazione(mailField.getText(), passwordField.getText(), ((Cittadino) stage2.getUserData()).get_Tessera_Sanitaria(1));
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Informazioni mancanti");
            alert.setHeaderText("Errore, dati mancanti");
            alert.setContentText("Il modulo di registrazione non è stato completato. Si prega di compilare tutti i campi");

            alert.showAndWait();
        }

        stage1 = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage1.close();
        root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
        scene = new Scene(root);
        stage2.setScene(scene);
        stage2.show();
    }



}
