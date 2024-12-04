package org.example.newsrecommendationsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Navigation {


    public static void navigateToPage(ActionEvent actionEvent, String fxmlFile, String windowTitle) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(Navigation.class.getResource(fxmlFile)));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            if (windowTitle != null) {
                stage.setTitle(windowTitle);  // Set custom title if provided
            }
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

