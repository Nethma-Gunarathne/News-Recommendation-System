package org.example.newsrecommendationsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize the database before using it
        Database.initDatabase();
        try {
            // Load the Login.fxml as the initial screen
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("user/Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: Could not load the initial FXML file.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
