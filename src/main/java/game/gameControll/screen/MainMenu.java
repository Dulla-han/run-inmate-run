package game.gameControll.screen;

import game.gameControll.GamePanel;
import game.gameControll.GameState;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * The initial main‐menu with background video and buttons
 * for “Start Game” and “Quit”.
 */
public class MainMenu {
    private final GamePanel gamePanel;
    private final VBox menu;
    private final BackgroundVideo backgroundVideo;
    private final ImageView mediaView;

    public MainMenu(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        menu = new VBox(20);
        menu.setAlignment(Pos.CENTER);
        menu.setStyle("-fx-padding: 40;-fx-background-color: rgba(0,0,0,0.7);-fx-background-radius: 10;");

        Button start = new Button("Start Game");
        start.setFont(gamePanel.getFont(50));
        start.setOnAction(e -> {
            hide();
            if (gamePanel.getGameWon().opened) gamePanel.getGameWon().hide();
            if (gamePanel.getGameOver().opened) gamePanel.getGameOver().hide();
        });

        Button load = new Button("Load Game");
        load.setFont(gamePanel.getFont(50));
        load.setOnAction(e -> {
            hide();
            gamePanel.loadGame();
            if (gamePanel.getGameWon().opened) gamePanel.getGameWon().hide();
            if (gamePanel.getGameOver().opened) gamePanel.getGameOver().hide();
        });

        Button quit = new Button("Quit");
        quit.setFont(gamePanel.getFont(50));
        quit.setOnAction(e -> System.exit(0));

        Label title = new Label("Run Inmate, Run!");
        title.setFont(gamePanel.getFont(70));
        title.setTextFill(Color.WHITE);

        menu.getChildren().addAll(title, start, load, quit);

        backgroundVideo = new BackgroundVideo(getClass().getResource("/Video/background.mp4"));
        mediaView = backgroundVideo.getView();

        mediaView.fitWidthProperty().bind(gamePanel.widthProperty());
        mediaView.fitHeightProperty().bind(gamePanel.heightProperty());
        mediaView.setPreserveRatio(true);
        StackPane.setAlignment(mediaView, Pos.CENTER);
    }

    /**
     * Displays the main menu, starts background media,
     * and sets state to MAIN_MENU.
     */
    public void show() {
        gamePanel.getGraphicsContext().clearRect(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
        gamePanel.setGameState(GameState.MAIN_MENU);
        gamePanel.getChildren().add(0, mediaView);
        gamePanel.getChildren().add(menu);
        backgroundVideo.play();
    }

    /**
     * Stops media playback and hides the menu,
     * switching state to PLAYING.
     */
    public void hide() {
        backgroundVideo.stop();
        gamePanel.setGameState(GameState.PLAYING);
        gamePanel.getChildren().remove(mediaView);
        gamePanel.getChildren().remove(menu);
    }
}
