package org.example.newsrecommendationsystem.article;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import org.example.newsrecommendationsystem.Navigation;
import org.example.newsrecommendationsystem.database.dbManager;

public class AddArticle {

    @FXML
    private TextField articleNumberField;

    @FXML
    private TextField categoryField;

    @FXML
    private TextArea textField;

    // Method to handle adding the article to the collection
    @FXML
    public void handleAddArticle() {
        try {
            // Retrieve user inputs
            String articleNumber = articleNumberField.getText();
            String category = categoryField.getText();
            String text = textField.getText();

            // Validate inputs
            if (articleNumber.isEmpty() || category.isEmpty() || text.isEmpty()) {
                System.out.println("All fields must be filled!");
                return;
            }

            // Create a MongoDB document with user-provided data
            Document articleDoc = new Document("Text", text)
                    .append("Category", category)
                    .append("Article Number", articleNumber);

            // Get the articles collection from the dbManager
            MongoCollection<Document> articlesCollection = dbManager.getDatabase().getCollection("articles");

            // Insert the document into the collection
            articlesCollection.insertOne(articleDoc);

            System.out.println("Article added successfully!");
        } catch (Exception e) {
            System.err.println("Error while adding article: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void Backtomanagement(ActionEvent actionEvent) {
        Navigation.navigateToPage(actionEvent, "/org/example/newsrecommendationsystem/ManagementPage.fxml", "Admin Login");

    }
}
