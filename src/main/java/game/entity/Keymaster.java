package game.entity;

import game.gameControll.DialogueNode;
import game.gameControll.GamePanel;
import game.gameControll.ImageId;


/**
 * A keymaster NPC that presents a branching riddle/dialogue
 * to the player and can reward them with a key.
 */
public class Keymaster extends Entity {

    private DialogueNode lie, lie2, lie3, truth, truth2, truth3, truth4, intro, help, help2, help3, victim, key, ques1, wrong1, wrong2, wrong3;

    /**
     * Constructs the Keymaster and initializes its dialogue tree.
     *
     * @param gp       the GamePanel
     * @param imageId  sprite sheet identifier
     * @param x        initial x‐coordinate
     * @param y        initial y‐coordinate
     * @param scale    scale factor for rendering
     */
    public Keymaster(GamePanel gp, ImageId imageId, int x, int y, double scale) {
        super(gp, imageId, x, y, scale);
        buildDialogue();
        name = "The Keymaster";
        current = intro;
    }

    /**
     * Builds the full dialogue decision tree for this NPC.
     */
    private void buildDialogue() {
        DialogueNode END = new DialogueNode(null, null, null);

        key = new DialogueNode("Well, here you go and good luck!", new String[] {"I owe you one!"}, "KEY", END);


        help3 = new DialogueNode("The key unlocks any door, making moving around in prison easier.", new String[] {"Damn, kinda useless.", "GIMME GIMME!"}, null,END, key);

        truth4 = new DialogueNode("Ugh, here, let me give you a key.", new String[]{"What's the key for?"}, null, help3);

        wrong3 = new DialogueNode("Unfortunately, you are out of luck! Sorry I cannot help you.", new String[]{"<<Continue>>"}, null, END);
        wrong2 = new DialogueNode("Whoever makes it, sells it. Whoever buys it, doesn't use it. Whoever uses it, doesn't know they're using it.", new String[]{"An idea.", "A lie.", "A coffin."}, null, wrong3, wrong3, truth4);
        wrong1 = new DialogueNode("Nope, but since am feeling generous, I will give you one more chance.", new String[] {"Please, do.", "Do not bother, see ya!"}, null, wrong2, END);

        ques1 = new DialogueNode("I have cities, but no houses, I have forests, but no trees, I have rivers, but no water. What am I?", new String[] {"A diagram.", "A map.", "A radar."}, null, wrong1, truth4, wrong1);

        help2 = new DialogueNode("Alright, I will help you, if you answer my riddle.", new String[] {"Alright, bring it.", "Am in no mood for riddles old man."}, null, ques1, END);
        victim = new DialogueNode("And you need what exactly?", new String[] {"Just your help, any you can offer.", "I forgot, see ya!"}, null, help2, END);
        help = new DialogueNode("And you want my help?", new String[] {"Not really...", "I want to get out. Am innocent.", "Ehhh, if possible?"}, null,END, victim, help2);

        truth3 = new DialogueNode("Are you sure you don't want my help then? You seem quite desperate.", new String[] {"Positive!", "AM JUST KIDDING! PLEASE HELP!"}, null, END, help2);
        truth2 = new DialogueNode("So you are escaping...", new String[] {"Correct...", "NAAAAH, how do you figure that?"},null , help, truth3);
        truth = new DialogueNode("A fellow prisoner you say?", new String[] {"<<Continue>>"},null , truth2);

        lie3 = new DialogueNode("You do? How interesting... Do tell.", new String[] {"HA! I won't tell you a thing!", "A fellow prisoner sent me..."},null , END, truth);

        lie2 = new DialogueNode("Whatever. Do you need anything?", new String[] {"No, I apologise for bothering you.", "I was sent to meet you by a prisoner."},null ,END, truth);
        lie = new DialogueNode("A lie... But am too exhausted to care.", new String[] {"<<Continue>>", "Am sorry, I do need your help."},null , lie2, lie3);
        intro = new DialogueNode("WHOA, WHO ARE YOU AND WHAT DO YOU THINK YOU'RE DOING!", new String[] {"Ah, sightseeing Sir!", "I was sent to meet you by a fellow prisoner"}, null, lie, truth);
        root = intro;
        current = intro;
    }


    @Override
    public void getEntityImage() {
        standing1 = "/Keymaster/keymaster1.png";
        standing2 = "/Keymaster/keymaster2.png";
        standing3 = "/Keymaster/keymaster3.png";
        standing4 = "/Keymaster/keymaster4.png";
    }


    /**
     * Advances animation frame for the Keymaster.
     * Called each tick to animate standing sprite.
     */
    public void update() {
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
            chooseImageId();}
    }

    /**
     * Chooses which standing image should be current
     * based on the sprite counter.
     */
    public void chooseImageId() {
        String imageId = null;

        if (spriteNum == 1) imageId = standing1;
        if (spriteNum == 2) imageId = standing2;
        if (spriteNum == 3 || spriteNum == 4) imageId = standing3;
        if (spriteNum == 5) imageId = standing4;

        ImageId.KEYMASTER.setFileName(imageId);
    }
}
