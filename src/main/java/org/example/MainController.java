package org.example;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class MainController implements Initializable{

    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;
    @FXML
    private ImageView background;

    //intro sound
    File introFile = new File("src/main/java/org/example/sounds/intro.mp3");
    Media introSound = new Media(introFile.toURI().toString());
    MediaPlayer playIntro = new MediaPlayer(introSound);


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            setBackground();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        playIntro.setAutoPlay(true);
    }

    //sets background image of the main menu
    private void setBackground() throws FileNotFoundException {
        String imagePath = "src/main/java/org/example/images/mainmenu.gif";
        FileInputStream inputStream = new FileInputStream(imagePath);
        Image backgroundImage = new Image(inputStream);
        background.setImage(backgroundImage);
    }

    public void switchToScene2(ActionEvent e){
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Menu.fxml")));
            stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch(Exception exception) {
            exception.printStackTrace();
        }

    }


}