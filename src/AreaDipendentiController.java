import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class AreaDipendentiController extends HomeController implements Initializable{

    @FXML
    ComboBox<String> sedeBox;
    @FXML
    ComboBox<String> tipoBox;
    @FXML
    DatePicker giornoPrenota;
    @FXML
    ComboBox<String> orarioPrenota;
    @FXML
    TreeView<String> treeViewDipendenti;

    TreeItem<String> rootNode = new TreeItem<>("Prenotazioni esistenti:");

    DAO Dao = new DAO();

    @Override
    public void switchHomePage(ActionEvent event) throws IOException {
        super.switchHomePage(event);
    }

    public void inserimentoPrenotazione(ActionEvent event) throws IOException {
        boolean error = false;
        try {
            Prenotazione temp = new Prenotazione(tipoBox.getValue(),new Date(giornoPrenota.getValue().getDayOfMonth(), giornoPrenota.getValue().getMonthValue(), giornoPrenota.getValue().getYear()),orarioPrenota.getValue(),sedeBox.getValue());
            if(!Dao.checkPrenotazionePresente(temp)) {
                Dao.addPrenotazione(sedeBox.getValue(), new Date(giornoPrenota.getValue().getDayOfMonth(), giornoPrenota.getValue().getMonthValue(), giornoPrenota.getValue().getYear()).toString(), orarioPrenota.getValue(), tipoBox.getValue());
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Prenotazione già presente");
                alert.setHeaderText("Errore, prenotazione già inserita");
                alert.setContentText("E' già presente una prenotazione nello slot selezionato");

                alert.showAndWait();

                error = true;
            }
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Informazioni mancanti");
            alert.setHeaderText("Errore, dati mancanti");
            alert.setContentText("Il modulo per inserire una prenotazione non è stato completato. Si prega di compilare tutti i campi");

            alert.showAndWait();

            error = true;

        }
        if(!error) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Prenotazione aggiunta");
            alert.setHeaderText("Prenotazione aggiunta");
            alert.setContentText("La prenotazione è stata aggiunta correttamente");

            alert.showAndWait();
            /**Ricarica pagina di modo da reinizializzarla*/
            root = FXMLLoader.load(getClass().getResource("AreaDipendenti.fxml"));
            stage1 = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage1.setScene(scene);
            stage1.show();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        firstTab();
        secondTab();
    }

    private void secondTab() {
        rootNode.setExpanded(true);
        Set<Prenotazione> listaPrenotazioni =  Dao.getPrenotazioniNonFiltrate();

        TreeItem<String> sectionCittadini = new TreeItem<>("Prenotazioni con cittadini già iscritti");
        TreeItem<String> sectionNoCittadini = new TreeItem<>("Prenotazioni senza cittadini già iscritti");

        for (Prenotazione item : listaPrenotazioni) {
            if(item.getCittadino() == null) {
                TreeItem<String> prenotazione = new TreeItem<String>("Prenotazione n: " + item.getId());
                TreeItem<String> sede = new TreeItem<String>("Sede: " + item.getSede());
                TreeItem<String> giorno = new TreeItem<String>("Giorno: " + item.getDate());
                TreeItem<String> orario = new TreeItem<String>("Data: " + item.getOrario());
                TreeItem<String> tipo = new TreeItem<String>("Tipo di prenotazione: " + item.getTipoPrenotazione());
                prenotazione.getChildren().addAll(sede, giorno, orario, tipo);
                sectionNoCittadini.getChildren().add(prenotazione);
            }
            else{
                TreeItem<String> prenotazione = new TreeItem<String>("Prenotazione n: " + item.getId());
                TreeItem<String> sede = new TreeItem<String>("Sede: " + item.getSede());
                TreeItem<String> giorno = new TreeItem<String>("Giorno: " + item.getDate());
                TreeItem<String> orario = new TreeItem<String>("Data: " + item.getOrario());
                TreeItem<String> tipo = new TreeItem<String>("Tipo di prenotazione: " + item.getTipoPrenotazione());
                TreeItem<String> cittadino = new TreeItem<>("Codice Fiscale cittadino: " + item.getCittadino().get_Tessera_Sanitaria(1));
                prenotazione.getChildren().addAll(sede, giorno, orario, tipo,cittadino);
                sectionCittadini.getChildren().add(prenotazione);
            }

        }
        rootNode.getChildren().addAll(sectionNoCittadini,sectionCittadini);
        treeViewDipendenti.setRoot(rootNode);
    }

    private void firstTab(){
        try {
            /**Lista orari*/
            ArrayList<String> listSedi = new ArrayList<>();
            ArrayList<String> tipoPrenotazioni = new ArrayList<>();
            ArrayList<String> orarioPrenotazioni = new ArrayList<>();
            String[] halfHour = {"00","30"};
            for(int i = 9; i < 17; i++){
                for(int j = 0; j < 2; j++){
                    String time = i + ":" + halfHour[j];
                    orarioPrenotazioni.add(time);
                }
            }

            File inputFile = new File("Prenotazioni.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setXIncludeAware(true);
            dbFactory.setNamespaceAware(true);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList sediXML = doc.getElementsByTagName("sedeDisp");

            /**Memorizzazione sedi dentro l'arrayList creato sopra*/
            for (int temp = 0; temp < sediXML.getLength(); temp++) {
                Node nSediNode = sediXML.item(temp);

                if (nSediNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eSediElement = (Element) nSediNode;
                    listSedi.add(eSediElement.getTextContent());
                }
            }
            /**Utilizzo di listSedi per inserirla dentro il menu a tendina dell'interfaccia grafica.*/
            ObservableList<String> sediList = FXCollections.observableList(listSedi);
            sedeBox.setItems(sediList);

            NodeList tipoXML = doc.getElementsByTagName("tipoDisp");

            /**Memorizzazione tipo di richiesta dentro l'arrayList creato sopra*/
            for(int temp = 0; temp < tipoXML.getLength(); temp++){
                Node nTipoNode = tipoXML.item(temp);

                if(nTipoNode.getNodeType() == Node.ELEMENT_NODE){
                    Element eTipoElement = (Element) nTipoNode;
                    tipoPrenotazioni.add(eTipoElement.getTextContent());
                }
            }
            /**Utilizzo di tipoPrenotazioni per inserirla dentro il menu a tendina dell'interfaccia grafica.*/
            ObservableList<String> tipoList = FXCollections.observableList(tipoPrenotazioni);
            tipoBox.setItems(tipoList);

            ObservableList<String> orarioList = FXCollections.observableList(orarioPrenotazioni);
            orarioPrenota.setItems(orarioList);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
