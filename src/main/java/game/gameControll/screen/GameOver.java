package game.gameControll.screen;

import game.gameControll.GamePanel;
import game.gameControll.GameState;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;

/**
 * Overlay menu shown when the player dies.
 * Offers options to respawn or quit to main menu.
 */
public class GameOver {
    private final GamePanel gamePanel;
    private final VBox menu;
    public boolean opened = false;

    /**
     * @param gamePanel the parent GamePanel to attach this menu to
     */
    public GameOver(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        menu = new VBox(20);
        menu.setAlignment(Pos.CENTER);
        menu.setStyle("-fx-padding: 40;-fx-background-color: rgba(0,0,0,0.7);-fx-background-radius: 10;");

        Button start = new Button("Main Menu");
        start.setFont(gamePanel.getFont(50));
        start.setOnAction(e -> {
            gamePanel.getMainMenu().show();
        });

        Button quit = new Button("Quit");
        quit.setFont(gamePanel.getFont(50));
        quit.setOnAction(e -> System.exit(0));

        Label gameOver = new Label("Game Over\nYou have failed to escape");
        gameOver.setFont(gamePanel.getFont(70));
        gameOver.setTextFill(Color.WHITE);
        gameOver.setFont(gamePanel.getFont(50));

        menu.getChildren().addAll(gameOver, start, quit);
    }

    /**
     * Displays the “Game Over” menu and switches
     * the GameState to GAME_OVER.
     */
    public void show() {
        opened = true;
        gamePanel.setGameState(GameState.GAME_OVER);
        gamePanel.getChildren().add(menu);
    }


    /**
     * Hides the menu and returns to the main menu state.
     */
    public void hide() {
        opened = false;
        gamePanel.setGameState(GameState.MAIN_MENU);
        gamePanel.getChildren().remove(menu);
    }
}