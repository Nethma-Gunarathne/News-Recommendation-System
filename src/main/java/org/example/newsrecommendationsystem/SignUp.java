package org.example.newsrecommendationsystem;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
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
        goToHomePage();
    }

    private boolean validateAllFields() {
        // Check for empty fields first
        if (userName.getText().isEmpty() || email.getText().isEmpty() ||
                password.getText().isEmpty() || confirmPassword.getText().isEmpty()) {
            showAlert("Incomplete Fields", "Please fill in all the fields.");
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

        if (getSelectedCheckMenuItemCount(newsCategoriesMenu) < 5) {
            showAlert("Selection Error", "Please select at least 5 news categories.");
            return false;
        }

        if (getSelectedCheckMenuItemCount(contentTypePreferencesMenu) < 3) {
            showAlert("Selection Error", "Please select at least 3 content type preferences.");
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

    @FXML
    private void loginLink() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent loginRoot = loader.load();
            Scene loginScene = new Scene(loginRoot);

            Stage currentStage = (Stage) loginLink.getScene().getWindow();
            currentStage.setScene(loginScene);
            currentStage.setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Could not load the Login page.");
        }
    }

    private void goToHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
            Parent homeRoot = loader.load();
            Scene homeScene = new Scene(homeRoot);

            Stage currentStage = (Stage) signInButton.getScene().getWindow();
            currentStage.setScene(homeScene);
            currentStage.setTitle("Home");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Could not load the Home page.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
