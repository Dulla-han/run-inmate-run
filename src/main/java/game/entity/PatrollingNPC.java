package game.entity;

import game.gameControll.GamePanel;
import game.gameControll.ImageId;
import game.world.Room;
import game.world.TileMap;
import javafx.scene.image.Image;

import java.util.Random;

/**
 * A non‐player character that patrols its room randomly,
 * switching direction when encountering walls.
 */
public class PatrollingNPC extends Entity {

    private final Image up1Img, up2Img, up3Img, up4Img,
            down1Img, down2Img, down3Img, down4Img,
            left1Img, left2Img, left3Img, left4Img,
            right1Img, right2Img, right3Img, right4Img,
            standing1Img, standing2Img, standing3Img, standing4Img;

    private GamePanel gp;

    private int speed;
    private int stepsRemaining;
    private Room homeRoom;
    private TileMap homeRoomMap;

    private String lastDirection = "standing";
    private Image currentImg;


    /**
     * Creates a PatrollingNPC that walks randomly within its home room.
     *
     * @param gp       the GamePanel
     * @param room     the Room to patrol
     * @param imageId  sprite sheet identifier
     * @param x        starting x‐coordinate
     * @param y        starting y‐coordinate
     * @param scale    scale factor for rendering
     * @param speed    movement speed
     */
    public PatrollingNPC(GamePanel gp, Room room, ImageId imageId, int x, int y, double scale, int speed) {
        super(gp, imageId, x, y, scale);
        this.gp = gp;
        this.homeRoom = room;
        this.homeRoomMap = homeRoom.getTileMap();
        this.speed = speed;

        standingCounter = new Random().nextInt(4);

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
        standing1Img = load(standing1);
        standing2Img = load(standing2);
        standing3Img = load(standing3);
        standing4Img = load(standing4);

        firstMove();
    }

    @Override
    public void getEntityImage() {
        up1 = "/Patroll/NPCup1.png";
        up2 = "/Patroll/NPCup2.png";
        up3 = "/Patroll/NPCup3.png";
        up4 = "/Patroll/NPCup4.png";
        down1 = "/Patroll/NPCdown1.png";
        down2 = "/Patroll/NPCdown2.png";
        down3 = "/Patroll/NPCdown3.png";
        down4 = "/Patroll/NPCdown4.png";
        left1 = "/Patroll/NPCleft1.png";
        left2 = "/Patroll/NPCleft2.png";
        left3 = "/Patroll/NPCleft1.png";
        left4 = "/Patroll/NPCleft2.png";
        right1 = "/Patroll/NPCright1.png";
        right2 = "/Patroll/NPCright2.png";
        right3 = "/Patroll/NPCright3.png";
        right4 = "/Patroll/NPCright4.png";
        standing1 = "/Patroll/NPCstanding1.png";
        standing2 = "/Patroll/NPCstanding2.png";
        standing3 = "/Patroll/NPCstanding3.png";
        standing4 = "/Patroll/NPCstanding4.png";
    }

    /**
     * Updates patrol movement, animation, and decides
     * when to change direction.
     *
     * @param player the Player (for potential line‐of‐sight logic)
     */
    public void update(Player player) {
        double nextX = x;
        double nextY = y;

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
            direction = "standing";
            if (standingCounter < 4) { return; }

            switch (lastDirection) {
                case "left":  direction = "right"; break;
                case "right": direction = "left";  break;
                case "up":    direction = "down";  break;
                case "down":  direction = "up";    break;
                default: break;
            }
            stepsRemaining = 200 + new Random().nextInt(400);
            standingCounter = 0;
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


        if (canSeePlayer(player) && !gamePanel.getAbility().isActive()) {
            player.getsSpotted();
        }

        boolean canMove = isCollision(nextX, nextY);

        if (!canMove) {
            lastDirection = direction;
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
            case "standing":
                if (spriteNum2 == 1) currentImg = standing1Img;
                if (spriteNum2 == 3) currentImg = standing2Img;
                if (spriteNum2 == 5 || spriteNum2 == 6) currentImg = standing3Img;
                if (spriteNum2 == 7) currentImg = standing4Img;
                break;
        }
    }

