package game.gameControll.screen;

import game.gameControll.GamePanel;
import game.gameControll.GameState;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;


/**
 * Overlay menu shown when the player wins.
 */
public class GameWon {
    private final GamePanel gamePanel;
    private final VBox menu;
    public boolean opened = false;

    public GameWon(GamePanel gamePanel) {
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

        Label gameOver = new Label("Game Won\nYou have escaped\nScore:" + gamePanel.getInventory().getCoinCounter() * gamePanel.getClock().getRemainingSeconds());
        gameOver.setFont(gamePanel.getFont(70));
        gameOver.setTextFill(Color.WHITE);
        gameOver.setFont(gamePanel.getFont(50));

        menu.getChildren().addAll(gameOver, start, quit);
    }

    /**
     * Shows the victory screen and sets state to GAME_WON.
     */
    public void show() {
        opened = true;
        gamePanel.setGameState(GameState.GAME_OVER);
        gamePanel.getChildren().add(menu);
    }


    /**
     * Hides the screen and returns to the main menu.
     */
    public void hide() {
        opened = false;
        gamePanel.setGameState(GameState.MAIN_MENU);
        gamePanel.getChildren().remove(menu);
    }
}