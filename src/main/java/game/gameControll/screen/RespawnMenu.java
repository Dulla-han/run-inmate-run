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
 * Overlay shown just before the player respawns
 * (after death), with a “Ready for more?” prompt.
 */
public class RespawnMenu {
    private final GamePanel gamePanel;
    private final VBox menu;

    /**
     * Constructs the respawn prompt overlay.
     *
     * @param parent the GamePanel to attach this overlay to
     */
    public RespawnMenu(GamePanel parent) {
        this.gamePanel = parent;
        menu = new VBox();

        Button respawnButton = new Button("Respawn");
        respawnButton.setFont(gamePanel.getFont(50));
        respawnButton.setOnAction(event -> {
            this.toggle();
        });

        menu.getChildren().addAll(respawnButton);
        menu.setStyle("-fx-spacing: 10; -fx-padding: 10px; -fx-alignment: center; -fx-font-size: 50;");
    }

    /**
     * Displays the respawn prompt, dims the screen,
     * and waits for player input.
     */
    public void toggle() {
        if (gamePanel.getChildren().contains(menu)) {
            gamePanel.setOpacity(1);
            gamePanel.getChildren().remove(menu);
            gamePanel.setGameState(GameState.PLAYING);
        } else {
            gamePanel.setGameState(GameState.RESPAWNING);

            GraphicsContext gc = gamePanel.getGraphicsContext();
            gc.save();
            gc.setFont(gamePanel.getFont(200));
            gc.setFill(Color.WHITE);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);

            double x = gamePanel.getCanvas().getWidth()  / 2;
            double y = gamePanel.getCanvas().getHeight() / 2;
            gc.fillText("READY\n\n\nFOR\nMORE?", x, y);
            gc.restore();

            gamePanel.setOpacity(0.5);
            gamePanel.getChildren().add(menu);
        }
    }
}