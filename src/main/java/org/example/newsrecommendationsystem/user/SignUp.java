package org.example.newsrecommendationsystem.user;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.bson.Document;
import org.example.newsrecommendationsystem.Database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SignUp {

    @FXML
    private TextField userName;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private MenuButton newsCategoriesMenu;

    @FXML
    private MenuButton contentTypePreferencesMenu;

    @FXML
    private Button signInButton;

    @FXML
    private Text loginLink;

    private final Pattern emailPattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    @FXML
    private void initialize() {
        // Initialize CheckMenuItem options for news categories
        addCheckMenuItems(newsCategoriesMenu, new String[]{
                "Technology", "Health", "Sports", "AI and Machine Learning", "Politics", "Business",
                "Science", "Environment", "Entertainment", "World News"});

        // Initialize CheckMenuItem options for content type preferences
        addCheckMenuItems(contentTypePreferencesMenu, new String[]{
                "Breaking News", "Opinion Pieces", "Analysis/Research", "Editorials", "Local News"});
    }

    private void addCheckMenuItems(MenuButton menuButton, String[] items) {
        for (String item : items) {
            CheckMenuItem checkMenuItem = new CheckMenuItem(item);
            menuButton.getItems().add(checkMenuItem);
        }
    }

    @FXML
    private void signInButton() {
        if (!validateAllFields()) {
            return;
        }

        // Add user credentials to the database
        saveUserToDatabase();
        clearFields();

        // Navigate to the login page after successful registration
        navigateToPage("Login.fxml", "Login");
    }

    private boolean validateAllFields() {
        // Check for empty fields first
        if (userName.getText().isEmpty() || email.getText().isEmpty() ||
                password.getText().isEmpty() || confirmPassword.getText().isEmpty()) {
            showAlert("Incomplete Fields", "Please fill all the fields.");
            return false;
        }

        if (!userName.getText().matches("[a-zA-Z]+")) {
            showAlert("Invalid Username", "Username should not contain numbers or special characters.");
            return false;
        }

        if (!emailPattern.matcher(email.getText()).matches()) {
            showAlert("Invalid Email", "Please enter a valid email address.");
            return false;
        }

        if (password.getText().length() < 5) {
            showAlert("Invalid Password", "Password must be at least 5 characters long.");
            return false;
        }

        if (!password.getText().equals(confirmPassword.getText())) {
            showAlert("Password Mismatch", "Confirm password must match the password.");
            return false;
        }

        if (getSelectedCheckMenuItemCount(newsCategoriesMenu) < 1) {
            showAlert("Selection Error", "Please select at least 1 news category.");
            return false;
        }

        if (getSelectedCheckMenuItemCount(contentTypePreferencesMenu) < 1) {
            showAlert("Selection Error", "Please select at least 1 content type preference.");
            return false;
        }

        return true;
    }

    private int getSelectedCheckMenuItemCount(MenuButton menuButton) {
        int count = 0;
        for (MenuItem item : menuButton.getItems()) {
            if (item instanceof CheckMenuItem) {
                CheckMenuItem checkMenuItem = (CheckMenuItem) item;
                if (checkMenuItem.isSelected()) {
                    count++;
                }
            }
        }
        return count;
    }

    private void saveUserToDatabase() {
        try {
            MongoDatabase database = Database.getDatabase();
            MongoCollection<Document> usersCollection = database.getCollection("users");

            if (isUserExists(usersCollection, userName.getText(), email.getText())) {
                showAlert("Duplicate User", "A user with the same username or email already exists.");
                return;
            }

            Document newUser = new Document("userName", userName.getText())
                    .append("email", email.getText())
                    .append("password", password.getText())
                    .append("newsCategories", getSelectedCheckMenuItems(newsCategoriesMenu))
                    .append("contentPreferences", getSelectedCheckMenuItems(contentTypePreferencesMenu));

            usersCollection.insertOne(newUser);
            System.out.println("User successfully added to the database.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to save user to the database.");
        }
    }

    private List<String> getSelectedCheckMenuItems(MenuButton menuButton) {
        List<String> selectedItems = new ArrayList<>();
        for (MenuItem item : menuButton.getItems()) {
            if (item instanceof CheckMenuItem && ((CheckMenuItem) item).isSelected()) {
                selectedItems.add(item.getText());
            }
        }
        return selectedItems;
    }

    private void clearFields() {
        userName.clear();
        email.clear();
        password.clear();
        confirmPassword.clear();

        for (MenuItem item : newsCategoriesMenu.getItems()) {
            if (item instanceof CheckMenuItem) {
                ((CheckMenuItem) item).setSelected(false);
            }
        }

        for (MenuItem item : contentTypePreferencesMenu.getItems()) {
            if (item instanceof CheckMenuItem) {
                ((CheckMenuItem) item).setSelected(false);
            }
        }
    }

    @FXML
    private void loginLink() {
        navigateToPage("Login.fxml", "Login");
    }

    private void navigateToPage(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) loginLink.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Could not load the " + title + " page.");
        }
    }

    private boolean isUserExists(MongoCollection<Document> usersCollection, String username, String email) {
        Document existingUser = usersCollection.find(
                new Document("$or", List.of(
                        new Document("userName", username),
                        new Document("email", email)
                ))
        ).first();

        return existingUser != null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
