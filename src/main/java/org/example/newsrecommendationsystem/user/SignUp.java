package org.example.newsrecommendationsystem.user;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;
import org.example.newsrecommendationsystem.database.dbManager;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
    private Button signUpButton;
    @FXML
    private Text loginLink;

    private final dbManager dbManager = new dbManager();

    @FXML
    private void initialize() {
        // Initialize CheckMenuItem options for news categories
        addCheckMenuItems(newsCategoriesMenu, new String[] {
                "Technology", "Health", "Sports", "AI and Machine Learning", "Politics", "Business",
                "Science", "Environment", "Entertainment", "World News"});

        // Initialize CheckMenuItem options for content type preferences
        addCheckMenuItems(contentTypePreferencesMenu, new String[] {
                "Breaking News", "Opinion Pieces", "Analysis/Research", "Editorials", "Local News"});
    }

    // Helper method to add CheckMenuItems to a MenuButton
    private void addCheckMenuItems(MenuButton menuButton, String[] items) {
        for (String item : items) {
            CheckMenuItem checkMenuItem = new CheckMenuItem(item);
            menuButton.getItems().add(checkMenuItem);
        }
    }

    @FXML
    private void signUpButton() {
        if (!validateAllFields()) {
            return;
        }

        // Add user credentials to the database
        saveUserToDatabase();
        clearFields();

        // Navigate to the login page after successful registration
        navigateToPage("Login.fxml", "Login");
    }

    @FXML
    private void loginLink(MouseEvent event) {
        navigateToPage("Login.fxml", "Login");
    }

    // Validate all fields
    private boolean validateAllFields() {
        String userNameInput = userName.getText().trim();
        String emailInput = email.getText().trim();
        String passwordInput = password.getText().trim();
        String confirmPasswordInput = confirmPassword.getText().trim();

        // Username validation
        if (!validateUserName(userNameInput)) {
            showAlert(AlertType.ERROR, "Invalid Username", "Username must contain only non-numerical characters.");
            return false;
        }

        // Email validation
        if (!validateEmail(emailInput)) {
            showAlert(AlertType.ERROR, "Invalid Email", "Please provide a valid email address.");
            return false;
        }

        // Password validation
        if (!validatePassword(passwordInput)) {
            showAlert(AlertType.ERROR, "Invalid Password", "Password must be at least 4 characters long.");
            return false;
        }

        // Confirm password validation
        if (!passwordInput.equals(confirmPasswordInput)) {
            showAlert(AlertType.ERROR, "Password Mismatch", "Passwords do not match.");
            return false;
        }

        // Get selected preferences
        List<String> selectedNewsCategories = getSelectedItems(newsCategoriesMenu);
        List<String> selectedContentPreferences = getSelectedItems(contentTypePreferencesMenu);

        // Validate that at least 2 preferences are selected
        if (selectedNewsCategories.size() + selectedContentPreferences.size() < 2) {
            showAlert(AlertType.ERROR, "Insufficient Preferences", "You must select at least 2 preferences.");
            return false;
        }

        return true;
    }

    // Helper method to save user to the database
    private void saveUserToDatabase() {
        String userNameInput = userName.getText().trim();
        String emailInput = email.getText().trim();
        String passwordInput = password.getText().trim();

        List<String> selectedNewsCategories = getSelectedItems(newsCategoriesMenu);
        List<String> selectedContentPreferences = getSelectedItems(contentTypePreferencesMenu);

        User user = new User(userNameInput, emailInput, passwordInput, selectedNewsCategories, selectedContentPreferences);

        if (dbManager.isUserExists(user.getUserName(), user.getEmail())) {
            showAlert(AlertType.ERROR, "Duplicate User", "A user with this username or email already exists.");
            return;
        }

        if (!dbManager.saveUser(user.toDocument())) {
            showAlert(AlertType.ERROR, "Database Error", "Failed to save user. Try again.");
        } else {
            showAlert(AlertType.INFORMATION, "Success", "User registered successfully!");
        }
    }

    // Helper method to get selected items from the MenuButton
    private List<String> getSelectedItems(MenuButton menuButton) {
        return menuButton.getItems().stream()
                .filter(item -> item instanceof CheckMenuItem && ((CheckMenuItem) item).isSelected())
                .map(MenuItem::getText)
                .toList();
    }

    // Validation methods for username, email, and password
    private boolean validateUserName(String userName) {
        return userName.matches("^[a-zA-Z]+$");
    }

    private boolean validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean validatePassword(String password) {
        return password.length() >= 4;
    }

    private void navigateToPage(String fxmlFile, String pageTitle) {
        try {
            System.out.println("Attempting to load FXML: " + fxmlFile);  // Debugging statement
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/newsrecommendationsystem/" + fxmlFile));

            if (loader.getLocation() == null) {
                System.out.println("Error: FXML file not found - " + fxmlFile);
            }

            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) loginLink.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(pageTitle);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading FXML: " + fxmlFile);
        }
    }



    // Show alert message
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Clear all input fields and selected preferences
    private void clearFields() {
        userName.clear();
        email.clear();
        password.clear();
        confirmPassword.clear();
        newsCategoriesMenu.getItems().forEach(item -> ((CheckMenuItem) item).setSelected(false));
        contentTypePreferencesMenu.getItems().forEach(item -> ((CheckMenuItem) item).setSelected(false));
    }
}
