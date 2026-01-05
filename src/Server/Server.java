package Server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Server extends Application {


    @Override
    public void start(Stage stage) throws Exception {

        BorderPane root = new BorderPane();
        FXMLLoader logloader = new FXMLLoader(getClass().getResource("Log.fxml"));

        ModelServer modelServer = new ModelServer();

        root.setCenter(logloader.load());
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        ServerController serverController = logloader.getController();
        serverController.initModel(modelServer);

        Thread connessione= new Thread(() -> {

            modelServer.connection();

        });
        connessione.setDaemon(true);
        connessione.start();

    }

    public static void main(String[] args) {
        launch(args);
    }



}


