package game.world;

import game.entity.Player;
import game.gameControll.GamePanel;

/**
 * A door in the world that the player can open when close enough.
 * Occupies exactly one tile in the current room.
 */
public final class Door {

    private final GamePanel gp;
    private final int worldX, worldY;
    private boolean locked = true;

    /**
     * Constructs a Door at the given world coordinates.
     *
     * @param gp     the GamePanel for tile and room context
     * @param worldX the x‐coordinate in world pixels
     * @param worldY the y‐coordinate in world pixels
     */
    public Door(GamePanel gp, int worldX, int worldY) {
        this.gp = gp;
        this.worldX = worldX;
        this.worldY = worldY;
    }

    /**
     * Checks whether the player is within range to open this door.
     *
     * @param p       the Player to test
     * @param rangePx the maximum distance in pixels
     * @return true if within open range
     */
    public boolean isCloseEnough(Player p, int rangePx) {
        double dx = (p.getCenterX() - (worldX + gp.getTileSize() / 2.0));
        double dy = (p.getCenterY() - (worldY + gp.getTileSize() / 2.0));
        return dx * dx + dy * dy <= rangePx * rangePx;
    }

    /**
     * Attempts to open (unlock) the door.
     * Clears the door tile on success.
     */
    public void tryOpen() {
        if (!locked) return;
        if (!gp.getInventory().hasKey()) return;

        locked = false;
        clearMapCell();
    }

    /**
     * Unlocks and clears the door tile at this door’s current map cell.
     * Calculates its own row/column based on worldX/worldY and the GamePanel’s tile size,
     * then tells the current Room’s TileMap to unlock that cell.
     */
    private void clearMapCell() {
        TileMap map = gp.getRoomManager().getCurrentRoom().getTileMap();
        int row = worldY / gp.getTileSize();
        int col = worldX / gp.getTileSize();
        map.unlockDoor(row, col);
    }
}
