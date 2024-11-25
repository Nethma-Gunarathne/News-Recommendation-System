package org.example.newsrecommendationsystem.user;

import com.mongodb.client.MongoDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.example.newsrecommendationsystem.Database;

import java.io.IOException;

public class Login {

    public Button SigninButton;
    @FXML
    private TextField userName;

    @FXML
    private TextField password;

    @FXML
    private Button loginButton;

    @FXML
    private Text signInLink;

    @FXML
    private void initialize() {
        // Initialize if necessary
    }

    // Method to handle the login button action
    @FXML
    private void loginButton() {
        String user = userName.getText();
        String pass = password.getText();

        // First check if both fields are filled
        if (user.isEmpty() || pass.isEmpty()) {
            showAlert("Incomplete Fields", "Please enter both username and password.");
            return;
        }

        // Check if the username contains only alphabetic characters
        if (!user.matches("[a-zA-Z]+")) {
            showAlert("Invalid Username", "Username should not contain numbers or special characters. Please check your username.");
            return;
        }

        // Verify credentials from the database
        if (verifyCredentials(user, pass)) {
            // If verification is successful, proceed to the Home page
            goToHomePage();
        } else {
            // If verification fails, show an alert
            showAlert("Login Failed", "Invalid username or password. Please try again.");
        }
    }

    // Method to navigate to the Sign-Up page when clicking signInLink
    @FXML
    public void SignInLink(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
            Parent signUpRoot = loader.load();
            Scene signUpScene = new Scene(signUpRoot);

            // Get current stage and set new scene
            Stage currentStage = (Stage) signInLink.getScene().getWindow();
            currentStage.setScene(signUpScene);
            currentStage.setTitle("Sign Up");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Could not load the Sign-Up page.");
        }
    }

    // Helper method to show alert messages
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to navigate to the Home page
    private void goToHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
            Parent homeRoot = loader.load();
            Scene homeScene = new Scene(homeRoot);

            // Get current stage and set new scene
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.setScene(homeScene);
            currentStage.setTitle("Home");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Could not load the Home page.");
        }
    }

    public void SignUpLink(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/newsrecommendationsystem/user/SignUp.fxml"));
            Parent signUpRoot = loader.load();
            Scene signUpScene = new Scene(signUpRoot);

            // Get current stage and set new scene
            Stage currentStage = (Stage) SigninButton.getScene().getWindow();
            currentStage.setScene(signUpScene);
            currentStage.setTitle("Sign Up");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Could not load the Sign-Up page.");
        }
    }


    // Method to verify credentials from the database
    private boolean verifyCredentials(String username, String password) {
        try {
            // Get the database instance
            MongoDatabase database = Database.getDatabase();

            // Access the "users" collection (replace with your actual collection name)
            var collection = database.getCollection("users");

            // Query the database for a document matching the username and password
            var query = new org.bson.Document("userName", username)
                    .append("password", password);

            var result = collection.find(query).first();
            return result != null; // If a matching document is found, credentials are valid
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while verifying credentials.");
            return false;
        }
    }
}