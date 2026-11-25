package game.entity;

import game.gameControll.Controlls;
import game.gameControll.GamePanel;
import game.gameControll.ImageId;
import game.items.Coins;
import game.world.Door;
import game.world.Room;
import game.world.TileMap;

import static java.lang.Math.round;

/**
 * The player‐controlled character; handles input, movement,
 * collision, room transitions, and death count.
 */
public class Player extends Entity {

    private final GamePanel gamePanel;
    Controlls keyH;
    private Room currentRoom;
    private int deathCount = 0;

    /**
     * Constructs the Player.
     *
     * @param gp       the GamePanel
     * @param imageId  sprite sheet identifier
     * @param x        starting x‐coordinate
     * @param y        starting y‐coordinate
     * @param speed    movement speed
     */
    public Player(GamePanel gp, ImageId imageId, int x, int y, int speed) {
        super(gp, imageId, x, y, gp.getPlayerScale());
        this.gamePanel = gp;
        this.setKeyH(gp.getKeyH());
        this.setCurrentRoom(gp.getRoomManager().getCurrentRoom());
        this.tileSize = gp.getTileSize();
        this.speed = speed;
        direction = "standing";
        getEntityImage();
    }

    @Override
    public void getEntityImage() {
        up1 = "/Player/up1.png";
        up2 = "/Player/up2.png";
        up3 = "/Player/up3.png";
        up4 = "/Player/up4.png";
        down1 = "/Player/down1.png";
        down2 = "/Player/down2.png";
        down3 = "/Player/down3.png";
        down4 = "/Player/down4.png";
        left1 = "/Player/left1.png";
        left2 = "/Player/left2.png";
        left3 = "/Player/left1.png";
        left4 = "/Player/left2.png";
        right1 = "/Player/right1.png";
        right2 = "/Player/right2.png";
        right3 = "/Player/right3.png";
        right4 = "/Player/right4.png";
        standing1 = "/Player/standing1.png";
        standing2 = "/Player/standing2.png";
        standing3 = "/Player/standing3.png";
        standing4 = "/Player/standing4.png";
        //playerImageID = standing1;
    }

