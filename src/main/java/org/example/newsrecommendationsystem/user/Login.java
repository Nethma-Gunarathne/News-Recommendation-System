package org.example.newsrecommendationsystem.user;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.bson.Document;
import org.example.newsrecommendationsystem.Database;

import java.io.IOException;

public class Login {

    @FXML
    private TextField userName;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginButton;

    @FXML
    private void loginButton() {
        if (!validateFields()) {
            return;
        }

        try {
            MongoDatabase database = Database.getDatabase();
            MongoCollection<Document> usersCollection = database.getCollection("users");

            Document user = usersCollection.find(
                    new Document("userName", userName.getText())
                            .append("password", password.getText())
            ).first();

            if (user == null) {
                showAlert("Login Failed", "Invalid username or password. Please try again.");
            } else {
                navigateToPage("Home.fxml", "Home");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not connect to the database.");
        }
    }

    private boolean validateFields() {
        if (userName.getText().isEmpty() || password.getText().isEmpty()) {
            showAlert("Validation Error", "Both email and password fields are required.");
            return false;
        }
        return true;
    }

    @FXML
    private void SignUpLink() {
        navigateToPage("SignUp.fxml", "SignUp");
    }

    private void navigateToPage(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Could not load the " + title + " page.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
