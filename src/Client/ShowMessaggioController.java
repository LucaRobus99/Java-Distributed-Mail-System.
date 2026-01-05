package Client;

import Posta.Property.PostaElettronicaProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

public class ShowMessaggioController {
    @FXML
    private ModelClient modelClient;
    @FXML
    private TextField mittente;
    @FXML
    private TextField destinatari;
    @FXML
    private TextField oggetto;
    @FXML
    private TextArea testo;

    @FXML
    public void initModel(ModelClient modelClient) {
        // ensure modelClient is only set once:
        if (this.modelClient != null) {
            throw new IllegalStateException("ModelClient can only be initialized once");
        }
        this.modelClient = modelClient;

        modelClient.currentEmailProperty().addListener(this::changed);
    }
//binding sulle property della  visione dell'email
    private void changed(ObservableValue<? extends PostaElettronicaProperty.EmailProperty> obs, PostaElettronicaProperty.EmailProperty oldeEmail, PostaElettronicaProperty.EmailProperty newEmail) {
        if (oldeEmail != null) {
            mittente.textProperty().unbindBidirectional(oldeEmail.mittenteProperty());
           destinatari.textProperty().unbindBidirectional(oldeEmail.destinatariProperty());
            oggetto.textProperty().unbindBidirectional(oldeEmail.argomentoProperty());
            testo.textProperty().unbindBidirectional(oldeEmail.testoProperty());
        }
        if (newEmail == null) {
            mittente.setText("");
            destinatari.setText("");
            oggetto.setText("");
            testo.setText("");
        } else {
            mittente.textProperty().bindBidirectional(newEmail.mittenteProperty());
            String a=null;
            for(int i=0;i<newEmail.getDestinatari().size();i++){
           if(a==null)
           {
            a=newEmail.getDestinatari().get(i).getValue();
           }else
            a+=","+newEmail.getDestinatari().get(i).getValue();
        }
            SimpleStringProperty s=new SimpleStringProperty();
            s.set(a);
            destinatari.textProperty().bindBidirectional(s);
          oggetto.textProperty().bindBidirectional(newEmail.argomentoProperty());
            testo.textProperty().bindBidirectional(newEmail.testoProperty());

        }

    }
}