    /**
     * Handles keyboard input and moves the player,
     * animates sprite frames, and checks collisions.
     */
    public void update() {
        double nextX = x;
        double nextY = y;

        int playerSize = (int)(gamePanel.getTileSize() * gamePanel.getPlayerScale());

        if (keyH.isUpPressed() && keyH.isLeftPressed()) {
            nextX = round(x - speed / 1.5);
            nextY = round(y - speed / 1.5);
            direction = "up";
        } else if (keyH.isDownPressed() && keyH.isLeftPressed()) {
            nextX = round(x - speed / 1.5);
            nextY = round(y + speed / 1.5);
            direction = "left";
        } else if (keyH.isUpPressed() && keyH.isRightPressed()) {
            nextX = round(x + speed / 1.5);
            nextY = round(y - speed / 1.5);
            direction = "up";
        } else if (keyH.isDownPressed() && keyH.isRightPressed()) {
            nextX = round(x + speed / 1.5);
            nextY = round(y + speed / 1.5);
            direction = "right";
        } else if (keyH.isUpPressed()) {
            nextY = round(y - speed);
            direction = "up";
        } else if (keyH.isDownPressed()) {
            nextY = round(y + speed);
            direction = "down";
        } else if (keyH.isLeftPressed()) {
            nextX = round(x - speed);
            direction = "left";
        } else if (keyH.isRightPressed()) {
            nextX = round(x + speed);
            direction = "right";
        } else {
            direction = "standing";
        }

        spriteCounter++;
        if (spriteCounter > 10) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                if (spriteNum2 == 5) spriteNum2 = 0;
                else spriteNum2++;
                spriteNum = 3;
            } else if (spriteNum == 3) {
                spriteNum = 4;
            } else if (spriteNum == 4) {
                if (spriteNum2 == 5) spriteNum2 = 0;
                else spriteNum2++;
                spriteNum = 5;
            } else if (spriteNum == 5) {
                spriteNum = 1;
                if (spriteNum2 == 5) spriteNum2 = 0;
                else spriteNum2++;
            }
            spriteCounter = 0;
        }
        chooseImageId();

        int centerCol = ((int)nextX + playerSize / 2) / tileSize;
        int centerRow = ((int)nextY + playerSize / 2) / tileSize;


        if (isNextRoom(centerRow, centerCol) && keyH.isInteractPressed()) {
            if (!gamePanel.getRoomManager().nextRoom()) return;
            currentRoom = gamePanel.getRoomManager().getCurrentRoom();
            x = currentRoom.getExitCol() * tileSize + (tileSize - playerSize) / 2;
            y = currentRoom.getExitRow() * tileSize;

            gamePanel.centerCameraOn(x, y);
            gamePanel.getKeyH().setInteractPressed(false);
            return;
        }

        if (isBackRoom(centerRow, centerCol) && keyH.isInteractPressed()) {
            if (!gamePanel.getRoomManager().previousRoom()) return;
            currentRoom = gamePanel.getRoomManager().getCurrentRoom();
            x = currentRoom.getEntranceCol() * tileSize + (tileSize - playerSize) / 2;
            y = currentRoom.getEntranceRow() * tileSize;

            gamePanel.centerCameraOn(x, y);
            gamePanel.getKeyH().setInteractPressed(false);
            return;
        }

        boolean canMove = !isCollision((int)nextX, (int)nextY);


        if (canMove) {

            double deltaX = nextX - x;
            double deltaY = nextY - y;

            if (gamePanel.getAbility().isActive()) {
                deltaX = 0;
                deltaY = 0;
                nextX = x;
                nextY = y;
            }
            x = nextX;
            y = nextY;
            gamePanel.shiftEnvironment(-deltaX, -deltaY);
        }

        double spriteWidthPx  = gamePanel.getTileSize() * gamePanel.getPlayerScale();
        double pickupRadius = spriteWidthPx / 2;
        if (keyH.isInteractPressed()) {
            for (Coins coin : currentRoom.getCoins()) {
                if (coin.isOnGround() && coin.isCloseEnough(this, pickupRadius * 1.5)) {
                    coin.pickUp();
                    keyH.setInteractPressed(false);
                    break;
                }
            }

            for (Entity e : currentRoom.getEntities()) {
                if (isCloseEnough(e, (int)(tileSize * 1.2))) {
                    e.interact();
                }
            }

            for (Door door : currentRoom.getDoors()) {
                if (door.isCloseEnough(this, (int)(tileSize * 1.2))) {
                    door.tryOpen();
                }
            }
        }
    }

    public void chooseImageId() {
        String imageId = null;

        switch (direction) {
            case "up":
                if (spriteNum == 1) imageId = up1;
                if (spriteNum == 2) imageId = up2;
                if (spriteNum == 3) imageId = up3;
                if (spriteNum == 4 || spriteNum == 5) imageId = up4;
                break;
            case "down":
                if (spriteNum == 1) imageId = down1;
                if (spriteNum == 2) imageId = down2;
                if (spriteNum == 3) imageId = down3;
                if (spriteNum == 4 || spriteNum == 5) imageId = down4;
                break;
            case "left":
                if (spriteNum == 1) imageId = left1;
                if (spriteNum == 2) imageId = left2;
                if (spriteNum == 3 || spriteNum == 4) imageId = left3;
                if (spriteNum == 5) imageId = left4;
                break;
            case "right":
                if (spriteNum == 1) imageId = right1;
                if (spriteNum == 2) imageId = right2;
                if (spriteNum == 3 || spriteNum == 4) imageId = right3;
                if (spriteNum == 5) imageId = right4;
                break;
            case "standing":
                if (spriteNum2 == 1) imageId = standing1;
                if (spriteNum2 == 2) imageId = standing2;
                if (spriteNum2 == 3 || spriteNum2 == 4) imageId = standing3;
                if (spriteNum2 == 5) imageId = standing4;
                break;
        }

        ImageId.PLAYER.setFileName(imageId);
    }

    /**
     * Detects collision with walls or room boundaries.
     *
     * @param nextX next x‐coordinate
     * @param nextY next y‐coordinate
     * @return true if collision detected
     */
    public boolean isCollision(int nextX, int nextY) {
        int tileSize = gamePanel.getTileSize();
        int margin = 5;
        int playerFullSize = (int)(tileSize * gamePanel.getPlayerScale());
        int playerReducedSize = playerFullSize - 2 * margin;

        int playerX1 = nextX + margin;
        int playerY1 = nextY + margin;
        int playerX2 = playerX1 + playerReducedSize;
        int playerY2 = playerY1 + playerReducedSize;

        TileMap tileMap = currentRoom.getTileMap();

        int leftColumn = playerX1 / tileSize;
        int rightColumn = (playerX2 - 1) / tileSize;
        int topRow = playerY1 / tileSize;
        int bottomRow = (playerY2 - 1) / tileSize;

        for (int row = topRow; row <= bottomRow; row++) {
            for (int column = leftColumn; column <= rightColumn; column++) {
                if (tileMap.isSolid(row, column) || tileMap.isDoor(row, column)) {
                    return true;
                }
            }
        }

        for (Entity other : currentRoom.getEntities()) {
            if (other == this)
                continue;
            double entityScale = 1.0;

            if (other instanceof Warden) entityScale *= gamePanel.getWardenScale();
            else if (other instanceof Keymaster) { entityScale *= gamePanel.getKeymasterScale(); margin += 30; }
            else if (other instanceof PatrollingNPC) entityScale *= gamePanel.getPatrollScale();
            else if (other instanceof Fireball) { entityScale *= gamePanel.getFireBallScale(); margin += 15; }

            int entityFullSize = (int)(tileSize * entityScale);
            int entityReducedSize = entityFullSize - 2 * margin;

            int entityX1 = (int)other.getX() + margin;
            int entityY1 = (int)other.getY() + margin;
            int entityX2 = entityX1 + entityReducedSize;
            int entityY2 = entityY1 + entityReducedSize;

            if (playerX1 < entityX2 && playerX2 > entityX1 && playerY1 < entityY2 && playerY2 > entityY1) {
                if (other instanceof Fireball && !gamePanel.getAbility().isActive()) getsSpotted();
                return true;
            }
        }

        return false;
    }

    /**
     * @return true if moving to (centerRow, centerCol) would enter a new room
     */
    public boolean isNextRoom(int centerRow, int centerCol) {
        return currentRoom.getTileMap().isPassageNext(centerRow, centerCol);
    }

    /**
     * @return true if moving to (centerRow, centerCol) would return to the previous room
     */
    public boolean isBackRoom(int centerRow, int centerCol) {
        return currentRoom.getTileMap().isPassageBack(centerRow, centerCol);
    }

    /**
     * Called when the player is spotted by an NPC/trap —
     * typically increases deathCount and resets position.
     */
    public void getsSpotted() {
        System.out.println("CAUGHT");
        deathCount++;
        gamePanel.getRespawnMenu().toggle();

        int ts = gamePanel.getTileSize();
        int playerSize = (int)(ts * gamePanel.getPlayerScale());

        x = currentRoom.getExitCol() * ts + (ts - playerSize) / 2;
        y = currentRoom.getExitRow() * ts;

        gamePanel.centerCameraOn(x, y);

        gamePanel.getClock().decreaseSeconds(60);
    }

    /**
     * Checks if another entity is within the given radius.
     *
     * @param other  the other Entity
     * @param radius detection radius
     * @return true if within radius
     */
    public boolean isCloseEnough(Entity other, double radius) {
        double distanceX = this.getCenterX() - (other.getCenterX());
        double distanceY = this.getCenterY() - (other.getCenterY());
        return distanceX * distanceX + distanceY * distanceY <= radius * radius;
    }

    /** @param currentRoom the Room the player is now in */
    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    /**
     * Instantly moves the player to the given coordinates.
     *
     * @param x new x‐coordinate
     * @param y new y‐coordinate
     */
    public void teleportTo(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /** @return number of times the player has died */
    public int getDeathCount() {
        return deathCount;
    }

    public void setKeyH(Controlls keyH) {
        this.keyH = keyH;
    }

    public Controlls getKeyH() {
        return keyH;
    }
}
