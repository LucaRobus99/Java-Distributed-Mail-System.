package Client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import Posta.Property.PostaElettronicaProperty;
import Posta.PostaElettronica;
import Posta.Request;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ModelClient {
    static SimpleStringProperty popupTesto = new SimpleStringProperty();//property sul testo del popup

    public SimpleStringProperty popupTestoProperty() {
        return popupTesto;
    }

    public static final String getpopupTesto() {
        return popupTesto.get();
    }

    public static final void setpopupTesto(String value) {
        popupTesto.set(value);
    }

    boolean nuovaEmail = false;//flag per controllare l'arrivo di nuove email
    boolean connesso = false;//
    Socket s;
    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;
    Request r = null;
    Date d = new Date();
    ObservableList<PostaElettronicaProperty.EmailProperty> emailObList = FXCollections.observableArrayList(email ->
            new Observable[]{email.mittenteProperty(), email.argomentoProperty(), email.dataProperty()});
    PostaElettronica p = null;
    private final ObjectProperty<PostaElettronicaProperty.EmailProperty> currenteEMail = new SimpleObjectProperty<>(null);

    public ObjectProperty<PostaElettronicaProperty.EmailProperty> currentEmailProperty() {
        return currenteEMail;
    }

    public final PostaElettronicaProperty.EmailProperty getCurrentEmail() {
        return currentEmailProperty().get();
    }

    public final void setCurrentEmail(PostaElettronicaProperty.EmailProperty email) {
        currentEmailProperty().set(email);
    }

    public ObservableList<PostaElettronicaProperty.EmailProperty> getEmailList() {
        return emailObList;
    }

    public boolean getNuovaEmail() {
        return nuovaEmail;
    }

    public void setEmailList(ArrayList<PostaElettronica.Email> serverlist) {
        Platform.runLater(() -> {
            nuovaEmail = false;
            //carica TUTTE le email
            if (serverlist.size() != 0) {
                for (int i = 0; i < serverlist.size(); i++) {
                    PostaElettronicaProperty.EmailProperty d = new PostaElettronicaProperty.EmailProperty(serverlist.get(i).getId(), serverlist.get(i).getMittente(), serverlist.get(i).getDestinatari(), serverlist.get(i).getArgomento(), serverlist.get(i).getTesto(), serverlist.get(i).getData());
                    emailObList.add(d);
                    nuovaEmail = true;
                }
            }

            //carica  SOLO email nuove
                for (int i = 0; i < serverlist.size(); i++) {
                    boolean trovato = false;
                    for (int j = 0; j < emailObList.size(); j++) {

                        if (serverlist.get(i).getId().equals(emailObList.get(j).getId())) {
                            trovato = true;

                        }

                    }
                    if (trovato == false) {
                        PostaElettronicaProperty.EmailProperty d = new PostaElettronicaProperty.EmailProperty(serverlist.get(i).getId(), serverlist.get(i).getMittente(), serverlist.get(i).getDestinatari(), serverlist.get(i).getArgomento(), serverlist.get(i).getTesto(), serverlist.get(i).getData());
                        emailObList.add(d);
                        nuovaEmail=true;

                    }

                }


        });
    }

    public void reconnection() {
        try {
            s = new Socket("localhost", 8199);
            outputStream = new ObjectOutputStream(s.getOutputStream());
            inputStream = new ObjectInputStream(s.getInputStream());
            connesso = true;

            if (p != null) {
                loginRequestServerPosta(p.getNomeP());
            }
        } catch (IOException e) {
            connesso = false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void connection() {
        while (!connesso) {
            try {
                s = new Socket("localhost", 8199);
                outputStream = new ObjectOutputStream(s.getOutputStream());
                inputStream = new ObjectInputStream(s.getInputStream());
                connesso = true;
            } catch (IOException e) {
                connesso = false;
            }
        }
    }


    public void sendRequest(Request r) throws IOException {
        if (connesso != false) {
            outputStream.writeObject(r);
            outputStream.flush();
        }


    }


    public void inviaEmail(PostaElettronica.Email email) throws IOException {

        email.setMittente(p.getNomeP());
        r = new Request("InviaEmail", email);

        sendRequest(r);
        outputStream.flush();

    }

    public ArrayList<PostaElettronica.Email> getListaMailServer() throws IOException, ClassNotFoundException {
        ArrayList<PostaElettronica.Email> listaEmail = null;
        r = new Request("LeggiEmail", p);


        sendRequest(r);
        listaEmail = (ArrayList<PostaElettronica.Email>) inputStream.readObject();


        return listaEmail;
    }


    public PostaElettronica loginRequestServerPosta(String postaE) throws IOException, ClassNotFoundException {

        r = new Request("Login", postaE);


        sendRequest(r);
        p = (PostaElettronica) inputStream.readObject();

        if (p != null) {

            try {
                Platform.runLater(() -> {
                    emailObList.clear();
                    for (int i = 0; i < p.getEmail().size(); i++) {
                        PostaElettronicaProperty.EmailProperty d = new PostaElettronicaProperty.EmailProperty(p.getEmail().get(i).getId(), p.getEmail().get(i).getMittente(), p.getEmail().get(i).getDestinatari(), p.getEmail().get(i).getArgomento(), p.getEmail().get(i).getTesto(), p.getEmail().get(i).getData());
                        emailObList.add(d);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return p;

    }

    public void cancellaEmail(PostaElettronicaProperty.EmailProperty email) throws IOException {
        ArrayList<String> destCancellazione = new ArrayList<>();
        destCancellazione.add(p.getNomeP());
        PostaElettronica.Email emailp = new PostaElettronica.Email(email.getId(), null, destCancellazione, null, null, null);
        Request r = new Request("Cancellazione", emailp);
        sendRequest(r);
        emailObList.remove(email);

    }


}





