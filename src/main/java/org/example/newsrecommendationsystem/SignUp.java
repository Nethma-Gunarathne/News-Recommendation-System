package org.example.newsrecommendationsystem;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

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
    private ComboBox<CheckBox> newsCategories;

    @FXML
    private ComboBox<CheckBox> contentTypePreferences;

    @FXML
    private Button signInButton;

    @FXML
    private Text loginLink;

    private final Pattern emailPattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    @FXML
    private void initialize() {
        // Initialize CheckBox items for news categories
        addCheckBoxItems(newsCategories, List.of(
                "Technology", "Health", "Sports", "AI and Machine Learning", "Politics", "Business",
                "Science", "Environment", "Entertainment", "World News"));

        // Initialize CheckBox items for content type preferences
        addCheckBoxItems(contentTypePreferences, List.of(
                "Breaking News", "Opinion Pieces", "Analysis/Research", "Editorials", "Local News"));
    }

    private void addCheckBoxItems(ComboBox<CheckBox> comboBox, List<String> items) {
        for (String item : items) {
            CheckBox checkBox = new CheckBox(item);
            CustomMenuItem menuItem = new CustomMenuItem(checkBox);
            menuItem.setHideOnClick(false);  // Allow multiple selections without closing the drop-down
            comboBox.getItems().add(checkBox);
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

        if (getSelectedCheckBoxCount(newsCategories) < 5) {
            showAlert("Selection Error", "Please select at least 5 news categories.");
            return false;
        }

        if (getSelectedCheckBoxCount(contentTypePreferences) < 3) {
            showAlert("Selection Error", "Please select at least 3 content type preferences.");
            return false;
        }

        return true;
    }

    private int getSelectedCheckBoxCount(ComboBox<CheckBox> comboBox) {
        int count = 0;
        for (CheckBox checkBox : comboBox.getItems()) {
            if (checkBox.isSelected()) {
                count++;
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
