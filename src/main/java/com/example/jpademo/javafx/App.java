package com.example.jpademo.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.net.URL;
import java.util.Objects;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlLocation = getClass().getResource("/com.example.jpademo/javafx/views/MainView.fxml");
        System.out.println("FXML Location: " + fxmlLocation);
        Parent root = FXMLLoader.load(fxmlLocation);
        primaryStage.setTitle("Tour Planner");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
