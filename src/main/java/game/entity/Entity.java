package game.entity;
import game.gameControll.DialogueNode;
import game.gameControll.GamePanel;
import game.gameControll.GameState;
import game.gameControll.ImageId;


/**
 * Base class for all in‐game entities (player, NPCs, projectiles).
 * Handles position, sprite animation, and basic dialogue.
 */
public abstract class Entity {

    protected double x, y;
    protected int speed;
    protected ImageId imageId;
    protected double entityScale;
    protected GamePanel gamePanel;
    public boolean inDialogue = false;

    public String up1, up2, up3, up4, down1, down2, down3, down4,
            left1, left2, left3, left4, right1, right2, right3, right4,
            standing1, standing2, standing3, standing4;

    protected String direction;
    protected String name;

    protected DialogueNode current;
    protected DialogueNode root;

    //animation counters
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public int standingCounter;

    //for slower standing
    public int spriteNum2 = 1;

    protected int tileSize;

    protected int dialogueScore;


    /**
     * Initializes common entity properties.
     *
     * @param gp           the GamePanel instance
     * @param imageId      identifier for the entity’s sprite sheet
     * @param x            initial x‐coordinate (pixels)
     * @param y            initial y‐coordinate (pixels)
     * @param entityScale  scale factor for rendering the sprite
     */
    public Entity(GamePanel gp, ImageId imageId, int x, int y, double entityScale) {
        this.gamePanel = gp;
        this.imageId = imageId;
        this.x = x;
        this.y = y;
        this.tileSize = gamePanel.getTileSize();
        this.entityScale = entityScale;
        getEntityImage();
        imageId.setFileName(standing1);
    }


    /**
     * Called when the player interacts with this entity.
     * By default, may open dialogue if implemented.
     */
    public void interact() {
        if (inDialogue) return;

        if (this instanceof Warden) {
            gamePanel.getClock().stopClock();
            ((Warden)this).buildDialogue();
        }

        inDialogue = true;
        gamePanel.setGameState(GameState.DIALOGUE);
        current = root;
        presentNode();
    }

    /**
     * Advances the current dialogue node based on player choice.
     * Ends dialogue and returns to play state when complete.
     */
    public void tickDialogue() {
        if (!inDialogue) return;

        int choice = gamePanel.pollChoiceKey();
        if (choice == 0) return;
        if (choice > current.next.length) return;

        if (this instanceof Warden && current == root && gamePanel.pollChoiceKey() > 0) {
            ((Warden)this).buildDialogue();
            current = root;
            presentNode();
            return;
        }

        DialogueNode next = current.next[choice - 1];

        String note = current.getNote();
        if ("LETTER".equals(note)) {
            gamePanel.getInventory().addToInventory(gamePanel.getLetter());
            current.setNote(null);
        } else if ("KEY".equals(note)) {
            gamePanel.getInventory().addToInventory(gamePanel.getKey());
            current.setNote(null);
        } else if ("OVER".equals(note)) {
            gamePanel.getGameOver().show();
        } else if ("WIN".equals(note)) {
            gamePanel.getGameWon().show();
        } else if ("good".equals(note)) {
            dialogueScore++;
        } else if ("bad".equals(note)) {
            dialogueScore--;
        } else if ("very good".equals(note)) {
            dialogueScore++;
            dialogueScore++;
        } else if ("very bad".equals(note)) {
            dialogueScore--;
            dialogueScore--;
        }

        if (next.npcLine == null) {
            System.out.println("Finished Dialogue");

            gamePanel.finishDialogue();
            inDialogue = false;
            return;
        }
        current = next;
        presentNode();
    }

    /**
     * Displays the NPC’s current dialogue line and player options.
     */
    public void presentNode() {
        gamePanel.showMessage(current.npcLine, name);
        gamePanel.showOptions(current.playerChoices);
    }

    public String getDirection() {
        return direction;
    }

    public ImageId getImageId() {
        return imageId;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public abstract void getEntityImage();

    /**
     * @return the x‐coordinate of the center point (for collision/vision)
     */
    public double getCenterX() {
        double size = gamePanel.getTileSize() * entityScale;
        return x + size/2;
    }

    /**
     * @return the y‐coordinate of the center point (for collision/vision)
     */
    public double getCenterY() {
        double size = gamePanel.getTileSize() * entityScale;
        return y + size/2;
    }

}
