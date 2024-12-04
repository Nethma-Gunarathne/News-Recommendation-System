package org.example.newsrecommendationsystem.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.example.newsrecommendationsystem.AbstractLogin;
import org.example.newsrecommendationsystem.Navigation;

import java.io.IOException;

public class Admin extends AbstractLogin {

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private static final String CORRECT_PASSWORD = "admin123";

    @FXML
    private void handleLogin() {
        String enteredPassword = passwordField.getText();

        if (verifyPassword(enteredPassword, CORRECT_PASSWORD)) {
            navigateToManagementPage();
        } else {
            errorLabel.setText("Incorrect password. Please try again.");
        }
    }


    private void navigateToManagementPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/newsrecommendationsystem/ManagementPage.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) passwordField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Management Page");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void BacktoLogin(ActionEvent actionEvent) {
        Navigation.navigateToPage(actionEvent, "/org/example/newsrecommender/Login.fxml", "Admin Login");

    }
}
