package org.example.newsrecommendationsystem.user;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

public class Home {

    @FXML
    private Button viewArticle;

    @FXML
    private Button recArticle;

    @FXML
    private Button exploreTopic;

    @FXML
    private Button logout;

    @FXML
    private ImageView profile;

    @FXML
    private StackPane contentStack;

    @FXML
    private AnchorPane viewArticlesPane;

    @FXML
    private AnchorPane recArticlesPane;

    @FXML
    private AnchorPane exploreTopicsPane;

    @FXML
    private void initialize() {
        showPane(viewArticlesPane); // Show View Articles pane by default
    }

    @FXML
    private void viewArticle() {
        showPane(viewArticlesPane);
    }

    @FXML
    private void recArticle() {
        showPane(recArticlesPane);
    }

    @FXML
    private void exploreTopic() {
        showPane(exploreTopicsPane);
    }

    @FXML
    private void logout() {
        // Step 1: Show confirmation dialog for logout
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText(""); // Empty content text for a cleaner view

        // Step 2: Add OK and CANCEL buttons for confirmation
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Step 3: Wait for user response and handle the actions accordingly
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {  // Use ButtonType.OK instead of ButtonType.YES
                goToLoginPage();
            }
        });
    }


    @FXML
    private void handleEditProfile() {
        // Logic for profile editing
        System.out.println("Edit profile clicked.");
    }

    private void goToLoginPage() {
        try {
            // Load the Login page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent loginRoot = loader.load();
            Scene loginScene = new Scene(loginRoot);

            // Get the current stage and set the login scene
            Stage currentStage = (Stage) logout.getScene().getWindow();
            currentStage.setScene(loginScene);
            currentStage.setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load the Login page.");
        }
    }

    private void showPane(AnchorPane paneToShow) {
        // Hide all other panes and show the selected one
        viewArticlesPane.setVisible(false);
        recArticlesPane.setVisible(false);
        exploreTopicsPane.setVisible(false);

        paneToShow.setVisible(true); // Show only the selected pane
    }

    private void showAlert(String title, String message) {
        // Display a simple error alert
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
