package Client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class PopupController {
    ModelClient model;
    @FXML
    Label testoPopup;
    @FXML
    Button closeButton;
    public void initModel(ModelClient modelClient) throws IOException {

        if (this.model != null) {
            throw new IllegalStateException("ModelClient can only be initialized once");
        }
        //binding sulla property popupTestoProperty
        this.model = modelClient;
        testoPopup.textProperty().bind(model.popupTestoProperty());
        model.popupTestoProperty().addListener((observableValue, oldVal, newVal) -> {});
    }
    //chiusura popUp
    public void ok(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
