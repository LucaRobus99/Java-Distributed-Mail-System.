package Client;

import Posta.PostaElettronica;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.fxml.FXML;

import java.io.IOException;

public class LoginController {

    @FXML
    private ModelClient model;
    @FXML
    private TextField nomeAccount;
    @FXML
    private Text errore;

    @FXML
    public void initModel(ModelClient modelClient) throws IOException {

        if (this.model != null) {
            throw new IllegalStateException("ModelClient can only be initialized once");
        }

        this.model = modelClient;


    }

    @FXML
    public void accedi(ActionEvent event) {
//controllo prima se il client è connesso
        if (model.connesso != false) {

            try {
                String nome = nomeAccount.getText();
                if (!nome.equals("")) {//controllo  se l'utente ha inserito dei caratteri
                    PostaElettronica postaRicevuta = model.loginRequestServerPosta(nome);
                    if (postaRicevuta != null) {//controllo  se l'utente ha inserito un idirizzo di posta elettronica
                        //corretta o se l'account di posta elttronica è già conesso
                        FXMLLoader listloader = new FXMLLoader(getClass().getResource("List.fxml"));
                        FXMLLoader operazioniLoader = new FXMLLoader(getClass().getResource("Operazioni.fxml"));
                        FXMLLoader messaggioLoader = new FXMLLoader(getClass().getResource("ShowMessaggio.fxml"));
                        BorderPane root = new BorderPane();
                        root.setCenter(listloader.load());
                        root.setBottom(operazioniLoader.load());
                        root.setRight(messaggioLoader.load());


                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setTitle(postaRicevuta.getNomeP());
                        stage.show();

                        ListaEmailController controllerlist = (ListaEmailController) listloader.getController();
                        OperazioniController controleroperazioni = (OperazioniController) operazioniLoader.getController();
                        ShowMessaggioController controllermessaggio = (ShowMessaggioController) messaggioLoader.getController();


                        controleroperazioni.initModel(model);
                        controllerlist.initModel(model);
                        controllermessaggio.initModel(model);
                    } else
                        errore.setText("Account inesistente o attualmente in uso");
                } else
                    errore.setText("Inserire un email");


            } catch (IOException e) {
                model.connesso = false;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else{
            model.reconnection();
        if (model.connesso == false) {
            errore.setText("Server down");
        }
        }

    }
}

