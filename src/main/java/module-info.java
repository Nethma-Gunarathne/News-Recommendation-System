module org.example.newsrecommendationsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;
    requires org.mongodb.bson;
    requires org.jsoup;

    opens org.example.newsrecommendationsystem.user to javafx.fxml;
    exports org.example.newsrecommendationsystem;
    opens  org.example.newsrecommendationsystem to  javafx.fxml;
    exports org.example.newsrecommendationsystem.user;
    opens org.example.newsrecommendationsystem.admin to javafx.fxml;
    exports org.example.newsrecommendationsystem.admin;
    opens  org.example.newsrecommendationsystem.article to javafx.fxml;
    exports org.example.newsrecommendationsystem.article;
}