package org.example.newsrecommendationsystem.user;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

public class Home {

    public MenuButton dropdownButton;
    @FXML
    private VBox sidebar;

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
    private Pane viewArticlesPane;

    @FXML
    private Pane recArticlesPane;

    @FXML
    private Pane exploreTopicsPane;

    @FXML
    private VBox headlines;

    @FXML
    private TextArea texts;

    // Action Handlers
    @FXML
    public void viewArticle() {
        showPane(viewArticlesPane);
    }

    @FXML
    public void recArticle() {
        showPane(recArticlesPane);
    }

    @FXML
    public void exploreTopic() {
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

    private void showPane(Pane paneToShow) {
        // Hide all panes first
        viewArticlesPane.setVisible(false);
        recArticlesPane.setVisible(false);
        exploreTopicsPane.setVisible(false);

        // Show the specified pane
        paneToShow.setVisible(true);
    }

    @FXML
    public void handleLike() {
        System.out.println("Article liked");
    }

    @FXML
    public void handleUnlike() {
        System.out.println("Article unliked");
    }

    @FXML
    public void handleNext() {
        System.out.println("Next article displayed");
    }

    @FXML
    public void handleDropdownSelection() {
        // Handle menu item selections if required
        System.out.println("Dropdown option selected");
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

    private void showAlert(String title, String message) {
        // Display a simple error alert
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
