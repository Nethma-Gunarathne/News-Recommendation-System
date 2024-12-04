package org.example.newsrecommendationsystem.user;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

public class User {
    private ObjectId userId;
    private String userName;
    private String email;
    private String password;
    private List<String> newsPreferences;
    private List<String> contentPreferences;

    public User(String userName, String email, String password, List<String> newsPreferences, List<String> contentPreferences) {
        this.userId = new ObjectId(); // Generate a unique ID
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.newsPreferences = newsPreferences;
        this.contentPreferences = contentPreferences;
    }

    // Constructor for users loaded from the database (with ObjectId)
    public User(ObjectId userId, String userName, String email, String password, List<String> newsPreferences, List<String> contentPreferences) {
        this.userId = userId; // Use the ObjectId from MongoDB
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.newsPreferences = newsPreferences;
        this.contentPreferences = contentPreferences;
    }

    // Getters and Setters
    public ObjectId getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getNewsPreferences() {
        return newsPreferences;
    }

    public void setNewsPreferences(List<String> newsPreferences) {
        this.newsPreferences = newsPreferences;
    }

    public List<String> getContentPreferences() {
        return contentPreferences;
    }

    public void setContentPreferences(List<String> contentPreferences) {
        this.contentPreferences = contentPreferences;
    }

    // Convert User object to MongoDB document
    public Document toDocument() {
        return new Document("_id", userId)
                .append("userName", userName)
                .append("email", email)
                .append("password", password)
                .append("newsPreferences", newsPreferences)
                .append("contentPreferences", contentPreferences);
    }

    // Convert MongoDB document to User object
    public static User fromDocument(Document document) {
        ObjectId userId = document.getObjectId("_id");
        String userName = document.getString("userName");
        String email = document.getString("email");
        String password = document.getString("password");
        List<String> newsPreferences = document.getList("newsPreferences", String.class);
        List<String> contentPreferences = document.getList("contentPreferences", String.class);

        return new User(userId, userName, email, password, newsPreferences, contentPreferences); // Use the constructor with ObjectId
    }
}
