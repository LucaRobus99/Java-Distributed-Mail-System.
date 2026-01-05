package Client;

import Posta.PostaElettronica;
import Posta.Property.PostaElettronicaProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessaggioController {
    @FXML
    private TextField destinatari;
    @FXML
    private TextArea testo;
    @FXML
    private TextField oggetto;
    @FXML
    private ModelClient modelClient;
    @FXML
    private Text output;
    String request = null;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public void initModel(ModelClient modelClient) {
        // ensure modelClient is only set once:
        if (this.modelClient != null) {
            throw new IllegalStateException("ModelClient can only be initialized once");
        }
        this.modelClient = modelClient;

        if (modelClient.getCurrentEmail() != null && request == "Replay") {
            destinatari.setText(modelClient.getCurrentEmail().getMittente());
            oggetto.setText("Ri:" + modelClient.getCurrentEmail().getArgomento());
            testo.setText(modelClient.getCurrentEmail().getTesto() + "\n--------------RISPOSTA-----" +
                    "-----------------------------------------------------------------------------------------\n");


        } else if (modelClient.getCurrentEmail() != null && request == "ReplayAll") {
            String a = modelClient.getCurrentEmail().getMittente();
            for (int i = 0; i < modelClient.getCurrentEmail().getDestinatari().size(); i++) {
                if (!modelClient.getCurrentEmail().getDestinatari().get(i).getValue().equals(modelClient.p.getNomeP()))
                    a += "," + modelClient.getCurrentEmail().getDestinatari().get(i).getValue();
            }
            destinatari.setText(a);
            oggetto.setText(modelClient.getCurrentEmail().getArgomento());
            testo.setText(modelClient.getCurrentEmail().getTesto() + "\n--------------RISPOSTA-----" +
                    "-----------------------------------------------------------------------------------------\n");
        } else if (modelClient.getCurrentEmail() != null && request == "ForWard") {
            oggetto.setText("FORWARD:" + modelClient.getCurrentEmail().getArgomento());
            testo.setText("Email di:" + modelClient.getCurrentEmail().getMittente() + "\n--------------FORWARD----------------------------------------------------------------------------------------------\n" + modelClient.getCurrentEmail().getTesto());

        }
    }

    @FXML
    public void setReuqest(String richiesta) {
        request = richiesta;
    }

    public void invia() throws IOException {

        ArrayList<String> dest = new ArrayList<>();
        String destString = destinatari.getText();
        String[] destSplit = destString.split(",");
        for (int i = 0; i < destSplit.length; i++) {
            dest.add(destSplit[i]);
        }
        String oggettoString = oggetto.getText();
        String testoString = testo.getText();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date data = new Date();
        PostaElettronica.Email p = new PostaElettronica.Email(null, null, dest, oggettoString, testoString, data);
        boolean errore = false;
        for (int i = 0; i < dest.size(); i++) {

            if (!validate(p.getDestinatari().get(i))) {
                output.setFill(Color.RED);
                output.setText("Errore:La sintassi dell'email: '" + dest.get(i) + "' è incorretta");
                errore = true;
            }

        }

        if (errore == false) {
            try {
                if (modelClient.connesso == true) {
                    if(oggetto.getText()==""){
                        output.setText(" ");
                        FXMLLoader fxloader = new FXMLLoader(getClass().getResource("Popup.fxml"));
                        Parent root = (Parent) fxloader.load();
                        Stage stage = new Stage();
                        stage.setTitle("Errore");
                        stage.setScene(new Scene(root, 300, 150));
                        stage.show();
                        PopupController controllerPopup = (PopupController) fxloader.getController();
                        controllerPopup.initModel(modelClient);
                        modelClient.popupTestoProperty().set("Stai inviando un email senza oggetto");

                    }else{
                        modelClient.inviaEmail(p);
                        output.setFill(Color.GREEN);
                        output.setText("Email inviata con successo");
                    }
                } else if (modelClient.connesso == false) {
                    output.setText(" ");
                    FXMLLoader fxloader = new FXMLLoader(getClass().getResource("Popup.fxml"));
                    Parent root = (Parent) fxloader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Errore");
                    stage.setScene(new Scene(root, 300, 150));
                    stage.show();
                    PopupController controllerPopup = (PopupController) fxloader.getController();
                    controllerPopup.initModel(modelClient);
                    modelClient.popupTestoProperty().set("ServerDown");
                }
            } catch (IOException e) {

                e.printStackTrace();
            }

        }

    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

}
