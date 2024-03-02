import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

public class PopPrenotazioniController extends HomeController implements Initializable {

    Cittadino cittadino;
    String date;
    String sede;

    TreeItem<String> rootNode;

    @FXML
    TreeView<String> treeView;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        cittadino = ((Prenotazione) stage2.getUserData()).getCittadino();
        date = ((Prenotazione) stage2.getUserData()).getDate();
        sede = ((Prenotazione) stage2.getUserData()).getSede();
        stage2.setUserData(cittadino);
        rootNode = new TreeItem<>("Prenotazioni giorno segnato: " + date);
        treeView();
    }

    private void treeView(){
        rootNode.setExpanded(true);
        Set<Prenotazione> listaPrenotazioni =  Dao.getPrenotazioniOggi(date, new Filtro(sede, null));
        Set<Prenotazione> listaFiltrata = new TreeSet<>();

        for (Prenotazione element: listaPrenotazioni) {
            if(element.getCittadino() == null){
                listaFiltrata.add(element);
            }
        }
        for (Prenotazione item : listaFiltrata) {
            TreeItem<String> prenotazione = new TreeItem<String>("Prenotazione n: " + item.getId());
            prenotazione.setExpanded(true);
            TreeItem<String> sede = new TreeItem<String>("Sede: " + item.getSede());
            TreeItem<String> giorno = new TreeItem<String>("Giorno: " + item.getDate());
            TreeItem<String> orario = new TreeItem<String>("Data: " + item.getOrario());
            TreeItem<String> tipo = new TreeItem<String>("Tipo di prenotazione: " + item.getTipoPrenotazione());
            prenotazione.getChildren().addAll(sede, giorno, orario, tipo);
            rootNode.getChildren().add(prenotazione);
        }
        treeView.setRoot(rootNode);
    }


    public void confirmAppointment(ActionEvent actionEvent) throws IOException {
        boolean error = false;
        TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
        if(selectedItem.getValue().contains("Prenotazione n: ")){
            Set<Prenotazione> listaPrenotazioni =  Dao.getPrenotazioniOggi(date, new Filtro(sede, null));
            for(Prenotazione temp : listaPrenotazioni){
                String split[]  = selectedItem.getValue().split(": ");
                if(temp.getId() == Integer.parseInt(split[1])){
                    try {
                        if(controlloRitiro(temp, cittadino)) {
                            boolean check = Dao.confirmPrenotazioneCittadino(cittadino, temp);
                            if(!check){
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Errore Selezione Appuntamento già selezionato");
                                alert.setHeaderText("Errore Selezione Appuntamento da parte di un altro utente");
                                alert.setContentText("E' stato selezionato un appuntamento che è già stato preso da un altro utente");
                                alert.showAndWait();

                                stage2.setUserData(new Prenotazione(cittadino,date,sede));
                                stage1 = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                                root = FXMLLoader.load(getClass().getResource("PopupPrenotazioni.fxml"));
                                scene = new Scene(root);
                                stage1.setScene(scene);
                                stage1.show();

                                root = FXMLLoader.load(getClass().getResource("PageCittadino.fxml"));
                                scene = new Scene(root);
                                stage2.setScene(scene);

                                error = true;
                            }
                        }else{
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Errore Selezione Ritiro");
                            alert.setHeaderText("Errore nella selezione di tipo: \"Ritiro Passaporto\"");
                            alert.setContentText("L'utente non possiede nessuna voce nelle sue prenotazioni di tipo Rilascio Passaporto," +
                                    "di conseguenza non può selezionare un Ritiro.");
                            alert.showAndWait();
                            error = true;
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore di selezione");
            alert.setHeaderText("Errore nel selezionare la prenotazione");
            alert.setContentText("Si prega di selezionare la voce con la struttura: \"Prenotazione n: \"");
            alert.showAndWait();
            error = true;
        }
        if(!error) {
            stage1 = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage1.close();

            root = FXMLLoader.load(getClass().getResource("PageCittadino.fxml"));
            scene = new Scene(root);
            stage2.setScene(scene);
            stage2.show();
        }
    }

    private boolean controlloRitiro(Prenotazione temp, Cittadino cittadino) {
        if(temp.getTipoPrenotazione().equals("Ritiro passaporto")){
            /**Recupero lista di tutte le prenotazioni del cittadino*/
            Set<Prenotazione> listaPrenotazioniCittadino = Dao.getPrenotazioniPerCittadino(cittadino);

            for (Prenotazione tmp: listaPrenotazioniCittadino) {
                if(!tmp.getTipoPrenotazione().equals("Ritiro passaporto") && tmp.getGiorno().differenceBetween(temp.getGiorno())){
                    return true;
                }else{
                    return false;
                }
            }
            return false;

        }
        return  true;
    }
}
