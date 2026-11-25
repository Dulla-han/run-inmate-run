package game.gameControll.screen;


import game.gameControll.GamePanel;
import game.gameControll.GameState;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;


/**
 * Semi‐transparent overlay shown when the game is paused.
 * Renders “Paused” text and options to resume or quit.
 */
public class PauseMenu {
    private final GamePanel gamePanel;
    private final VBox menu;

    public PauseMenu(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        menu = new VBox(20);
        Button backButton = new Button("Continue");
        backButton.setFont(gamePanel.getFont(50));
        backButton.setOnAction(event -> {
            hide();
        });
        Button exitButton = new Button("Quit & save");
        exitButton.setFont(gamePanel.getFont(50));
        exitButton.setOnAction(event -> {
            gamePanel.saveGame();
            hide();
            gamePanel.getMainMenu().show();
        });
        Button mainMenuButton = new Button("Main Menu");
        mainMenuButton.setFont(gamePanel.getFont(50));
        mainMenuButton.setOnAction(event -> {
            hide();
            gamePanel.getMainMenu().show();
        });

        menu.getChildren().addAll(backButton, exitButton, mainMenuButton);
        menu.setStyle("-fx-padding: 500px; -fx-alignment: baseline-center; -fx-font-size: 50");
    }

    /**
     * Draws the paused overlay, dims the game,
     * and sets state to PAUSED.
     */
    public void show() {
        GraphicsContext gc = gamePanel.getGraphicsContext();
        gc.save();
        gc.setFont(gamePanel.getFont(200));
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        double x = gamePanel.getCanvas().getWidth()  / 2;
        double y = gamePanel.getCanvas().getHeight() / 2;
        gc.fillText("QUITING\n\n\n\nALREADY?", x, y);
        gc.restore();
        gamePanel.setOpacity(0.5);
        gamePanel.getChildren().add(menu);
        gamePanel.setGameState(GameState.PAUSED);
    }

    /**
     * Removes the overlay and resumes gameplay.
     */
    public void hide() {
            gamePanel.setOpacity(1);
            gamePanel.getChildren().remove(menu);
            gamePanel.setGameState(GameState.PLAYING);
    }

}
