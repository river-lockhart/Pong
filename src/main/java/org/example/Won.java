package org.example;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Won implements Initializable{

    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;
    @FXML
    private Pane background;

    //audio objects
    File musicFile;
    Media musicMedia;
    MediaPlayer playMusic;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        setBackground();
        setMusic();
    }

    public void setMusic() {
        musicFile = new File("./src/sounds/winsong.mp3");
        musicMedia = new Media(musicFile.toURI().toString());
        playMusic = new MediaPlayer(musicMedia);
        playMusic.setOnEndOfMedia(new Runnable() {
            public void run() {
                playMusic.seek(Duration.ZERO);
            }
        });
        playMusic.play();
    }

    //sets background image of the main menu
    private void setBackground() {
        Image wonImage = new Image("./images/won.png");
        ImagePattern wonPattern = new ImagePattern(wonImage);
        BackgroundFill wonFill = new BackgroundFill(wonPattern, null, null);
        Background wonBackground = new Background(wonFill);
        background.setBackground(wonBackground);
    }
}