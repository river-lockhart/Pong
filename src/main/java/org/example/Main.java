package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.util.Objects;


public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage){
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main.fxml")));
            Scene scene = new Scene(root);
            String imagePath = "src/main/java/org/example/images/pongicon.png";
            FileInputStream inputStream = new FileInputStream(imagePath);
            Image icon = new Image(inputStream);
            stage.getIcons().add(icon);
            stage.setTitle("Pong Master");
            stage.setResizable(false);

            stage.setScene(scene);
            stage.show();

        }catch(Exception e) {
            e.printStackTrace();
        }




    }


}