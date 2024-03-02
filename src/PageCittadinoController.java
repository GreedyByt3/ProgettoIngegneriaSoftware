import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.*;

public class PageCittadinoController extends HomeController implements Initializable {
    @FXML
    private ComboBox<String> filtroTipo;
    ZonedDateTime dateFocus;
    ZonedDateTime today;

    @FXML
    private Text year;

    @FXML
    private Text month;

    @FXML
    private FlowPane calendar;

    @FXML
    private TreeView<String> treeView;

    TreeItem<String> rootNode = new TreeItem<>("Prenotazioni Confermate");


    @FXML
    ComboBox<String> filtroSede;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
        treeView();
        setFiltroSede();
        setFiltroTipo();
    }

    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    private void drawCalendar(){
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        //List of activities for a given month
        //Map<Integer, Integer> calendarActivityMap = getCalendarActivitiesMonth(dateFocus);

        int monthMaxDate = dateFocus.getMonth().maxLength();
        //Check for leap year
        if(dateFocus.getYear() % 4 != 0 && monthMaxDate == 29){
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1,0,0,0,0,dateFocus.getZone()).getDayOfWeek().getValue();
         /**
          * Creazione dello stackpane a stile calendario.
          * */
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth =(calendarWidth/7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight/6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j+1)+(7*i);
                if(calculatedDate > dateOffset){
                    int currentDate = calculatedDate - dateOffset;
                    if(currentDate <= monthMaxDate){
                        Set<Prenotazione> prenotazioniOggi = Dao.getPrenotazioniOggi(currentDate+"/"+dateFocus.getMonthValue()+"/"+dateFocus.getYear(),(Filtro) choice2.getUserData());
                        Set<Prenotazione> prenotazioniFiltrate = new TreeSet<>();
                        for(Prenotazione item : prenotazioniOggi){
                            if(item.getCittadino() == null){
                                prenotazioniFiltrate.add(item);
                            }
                        }
                        Text dispPrenotazioni = new Text("Disp: " + prenotazioniFiltrate.size());
                        if(new Date(currentDate,dateFocus.getMonthValue(),dateFocus.getYear()).compareTo(new Date(today.getDayOfMonth(),today.getMonthValue(),today.getYear())) >= 0){
                            if (prenotazioniFiltrate.size() == 0) {
                                dispPrenotazioni.setFill(Color.RED);
                            } else {
                                dispPrenotazioni.setFill(Color.BLUE);
                            }
                        }
                        else{
                            dispPrenotazioni.setFill(Color.WHITE);
                        }
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = - (rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        dispPrenotazioni.setTranslateY(textTranslationY * (-1));
                        stackPane.getChildren().add(date);
                        stackPane.getChildren().add(dispPrenotazioni);
                        rectangle.setOnMouseClicked(mouseEvent -> {
                            try {
                                if(new Date(currentDate,dateFocus.getMonthValue(),dateFocus.getYear()).compareTo(new Date(today.getDayOfMonth(),today.getMonthValue(),today.getYear())) >= 0) {
                                    Cittadino tmp = (Cittadino) stage2.getUserData();
                                    stage2.setUserData(new Prenotazione(tmp, currentDate + "/" + dateFocus.getMonthValue() + "/" + dateFocus.getYear(), filtroSede.getValue()));
                                    popupPrenotazione();
                                }

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });


                    }
                    if(today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate){
                        rectangle.setStroke(Color.BLUE);
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }

    private void popupPrenotazione() throws IOException {
        root = FXMLLoader.load(getClass().getResource("PopupPrenotazioni.fxml"));
        Scene scene1 = new Scene(root);
        Stage stageLogin = new Stage();
        stageLogin.setTitle("Appointments Area");
        stageLogin.initModality(Modality.APPLICATION_MODAL);
        stageLogin.setScene(scene1);
        stageLogin.show();
        stageLogin.setResizable(false);

    }

    private void treeView(){
        rootNode.setExpanded(true);
        Set<Prenotazione> listaPrenotazioni =  Dao.getPrenotazioniPerCittadino((Cittadino) stage2.getUserData());
        for (Prenotazione item : listaPrenotazioni) {
            TreeItem<String> prenotazione = new TreeItem<String>("Prenotazione n: " + item.getId());
            prenotazione.setExpanded(true);
            TreeItem<String> sede = new TreeItem<>("Sede: " + item.getSede());
            TreeItem<String> giorno = new TreeItem<>("Giorno: " + item.getDate());
            TreeItem<String> orario = new TreeItem<>("Data: " + item.getOrario());
            TreeItem<String> tipo = new TreeItem<>("Tipo di prenotazione: " + item.getTipoPrenotazione());
            prenotazione.getChildren().addAll(sede, giorno, orario, tipo);
            rootNode.getChildren().add(prenotazione);
        }
        treeView.setRoot(rootNode);
    }

    /**Metodo per inserire le sedi dentro il menu a tendina della prenotazioni da parte dei cittadini*/
    public void setFiltroSede(){
        filtroSede.setValue(((Filtro) choice2.getUserData()).getSede());
        List<String> listaSedi = new ArrayList<>();
        listaSedi.add(null);
        try {
            File inputFile = new File("Prenotazioni.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList sediXML = doc.getElementsByTagName("sedeDisp");

            /**Memorizzazione sedi dentro l'arrayList creato sopra*/
            for (int temp = 0; temp < sediXML.getLength(); temp++) {
                Node nSediNode = sediXML.item(temp);

                if (nSediNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eSediElement = (Element) nSediNode;
                    listaSedi.add(eSediElement.getTextContent());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        /**Utilizzo di listaSedi per inserirla dentro il menu a tendina dell'interfaccia grafica.*/
        ObservableList<String> sediList = FXCollections.observableList(listaSedi);
        filtroSede.setItems(sediList);
    }



    public void filtroSelected(ActionEvent event) throws IOException{
        String choice = filtroSede.getValue();
        String secondChoice = filtroTipo.getValue();
        /**choice2 utile per salvare i dati di un scena*/
        choice2.setUserData(new Filtro(choice, secondChoice));
        root = FXMLLoader.load(getClass().getResource("PageCittadino.fxml"));
        Scene scene3 = new Scene(root);
        stage1 = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
        stage1.setScene(scene3);
        stage1.show();
    }

    public void logoutAction(ActionEvent actionEvent) throws Exception{
        stage2.setUserData(null);
        choice2.setUserData(null);
        root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
        stage1 = (Stage)((javafx.scene.Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage1.setScene(scene);
        stage1.show();
    }

    public void filtroTipo(ActionEvent actionEvent) throws Exception{
        String choice = filtroTipo.getValue();
        String secondChoice = filtroSede.getValue();
        /**choice2 utile per salvare i dati di un scena*/
        choice2.setUserData(new Filtro(secondChoice, choice));
        root = FXMLLoader.load(getClass().getResource("PageCittadino.fxml"));
        Scene scene3 = new Scene(root);
        stage1 = (Stage)((javafx.scene.Node)actionEvent.getSource()).getScene().getWindow();
        stage1.setScene(scene3);
        stage1.show();
    }

    public void setFiltroTipo(){
        filtroTipo.setValue(((Filtro) choice2.getUserData()).getTipo());
        List<String> listaTipo = new ArrayList<>();
        listaTipo.add(null);
        try {
            File inputFile = new File("Prenotazioni.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList tipoPrenotazioneXML = doc.getElementsByTagName("tipoDisp");

            /**Memorizzazione tipi di prenotazione dentro l'arrayList creato sopra*/
            for (int temp = 0; temp < tipoPrenotazioneXML.getLength(); temp++) {
                Node nSediNode = tipoPrenotazioneXML.item(temp);

                if (nSediNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eSediElement = (Element) nSediNode;
                    listaTipo.add(eSediElement.getTextContent());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        /**Utilizzo di listaSedi per inserirla dentro il menu a tendina dell'interfaccia grafica.*/
        ObservableList<String> tipoList = FXCollections.observableList(listaTipo);
        filtroTipo.setItems(tipoList);
    }
}
