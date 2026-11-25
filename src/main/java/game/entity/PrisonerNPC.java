package game.entity;

import game.gameControll.DialogueNode;
import game.gameControll.GamePanel;
import game.gameControll.ImageId;

/**
 * A prison cell NPC who provides hints or items
 * through a multi‐branch dialogue tree.
 */
public class PrisonerNPC extends Entity { ;
    private GamePanel gp;


    private DialogueNode letterYes, letterNo, letterWhat, letterWhat2, letter, warden, warden2, theWay, theWay2, suicide, lie, truth, deceive, intro, keymaster;


    /**
     * Constructs the PrisonerNPC and sets up its dialogue.
     *
     * @param gp    the GamePanel
     * @param id    sprite sheet identifier
     * @param x     x‐coordinate
     * @param y     y‐coordinate
     * @param scale scale factor
     */
    public PrisonerNPC(GamePanel gp, ImageId id, int x, int y, double scale) {
        super(gp, id, x, y, scale);
        this.gp = gp;
        buildDialogue();
        name = "Prisoner";
        current = intro;
    }

    /**
     * Builds the branching dialogue tree for the prisoner.
     */
    private void buildDialogue() {
        DialogueNode END = new DialogueNode(null, null, null);

        letterYes = new DialogueNode("I really appreciate it mate! Good luck!", new String[] {"<<Continue>>"}, "LETTER", END);
        letterNo = new DialogueNode("Well! At least I tried!", new String[] {"<<Continue>>"}, null, END);
        letterWhat2 = new DialogueNode("Lets just call it a favor from me to you.", new String[] {"Alright, gimme it.", "Hmm, I don't trust you..."}, "SURE", letterYes, letterNo);
        letterWhat = new DialogueNode("It will increase the likelyhood of him letting you go.", new String[] {"<<Continue>>"}, null, letterWhat2);
        letter = new DialogueNode("That reminds me! Would you be willing to deliver this letter to the Warden?", new String[] {"What is in the letter?", "Am no postman, sorry", "Sure, no worries!"}, null, letterWhat, letterNo, letterYes);

        warden2 = new DialogueNode("Be sincere with him and he might even let you leave...", new String[] {"Anything else?", "Alright, I appreciate the advice!"}, null, letter, END);
        warden = new DialogueNode("The Warden ey? No matter what anyone says, he's a good guy.", new String[] {"<<Continue>>"}, null, warden2);

        keymaster = new DialogueNode("No idea! He never stays in one place for long.", new String[] {"Hmm. I will find him then.", "And the Warden?"}, null, END, warden);

        theWay2 = new DialogueNode("Hmm... OH! You might wanna have a chat with the Keymaster.", new String[] {"Not Interested.", "Where do I find him?"}, null, END, keymaster);
        theWay = new DialogueNode("Well, you might want to avoid both the fireballs and the patrolls.", new String[] {"Anything else?", "What about the warden? Should I be concerned?", "That's all thank you!"}, null, theWay2, warden, END);
        suicide = new DialogueNode("Yeeeeeah... GO GET HIM TIGER!", new String[] {"<<Continue>>"}, null, END);

        deceive = new DialogueNode("Hmmm, well then if you say so!", new String[] {"<<Continue>>"}, null, END);
        truth = new DialogueNode("Of course! Just keep going straight! But you will run into the Warden soon...", new String[] {"The Warden? Is there something I need to know?", "That's fine! I wanted to give him a piece of my mind!", "Is there anything else I should look out for?"},null, warden, suicide, theWay);
        lie = new DialogueNode("Uh huh, so you don't mind if I just call the guards?", new String[] {"NO! Ok I lied! I need to find a way out...", "Yep, nothing to see here...", "<<RUN>>"}, null, truth, deceive, END);

        intro = new DialogueNode("What you doin? Escaping prison are we?", new String[] {"Nah, just hangin around!", "Yes, any ideas on how to get out of here?"}, null, lie, truth);
        root = intro;
        current = intro;
    }


    @Override
    public void getEntityImage() {
        standing1 = "/Environment/npccell1.png";
        standing2 = "/Environment/npccell2.png";
        standing3 = "/Environment/npccell3.png";
        standing4 = "/Environment/npccell4.png";
    }

    /**
     * Updates the NPC’s standing animation frames.
     */
    public void update() {
        spriteCounter++;
        if (spriteCounter > 13) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2 || spriteNum == 7) {
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

            chooseImageId();}
    }

    public void chooseImageId() {
        String imageId = null;

        if (spriteNum2 == 1) imageId = standing1;
        if (spriteNum2 == 3) imageId = standing2;
        if (spriteNum2 == 5 || spriteNum2 == 6) imageId = standing3;
        if (spriteNum2 == 7) imageId = standing4;

        ImageId.NPCCELL.setFileName(imageId);
    }

}