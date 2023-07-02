module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens org.example to javafx.fxml;
    exports org.example;
}