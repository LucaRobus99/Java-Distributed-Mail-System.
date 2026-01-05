package Client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.*;

public class Client extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        ModelClient modelClient = new ModelClient();
        BorderPane root = new BorderPane();
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        root.setCenter(loginLoader.load());
        LoginController loginController = loginLoader.getController();
        loginController.initModel(modelClient);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login Posta");
        stage.show();
        stage.setOnCloseRequest(e -> Platform.exit());

        Runnable r = () -> modelClient.connection();
        Thread connessione = new Thread(r);
        connessione.setDaemon(true);
        connessione.start();
    }


    public static void main(String[] args) throws IOException {

        launch(args);


    }

}


