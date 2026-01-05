package Server;
import Posta.PostaElettronica;
import javafx.beans.property.SimpleStringProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ModelServer {

    static PostaElettronica p1 = new PostaElettronica("Davide@gmail.com", null);
    static PostaElettronica p2 = new PostaElettronica("Luca@gmail.com", null);
    static PostaElettronica p3 = new PostaElettronica("Marco@gmail.com", null);
    public static ArrayList<PostaElettronica> posta = new ArrayList<>(Arrays.asList(p1, p2, p3));
    public static ArrayList<String> postaOnline = new ArrayList<>(Arrays.asList(p1.getNomeP(), p2.getNomeP(), p3.getNomeP()));
    static SimpleStringProperty log = new SimpleStringProperty();

    public static final String getLog() {
        return log.get();
    }

    public static final void setLog(String value) {
        log.set(value);
    }

    public SimpleStringProperty logProperty() {
        return log;
    }


    public static String getDataLog() {
        String dataLog;
         SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yy HH:mm:ss");//dd/MM/yyyy
        Date strDate = new Date();
        dataLog = sdfDate.format(strDate);
        return dataLog;
    }


    public void setPostaOnline(String p) {
        postaOnline.add(p);
    }

    public void connection() {
        try {

            ServerSocket s = new ServerSocket(8199);
            Socket incoming = null;
            while (true) {
                // si mette in attesa di richiesta di connessione e la apre
                try {
                    incoming = s.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Runnable r = new ThreadConnection(incoming);
                new Thread(r).start();


            }
        } catch (SocketException se) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized PostaElettronica identificatore(String identificatore) {
        if (postaOnline.contains(identificatore)) {
            postaOnline.remove(identificatore);
            for (int i = 0; i < posta.size(); i++)
                if (posta.get(i).getNomeP().equals(identificatore)) {
                    posta.get(i).setEmail(caricaPosta(posta.get(i)));


                    setLog(getLog() + "\n[" + getDataLog() + "]" + identificatore + " si è connesso");
                    return posta.get(i);
                }
        }

        return null;
    }

    public static synchronized ArrayList<PostaElettronica.Email> caricaPosta(PostaElettronica NomeP) {
        ArrayList<PostaElettronica.Email> listaEmail = new ArrayList<>();
        boolean trovato = false;
        int k = 0;
        while (k < posta.size() && !trovato) {
            if (posta.get(k).getNomeP().equals(NomeP.getNomeP())) {
                PostaElettronica.Email postaEmail = null;
                final String xmlFilePath = NomeP.getNomeP() + ".xml";
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                try {
                    ArrayList<String> destinatari = null;
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse(new File(xmlFilePath));
                    Node nodePostaEletronica = doc.getElementsByTagName("PostaEletronica").item(0);
                    NodeList listaEmailNode = nodePostaEletronica.getChildNodes();
                    for (int temp = 0; temp < listaEmailNode.getLength(); temp++) {
                        Node email = listaEmailNode.item(temp);
                        if (email.getNodeType() == Node.ELEMENT_NODE) {

                            Element element = (Element) email;
                            String id = email.getAttributes().getNamedItem("id").getNodeValue();
                            String mittente = element.getElementsByTagName("mittente").item(0).getTextContent();
                            NodeList dest = element.getElementsByTagName("destinatari");
                            int l = dest.getLength();
                            destinatari = new ArrayList<String>(l);
                            for (int i = 0; i < l; i++) {
                                destinatari.add(dest.item(i).getTextContent());
                            }
                            String argomento = element.getElementsByTagName("argomento").item(0).getTextContent();
                            String testo = element.getElementsByTagName("testo").item(0).getTextContent();
                            String d = element.getElementsByTagName("data").item(0).getTextContent();
                            DateFormat dateformat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
                            dateformat.setTimeZone(TimeZone.getTimeZone("UTC"));
                            Date data = null;
                            try {
                                data = dateformat.parse(d);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            postaEmail = new PostaElettronica.Email(id, mittente, destinatari, argomento, testo, data);
                            listaEmail.add(postaEmail);

                        }
                    }

                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                trovato = true;
            }
            k++;
        }


        return listaEmail;
    }

    public static synchronized ArrayList<PostaElettronica.Email> aggiorna(PostaElettronica NomeP) {
        ArrayList<PostaElettronica.Email> listaEmail = new ArrayList<>();
        boolean trovato = false;
        int k = 0;
        while (k < posta.size() && !trovato) {
            if (posta.get(k).getNomeP().equals(NomeP.getNomeP())) {
                trovato = true;
                PostaElettronica.Email postaEmail = null;
                final String xmlFilePath = NomeP.getNomeP() + ".xml";
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                try {
                    ArrayList<String> destinatari = null;
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse(new File(xmlFilePath));
                    Node nodePostaEletronica = doc.getElementsByTagName("PostaEletronica").item(0);
                    NodeList listaEmailNode = nodePostaEletronica.getChildNodes();
                    for (int temp = 0; temp < listaEmailNode.getLength(); temp++) {
                        Node email = listaEmailNode.item(temp);
                        if (email.getNodeType() == Node.ELEMENT_NODE) {

                            Element element = (Element) email;
                            String id = email.getAttributes().getNamedItem("id").getNodeValue();
                            String mittente = element.getElementsByTagName("mittente").item(0).getTextContent();
                            NodeList dest = element.getElementsByTagName("destinatari");
                            int l = dest.getLength();
                            destinatari = new ArrayList<String>(l);
                            for (int i = 0; i < l; i++) {
                                destinatari.add(dest.item(i).getTextContent());
                            }
                            String argomento = element.getElementsByTagName("argomento").item(0).getTextContent();
                            String testo = element.getElementsByTagName("testo").item(0).getTextContent();
                            String d = element.getElementsByTagName("data").item(0).getTextContent();
                            DateFormat dateformat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
                            dateformat.setTimeZone(TimeZone.getTimeZone("UTC"));
                            Date data = null;
                            try {
                                data = dateformat.parse(d);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            postaEmail = new PostaElettronica.Email(id, mittente, destinatari, argomento, testo, data);

                            boolean trovato1 = false;
                            if (posta.get(k).getEmail() != null) {
                                for (int j = 0; j < posta.get(k).getEmail().size(); j++) {
                                    if (posta.get(k).getEmail().get(j).getId().equals(postaEmail.getId())) {
                                        trovato1 = true;

                                    }

                                }
                                if (trovato1 == false) {
                                    listaEmail.add(postaEmail);
                                    posta.get(k).getEmail().add(postaEmail);

                                }

                            }
                        }
                    }

                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            k++;

        }


        return listaEmail;
    }


    public synchronized void inviaEmail(PostaElettronica.Email email) {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        ArrayList<String> destinatari = new ArrayList<>();


        try {
            destinatari.addAll(email.getDestinatari());
            DocumentBuilder builder = factory.newDocumentBuilder();
            for (int i = 0; i < destinatari.size(); i++) {
                int k = 0;
                boolean trovato = false;
                while (k < posta.size() && !trovato) {

                    if (posta.get(k).getNomeP().equals(destinatari.get(i))) {

                        final String xmlFilePath = destinatari.get(i) + ".xml";
                        Document doc = builder.parse(new File(xmlFilePath));

                        Node nodePostaEletronica = doc.getElementsByTagName("PostaEletronica").item(0);

                        String uniqueID = UUID.randomUUID().toString();
                        Element newEmail = doc.createElement("email");
                        newEmail.setAttribute("id", uniqueID);

                        Element mittente = doc.createElement("mittente");
                        mittente.setTextContent(email.getMittente());
                        newEmail.appendChild(mittente);
                        for (int j = 0; j < destinatari.size(); j++) {
                            Element dest = doc.createElement("destinatari");
                            dest.setTextContent(destinatari.get(j));
                            newEmail.appendChild(dest);
                        }
                        Element argomento = doc.createElement("argomento");
                        argomento.setTextContent(email.getArgomento());
                        newEmail.appendChild(argomento);

                        Element testo = doc.createElement("testo");
                        testo.setTextContent(email.getTesto());
                        newEmail.appendChild(testo);
                        Element data = doc.createElement("data");
                        DateFormat dateformatter = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
                        String date = dateformatter.format(email.getData());
                        data.setTextContent(date);
                        newEmail.appendChild(data);

                        nodePostaEletronica.appendChild(newEmail);


                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
                        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "0");
                        DOMSource dbsource = new DOMSource(doc);
                        StreamResult result = new StreamResult(destinatari.get(i) + ".xml");
                        transformer.transform(dbsource, result);
                        trovato = true;

                        setLog(getLog() + "\n[" + getDataLog() + "]  Il client: " + email.getMittente() + "ha inviato un email al Client: " + email.getDestinatari().get(i) + "");
                        wait(1000);
                        setLog(getLog() + "\n[" + getDataLog() + "]  Il client: " + email.getDestinatari().get(i) + "ha ricevuto un email dal Client: " + email.getMittente() + "");
                    }
                    k++;
                }
                if (trovato == false) {
                    String uniqueID = UUID.randomUUID().toString();

                    ArrayList<String> dest = new ArrayList<>();
                    dest.add(email.getMittente());
                    Date strDate = new Date();
                    PostaElettronica.Email emailErrore = new PostaElettronica.Email(uniqueID, "ServerGmail@gmail.com", dest, "(NO REPLAY) Errore messaggio:" + email.getArgomento()
                            + ""
                            , " Indirizzo non trovato\n" +
                            "Il tuo messaggio non è stato recapitato a " + email.getDestinatari().get(i) + " perché l'indirizzo risulta inesistente \n" +
                            "---------- FORWARD del messaggio ----------\n" +
                            "Da: " + email.getMittente() + "\n" +
                            "A: " + email.getDestinatari().get(i) + "\n" +
                            "Data:" + email.getData() + "\n" +
                            "Oggetto:" + email.getArgomento() + "\n" +
                            "Testo:" + email.getTesto() + "\n", strDate);

                    this.inviaEmail(emailErrore);

                    setLog(getLog() + ("\n[" + getDataLog() + "]  ERRORE:Il client: " + email.getMittente() + "ha tentato di inviare un email ad indirizzo di postta elettronica inesistente: "
                            + email.getDestinatari().get(i) + ""));
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    public synchronized void cancellaEmail(PostaElettronica.Email email) {
        int k = 0;
        boolean trovato = false;
        while (k < posta.size() && !trovato) {
            if (posta.get(k).getNomeP().equals(email.getDestinatari().get(0))) {

                final String xmlFilePath = email.getDestinatari().get(0) + ".xml";
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                try {

                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse(new File(xmlFilePath));
                    NodeList listaEmailNode = doc.getElementsByTagName("email");


                    for (int i = 0; i < listaEmailNode.getLength(); i++) {
                        Node emailDel = listaEmailNode.item(i);
                        if (emailDel.getNodeType() == Node.ELEMENT_NODE) {
                            String id = emailDel.getAttributes().getNamedItem("id").getTextContent();
                            if (email.getId().equals(id)) {
                                emailDel.getParentNode().removeChild(emailDel);

                                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                                transformer.setOutputProperty(OutputKeys.INDENT, "no");
                                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                                StreamResult result = new StreamResult(new File(xmlFilePath));
                                DOMSource source = new DOMSource(doc);
                                transformer.transform(source, result);




                            }


                        }

                    }

                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TransformerConfigurationException e) {
                    e.printStackTrace();
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
                for(int i=0;i<posta.get(k).getEmail().size();i++){
                    if(posta.get(k).getEmail().get(i).getId().equals(email.getId()));

                }

                trovato=true;
            }
            k++;
        }

    }


}

