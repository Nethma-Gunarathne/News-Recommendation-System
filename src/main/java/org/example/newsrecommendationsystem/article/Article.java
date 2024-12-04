package org.example.newsrecommendationsystem.article;

import org.bson.types.ObjectId;

public class Article {

    private final ObjectId articleId; // MongoDB's _id
    private final String text;        // Article content
    private final String category;    // Category of the article
    private final String articleNumber; // Article number, treated as title

    // Constructor to initialize the Article object
    public Article(ObjectId articleId, String text, String category, String articleNumber) {
        this.articleId = articleId;
        this.text = text;
        this.category = category;
        this.articleNumber = articleNumber;
    }

    // Getter methods for the fields
    public ObjectId getArticleId() {
        return articleId;
    }

    public String getText() {
        return text;
    }

    public String getCategory() {
        return category;
    }

    public String getArticleNumber() {
        return articleNumber;
    }

    // Method to get the title of the article
    public String getTitle() {
        return articleNumber; // Assuming Article Number is the title
    }

    // Method to get the content of the article
    public String getContent() {
        return text; // Returning the Text field as content
    }

    @Override
    public String toString() {
        return "Article{" +
                "articleId=" + articleId +
                ", text='" + text + '\'' +
                ", category='" + category + '\'' +
                ", articleNumber='" + articleNumber + '\'' +
                '}';
    }
}
