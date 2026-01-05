package Client;
import Posta.PostaElettronica;
import Posta.Property.PostaElettronicaProperty;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;


public class ListaEmailController {

    @FXML
    private ListView<PostaElettronicaProperty.EmailProperty> listView;

    private ModelClient modelClient;

//inizializzaione model
    @FXML
    public void initModel(ModelClient modelClient) throws IOException {
        // ensure modelClient is only set once:
        if (this.modelClient != null) {
            throw new IllegalStateException("ModelClient can only be initialized once");

        }
        //Thread che gestisce la riecezione dei messaggi,facendo una richiesta di controllo ogni secondo
        Thread threadListeEmail = new Thread(() -> {
            while (true) {
                ArrayList<PostaElettronica.Email> listEmail = null;

                try {
                    listEmail = modelClient.getListaMailServer();
                  modelClient.setEmailList(listEmail);
                    boolean nuovaEmail=modelClient.getNuovaEmail();

                    if(nuovaEmail==true){
                        Platform.runLater(() -> {
                          try {
                            FXMLLoader fxloader = new FXMLLoader(getClass().getResource("Popup.fxml"));
                            Parent root = (Parent) fxloader.load();
                            Stage stage = new Stage();
                            stage.setTitle("PopUp");
                            stage.setScene(new Scene(root, 300, 150));
                            stage.show();
                            PopupController controllerPopup = (PopupController) fxloader.getController();
                            controllerPopup.initModel(modelClient);
                            modelClient.popupTestoProperty().set("Ci sono nuove email ! ");
                          } catch (IOException e) {
                              e.printStackTrace();
                          }
                        });

                    }
                    Thread.sleep(1000);
                } catch (IOException e) {
                    modelClient.connesso=false;
                 modelClient.reconnection();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        threadListeEmail.setDaemon(true);
        threadListeEmail.start();

        this.modelClient = modelClient;

        listView.setItems(modelClient.getEmailList());

        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->
                modelClient.setCurrentEmail(newSelection));


        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            public void updateItem(PostaElettronicaProperty.EmailProperty email, boolean empty) {
                super.updateItem(email, empty);

                if (empty) {
                    setText(null);
                } else {

                    setText("|"+email.getMittente() +"|"+ email.getArgomento() +"|"+ email.getData()+"|");
                }
                listView.refresh();

            }
        });

    }

}














