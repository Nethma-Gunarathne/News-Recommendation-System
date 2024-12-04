package org.example.newsrecommendationsystem.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.newsrecommendationsystem.article.Article;
import org.example.newsrecommendationsystem.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class dbManager {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017/";
    private static final String DATABASE_NAME = "news_recommender";

    private static MongoClient mongoClient; // MongoDB client instance
    private static MongoDatabase database;  // MongoDB database instance

    // Static block to initialize MongoDB connection
    static {
        try {
            mongoClient = MongoClients.create(CONNECTION_STRING);
            database = mongoClient.getDatabase(DATABASE_NAME);
            System.out.println("Connected to database: " + DATABASE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database");
        }
    }

    /**
     * Provides the MongoDatabase instance for database operations.
     *
     * @return MongoDatabase instance for the configured database.
     */
    public static MongoDatabase getDatabase() {
        if (database == null) {
            throw new RuntimeException("Database connection not initialized");
        }
        return database;
    }

    public List<Article> getArticlesByCategories(List<String> categories) {
        // Example query: SELECT * FROM articles WHERE category IN (?, ?, ?)
        List<Article> articles = new ArrayList<>();

        // Loop through categories and fetch articles (or use a single SQL query)
        for (String category : categories) {
            articles.addAll(getArticlesByCategory(category));
        }

        return articles;
    }

    // Fetch user preferences from the database
    public Map<String, Integer> getUserPreferences(String userId) {
        Map<String, Integer> preferences = new HashMap<>();

        try {
            MongoCollection<Document> usersCollection = database.getCollection("preferences");
            Document userDoc = usersCollection.find(new Document("userId", userId)).first();

            if (userDoc != null && userDoc.containsKey("preferences")) {
                Document preferencesDoc = userDoc.get("preferences", Document.class);
                for (Map.Entry<String, Object> entry : preferencesDoc.entrySet()) {
                    preferences.put(entry.getKey(), (Integer) entry.getValue());
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching user preferences: " + e.getMessage());
            e.printStackTrace();
        }

        return preferences;
    }

    // Update user preferences in the database
    public void updateUserPreferences(String userId, Map<String, Integer> preferences) {
        try {
            MongoCollection<Document> usersCollection = database.getCollection("preferences");

            // Prepare the preference document
            Document preferencesDoc = new Document();
            for (Map.Entry<String, Integer> entry : preferences.entrySet()) {
                preferencesDoc.append(entry.getKey(), entry.getValue());
            }

            // Upsert the preferences document for the user
            Document filter = new Document("userId", userId);
            Document update = new Document("$set", new Document("preferences", preferencesDoc));
            usersCollection.updateOne(filter, update, new com.mongodb.client.model.UpdateOptions().upsert(true));
        } catch (Exception e) {
            System.err.println("Error updating user preferences: " + e.getMessage());
            e.printStackTrace();
        }
    }



    // Method to check if a user exists based on username and password
    public User validateUser(String userName, String password) {
        try {
            MongoCollection<Document> usersCollection = database.getCollection("users");
            Document userDoc = usersCollection.find(
                    new Document("userName", userName).append("password", password)
            ).first(); // Find the first matching user

            if (userDoc != null) {
                // Convert the MongoDB document to a User object
                return User.fromDocument(userDoc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if no user is found
    }


    // Method to check if a user exists based on username and email
    public boolean isUserExists(String userName, String email) {
        try {
            MongoCollection<Document> usersCollection = database.getCollection("users");
            long count = usersCollection.countDocuments(
                    new Document("userName", userName).append("email", email)
            );
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to save a user document to the database
    public boolean saveUser(Document userDocument) {
        try {
            MongoCollection<Document> usersCollection = database.getCollection("users");
            usersCollection.insertOne(userDocument);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Article> getArticlesByCategory(String category) {
        MongoCollection<Document> articlesCollection = database.getCollection("articles");
        List<Article> articles = new ArrayList<>();

        for (Document doc : articlesCollection.find(new Document("Category", category))) {
            articles.add(new Article(
                    doc.getObjectId("_id"),
                    doc.getString("Text"),
                    doc.getString("Category"),
                    doc.getString("Article Number")
            ));
        }

        return articles;
    }

    public List<String> getCategories() {
        MongoCollection<Document> articlesCollection = database.getCollection("articles");
        List<String> categories = new ArrayList<>();

        // Assuming articles have a "category" field
        for (Document doc : articlesCollection.find()) {
            String category = doc.getString("Category");
            if (!categories.contains(category)) {
                categories.add(category);
            }
        }

        return categories;
    }

    public User getUserDetails(ObjectId userId) {
        try {
            MongoCollection<Document> usersCollection = database.getCollection("users");
            System.out.println("Querying database with user ID: " + userId.toHexString());  // Debugging line

            // Ensure that we are using the ObjectId correctly in the query
            Document userDoc = usersCollection.find(new Document("_id", userId)).first();

            if (userDoc != null) {
                return User.fromDocument(userDoc);  // Convert document to User object
            } else {
                System.err.println("No user found with ID: " + userId.toHexString());  // Debugging line
            }
        } catch (Exception e) {
            System.err.println("Error retrieving user from database: " + e.getMessage());
            e.printStackTrace();
        }
        return null;  // Return null if no user is found
    }



    public boolean updateUserProfile(ObjectId userId, String name, String email, String password, List<String> newsPreferences, List<String> contentPreferences) {
        try {
            Document updatedData = new Document()
                    .append("userName", name)
                    .append("email", email)
                    .append("password", password)
                    .append("newsPreferences", newsPreferences)
                    .append("contentPreferences", contentPreferences);

            // Update the user document in the database
            MongoCollection<Document> collection = database.getCollection("users");
            collection.updateOne(new Document("_id", userId), new Document("$set", updatedData));

            return true; // Update successful
        } catch (Exception e) {
            System.err.println("Error updating user profile: " + e.getMessage());
            return false;
        }
    }

}



// Optional: Add more utility methods for other collections or operations as needed


