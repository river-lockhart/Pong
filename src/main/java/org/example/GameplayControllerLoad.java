package org.example;

import java.io.*;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameplayControllerLoad implements Initializable {

    //viewport objects
    @FXML
    private Stage stage;

    @FXML
    private Scene scene;

    @FXML
    private Parent root;

    @FXML
    private Pane pane;

    //game objects
    @FXML
    private Rectangle playerPaddle;

    @FXML
    private Rectangle aiPaddle;

    @FXML
    private Circle ball;

    //level text display objects
    @FXML
    private Pane levelBackground;

    @FXML
    private Label level;

    @FXML
    private Label levelText;

    //score text display objects
    @FXML
    private Label playerScore;
    @FXML
    private Label aiScore;

    //pause button/menu objects
    @FXML
    private Pane pausePane;
    @FXML
    private Button pauseButton;
    @FXML
    private Pane pauseMenu;
    @FXML
    private Button pauseResume;
    @FXML
    private Button pauseSave;
    @FXML
    private Button pauseMainMenu;

    //animation timer for game loop
    //perfectly useless comment on a perfectly self explanatory line of code
    private AnimationTimer gameLoop;

    //movement variables for paddles and ball
    Random xDirection = new Random();
    Random yDirection = new Random();
    int xVelocity;
    int yVelocity;
    int initialSpeed;
    boolean isMovingUp;
    boolean isMovingDown;

    //image array/variable for length for the background images
    private final String[] imageList = {"src/main/java/org/example/images/level1.png","src/main/java/org/example/images/level2.png","src/main/java/org/example/images/level3.png",
            "src/main/java/org/example/images/level4.png","src/main/java/org/example/images/level5.png","src/main/java/org/example/images/level6.png",
            "src/main/java/org/example/images/level7.png","src/main/java/org/example/images/level8.png","src/main/java/org/example/images/level9.png",
            "src/main/java/org/example/images/level10.png"};
    private final int imageListLength = imageList.length;

    //checks previous scores for the changeLevel method
    private int previousPlayerScore = -1;
    private int previousAiScore = -1;



    //sets a boolean for changeLevel() that allows background image to be loaded once when the animation timer starts
    public boolean loadedGame = true;

    //save file for...saving...the game
    private final File saveFile = new File("src/main/resources/org/example/save.txt");

    //game audio
    private final String[] audioList = {"src/main/java/org/example/sounds/song1.mp3","src/main/java/org/example/sounds/song2.mp3","src/main/java/org/example/sounds/song3.mp3",
            "src/main/java/org/example/sounds/song4.mp3","src/main/java/org/example/sounds/song5.mp3","src/main/java/org/example/sounds/song6.mp3",
            "src/main/java/org/example/sounds/song7.mp3","src/main/java/org/example/sounds/song8.mp3","src/main/java/org/example/sounds/song9.mp3",
            "src/main/java/org/example/sounds/song10.mp3"};
    private final int audioArrayLength = audioList.length;

    //creates fade transition for levelBackground
    private FadeTransition fadeOut = new FadeTransition(
            Duration.millis(3000)
    );

    //initialize scene components
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        loadGame();
        convertAudioFiles();
        //initialize fadeOut
        fadeOut.setNode(levelBackground);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setCycleCount(1);
        fadeOut.setAutoReverse(false);

        //sets pause icon and menu image backgrounds
        try {
            pauseImages();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        //defaults the pause menu as not visible
        pauseMenu.setVisible(false);

        //gets ball directions for movement
        ballMovement(xDirection, yDirection);
        //gets beginning score
        score(playerScore, aiScore);

        //starts the game loop
        startGameLoop();
        //initializes event filters to add smoothness to paddle movement
        addEventFilters();
    }



    //event filters to remove key press delay/passed to initialize function
    private void addEventFilters() {
        pane.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            handleKeyPressed(event);
        });

        pane.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            handleKeyReleased(event);
        });
    }

    //key press method for player paddle movement
    private void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case W:
                isMovingUp = true;
                break;
            case S:
                isMovingDown = true;
                break;
            default:
                break;
        }
    }

    //key release method for player paddle movement
    private void handleKeyReleased(KeyEvent event) {
        switch (event.getCode()) {
            case W:
                isMovingUp = false;
                break;
            case S:
                isMovingDown = false;
                break;
            default:
                break;
        }
    }

    //player paddle UP logic
    private void movePlayerPaddleUp() {
        //checks and prevents if players paddle goes above screen
        if(playerPaddle.getLayoutY() <= 0) {
            playerPaddle.setLayoutY(0);
            //otherwise player paddle moves up the screen by 10 pixels
        }else {
            playerPaddle.setLayoutY(playerPaddle.getLayoutY() - 10);
        }
    }

    //player paddle DOWN logic
    private void movePlayerPaddleDown() {
        //does the same as UP logic except for bottom of screen
        if(playerPaddle.getLayoutY() >= 310) {
            playerPaddle.setLayoutY(310);
        }else {
            playerPaddle.setLayoutY(playerPaddle.getLayoutY() + 10);
        }
    }

    //move method passed to game loop for player paddle/ball movement
    private void move() {
        // allows paddle movement
        if (isMovingUp) {
            movePlayerPaddleUp();
        } else if (isMovingDown) {
            movePlayerPaddleDown();
        }

        // Ball movement
        double currentBallX = ball.getLayoutX();
        double currentBallY = ball.getLayoutY();
        double newBallX = currentBallX + xVelocity;
        double newBallY = currentBallY + yVelocity;

        //moves ball by setting its new co-ords every frame
        ball.setLayoutX(newBallX);
        ball.setLayoutY(newBallY);

        //reverses the balls velocity when it hits the top or bottom of the screen
        if (newBallY <= 0 || newBallY >= pane.getHeight() - ball.getRadius()) {
            yVelocity = -yVelocity;
        }

        //checks if ball hits players paddle and reverses direction and speeds up
        if (ball.getBoundsInParent().intersects(playerPaddle.getBoundsInParent())) {
            xVelocity = -xVelocity;
            xVelocity++;
            yVelocity++;
        }

        //does same as above but for ai paddle, does not speed up the ball
        if (ball.getBoundsInParent().intersects(aiPaddle.getBoundsInParent())) {
            xVelocity = -xVelocity;
        }
    }

    //sets velocity of ball X and Y directions
    private void setXDirection(int randomXDirection) {
        xVelocity = randomXDirection;
    }

    private void setYDirection(int randomYDirection) {
        yVelocity = randomYDirection;
    }

    //method to initialize ball movement
    private void ballMovement(Random xDirection, Random yDirection) {
        int initialSpeed = 3;
        int randomXDirection = xDirection.nextInt(2);
        if (randomXDirection == 0) {
            randomXDirection--;
        }
        setXDirection(randomXDirection * initialSpeed);

        int randomYDirection = yDirection.nextInt(2);
        if (randomYDirection == 0) {
            randomYDirection--;
        }
        setYDirection(randomYDirection * initialSpeed);
    }

    //method to control ai paddle movement
    private void aiMovement() {
        double currentBallX = ball.getLayoutX();
        double currentBallY = ball.getLayoutY();
        double currentAiPaddleY = aiPaddle.getLayoutY();
        double aiPaddleHeight = aiPaddle.getHeight();
        //double newAiPaddleY = currentAiPaddleY + (yVelocity*0.7);

        if(currentBallX > 200) {
            if((currentAiPaddleY + aiPaddleHeight) < currentBallY) {
                moveAiPaddleDown();
                if(currentAiPaddleY <= 0) {

                    moveAiPaddleDown();
                }else if(currentAiPaddleY >= (600-aiPaddle.getHeight())) {
                    moveAiPaddleUp();
                }
            }else if((currentAiPaddleY + aiPaddleHeight) > currentBallY) {
                moveAiPaddleUp();
                if(currentAiPaddleY <= 0) {
                    moveAiPaddleDown();
                }else if(currentAiPaddleY >= (600-aiPaddle.getHeight())) {
                    moveAiPaddleUp();
                }
            }
        }
    }

    //moves ai up the screen as long as it's within the screen
    private void moveAiPaddleUp() {
        double currentAiPaddleY = aiPaddle.getLayoutY();
        double newAiPaddleY = currentAiPaddleY - 4;

        if(newAiPaddleY >= 0) {
            aiPaddle.setLayoutY(newAiPaddleY);
        }
    }

    //moves ai down the screen as long as it's within the screen
    private void moveAiPaddleDown() {
        double currentAiPaddleY = aiPaddle.getLayoutY();
        double newAiPaddleY = currentAiPaddleY + 4;

        if(newAiPaddleY + aiPaddle.getHeight() <= pane.getHeight()) {
            aiPaddle.setLayoutY(newAiPaddleY);
        }
    }

    //scoring system
    private void score(Label playerScore,Label aiScore) {
        //did not want to hardcode these here, may change later
        int ballStartX = 300;
        int ballStartY = 200;
        int aiPaddleStartX = 581;
        int aiPaddleStartY = 155;
        //converts score text to an int so that it can be adjusted
        int currentPlayerScore = Integer.parseInt(playerScore.getText());
        int currentAiScore = Integer.parseInt(aiScore.getText());

        //if ball hits ai side wall player gets a point and the ball and ai paddle are reset to original position
        if(ball.getLayoutX() >= 600) {
            currentPlayerScore++;
            playerScore.setText(String.valueOf(currentPlayerScore));
            ballMovement(xDirection, yDirection);
            ball.setLayoutX(ballStartX);
            ball.setLayoutY(ballStartY);
            aiPaddle.setLayoutX(aiPaddleStartX);
            aiPaddle.setLayoutY(aiPaddleStartY);
            //if ball hits player side wall ai gets a point and the ball and ai paddle are reset to original position
        }else if(ball.getLayoutX() <= 0) {
            currentAiScore++;
            aiScore.setText(String.valueOf(currentAiScore));
            ballMovement(xDirection, yDirection);
            ball.setLayoutX(ballStartX);
            ball.setLayoutY(ballStartY);
            aiPaddle.setLayoutX(aiPaddleStartX);
            aiPaddle.setLayoutY(aiPaddleStartY);
        }
    }


    //changes the level based on player/ai scores

    private void changeLevel() throws FileNotFoundException {
        int currentLevel = Integer.parseInt(level.getText());
        int newLevel;

        //if player gets 10 points scoreboard is reset, the level is increased and player is shown the next level
        //also checks if the text that shows the current level is not currently visible, if its not it makes it visible
        if(Integer.parseInt(playerScore.getText()) == 10) {
            playerScore.setText("0");
            aiScore.setText("0");
            newLevel = currentLevel += 1;
            if (!levelBackground.isVisible()) {
                levelBackground.setVisible(true);
            }
            //if player beats level 10 then the win screen is loaded
            if(newLevel > 10) {
                gameLoop.stop();
                switchToWon();
            }
            level.setText(String.valueOf(newLevel));
            //if ai gets 10 points then player loses and the fail state is called
        }else if(Integer.parseInt(aiScore.getText()) == 10) {
            gameLoop.stop();
            switchToFailed();
        }

        //runs image file only when scores = 0 and the previous score was NOT 0, preventing image converting and setting background every frame
        //it also checks if the text that shows the current level is visible, if it is then it fades it out
        //for loaded games this checks if loadedGame is true, which it will be when this controller is loaded
        //this allows the game to load the background image without specific scores being necessary
        if ((loadedGame == true) || (Integer.parseInt(playerScore.getText()) == 0 && Integer.parseInt(aiScore.getText()) == 0 &&
                (previousPlayerScore != 0 || previousAiScore != 0))) {
            if (levelBackground.isVisible()) {
                levelBackground.setVisible(true);
                fadeOut.playFromStart();
            }
            convertImageFiles();

            if(Integer.parseInt(level.getText()) > 1) {
                playMusic.dispose();
                convertAudioFiles();
            }
            //sets loadedGame to false so that convertImageFiles and fadeOut are not constantly being ran
            loadedGame = false;
        }
        // Update previous scores
        previousPlayerScore = Integer.parseInt(playerScore.getText());
        previousAiScore = Integer.parseInt(aiScore.getText());
    }

    //iterates through image array, if the index + 1 equals the current level then string is converted to background and pane is set to that background
    private void convertImageFiles() throws FileNotFoundException {
        int currentLevel = Integer.parseInt(level.getText());
        for(int i = 0; i < imageListLength; i++) {
            if((i + 1) == currentLevel) {
                String imagePath = imageList[i];
                FileInputStream inputStream = new FileInputStream(imagePath);
                Image backgroundImage = new Image(inputStream);
                ImagePattern backgroundPattern = new ImagePattern(backgroundImage);
                BackgroundFill backgroundFill = new BackgroundFill(backgroundPattern, null, null);
                Background background = new Background(backgroundFill);
                pane.setBackground(background);
                pauseMenu.setBackground(background);
            }
        }
    }

    MediaPlayer playMusic;
    private void convertAudioFiles() {
        //gets the current level of gameplay
        int currentLevel = Integer.parseInt(level.getText());
        //iterates through audio file array
        for(int i = 0; i < audioArrayLength; i++) {
            //if the index + 1 is the current level
            if((i + 1) == currentLevel) {
                //then the current song is set to the correct index
                String currentSong = audioList[i];
                //that song is turned into a file
                File musicFile = new File(currentSong);
                //and converted to media
                Media musicMedia = new Media(musicFile.toURI().toString());
                //passed into the media player
                playMusic = new MediaPlayer(musicMedia);
                //made to repeat if it ends
                playMusic.setOnEndOfMedia(new Runnable() {
                    public void run() {
                        playMusic.seek(Duration.ZERO);
                    }
                });
                //and played
                playMusic.play();
            }
        }
    }

    //creates and sets a background image for the pause button and pause menu, passed to initialize
    private void pauseImages() throws FileNotFoundException {
        //pause button
        String imagePath = "src/main/java/org/example/images/pausebutton.png";
        FileInputStream inputStream = new FileInputStream(imagePath);
        Image pauseImage = new Image(inputStream);
        ImagePattern pausePattern = new ImagePattern(pauseImage);
        BackgroundFill pauseFill = new BackgroundFill(pausePattern, null, null);
        Background pauseIcon = new Background(pauseFill);
        pausePane.setBackground(pauseIcon);

    }

    //pauses game and opens pause menu
    @FXML
    private void pauseGame(ActionEvent e) {
        if(!pauseMenu.isVisible()) {
            pauseMenu.setVisible(true);
            gameLoop.stop();
        }
    }

    //resumes the game when player selects "Resume" from pause menu
    @FXML
    private void resumeGame(ActionEvent e) {
        if(pauseMenu.isVisible()) {
            pauseMenu.setVisible(false);
            gameLoop.start();
        }
    }

    //returns player to the main menu from the pause menu
    @FXML
    private void returnMainMenu(ActionEvent e) {
        playMusic.stop();
        try {
            root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
            stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    //starts game loop/passed to initialize function
    public void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                move();
                score(playerScore, aiScore);
                aiMovement();
                try {
                    changeLevel();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        gameLoop.start();
    }

    //loads the fail state of the game when player loses to the ai
    private void switchToFailed(){
        playMusic.stop();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Failed.fxml"));
            Parent root = loader.load();
            Failed failedController = new Failed();
            loader.setController(failedController);
            Scene failStateScene = new Scene(root);
            Stage stage = (Stage) pane.getScene().getWindow();
            stage.setScene(failStateScene);
            stage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    //loads the win state when player beats level 10
    private void switchToWon(){
        playMusic.stop();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Won.fxml"));
            Parent root = loader.load();
            Won wonController = new Won();
            loader.setController(wonController);
            Scene winStateScene = new Scene(root);
            Stage stage = (Stage) pane.getScene().getWindow();
            stage.setScene(winStateScene);
            stage.show();
        }catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    public void saveGame() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile));
            // Write the necessary data to the file
            writer.write(playerScore.getText());
            writer.newLine();
            writer.write(aiScore.getText());
            writer.newLine();
            writer.write(level.getText());
            writer.close();

            // You can write additional data as needed

            System.out.println("Game saved successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    //loads the game by reading the strings in the save file and setting the data as values for wanted objects
    public void loadGame() {
        try (BufferedReader reader = new BufferedReader(new FileReader(saveFile))) {
            String playerScoreStr = reader.readLine();
            String aiScoreStr = reader.readLine();
            String levelStr = reader.readLine();

            playerScore.setText(playerScoreStr);
            aiScore.setText(aiScoreStr);
            level.setText(levelStr);

            System.out.println(playerScore.getText());
            System.out.println(aiScore.getText());
            System.out.println(level.getText());

            System.out.println("Game loaded successfully.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}