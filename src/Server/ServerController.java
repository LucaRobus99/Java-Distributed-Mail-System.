package Server;


import java.io.IOException;

import javafx.fxml.FXML;

import javafx.scene.control.TextArea;


public class ServerController {
    @FXML
    private ModelServer modelServer;
    @FXML
    private TextArea logAzioni;


    public void initModel(ModelServer Server) throws IOException {
        // ensure modelClient is only set once:
        if (this.modelServer != null) {
            throw new IllegalStateException("ModelServer can only be initialized once");
        }
        this.modelServer = Server;

        modelServer.logProperty().set("Log Azioni");
        logAzioni.textProperty().bind(modelServer.logProperty());
        modelServer.logProperty().addListener((observableValue, oldVal, newVal) -> {
        });


    }

}

