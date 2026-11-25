package game.entity;

import game.gameControll.GamePanel;
import game.gameControll.ImageId;
import game.world.Room;
import game.world.TileMap;
import javafx.scene.image.Image;

import java.util.Random;

/**
 * A fireball projectile that wanders randomly until colliding
 * with walls or interacting with other entities.
 */
public class Fireball extends Entity {
    private final Image up1Img, up2Img, up3Img, up4Img,
            down1Img, down2Img, down3Img, down4Img,
            left1Img, left2Img, left3Img, left4Img,
            right1Img, right2Img, right3Img, right4Img;

    private GamePanel gp;

    private int speed;
    private int stepsRemaining;
    private Room homeRoom;

    private Image currentImg;



    /**
     * Creates a new Fireball in the given room.
     *
     * @param gp       the GamePanel
     * @param room     the Room this fireball belongs to
     * @param imageId  sprite sheet identifier
     * @param x        starting x‐coordinate
     * @param y        starting y‐coordinate
     * @param scale    scale factor for rendering
     * @param speed    movement speed in pixels/tick
     */
    public Fireball(GamePanel gp, Room room, ImageId imageId, int x, int y, double scale, int speed) {
        super(gp, imageId, x, y, scale);
        this.gp = gp;
        this.homeRoom = room;
        this.speed = speed;
        this.stepsRemaining = new Random().nextInt(100);
        direction = "right";

        getEntityImage();
        up1Img = load(up1);
        up2Img = load(up2);
        up3Img = load(up3);
        up4Img = load(up4);
        down1Img = load(down1);
        down2Img = load(down2);
        down3Img = load(down3);
        down4Img = load(down4);
        left1Img = load(left1);
        left2Img = load(left2);
        left3Img = load(left3);
        left4Img = load(left4);
        right1Img = load(right1);
        right2Img = load(right2);
        right3Img = load(right3);
        right4Img = load(right4);
    }



    @Override
    public void getEntityImage() {
        up1 = "/Fireball/fireballup1.png";
        up2 = "/Fireball/fireballup2.png";
        up3 = "/Fireball/fireballup3.png";
        up4 = "/Fireball/fireballup4.png";
        down1 = "/Fireball/fireballdown1.png";
        down2 = "/Fireball/fireballdown2.png";
        down3 = "/Fireball/fireballdown3.png";
        down4 = "/Fireball/fireballdown4.png";
        left1 = "/Fireball/fireballleft1.png";
        left2 = "/Fireball/fireballleft2.png";
        left3 = "/Fireball/fireballleft3.png";
        left4 = "/Fireball/fireballleft4.png";
        right1 = "/Fireball/fireballright1.png";
        right2 = "/Fireball/fireballright2.png";
        right3 = "/Fireball/fireballright3.png";
        right4 = "/Fireball/fireballright4.png";
    }

    @Override
    public void interact() {
    }


