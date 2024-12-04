package org.example.newsrecommendationsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the Login.fxml as the initial screen
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("News Recommendation System");
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
