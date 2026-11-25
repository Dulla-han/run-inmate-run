package game.items;

import game.gameControll.Controlls;
import game.gameControll.GamePanel;
import game.gameControll.ImageId;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The player’s inventory HUD. Tracks collected items,
 * coin count, and renders the overlay.
 */
public class Inventory {
    private final GamePanel gamePanel;
    private final Controlls keyH;
    private final List<Item> items = new ArrayList<>();

    private boolean hasLamp;
    private boolean hasKey;
    private boolean hasLetter;
    private int coinCounter;

    /**
     * Constructs the Inventory overlay.
     *
     * @param gamePanel the GamePanel for rendering context
     * @param keyH      the Controlls instance for input
     */
    public Inventory(GamePanel gamePanel, Controlls keyH) {
        this.gamePanel = gamePanel;
        this.keyH = keyH;

        coinCounter = 0;
        hasKey = false;
        hasLetter = false;

        hasLamp = true;
    }

    /**
     * Adds an item to the inventory.
     *
     * @param item the Item to store
     */
    public void addToInventory(Item item) {
        items.add(item);

        if (item instanceof Key) hasKey = true;
        if (item instanceof Letter) hasLetter = true;
    }

    /**
     * Draws the inventory screen: icons, counts, and items.
     *
     * @param images map of ImageId to loaded Image objects
     */
    public void drawInventory(Map<ImageId, Image> images) {
        GraphicsContext gc = gamePanel.getGraphicsContext();
        gc.setFont(gamePanel.getFont(20));
        gc.setFill(Color.WHITE);
        gc.fillText("Inventory[I]\nLamp[E]\nInteract[SPACE]\nAbility[R]", 50, gamePanel.getHeight() - 100);

        if (!keyH.isInventoryPressed()) return;
        double iconSize = gamePanel.getTileSize();

        double x = 16;
        double y = 16;

        Image img = images.get(ImageId.COINS);
        gc.drawImage(img, x, y + 200, iconSize / 2, iconSize / 2);
        gc.fillText("COIN COUNT: " + coinCounter, x + iconSize / 4, y + iconSize + 100);

        for (Item it : items) {

            img = images.get(it.getImageId());

            if (img != null) {
                gc.drawImage(img, x, y, iconSize, iconSize);
                gc.setFont(gamePanel.getFont(24));
                gc.setFill(Color.WHITE);
                gc.fillText(it.getItem(), x + iconSize/2, y + iconSize + 12);
            }
            x += iconSize + 16;
        }

        gc.setFont(gamePanel.getFont(24));
        gc.setFill(Color.WHITE);
        gc.fillText("INVENTORY OPENED", 50, gamePanel.getCanvas().getHeight() - 150);
    }

    /**
     * Increments the coin counter.
     *
     * @param coinCounter number of coins to add
     */
    public void addToCoinCounter(int coinCounter) {
        this.coinCounter += coinCounter;
    }

    public boolean hasLamp() { return hasLamp; }

    public boolean hasLetter() {
        return hasLetter;
    }

    public boolean hasKey() {
        return hasKey;
    }

    public int getCoinCounter() {
        return coinCounter;
    }

    public void setCoinCounter(int coinCounter) {
        this.coinCounter = coinCounter;
    }

    public List<Item> getItems() {
        return items;
    }
}

