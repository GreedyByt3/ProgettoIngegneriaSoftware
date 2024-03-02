import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Set;
import java.util.TreeSet;

public class DAO implements DAO_Interface{

    private Set<Prenotazione> listaPrenotazioniFiltrata;

    public DAO(){

    }


    @Override
    public Set<Prenotazione> getPrenotazioniOggi(String giorno, Filtro filtro) {
        listaPrenotazioniFiltrata = new TreeSet<>();
        try {
            File inputFile = new File("Prenotazioni.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("prenotazione");
            if (filtro.getSede() == null && filtro.getTipo() == null) {
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        if(eElement.getElementsByTagName("giorno").item(0).getTextContent().equals(giorno)){
                            listaPrenotazioniFiltrata.add(new Prenotazione(getCittadinoByCF(eElement.getElementsByTagName("cittadino").item(0).getTextContent()), eElement.getElementsByTagName("tipo").item(0).getTextContent(),
                                    new Date(eElement.getElementsByTagName("giorno").item(0).getTextContent()),
                                    eElement.getElementsByTagName("orario").item(0).getTextContent(),
                                    eElement.getElementsByTagName("sede").item(0).getTextContent(), Integer.parseInt(eElement.getAttribute("id"))));
                        }
                    }
                }

            }else if(filtro.getSede() != null && filtro.getTipo() == null){
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        if(eElement.getElementsByTagName("giorno").item(0).getTextContent().equals(giorno) && eElement.getElementsByTagName("sede").item(0).getTextContent().equals(filtro.getSede())){
                            listaPrenotazioniFiltrata.add(new Prenotazione(getCittadinoByCF(eElement.getElementsByTagName("cittadino").item(0).getTextContent()), eElement.getElementsByTagName("tipo").item(0).getTextContent(),
                                    new Date(eElement.getElementsByTagName("giorno").item(0).getTextContent()),
                                    eElement.getElementsByTagName("orario").item(0).getTextContent(),
                                    eElement.getElementsByTagName("sede").item(0).getTextContent(), Integer.parseInt(eElement.getAttribute("id"))));
                        }
                    }
                }
            }else if(filtro.getSede() == null && filtro.getTipo() != null){
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        if(eElement.getElementsByTagName("giorno").item(0).getTextContent().equals(giorno) && eElement.getElementsByTagName("tipo").item(0).getTextContent().equals(filtro.getTipo())){
                            listaPrenotazioniFiltrata.add(new Prenotazione(getCittadinoByCF(eElement.getElementsByTagName("cittadino").item(0).getTextContent()), eElement.getElementsByTagName("tipo").item(0).getTextContent(),
                                    new Date(eElement.getElementsByTagName("giorno").item(0).getTextContent()),
                                    eElement.getElementsByTagName("orario").item(0).getTextContent(),
                                    eElement.getElementsByTagName("sede").item(0).getTextContent(), Integer.parseInt(eElement.getAttribute("id"))));
                        }
                    }
                }
            }else{
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        if(eElement.getElementsByTagName("giorno").item(0).getTextContent().equals(giorno) &&
                                eElement.getElementsByTagName("tipo").item(0).getTextContent().equals(filtro.getTipo()) &&
                                eElement.getElementsByTagName("sede").item(0).getTextContent().equals(filtro.getSede())){
                            listaPrenotazioniFiltrata.add(new Prenotazione(getCittadinoByCF(eElement.getElementsByTagName("cittadino").item(0).getTextContent()), eElement.getElementsByTagName("tipo").item(0).getTextContent(),
                                    new Date(eElement.getElementsByTagName("giorno").item(0).getTextContent()),
                                    eElement.getElementsByTagName("orario").item(0).getTextContent(),
                                    eElement.getElementsByTagName("sede").item(0).getTextContent(), Integer.parseInt(eElement.getAttribute("id"))));
                        }
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return listaPrenotazioniFiltrata;
    }

    @Override
    public Set<Prenotazione> getPrenotazioniNonFiltrate() {
        listaPrenotazioniFiltrata = new TreeSet<>();

        try {

            File inputFile = new File("Prenotazioni.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("prenotazione");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    listaPrenotazioniFiltrata.add(new Prenotazione(getCittadinoByCF(eElement.getElementsByTagName("cittadino").item(0).getTextContent()),
                            eElement.getElementsByTagName("tipo").item(0).getTextContent(),
                            new Date(eElement.getElementsByTagName("giorno").item(0).getTextContent()),
                            eElement.getElementsByTagName("orario").item(0).getTextContent(),
                            eElement.getElementsByTagName("sede").item(0).getTextContent(),
                            Integer.parseInt(eElement.getAttribute("id"))));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listaPrenotazioniFiltrata;
    }

    @Override
    public Set<Prenotazione> getPrenotazioniPerCittadino(Cittadino cittadino) {
        listaPrenotazioniFiltrata = new TreeSet<>();
        try {
            File inputFile = new File("Prenotazioni.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("prenotazione");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if(eElement.getElementsByTagName("cittadino").item(0).getTextContent().equals(cittadino.get_Tessera_Sanitaria(1))){
                        listaPrenotazioniFiltrata.add(new Prenotazione(eElement.getElementsByTagName("tipo").item(0).getTextContent(),
                                new Date(eElement.getElementsByTagName("giorno").item(0).getTextContent()),
                                eElement.getElementsByTagName("orario").item(0).getTextContent(),
                                eElement.getElementsByTagName("sede").item(0).getTextContent(), temp + 1));
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return listaPrenotazioniFiltrata;
    }

    public Cittadino getCittadinoByCF(String cf){
        try {
            File inputFile = new File("Anagrafe.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("persona");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if(eElement.getAttribute("cd_fiscale").equals(cf)){
                        return new Cittadino(cf, eElement.getElementsByTagName("numTessera").item(0).getTextContent(),
                                eElement.getElementsByTagName("nome").item(0).getTextContent(),
                                eElement.getElementsByTagName("cognome").item(0).getTextContent(),
                                eElement.getElementsByTagName("luogoNascita").item(0).getTextContent(),
                                new Date(eElement.getElementsByTagName("dataNascita").item(0).getTextContent()));
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Cittadino getCittadino(String mail, String psw){
        try{
            File inputFile = new File("CittadiniRegistrati.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("cittadino_registrato");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if(eElement.getElementsByTagName("mail").item(0).getTextContent().equals(mail) &&
                            eElement.getElementsByTagName("password").item(0).getTextContent().equals(psw))
                    {
                        return getCittadinoByCF(eElement.getAttribute("cd_fiscale"));
                    }
                }
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public boolean checkPerson(Cittadino cittadino){
        try {
            File inputFile = new File("Anagrafe.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("persona");


            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if(eElement.getAttribute("cd_fiscale").equals(cittadino.get_Tessera_Sanitaria(1))){
                        if(eElement.getElementsByTagName("nome").item(0).getTextContent().equals(cittadino.getInfo(1)) &&
                           eElement.getElementsByTagName("cognome").item(0).getTextContent().equals(cittadino.getInfo(0)) &&
                           eElement.getElementsByTagName("numTessera").item(0).getTextContent().equals(cittadino.get_Tessera_Sanitaria(0)) &&
                           eElement.getElementsByTagName("luogoNascita").item(0).getTextContent().equals(cittadino.getNascita(0)) &&
                           eElement.getElementsByTagName("dataNascita").item(0).getTextContent().equals(cittadino.getNascita(1)))
                           {
                               return true;
                           }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void addRegistrazione(String mail, String password, String cd_fiscale) {
        try {
            File inputFile = new File("CittadiniRegistrati.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            //Prendo la root del documento
            Element cittadiniIscritti = doc.getDocumentElement();
            //Creo l'elemento cittadino registrato
            Element cittadinoRegistrato = doc.createElement("cittadino_registrato");
            cittadinoRegistrato.setAttribute("cd_fiscale",cd_fiscale);
            Element mailEl = doc.createElement("mail");

            //Scrivo dentro l'elemento mailEl
            mailEl.appendChild(doc.createTextNode(mail));
            Element passwordEl = doc.createElement("password");

            //Scrivo dentro l'elemento passwordEl
            passwordEl.appendChild(doc.createTextNode(password));

            cittadinoRegistrato.appendChild(mailEl);
            cittadinoRegistrato.appendChild(passwordEl);

            cittadiniIscritti.appendChild(cittadinoRegistrato);

            DOMSource source = new DOMSource(doc);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer(new StreamSource(new File("pretty_xml.xslt")));
            StreamResult result = new StreamResult("CittadiniRegistrati.xml");
            transformer.transform(source,result);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void addPrenotazione(String sede, String giorno, String orario, String tipo) throws Exception{

            File inputFile = new File("Prenotazioni.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList prenotazioniList = doc.getElementsByTagName("prenotazione");
            int cont = prenotazioniList.getLength() + 1;

            Element prenotazioni = doc.getDocumentElement();
            Element prenotazione = doc.createElement("prenotazione");
            prenotazione.setAttribute("id",""+cont);

            Element eSede = doc.createElement("sede");
            eSede.appendChild(doc.createTextNode(sede));

            Element eGiorno = doc.createElement("giorno");
            eGiorno.appendChild(doc.createTextNode(giorno));

            Element eOrario = doc.createElement("orario");
            eOrario.appendChild(doc.createTextNode(orario));

            Element eTipo = doc.createElement("tipo");
            eTipo.appendChild(doc.createTextNode(tipo));

            Element eCittadino = doc.createElement("cittadino");

            prenotazione.appendChild(eSede);
            prenotazione.appendChild(eGiorno);
            prenotazione.appendChild(eOrario);
            prenotazione.appendChild(eTipo);
            prenotazione.appendChild(eCittadino);

            prenotazioni.appendChild(prenotazione);

            DOMSource source = new DOMSource(doc);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer(new StreamSource(new File("pretty_xml.xslt")));
            StreamResult result = new StreamResult("Prenotazioni.xml");
            transformer.transform(source,result);
    }

    @Override
    public boolean checkLogin(String mail, String password) throws Exception{
        File inputFile = new File("CittadiniRegistrati.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

        NodeList mailList = doc.getElementsByTagName("mail");
        NodeList passwordList = doc.getElementsByTagName("password");

        for(int temp = 0; temp < mailList.getLength(); temp++){
            if(mailList.item(temp).getTextContent().equals(mail)){
                if(passwordList.item(temp).getTextContent().equals(password)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean confirmPrenotazioneCittadino(Cittadino cittadino, Prenotazione temp) throws Exception {
        boolean noCittadino = true;
        File inputFile = new File("Prenotazioni.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        NodeList prenotazioniList = doc.getElementsByTagName("prenotazione");

        for (int i = 0; i < prenotazioniList.getLength(); i++) {
            Node nNode = prenotazioniList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                if (Integer.parseInt(eElement.getAttribute("id")) == temp.getId()) {
                    if (eElement.getElementsByTagName("cittadino").item(0).getTextContent().equals("")) {
                        eElement.getElementsByTagName("cittadino").item(0).setTextContent(cittadino.get_Tessera_Sanitaria(1));

                    }else{
                        noCittadino = false;
                    }
                }
            }
        }
        DOMSource source = new DOMSource(doc);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer(new StreamSource(new File("pretty_xml.xslt")));
        StreamResult result = new StreamResult("Prenotazioni.xml");
        transformer.transform(source, result);
        return noCittadino;
    }

    public boolean checkPrenotazionePresente(Prenotazione temp) throws Exception {
        File inputFile = new File("Prenotazioni.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        NodeList prenotazioniList = doc.getElementsByTagName("prenotazione");

        for (int i = 0; i < prenotazioniList.getLength(); i++) {
            Node nNode = prenotazioniList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                if(eElement.getElementsByTagName("giorno").item(0).getTextContent().equals(temp.getDate()) &&
                   eElement.getElementsByTagName("orario").item(0).getTextContent().equals(temp.getOrario()) &&
                   eElement.getElementsByTagName("sede").item(0).getTextContent().equals(temp.getSede())){
                    return true;
                }
            }
        }
        return false;
    }
}
