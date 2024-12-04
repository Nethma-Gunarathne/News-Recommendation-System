package org.example.newsrecommendationsystem.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.newsrecommendationsystem.article.Article;
import org.example.newsrecommendationsystem.article.viewArticle;
import org.example.newsrecommendationsystem.article.recommendedArticle;

import java.io.IOException;

public class Home {

    public ImageView profile;
    public javafx.scene.control.ButtonBar ButtonBar1;
    public MenuButton dropdownButton1;
    public VBox headlinesContainer1;
    public TextArea texts1;
    @FXML
    private MenuButton dropdownButton;
    @FXML
    private Button dislikeButton, likeButton, viewArticle, recommendedArticle, Exit;
    @FXML
    private VBox headlinesContainer; // Updated name for clarity
    @FXML
    private TextArea texts;
    @FXML
    private ButtonBar ButtonBar;
    @FXML
    private StackPane contentStack;
    @FXML
    private Pane mainPane, viewArticlePane, recommendedArticlePane;

    private viewArticle viewArticleClass;



    @FXML
    private void initialize() {
        mainPane.setVisible(true);
        viewArticlePane.setVisible(false);
        recommendedArticlePane.setVisible(false);

        String userId = "current_user_id"; // Replace with actual logic to retrieve userId
        viewArticleClass = new viewArticle(viewArticlePane, dropdownButton, headlinesContainer, texts, userId);
        viewArticleClass.loadCategories();
    }

    @FXML
    private void viewArticle(ActionEvent event) {
        // Switch to the View Articles pane
        mainPane.setVisible(false);
        viewArticlePane.setVisible(true);
        recommendedArticlePane.setVisible(false);

        // Clear previous content
        headlinesContainer.getChildren().clear();
        texts.clear();

        // Reload categories and articles into the view
        viewArticleClass.loadCategories();
    }

    @FXML
    private void recommendedArticle(ActionEvent event) {
        // Switch to the Recommend Articles pane
        mainPane.setVisible(false);
        viewArticlePane.setVisible(false);
        recommendedArticlePane.setVisible(true);

        // Clear previous content
        headlinesContainer1.getChildren().clear();
        texts1.clear();

        // Get the userId from the session or other means
        String userId = String.valueOf(Session.getCurrentUser().getUserId());

        // Instantiate recommendedArticle with required parameters
        recommendedArticle recommendedArticleClass = new recommendedArticle(recommendedArticlePane, dropdownButton1, headlinesContainer1, texts1, userId);

        // Load recommended articles into the view
        // recommendedArticleClass.loadRecommendedArticles();
    }




    // Navigate to Profile page
    @FXML
    private void navigateToProfile() {
        System.out.println("Profile image clicked! Navigating...");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/newsrecommendationsystem/Profile.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) profile.getScene().getWindow(); // Get the current window
            stage.setScene(scene); // Set the new scene
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void exit(ActionEvent event) {
        // Confirm exit with a popup dialog
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("Press Yes to exit or No to stay.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.out.println("Exiting the program...");
                Stage stage = (Stage) Exit.getScene().getWindow();
                stage.close(); // Close the application
            }
        });
    }


    @FXML
    private void like(ActionEvent actionEvent) {
        // Check if an article is selected
        if (viewArticleClass.getSelectedArticle() != null) {
            // Proceed with liking the article
            String category = viewArticleClass.getSelectedArticle().getCategory();
            viewArticleClass.updateCategoryPreference(category, 1);  // Add a point to the selected category
            System.out.println("Liked article in category: " + category);

            // Disable like and dislike buttons after interaction

        } else {
            // Show alert if no article is selected
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No Article Selected");
            alert.setHeaderText("Please select an article before liking.");
            alert.showAndWait();
        }
    }

    @FXML
    private void dislike(ActionEvent actionEvent) {
        // Check if an article is selected
        if (viewArticleClass.getSelectedArticle() != null) {
            // Proceed with disliking the article
            String category = viewArticleClass.getSelectedArticle().getCategory();
            viewArticleClass.updateCategoryPreference(category, -1);  // Deduct a point from the selected category
            System.out.println("Disliked article in category: " + category);

            // Disable like and dislike buttons after interaction
        } else {
            // Show alert if no article is selected
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No Article Selected");
            alert.setHeaderText("Please select an article before disliking.");
            alert.showAndWait();
        }
    }




}
