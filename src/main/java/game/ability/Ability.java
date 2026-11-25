package game.ability;

import game.gameControll.GamePanel;
import game.gameControll.ImageId;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

import java.util.Map;

/**
 * Represents a player‐usable ability with cooldown management and
 * on‐screen cooldown visualization.
 */

public class Ability {
    private GamePanel gamePanel;
    private int now;
    private int cooldown = 100000;
    private boolean active = false;
    private boolean abilityReady = true;
    private int cooldownSeconds = 5;
    private ImageId imageId;


    /**
     * Constructs an Ability tied to the provided GamePanel.
     *
     * @param gamePanel the main game panel controlling rendering and timing
     */
    public Ability(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        imageId = ImageId.ABILITY;
    }


    /**
     * Starts the cooldown timer for this ability.
     * Should be called when the ability is first activated.
     */
    public void inicializeCooldown() {
        cooldown = gamePanel.getClock().getRemainingSeconds();
        abilityReady = false;
    }


    /**
     * Updates the cooldown display and readiness state.
     * Renders either a grayed‐out icon with remaining seconds or
     * a glowing “READY” indicator when cooldown has elapsed.
     *
     * @param gc      the GraphicsContext on which to draw
     * @param images  map of ImageId to loaded Image objects
     */
    public void cooldownUpdate(GraphicsContext gc, Map<ImageId, Image> images) {
        double imageSize = gamePanel.getTileSize() * 1.2;
        double x = gamePanel.getCanvas().getWidth() - 300;
        double y = gamePanel.getCanvas().getHeight() - 300;

        Image img = images.get(ImageId.ABILITY);
        now = gamePanel.getClock().getRemainingSeconds();
        int remaining = cooldownSeconds - cooldown + now;
        if (cooldown - now < cooldownSeconds) {
            int scale = 30;
            abilityReady = false;
            gc.save();
            gc.setGlobalAlpha(0.5);
            gc.drawImage(img, x, y, imageSize, imageSize);
            gc.restore();

            gc.setFont(gamePanel.getFont(scale));
            gc.setFill(Color.WHITE);
            gc.fillText("" + remaining, x + imageSize / 2 - scale / 2, y + imageSize / 2 - scale / 2);
        } else {
            // radiant red glow
            double radius = imageSize;
            RadialGradient rg = new RadialGradient(
                    0, 0,
                    x + imageSize/2, y + imageSize/2,
                    radius,
                    false, CycleMethod.NO_CYCLE,
                    new Stop(0.0, Color.color(1, 0, 0, 0.6)),
                    new Stop(0.7, Color.color(1, 0, 0, 0.2)),
                    new Stop(1.0, Color.TRANSPARENT)
            );
            gc.setFill(rg);
            gc.fillRect(x - radius, y - radius, imageSize + radius*2, imageSize + radius*2);
            gc.setGlobalAlpha(1.0);

            int scale = 26;
            gc.drawImage(img, x, y, imageSize, imageSize);
            gc.setFont(gamePanel.getFont(scale));
            gc.setFill(Color.WHITE);
            gc.fillText("READY", x + scale, y + imageSize - scale);
            abilityReady = true;
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isAbilityReady() {
        return abilityReady;
    }
}
