package org.example.newsrecommendationsystem.article;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.example.newsrecommendationsystem.database.dbManager;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class recommendedArticle {

    private final Pane recommendedArticlePane;
    private MenuButton dropdownButton1; // ComboBox for categories
    private VBox headlinesContainer1;   // ScrollPane for article headlines
    private TextArea texts1;            // TextArea to display the article content
    private final String userId;       // User identifier
    private Map<String, Integer> preferences; // User preference mapping
    private Article selectedArticle;   // To store the currently selected article

    // ExecutorService for managing threads
    private final ExecutorService executorService;

    public recommendedArticle(Pane recommendedArticlePane, MenuButton dropdownButton1, VBox headlinesContainer1, TextArea texts1, String userId) {
        this.recommendedArticlePane = recommendedArticlePane;
        this.headlinesContainer1 = headlinesContainer1;
        this.texts1 = texts1;
        this.userId = userId;
        this.preferences = new HashMap<>();
        this.dropdownButton1 = dropdownButton1;

        // Initialize ExecutorService with a cached thread pool for flexibility
        this.executorService = Executors.newCachedThreadPool();

        // Load categories when the class is initialized
        loadCategories();
    }

    // Method to load categories asynchronously
    public void loadCategories() {
        // Create a Task to fetch categories in a background thread
        Task<Void> loadTask = new Task<>() {
            @Override
            protected Void call() {
                dbManager db = new dbManager();
                // Fetch user preferences from the database
                preferences = db.getUserPreferences(userId);
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                // Update UI with the fetched categories after task completes
                updateCategories();
            }

            @Override
            protected void failed() {
                super.failed();
                // Log the exception if the task fails
                System.out.println("Failed to load categories: " + getException());
            }
        };

        // Submit the task to the ExecutorService for execution in a separate thread
        executorService.submit(loadTask);
    }

    // Update the UI with the fetched categories
    private void updateCategories() {
        // Ensure UI updates are run on the JavaFX Application Thread
        Platform.runLater(() -> {
            // Sort preferences by score in descending order
            List<Map.Entry<String, Integer>> sortedPreferences = new ArrayList<>(preferences.entrySet());
            sortedPreferences.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

            // Fetch top 3 categories
            List<String> topCategories = new ArrayList<>();
            for (int i = 0; i < Math.min(3, sortedPreferences.size()); i++) {
                topCategories.add(sortedPreferences.get(i).getKey());
            }

            // Clear existing items in the dropdown and add new categories
            dropdownButton1.getItems().clear();
            for (String category : topCategories) {
                MenuItem item = new MenuItem(category);
                // Set action to load articles when a category is selected
                item.setOnAction(event -> loadArticlesByCategory(category));
                dropdownButton1.getItems().add(item);
            }
        });
    }

    // Method to load articles asynchronously based on the selected category
    private void loadArticlesByCategory(String category) {
        // Create a Task to fetch articles in a background thread
        Task<List<Article>> loadArticlesTask = new Task<>() {
            @Override
            protected List<Article> call() {
                dbManager db = new dbManager();
                // Fetch articles for the selected category from the database
                return db.getArticlesByCategory(category);
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                // Update UI with the fetched articles after task completes
                updateArticles(getValue());
            }

            @Override
            protected void failed() {
                super.failed();
                // Log the exception if the task fails
                System.out.println("Failed to load articles: " + getException());
            }
        };

        // Submit the task to the ExecutorService for execution in a separate thread
        executorService.submit(loadArticlesTask);
    }

    // Update the UI with the fetched articles
    private void updateArticles(List<Article> articles) {
        // Ensure UI updates are run on the JavaFX Application Thread
        Platform.runLater(() -> {
            // Clear previous article headlines
            headlinesContainer1.getChildren().clear();
            for (Article article : articles) {
                // Create a button for each article
                Button headlineButton = new Button(article.getTitle());
                // Set action to display article content when the button is clicked
                headlineButton.setOnAction(event -> displayArticleContent(article));
                // Add the button to the container
                headlinesContainer1.getChildren().add(headlineButton);
            }
        });
    }

    // Method to display the content of the selected article
    private void displayArticleContent(Article article) {
        this.selectedArticle = article;  // Store the selected article
        // Update the TextArea content on the JavaFX Application Thread
        Platform.runLater(() -> {
            if (article.getContent() == null || article.getContent().isEmpty()) {
                texts1.setText("No content available.");
                return;
            }
            texts1.setText(article.getContent().replaceAll("<br>", "\n"));
            texts1.setWrapText(true);
        });
    }

    // Getter for the selected article
    public Article getSelectedArticle() {
        return selectedArticle;
    }
}
