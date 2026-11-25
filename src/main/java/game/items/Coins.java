package game.items;

import game.gameControll.GamePanel;
import game.gameControll.ImageId;
import game.entity.Player;

import java.util.Random;

/**
 * A collectible coin item that the player can pick up.
 * Increments the player’s coin total and then disappears.
 */
public class Coins extends Item {
    private int value;
    private int x, y;
    private GamePanel gamePanel;

    private boolean onGround = true;

    /**
     * Constructs a Coins item at the given position.
     *
     * @param imageId   the ImageId for the coin sprite
     * @param x         the x‐coordinate in world pixels
     * @param y         the y‐coordinate in world pixels
     * @param gamePanel the GamePanel context for pick‐up logic
     */
    public Coins(ImageId imageId, int x, int y, GamePanel gamePanel) {
        super(imageId);
        this.x = x;
        this.y = y;
        this.gamePanel = gamePanel;
        value = new Random().nextInt(10);
    }

    /**
     * Checks if the player is within pick‐up radius.
     *
     * @param p      the Player to test
     * @param radius maximum distance in pixels
     * @return true if within radius, false otherwise
     */
    public boolean isCloseEnough(Player p, double radius) {
        double distanceX = p.getCenterX() - (x + gamePanel.getTileSize() / 2.0);
        double distanceY = p.getCenterY() - (y + gamePanel.getTileSize() / 2.0);
        return distanceX * distanceX + distanceY * distanceY <= radius * radius;
    }

    /**
     * Handles the logic when the coin is picked up.
     */
    public void pickUp() {
        gamePanel.getInventory().addToCoinCounter(value);
        onGround = false;
        System.out.println("Uuuuu piece of candy!");
    }

    /**
     * @return the coin’s value (how many coins it adds)
     */
    public int getValue() {
        return value;
    }

    /**
     * @return true if the coin is still on the ground
     */
    public boolean isOnGround() {
        return onGround;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
