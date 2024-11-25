package org.example.newsrecommendationsystem.article;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class viewArticle {
    private List<Article> articles;

    public viewArticle() {
        articles = new ArrayList<>();
        loadArticles();
    }

    private void loadArticles() {
        // Sample data; replace with actual data fetching logic
        articles.add(new Article("6742d7a48c9705be232dfc99",
                "Credit...From left: Craig Ruttle/Associated Press; Robert F. Bukaty/As…",
                "Business",
                "Article 1"));
        articles.add(new Article("6742d7a48c9705be232dfca0",
                "Tech innovations are reshaping industries.",
                "Technology",
                "Article 2"));
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void displayArticles(VBox headlines) {
        headlines.getChildren().clear();
        for (Article article : articles) {
            Label articleLabel = new Label(article.toString());
            articleLabel.setOnMouseClicked(event -> System.out.println("Selected: " + article.getText()));
            headlines.getChildren().add(articleLabel);
        }
    }
}
