package org.example.newsrecommendationsystem.article;

import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.example.newsrecommendationsystem.database.dbManager;
import org.example.newsrecommendationsystem.user.Session;

import java.util.List;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class viewArticle {

    private Article selectedArticle;  // Add this field to store the selected article

    private Button likeButton;
    private Button dislikeButton;
    private final Pane viewArticlePane;       // Pane to display content
    private final MenuButton dropdownButton;  // Dropdown for categories
    private final VBox headlinesContainer;    // Container for article headlines
    private final TextArea texts;             // Area for displaying article content
    private String selectedCategory;          // To store the currently selected category

    private final String userId;              // User identifier
    private Map<String, Integer> preferences; // User preference mapping


    public viewArticle(Pane viewArticlePane, MenuButton dropdownButton, VBox headlinesContainer, TextArea texts, String userId) {
        this.viewArticlePane = viewArticlePane;
        this.dropdownButton = dropdownButton;
        this.headlinesContainer = headlinesContainer;
        this.texts = texts;
        this.userId = userId;
        this.preferences = new HashMap<>(); // Initialize preferences map
    }

    // Optional: Default constructor for cases without userId
    public viewArticle(Pane viewArticlePane, MenuButton dropdownButton, VBox headlinesContainer, TextArea texts) {
        this(viewArticlePane, dropdownButton, headlinesContainer, texts, null); // Call the main constructor with null userId
    }

    // Load categories into the dropdown menu
    public void loadCategories() {
        dropdownButton.getItems().clear();  // Clear existing dropdown items

        dbManager db = new dbManager();
        List<String> categories = db.getCategories();  // Fetch categories

        for (String category : categories) {
            MenuItem menuItem = new MenuItem(category);
            menuItem.setOnAction(event -> {
                selectedCategory = category;  // Store selected category
                updateCategoryPreference(category, 1); // Add point to the selected category
                clearPreviousArticles();  // Clear articles from the previous selection
                clearArticleContent();    // Clear the article content
                loadArticlesByCategory(category);  // Load articles for the selected category
            });
            dropdownButton.getItems().add(menuItem);
        }
    }

    // Update the preference for a category
    public void updateCategoryPreference(String category, int points) {
        dbManager db = new dbManager();

        // Retrieve userId from the current session
        String userId = String.valueOf(Session.getCurrentUser().getUserId());

        // Retrieve current preferences if not loaded
        if (preferences.isEmpty()) {
            preferences = db.getUserPreferences(userId);
        }

        // Update the preference score
        preferences.put(category, preferences.getOrDefault(category, 0) + points);

        // Save updated preferences to the database
        db.updateUserPreferences(userId, preferences);
    }

    // Handle category selection
    private void handleCategorySelection(String category) {
        clearPreviousArticles();  // Clear previous articles
        clearArticleContent();    // Clear the article content
        loadArticlesByCategory(category);  // Load articles for the selected category
    }

    // Clear the article content
    private void clearArticleContent() {
        texts.clear();
        texts.setPromptText("Select an article to view its content.");  // Add a helpful prompt
    }

    // Clear previous articles from the view
    private void clearPreviousArticles() {
        headlinesContainer.getChildren().clear();
    }

    // Load articles by category and display them
    private void loadArticlesByCategory(String category) {
        dbManager db = new dbManager();
        List<Article> articles = db.getArticlesByCategory(category);

        clearPreviousArticles();  // Ensure the container is clean

        for (Article article : articles) {
            Button headlineButton = new Button(article.getTitle());  // Display article title
            headlineButton.setOnAction(event -> displayArticleContent(article));  // Show content on click
            headlinesContainer.getChildren().add(headlineButton);  // Add the button to the container
        }
    }


    // Method to display the content of the selected article
    // Method to display the content of the selected article
    private void displayArticleContent(Article article) {
        this.selectedArticle = article;  // Store the selected article

        // Clear the previous content from the TextArea
        texts.clear();

        // Format the article content for better readability (remove <br> and <p> tags)
        String formattedContent = article.getContent()
                .replaceAll("\\<br\\>", "\n")  // Replace <br> tags with newlines
                .replaceAll("\\<p\\>", "\n")   // Replace <p> tags with newlines
                .replaceAll("\\</p\\>", "");   // Remove closing </p> tags

        // Set the formatted content into the TextArea
        texts.setText(formattedContent);

        // Enable word wrapping for better readability
        texts.setWrapText(true);

        // Enable the like and dislike buttons because a new article is selected
        likeButton.setDisable(false);
        dislikeButton.setDisable(false);

        // Optionally, print a debug statement to ensure the content is correctly set
        System.out.println("Displaying content for article: " + article.getTitle());
    }

    // Display the selected article's content
    public void displayArticleContent(Article article, Button likeButton, Button dislikeButton) {
        this.selectedArticle = article;  // Store the selected article
        texts.clear();  // Clear the previous content

        // Ensure content is formatted properly for paragraphs
        String formattedContent = article.getContent()
                .replaceAll("\\<br\\>", "\n")        // Replace <br> tags with newlines
                .replaceAll("\\<p\\>", "\n")         // Replace <p> tags with newlines
                .replaceAll("\\</p\\>", "");         // Remove closing <p> tags

        texts.setText(formattedContent);  // Set the formatted content
        texts.setWrapText(true);          // Enable word wrapping for readability

        // Enable like and dislike buttons since a new article is selected
        likeButton.setDisable(false);
        dislikeButton.setDisable(false);
    }


    public Article getSelectedArticle() {
        return selectedArticle;
    }

}

