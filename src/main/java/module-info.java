module org.example.newsrecommendationsystem {
    requires javafx.controls;
    requires javafx.fxml;

    // Add the required MongoDB modules
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;

    opens org.example.newsrecommendationsystem to javafx.fxml;
    exports org.example.newsrecommendationsystem;
    exports org.example.newsrecommendationsystem.user;
    opens org.example.newsrecommendationsystem.user to javafx.fxml;
}