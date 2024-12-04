package org.example.newsrecommendationsystem.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.newsrecommendationsystem.database.dbManager;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Profile {

    public Button logout;
    @FXML
    private ImageView profileImage;

    @FXML
    private TextField userEmail;

    @FXML
    private TextField userName;

    @FXML
    private TextField userPassword;

    @FXML
    private TextArea userPreferences;

    @FXML
    private Button backButton;

    @FXML
    private Button saveButton;

    // Initialize method to load user data
    @FXML
    public void initialize() {
        loadUserProfile();
    }

    private void loadUserProfile() {
        try {
            // Retrieve the current user from session
            User currentUser = Session.getCurrentUser();

            if (currentUser == null) {
                System.err.println("No user is logged in.");
                return;
            }

            ObjectId userId = currentUser.getUserId(); // Fetch the logged-in user's ID
            System.out.println("Fetching profile for user with ID: " + userId);  // Debugging line

            dbManager database = new dbManager();
            User user = database.getUserDetails(userId); // Fetch user details by ObjectId

            // Debugging: Print the fetched user details
            if (user != null) {

                // Set the fields with user data
                userName.setText(user.getUserName());
                userEmail.setText(user.getEmail());
                userPassword.setText(user.getPassword());

                // Combine preferences into a single text for display
                StringBuilder preferencesBuilder = new StringBuilder();
                List<String> newsPrefs = user.getNewsPreferences();
                List<String> contentPrefs = user.getContentPreferences();

                if (newsPrefs != null) {
                    preferencesBuilder.append("News Preferences:\n");
                    newsPrefs.forEach(pref -> preferencesBuilder.append("- ").append(pref).append("\n"));
                }
                if (contentPrefs != null) {
                    preferencesBuilder.append("Content Preferences:\n");
                    contentPrefs.forEach(pref -> preferencesBuilder.append("- ").append(pref).append("\n"));
                }

                userPreferences.setText(preferencesBuilder.toString());

                // Load profile image (set a placeholder path or URL if not available)
                Image image = new Image("file:src/main/resources/images/profile.jpg");
                profileImage.setImage(image);
            } else {
                System.err.println("User not found.");
            }
        } catch (Exception e) {
            System.err.println("Error loading user profile: " + e.getMessage());
            e.printStackTrace();  // Print stack trace for more details
        }
    }


    @FXML
    private void handleBackButton() {
        // Close current window or navigate back to the previous scene
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close(); // Assuming closing the current profile window is the intended behavior
    }

    @FXML
    private void handleSaveButton() {
        try {
            // Save updated data to the database
            String updatedName = userName.getText();
            String updatedEmail = userEmail.getText();
            String updatedPassword = userPassword.getText();

            // Parse preferences from TextArea (split by newline and categorize)
            String[] preferences = userPreferences.getText().split("\n");
            List<String> updatedNewsPreferences = new ArrayList<>();
            List<String> updatedContentPreferences = new ArrayList<>();

            boolean isNewsSection = false, isContentSection = false;
            for (String line : preferences) {
                line = line.trim();
                if (line.equalsIgnoreCase("News Preferences:")) {
                    isNewsSection = true;
                    isContentSection = false;
                } else if (line.equalsIgnoreCase("Content Preferences:")) {
                    isNewsSection = false;
                    isContentSection = true;
                } else if (!line.isEmpty()) {
                    if (isNewsSection) {
                        updatedNewsPreferences.add(line.substring(2).trim());
                    } else if (isContentSection) {
                        updatedContentPreferences.add(line.substring(2).trim());
                    }
                }
            }

            // Retrieve the current user from session
            User currentUser = Session.getCurrentUser();

            if (currentUser == null) {
                System.err.println("No user is logged in.");
                return;
            }

            ObjectId userId = currentUser.getUserId(); // Fetch the logged-in user's ID
            dbManager database = new dbManager();

            boolean success = database.updateUserProfile(
                    userId, updatedName, updatedEmail, updatedPassword, updatedNewsPreferences, updatedContentPreferences
            );

            if (success) {
                System.out.println("User profile updated successfully.");
            } else {
                System.err.println("Failed to update user profile.");
            }
        } catch (Exception e) {
            System.err.println("Error saving user profile: " + e.getMessage());
        }
    }


    // Navigate back to Home page
    @FXML
    private void navigateToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/newsrecommendationsystem/Home.fxml"));
            Scene scene = new Scene(loader.load());

            // Get the current stage from any control in the current scene (e.g., a Button)
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene); // Set the new scene
            stage.show(); // Ensure the stage is displayed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/newsrecommendationsystem/Login.fxml"));
            Scene scene = new Scene(loader.load());

            // Get the current stage from any control in the current scene (e.g., a Button)
            Stage stage = (Stage) logout.getScene().getWindow();
            stage.setScene(scene); // Set the new scene
            stage.show(); // Ensure the stage is displayed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