    /**
     * Updates this fireball’s position, sprite animation,
     * and randomly changes direction after a number of steps.
     */
    public void update() {

        double nextX = x;
        double nextY = y;

        int fireballSize = (int) (tileSize * gp.getFireBallScale());

        spriteCounter++;
        if (spriteCounter > 13) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                if (spriteNum2 == 7) spriteNum2 = 0;
                else spriteNum2++;
                spriteNum = 3;
            } else if (spriteNum == 3) {
                spriteNum = 4;
            } else if (spriteNum == 4) {
                if (spriteNum2 == 7) spriteNum2 = 0;
                else spriteNum2++;
                spriteNum = 5;
            } else if (spriteNum == 5) {
                spriteNum = 1;
                if (spriteNum2 == 7) spriteNum2 = 0;
                else {
                    spriteNum2++;
                    standingCounter++;
                }
            }
            spriteCounter = 0;
        }

        chooseImageId();

        if (stepsRemaining <= 0) {
            int rand = new Random().nextInt(4);
            switch (rand) {
                case 0 -> direction = "right";
                case 1 -> direction = "left";
                case 2 -> direction = "down";
                case 3 -> direction = "up";
            }
            stepsRemaining = 50 + new Random().nextInt(100);
        }

        switch (direction) {
            case "left":
                nextX = x - speed;
                break;
            case "right":
                nextX = x + speed;
                break;
            case "up":
                nextY = y - speed;
                break;
            case "down":
                nextY = y + speed;
                break;
        }

        int topLeftRow = (int) nextY / tileSize;
        int topLeftCol = (int) nextX / tileSize;

        int topRightRow = topLeftRow;
        int topRightCol = (int) ((nextX + fireballSize - 1) / tileSize);

        int bottomLeftRow = (int) ((nextY + fireballSize - 1) / tileSize);
        int bottomLeftCol = topLeftCol;

        int bottomRightRow = bottomLeftRow;
        int bottomRightCol = topRightCol;


        boolean canMove = isCollision(nextX, nextY, topLeftRow, topLeftCol, topRightRow, topRightCol, bottomLeftRow, bottomLeftCol, bottomRightRow, bottomRightCol);

        if (!canMove) {
            x = nextX;
            y = nextY;
            stepsRemaining--;
        } else stepsRemaining = 0;
    }

    public void chooseImageId() {

        switch (direction) {
            case "up":
                if (spriteNum == 1) currentImg = up1Img;
                if (spriteNum == 2) currentImg = up2Img;
                if (spriteNum == 3) currentImg = up3Img;
                if (spriteNum == 4 || spriteNum == 5) currentImg = up4Img;
                break;
            case "down":
                if (spriteNum == 1) currentImg = down1Img;
                if (spriteNum == 2) currentImg = down2Img;
                if (spriteNum == 3) currentImg = down3Img;
                if (spriteNum == 4 || spriteNum == 5) currentImg = down4Img;
                break;
            case "left":
                if (spriteNum == 1) currentImg = left1Img;
                if (spriteNum == 2) currentImg = left2Img;
                if (spriteNum == 3 || spriteNum == 4) currentImg = left3Img;
                if (spriteNum == 5) currentImg = left4Img;
                break;
            case "right":
                if (spriteNum == 1) currentImg = right1Img;
                if (spriteNum == 2) currentImg = right2Img;
                if (spriteNum == 3 || spriteNum == 4) currentImg = right3Img;
                if (spriteNum == 5) currentImg = right4Img;
                break;
        }
    }

    /**
     * Tests whether moving to the given next coordinates
     * would cause a collision with solid tiles or entities.
     *
     * @param nextX             proposed x‐coordinate
     * @param nextY             proposed y‐coordinate
     * @param topLeftRow        top‐left tile row index
     * @param topLeftCol        top‐left tile column index
     * @param topRightRow       top‐right tile row index
     * @param topRightCol       top‐right tile column index
     * @param bottomLeftRow     bottom‐left tile row index
     * @param bottomLeftCol     bottom‐left tile column index
     * @param bottomRightRow    bottom‐right tile row index
     * @param bottomRightCol    bottom‐right tile column index
     * @return true if a collision would occur, false otherwise
     */
    public boolean isCollision(double nextX, double nextY, int topLeftRow, int topLeftCol, int topRightRow, int topRightCol,
                               int bottomLeftRow, int bottomLeftCol, int bottomRightRow, int bottomRightCol){
        int sizeEntity = tileSize;
        int sizePlayer = tileSize;
        TileMap homeRoomMap = homeRoom.getTileMap();

        if (homeRoomMap.isPassageBack(topLeftRow, topLeftCol)
                || homeRoomMap.isPassageBack(topRightRow, topRightCol)
                || homeRoomMap.isPassageBack(bottomLeftRow, bottomLeftCol)
                || homeRoomMap.isPassageBack(bottomRightRow, bottomRightCol)) return true;

        if (homeRoomMap.isSolid(topLeftRow, topLeftCol)
                || homeRoomMap.isSolid(topRightRow, topRightCol)
                || homeRoomMap.isSolid(bottomLeftRow, bottomLeftCol)
                || homeRoomMap.isSolid(bottomRightRow, bottomRightCol)) return true;
        for (Entity other : homeRoom.getEntities()) {
            if (other == this) {
                continue;
            }
            if (other instanceof Warden) {
                sizeEntity = (int)(sizeEntity * gamePanel.getWardenScale());
            } else if (other instanceof Fireball) {
                sizeEntity = (int)(sizeEntity * gamePanel.getFireBallScale());
            } else if (other instanceof Keymaster) {
                sizeEntity = (int)(sizeEntity * gamePanel.getKeymasterScale());
            }
            if (nextX < other.x + sizeEntity && nextX + sizePlayer > other.x && nextY < other.y + sizeEntity && nextY + sizePlayer > other.y) return true;
        }
        return false;
    }

    /**
     * Helper to load an Image from a resource path.
     *
     * @param path the resource path to the image file
     * @return the loaded Image
     */
    private Image load(String path) {
        return new Image(getClass().getResource(path).toExternalForm());
    }

    public Image getCurrentImg() {
        return currentImg;
    }
}
