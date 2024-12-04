package org.example.newsrecommendationsystem.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.newsrecommendationsystem.Navigation;

import java.io.IOException;

public class ManagementPageController {

    @FXML
    private void TotheAddArticlePage(ActionEvent actionEvent) {
        Navigation.navigateToPage(actionEvent, "/org/example/newsrecommendationsystem/addArticle.fxml", "Admin Login");

    }

    @FXML
    private void TotheDeleteArticlePage(ActionEvent actionEvent) {
        Navigation.navigateToPage(actionEvent, "/org/example/newsrecommendationsystem/DeleteArticlePage.fxml", "Admin Login");

    }


    public void backToHomePage(ActionEvent actionEvent) {
        Navigation.navigateToPage(actionEvent, "/org/example/newsrecommendationsystem/Login.fxml", "Admin Login");

    }
}
