package org.example.newsrecommendationsystem.article;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.bson.Document;
import org.bson.types.ObjectId;
import com.mongodb.client.MongoCollection;
import org.example.newsrecommendationsystem.Navigation;
import org.example.newsrecommendationsystem.database.dbManager;

import java.util.List;

public class DeleteArticle {

    @FXML
    private TableView<Article> articleTable;

    @FXML
    private TableColumn<Article, String> articleNumberColumn;

    @FXML
    private TableColumn<Article, String> categoryColumn;

    @FXML
    private TableColumn<Article, String> textColumn;

    @FXML
    private Text statusMessage;

    private final ObservableList<Article> articles = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Set up table columns
        articleNumberColumn.setCellValueFactory(new PropertyValueFactory<>("articleNumber"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        textColumn.setCellValueFactory(new PropertyValueFactory<>("text"));

        // Load articles into the table
        loadArticles();
    }

    private void loadArticles() {
        try {
            // Get the articles collection
            MongoCollection<Document> articlesCollection = dbManager.getDatabase().getCollection("articles");

            // Fetch all articles and add them to the ObservableList
            List<Document> documents = articlesCollection.find().into(new java.util.ArrayList<>());
            for (Document doc : documents) {
                Article article = new Article(
                        doc.getObjectId("_id"), // MongoDB _id
                        doc.getString("Text"),
                        doc.getString("Category"),
                        doc.getString("Article Number")
                );
                articles.add(article);
            }

            // Add articles to the TableView
            articleTable.setItems(articles);
        } catch (Exception e) {
            e.printStackTrace();
            statusMessage.setText("Error loading articles!");
        }
    }

    @FXML
    private void handleDeleteSelectedArticle() {
        // Get the selected article
        Article selectedArticle = articleTable.getSelectionModel().getSelectedItem();

        if (selectedArticle == null) {
            statusMessage.setText("Please select an article to delete.");
            return;
        }

        try {
            // Get the articles collection
            MongoCollection<Document> articlesCollection = dbManager.getDatabase().getCollection("articles");

            // Delete the selected article by its ObjectId
            ObjectId articleId = selectedArticle.getArticleId();
            articlesCollection.deleteOne(new Document("_id", articleId));

            // Remove the article from the table
            articles.remove(selectedArticle);

            statusMessage.setText("Article deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            statusMessage.setText("Error deleting the article.");
        }
    }

    public void backtomanagement(ActionEvent actionEvent) {
        Navigation.navigateToPage(actionEvent, "/org/example/newsrecommendationsystem/ManagementPage.fxml", "Admin Login");

    }
}

