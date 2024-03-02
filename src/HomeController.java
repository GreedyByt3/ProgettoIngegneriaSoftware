import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class HomeController {

    private static final Map<String, String> dipendenti = new HashMap<String, String>();
    @FXML TextField UsernameDipendenti;
    @FXML PasswordField PasswordDipendenti;

    Stage stage1;
    public static Stage stage2;
    public static Stage choice2 ;
    Scene scene;
    Parent root;

    @FXML TextField mailField;
    @FXML PasswordField passwordField;

    DAO Dao = new DAO();

    public static void riempi() {
        dipendenti.put("Otted1979", "thuth9Shoh");
        dipendenti.put("Proke1985", "Iefie0ahsee");
        dipendenti.put("Braill", "moh6Cahoo");
        dipendenti.put("Inower", "Durahs9ia2");
        dipendenti.put("Seencent", "peiF3Aik3k");
        dipendenti.put("admin","admin");
    }



    public void switchHomePage(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
        stage1 = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage1.setScene(scene);
        stage1.show();
    }

    public void switchRegister(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Register.fxml"));
        stage1 = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage1.setScene(scene);
        stage1.show();
    }

    public void loginAreaDipendentiPopup(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Popup.fxml"));
        Scene scene1 = new Scene(root);
        Stage stageLogin = new Stage();
        stageLogin.setTitle("Login Area");
        stageLogin.initModality(Modality.APPLICATION_MODAL);
        stageLogin.setScene(scene1);
        stageLogin.show();
        stageLogin.setResizable(false);
    }

    public void accedi(ActionEvent event) throws IOException, ParserConfigurationException, SAXException {
        if(dipendenti.containsKey(UsernameDipendenti.getText())){
            if(dipendenti.get(UsernameDipendenti.getText()).equals(PasswordDipendenti.getText())){
               stage1 = (Stage)((Node)event.getSource()).getScene().getWindow();
               stage1.close();

               root = FXMLLoader.load(getClass().getResource("AreaDipendenti.fxml"));
               scene = new Scene(root);
               stage2.setScene(scene);
               stage2.show();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Informazioni non corrette");
                alert.setHeaderText("Errore nei dati inseriti");
                alert.setContentText("Username o password errati.\nSi prega di controllare che i dati siano corretti.");

                alert.showAndWait();
            }
        }
    }


    public void loginCittadino(ActionEvent event) throws IOException{
        boolean error = false;
        boolean check = false;
            try{
                check = Dao.checkLogin(mailField.getText(),passwordField.getText());
            }catch(Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Informazioni non corrette");
                alert.setHeaderText("Errore nei dati inseriti");
                alert.setContentText("Username o password errati.\nSi prega di controllare che i dati siano corretti.");

                alert.showAndWait();
                error = true;
            }
            if(!error && check) {
                choice2.setUserData(new Filtro(null, null));
                stage2.setUserData(Dao.getCittadino(mailField.getText(),passwordField.getText()));
                root = FXMLLoader.load(getClass().getResource("PageCittadino.fxml"));
                stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage1.setScene(scene);
                stage1.show();
            }
    }
}
