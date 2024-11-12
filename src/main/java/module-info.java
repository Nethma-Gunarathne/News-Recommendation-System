module org.example.newsrecommendationsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.newsrecommendationsystem to javafx.fxml;
    exports org.example.newsrecommendationsystem;
}