    /**
     * Checks if moving to (nextX, nextY) collides with any solid tile.
     *
     * @param nextX proposed x‐coordinate
     * @param nextY proposed y‐coordinate
     * @return true if collision, false otherwise
     */
    public boolean isCollision(double nextX, double nextY) {
        int tileSize = gp.getTileSize();
        int entitySize = (int) (tileSize * gp.getPatrollScale());
        int entityX1 = (int) nextX;
        int entityY1 = (int) nextY;
        int entityX2 = entityX1 + entitySize;
        int entityY2 = entityY1 + entitySize;

        int leftCol = entityX1 / tileSize;
        int rightCol = (entityX2 - 1) / tileSize;
        int topRow = entityY1 / tileSize;
        int bottomRow = (entityY2 - 1) / tileSize;

        for (int row = topRow; row <= bottomRow; row++) {
            for (int col = leftCol; col <= rightCol; col++) {
                if (homeRoomMap.isSolid(row, col)) return true;
            }
        }

        for (Entity other : homeRoom.getEntities()) {
            if (other == this || other instanceof Player) continue;
            double scale = 1.0;
            if (other instanceof Warden) scale = gp.getWardenScale();
            else if (other instanceof Fireball) scale = gp.getFireBallScale();
            else if (other instanceof PrisonerNPC) scale = entityScale;
            else if (other instanceof Keymaster) scale = gp.getKeymasterScale();

            int otherSize = (int) (tileSize * scale);

            int otherX1 = (int) other.getX();
            int otherY1 = (int) other.getY();
            int otherX2 = otherX1 + otherSize;
            int otherY2 = otherY1 + otherSize;

            if (entityX1 < otherX2 && entityX2 > otherX1 && entityY1 < otherY2 && entityY2 > otherY1)
                return true;
        }
        return false;
    }

    /**
     * Picks an initial random direction and step count
     * when the NPC is first placed.
     */
    public void firstMove() {

        int row = (int)y / tileSize;
        int col = (int)x / tileSize;

        int upDist = 0;
        while (!homeRoomMap.isSolid(row - upDist - 1, col)) upDist++;

        int downDist = 0;
        while (!homeRoomMap.isSolid(row + downDist + 1, col)) downDist++;

        int leftDist = 0;
        while (!homeRoomMap.isSolid(row, col - leftDist - 1)) leftDist++;

        int rightDist = 0;
        while (!homeRoomMap.isSolid(row, col + rightDist + 1)) rightDist++;

        direction = "up";
        int max = upDist;
        if (downDist  > max) { max = downDist;  direction = "down"; }
        if (leftDist  > max) { max = leftDist;  direction = "left"; }
        if (rightDist > max) { direction = "right"; }
        stepsRemaining = 200 + new Random().nextInt(100);
    }

    /**
     * Determines whether this NPC can spot the player, taking into account
     * distance, direction faced, field‐of‐view angle, and (optionally) a lamp’s light radius.
     *
     * <p>If the NPC is in “standing” (idle) state, it can spot the player in any direction
     * within the full radius. Otherwise, it uses a half‐angle of 55° around its facing direction.</p>
     *
     * @param player the Player whose visibility is being tested
     * @return true if the player is within the NPC’s field of view and detection radius
     */
    private boolean canSeePlayer(Player player) {
        double halfPovSpotting = 55;

        double pcx = player.getCenterX();
        double pcy = player.getCenterY();
        double ncx = this.getCenterX();
        double ncy = this.getCenterY();

        double dx = pcx - ncx;
        double dy = pcy - ncy;
        double distance = Math.hypot(dx, dy);

        double radius = (gp.getInventory().hasLamp() && gp.getKeyH().isLampActivated()) ? gp.getTileSize() * 3 : gp.getTileSize() * 1.5;

        if ("standing".equals(direction)) {
            return distance <= radius;
        }

        if (distance > radius) return false;

        double toPlayerDeg = Math.toDegrees(Math.atan2(-dy, dx));
        if (toPlayerDeg < 0) toPlayerDeg += 360;

        double dirDeg = switch (direction) {
            case "right" -> 0;
            case "up"    -> 90;
            case "left"  -> 180;
            case "down"  -> 270;
            default      -> 0;
        };

        double delta = Math.abs(toPlayerDeg - dirDeg);
        delta = Math.min(delta, 360 - delta);

        return delta <= halfPovSpotting;
    }

    @Override
    public void interact() {
        gamePanel.getPlayer().getsSpotted();
    }

    private Image load(String path) {
        return new Image(getClass().getResource(path).toExternalForm());
    }

    public Image getCurrentImg() {
        return currentImg;
    }

    public String getDirection() {
        return direction;
    }
}
