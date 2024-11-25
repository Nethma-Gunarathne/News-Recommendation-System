package org.example.newsrecommendationsystem.user;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


    @FXML
    private void initialize() {
        // Initialize the database connection
        org.example.newsrecommendationsystem.Database.initDatabase();

        // Fetch categories from the database
        Set<String> categories = fetchCategoriesFromDatabase();

        // Populate dropdown menu with categories
        populateDropdown(categories);
    }

    private Set<String> fetchCategoriesFromDatabase() {
        Set<String> categories = new HashSet<>();
        try {
            // Get the database instance
            MongoDatabase database = org.example.newsrecommendationsystem.Database.getDatabase();

            // Access the "articles" collection
            MongoCollection<Document> articlesCollection = database.getCollection("articles");

            // Fetch categories
            for (Document document : articlesCollection.find()) {
                // Assuming each document has a "category" field
                String category = document.getString("Category");
                if (category != null) {
                    categories.add(category);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch categories from the database");
        }
        return categories;
    }

    private void populateDropdown(Set<String> categories) {
        try {
            dropdownButton.getItems().clear(); // Clear existing items, if any

            for (String category : categories) {
                MenuItem menuItem = new MenuItem(category);

                // Add an action handler for the menu item
                menuItem.setOnAction(event -> handleCategorySelection(category));

                // Add the menu item to the dropdown button
                dropdownButton.getItems().add(menuItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to populate dropdown with categories");
        }
    }

    private void handleCategorySelection(String category) {
        System.out.println("Selected category: " + category);

        // Fetch headlines for the selected category
        List<String> headlinesList = fetchHeadlinesByCategory(category);

        // Display headlines in the VBox
        displayHeadlines(headlinesList);
    }


    private List<String> fetchHeadlinesByCategory(String category) {
        List<String> headlines = new ArrayList<>();
        try {
            // Get the database instance
            MongoDatabase database = org.example.newsrecommendationsystem.Database.getDatabase();

            // Access the "articles" collection
            MongoCollection<Document> articlesCollection = database.getCollection("articles");

            // Query the collection for the selected category
            for (Document document : articlesCollection.find(new Document("Category", category))) {
                // Assuming each document has an "Article Number" field (the headline)
                String headline = document.getString("Article Number");
                if (headline != null) {
                    headlines.add(headline);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch headlines from the database");
        }
        return headlines;
    }

    private void displayHeadlines(List<String> headlinesList) {
        headlines.getChildren().clear(); // Clear existing headlines

        for (String headline : headlinesList) {
            // Create a Text node for each headline
            Text headlineText = new Text(headline);
            headlineText.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");

            // Set the headline as clickable
            headlineText.setOnMouseClicked(event -> handleHeadlineClick(headline));

            // Add the Text node to the VBox
            headlines.getChildren().add(headlineText);
        }
    }

    // Fetch the article text for the selected headline
    private String fetchArticleTextByHeadline(String headline) {
        String articleText = "";
        try {
            // Get the database instance
            MongoDatabase database = org.example.newsrecommendationsystem.Database.getDatabase();

            // Access the "articles" collection
            MongoCollection<Document> articlesCollection = database.getCollection("articles");

            // Query the collection for the selected headline
            Document articleDocument = articlesCollection.find(new Document("Article Number", headline)).first();

            if (articleDocument != null) {
                // Assuming the article text is stored in the "Text" field
                articleText = articleDocument.getString("Text");

                // If needed, insert line breaks where appropriate
                // Example: Replace multiple spaces with a newline (if the text is too compact in the database)
                articleText = articleText.replaceAll("\\.\\s", ".\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch article text from the database");
        }
        return articleText;
    }

    private void handleHeadlineClick(String headline) {
        // Fetch the full text for the clicked headline
        String articleText = fetchArticleTextByHeadline(headline);

        // Display the text in the TextArea, ensuring that line breaks are preserved
        texts.setText(articleText);
    }

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
