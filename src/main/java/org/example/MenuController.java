package org.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MenuController implements Initializable{

    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;
    @FXML
    private ImageView background;

    //only initializes the menu background image
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            setBackground();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //sets background image of the main menu
    private void setBackground() throws FileNotFoundException {
        String imagePath = "src/main/java/org/example/images/mainmenu.gif";
        FileInputStream inputStream = new FileInputStream(imagePath);
        Image backgroundImage = new Image(inputStream);
        background.setImage(backgroundImage);
    }

    //starts gameplay
    @FXML
    public void start(MouseEvent e) throws IOException {
        switchToGameplay(e);
    }

    //will load previous save
    @FXML
    public void load(MouseEvent e) {
        loadToGameplay(e);
    }

    //exits game from menu
    @FXML
    public void quit(MouseEvent e) {
        System.exit(0);
    }

    //loads the scene responsible for gameplay
    public void switchToGameplay(MouseEvent e){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Gameplay.fxml"));
            root = loader.load();
            Scene gameplayScene = new Scene(root);
            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(gameplayScene);
            stage.show();
        }catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    public void loadToGameplay(MouseEvent e){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameplayLoad.fxml"));
            root = loader.load();
            Scene gameplayScene = new Scene(root);
            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(gameplayScene);
            stage.show();
        }catch(Exception exception) {
            exception.printStackTrace();
        }
    }








}