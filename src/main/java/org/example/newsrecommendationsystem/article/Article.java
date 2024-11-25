package org.example.newsrecommendationsystem.article;

public class Article {
    private String id;
    private String text;
    private String category;
    private String articleNumber;

    public Article(String id, String text, String category, String articleNumber) {
        this.id = id;
        this.text = text;
        this.category = category;
        this.articleNumber = articleNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
    }

    @Override
    public String toString() {
        return articleNumber + ": " + category;
    }
}
