package game;

import game.gameControll.GamePanel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Entry point for the GameOn app.  Launches JavaFX and shows the main window.
 */
public class GameStart extends Application {

    /**
     * Main method: delegates to JavaFX Application launcher.
     *
     * @param args command‐line arguments (ignored)
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Sets up the primary stage with a GamePanel and shows it.
     *
     * @param primaryStage the JavaFX Stage provided by the runtime
     */
    @Override
    public void start(Stage primaryStage) {

        GamePanel gamePanel = new GamePanel();

        Scene scene = new Scene(gamePanel, gamePanel.getScreenWidth(), gamePanel.getScreenHeight());

        scene.setOnKeyPressed(e -> gamePanel.keyPressed(e.getCode()));
        scene.setOnKeyReleased(e -> gamePanel.keyReleased(e.getCode()));
        scene.setFill(Color.BLACK);

        primaryStage.setMinWidth(gamePanel.getScreenWidth()  / 2.0);
        primaryStage.setMinHeight(gamePanel.getScreenHeight() / 2.0);

        primaryStage.setFullScreenExitHint("");
        scene.setOnKeyPressed(e -> {if (e.getCode() == KeyCode.F11) primaryStage.setFullScreen(!primaryStage.isFullScreen());gamePanel.keyPressed(e.getCode());});

        primaryStage.setTitle("2D Game");
        primaryStage.setResizable(true);
        primaryStage.setScene(scene);
        primaryStage.show();
        Platform.runLater(() -> gamePanel.centerCameraOn(gamePanel.getPlayer().getX(), gamePanel.getPlayer().getY()));

        gamePanel.startGameLoop();
    }
}
