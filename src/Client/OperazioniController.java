package Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.stage.Stage;
import java.io.IOException;
public class OperazioniController {


    @FXML
    private ButtonBar operations;

    private ModelClient modelClient;

    @FXML
    public void initModel(ModelClient modelClient) {

        if (this.modelClient != null) {
            throw new IllegalStateException("ModelClient can only be initialized once");
        }
        this.modelClient = modelClient;

    }

    @FXML
    public void crea() {
//creo un'email
        try {

            FXMLLoader fxloader = new FXMLLoader(getClass().getResource("Messaggio.fxml"));
            Parent root = (Parent) fxloader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 500));
            stage.show();

            MessaggioController controllerMessaggio = (MessaggioController) fxloader.getController();
            controllerMessaggio.initModel(modelClient);


        } catch (IOException e) {

        }
    }

    @FXML
    public void elimina() throws IOException {
        try {
//elimina un email
            if (modelClient.connesso != false ) {
                if(modelClient.getCurrentEmail()==null){
                    FXMLLoader fxloader = new FXMLLoader(getClass().getResource("Popup.fxml"));
                    Parent root = (Parent) fxloader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Errore");
                    stage.setScene(new Scene(root, 300, 150));
                    stage.show();
                    PopupController controllerPopup = (PopupController) fxloader.getController();
                    controllerPopup.initModel(modelClient);
                    modelClient.popupTestoProperty().set("Selezionere prima un'email! ");
                }
                else{
                    modelClient.cancellaEmail(modelClient.getCurrentEmail());
                }

            } else {
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
    //condivido un email
    @FXML
    public void condividi() throws IOException {
        try {
            if(modelClient.getCurrentEmail()==null){
                FXMLLoader fxloader = new FXMLLoader(getClass().getResource("Popup.fxml"));
                Parent root = (Parent) fxloader.load();
                Stage stage = new Stage();
                stage.setTitle("Errore");
                stage.setScene(new Scene(root, 300, 150));
                stage.show();
                PopupController controllerPopup = (PopupController) fxloader.getController();
                controllerPopup.initModel(modelClient);
                modelClient.popupTestoProperty().set("Selezionere prima un'email! ");
            }else{
                FXMLLoader fxloader = new FXMLLoader(getClass().getResource("Messaggio.fxml"));
                Parent root = (Parent) fxloader.load();
                Stage stage = new Stage();
                stage.setTitle("Condividi");
                stage.setScene(new Scene(root, 500, 500));
                stage.show();
                MessaggioController controllerMessaggio = (MessaggioController) fxloader.getController();
                controllerMessaggio.setReuqest("ForWard");
                controllerMessaggio.initModel(modelClient);
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //rispondo un email
    @FXML
    public void rispondi() {
        try {
            if(modelClient.getCurrentEmail()==null){
                FXMLLoader fxloader = new FXMLLoader(getClass().getResource("Popup.fxml"));
                Parent root = (Parent) fxloader.load();
                Stage stage = new Stage();
                stage.setTitle("Errore");
                stage.setScene(new Scene(root, 300, 150));
                stage.show();
                PopupController controllerPopup = (PopupController) fxloader.getController();
                controllerPopup.initModel(modelClient);
                modelClient.popupTestoProperty().set("Selezionere prima un'email! ");
            }else {
                FXMLLoader fxloader = new FXMLLoader(getClass().getResource("Messaggio.fxml"));
                Parent root = (Parent) fxloader.load();
                Stage stage = new Stage();
                stage.setTitle("Risposta");
                stage.setScene(new Scene(root, 500, 500));
                stage.show();
                MessaggioController controllerMessaggio = (MessaggioController) fxloader.getController();
                controllerMessaggio.setReuqest("Replay");
                controllerMessaggio.initModel(modelClient);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }


    }
    //rispondo in broadcast con tutti i destinatari dell'  email
    public void rispondiAtutti() {
        try {
            if(modelClient.getCurrentEmail()==null){
                FXMLLoader fxloader = new FXMLLoader(getClass().getResource("Popup.fxml"));
                Parent root = (Parent) fxloader.load();
                Stage stage = new Stage();
                stage.setTitle("Errore");
                stage.setScene(new Scene(root, 300, 150));
                stage.show();
                PopupController controllerPopup = (PopupController) fxloader.getController();
                controllerPopup.initModel(modelClient);
                modelClient.popupTestoProperty().set("Selezionere prima un'email! ");
            }else{
                FXMLLoader fxloader = new FXMLLoader(getClass().getResource("Messaggio.fxml"));
                Parent root = (Parent) fxloader.load();
                Stage stage = new Stage();
                stage.setTitle("Risposta a Tutti");
                stage.setScene(new Scene(root, 500, 500));
                stage.show();
                MessaggioController controllerMessaggio = (MessaggioController) fxloader.getController();
                controllerMessaggio.setReuqest("ReplayAll");
                controllerMessaggio.initModel(modelClient);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
