package org.example.newsrecommendationsystem.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.newsrecommendationsystem.AbstractLogin;
import org.example.newsrecommendationsystem.database.dbManager;

import java.io.IOException;

public class Login extends AbstractLogin {

    @FXML
    private TextField userName;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginButton;

    @FXML
    private Button signUpLink;

    @FXML
    private Button adminLogin;

    private dbManager databaseManager = new dbManager();

    @FXML
    private void loginButton(ActionEvent event) {
        String username = userName.getText();
        String pass = password.getText();

        // Validate fields are not empty
        if (username.isEmpty() || pass.isEmpty()) {
            showAlert("Validation Error", "Both fields must be filled in.", Alert.AlertType.ERROR);
            return;
        }

        // Check if the user exists using the validateUser method
        User user = databaseManager.validateUser(username, pass);

        if (user != null) {
            // Verify password
            if (verifyPassword(pass, user.getPassword())) {
                // Set the current user in the session
                Session.setCurrentUser(user);
                System.out.println("Login successful: " + user.getUserName());
                // Navigate to Home page
                navigateToPage("Home.fxml", "Home");
            } else {
                showAlert("Login Failed", "Incorrect password.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Login Failed", "User not found.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void signUpLink(ActionEvent event) {
        navigateToPage("SignUp.fxml", "Sign Up");
    }

    @FXML
    private void adminLogin(ActionEvent event) {
        navigateToPage("adminPage.fxml", "Admin Login");
    }

    private void navigateToPage(String fxmlFile, String pageTitle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/newsrecommendationsystem/" + fxmlFile));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(pageTitle);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
