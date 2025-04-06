package com.example.provan1;

import javafx.application.Application;
//import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/provan1/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 711, 489);

        stage.setTitle("Farmácia - Controle de Medicamentos");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}



