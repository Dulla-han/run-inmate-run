package game.entity;

import game.gameControll.DialogueNode;
import game.gameControll.GamePanel;
import game.gameControll.ImageId;

/**
 * The prison warden NPC; leads final dialogue sequence
 * based on the player’s performance.
 */
public class Warden extends Entity {


    private DialogueNode intro, greeting, question1, question2, question3, stalker, apology, fight, fight2, continue1, continue2, escape1, trouble1, letter1, letter2, letter3, death,
            pride1, facility1, people, people2, people3, architecture, thrill, strength0, nomercy1, nomercy2, apologetic, giveUp, sympathetic1, sympathetic2,
            humility1, humility2, humility2A, humility2B, humilityBad1, humility3A, humilityBad2B, humilityBad2, humility2B2, humilityFail, desperate1, regret1,
            finish1, finish2, finish3;

    /**
     * Constructs the Warden.
     *
     * @param gp       the GamePanel
     * @param imageId  sprite sheet identifier
     * @param x        x‐coordinate
     * @param y        y‐coordinate
     * @param scale    scale factor
     */
    public Warden(GamePanel gp, ImageId imageId, int x, int y, double scale) {
        super(gp, imageId, x, y, scale);
        name = "The Warden";
    }

    /**
     * Builds the endgame dialogue tree based on dialogueScore.
     */
    public void buildDialogue() {
        DialogueNode END = new DialogueNode(null, null, null);

        if (dialogueScore < 0) {
            finish3 = new DialogueNode("I see, well be here for while so you will have plenty of time to change your opinion!", new String[] {"<<Continue>>"}, "OVER", END);
        } else {
            finish3 = new DialogueNode("You are a fine man. Freedom is yours, take it.", new String[] {"<<Continue>>"}, "WIN", END);
        }

        finish2 = new DialogueNode("How did you enjoy the prison experience?", new String[] {"I enjoyed it a lot.", "I guess it was alright", "It sucked, I hope to never return."}, null, finish3);
        finish1 = new DialogueNode("Well then, I have just one more question to ask you.", new String[] {"Ask away."}, null, finish2);

        //Humility route
        humilityFail = new DialogueNode("Enough talk! Guards, return him to his cell!", new String[]{"<<Continue>>"}, "OVER", END);

        humility3A = new DialogueNode("Then tell me: how would you describe the prison natives?", new String[]{"Good people, rather helpful.", "Terrible, could they be any worse?"}, null, people, people3);
        humility2B2 = new DialogueNode("For whatever reason, everyone does when standing in my presence.", new String[]{"<<Continue>>"}, null, desperate1);

        humilityBad2B = new DialogueNode("HA! You are a funny one, I will give you that.", new String[]{"Appreciate it.", "If you say so."}, null, desperate1, desperate1);
        regret1 = new DialogueNode("Good. Listening is the first step in learning.", new String[]{"I appreciate your patience.", "Just kidding. Let me go back to my cell."}, "good", humility3A, giveUp);

        desperate1 = new DialogueNode("Everybody is desperate, regardless, I will not tolerate lying.", new String[]{"I understand.", "Whatever, old man."}, null, regret1, humilityFail);

        humilityBad2 = new DialogueNode("This is where you are wrong, it still might.", new String[]{"Forgive me, I’ll be honest.", "This is ridiculous."}, null, regret1, giveUp);

        humilityBad1 = new DialogueNode("You’re full of yourself, prisoner.", new String[]{"Sorry, that was rude.", "I don’t care what you say."}, "bad", regret1, giveUp);

        humility2B = new DialogueNode("Truth-tellers are scarce. What makes you so sure?", new String[]{"I prefer truth over deceit.", "Truth won’t save me now.", "I’ll say what it takes."}, null, humility2B2, humilityBad2, humility2B2);
        humility2A = new DialogueNode("Innocent? That’s what they all claim. Why should I buy yours?", new String[]{"I can only offer my honest word.", "I’m terrible at lying."}, null, humility3A, humilityBad2B);

        humility2 = new DialogueNode("Speak your piece.", new String[]{"I’m innocent of these charges.", "I won’t hide the truth.", "Keep your mercy. I don’t need it."}, null, humility2A, humility2B, humilityBad1);
        humility1 = new DialogueNode("So, you want mercy? Convince me.", new String[]{"<<Continue>>"}, null, humility2);

        //Pride route
        giveUp = new DialogueNode("Very well.", new String[] {"<<Continue>>"}, "OVER", END);
        nomercy2 = new DialogueNode("You deserve to spend eternity here.", new String[] {"That was rude of me, I apologise.", "It is what is."}, "bad", finish1, giveUp);
        nomercy1 = new DialogueNode("Then you did not value any help nor advice we provided.", new String[] {"<<Continue>>"}, null, nomercy2);
        apologetic = new DialogueNode("You make it hard to believe.", new String[] {"<<Continue>>"}, "good", finish1);

        sympathetic1 = new DialogueNode("Do not be, I am glad I told you.", new String[] {"<<Continue>>"}, "good", finish1);
        strength0 = new DialogueNode("Do you have no sympathy at all?", new String[] {"None, since you guys are the ones keeping me here.", "Of course I do."}, "bad", nomercy1, apologetic);
        people = new DialogueNode("That makes me very glad, everyone of us did not chose to be here, nonetheless, here we are.", new String[] {"And your strength? Is it really incredible?", "I see, sounds painful, am sorry."}, "good", strength0, sympathetic1);

        people3 = new DialogueNode("YOU HAVE NO RIGHT TO JUDGE. Everyone of us did not chose to be here, nonetheless, here we are.", new String[] {"So? Cry me a river.", "Did not know that, am sorry."}, "bad", giveUp, apologetic);

        sympathetic2 = new DialogueNode("No, you don't.", new String[] {"<<Continue>>"}, null, people2);
        people2 = new DialogueNode("Everyone of us did not chose to be here, nonetheless, here we are.", new String[] {"And your strength? Is it really incredible?", "I see, sounds painful, am sorry."}, null, strength0, sympathetic1);
        architecture = new DialogueNode("I personally hate it. Being here for eternity is depressing.", new String[] {"The natives are not bad.", "I feel you."}, null, people2, sympathetic2);
        thrill = new DialogueNode("As did I, but how were the natives? Were they helpful?", new String[] {"Yes, beyond helpful.", "They were alright I guess.", "The people were the worst part, don't even get me started."}, "good", people, people2, people3);
        facility1 = new DialogueNode("It is spectacular, since we are on the topic what was your favorite part?", new String[] {"The people here aren't bad, meeting them elsewhere would mean becoming friends.", "I liked the architecture the best, cell designs are beautiful!", "I enjoyed the thrill of escaping."}, null, people, architecture, thrill);
        pride1 = new DialogueNode("...", new String[] {"This prison is an incredible facility!"}, null, facility1);

        //Introduction

        escape1 = new DialogueNode("As to what should I ask you... Hmmm.        *Choose an attitude with which you will approach the warden*", new String[] {"*Appeal to his pride*", "*Show humility*"}, null, pride1, humility1);

        fight2 = new DialogueNode("If you wish to rot in here then by my guest.", new String[] {"On second thought I don't.", "I don't need your help!"}, "very bad", escape1, death);

        letter3 = new DialogueNode("Well I will be damned. Alright I will give you a chance to escape.", new String[] {"I appreciate it.", "Like I would want something from you!"}, null, escape1, fight2);
        letter2 = new DialogueNode("That so? hmmmmm... Interesting...", new String[] {"Hmmmmm..."}, null, letter3);
        letter1 = new DialogueNode("What is it?", new String[] {"I was asked to give you this by a prisoner.", "Something I picked up on the way.", "Honestly I have no idea myself."}, "very good", letter2, letter2, letter2);

        continue2 = new DialogueNode("Not easily, but yes. Satisfy me and you will be free to go.", new String[] {"I appreciate it.", "Like I would want something from you!"}, null, escape1, fight2);

        if (!gamePanel.getInventory().hasLetter()) {continue1 = new DialogueNode("I do! I had a great time watching you struggle! I grew quite fond of you.", new String[]{ "So will you allow me to leave?" }, "good", continue2);
        } else {continue1 = new DialogueNode("I do! I had a great time watching you struggle! I grew quite fond of you.", new String[]{ "So will you allow me to leave?", "<<Give Letter>>" }, "good", continue2, letter1);}
        trouble1 = new DialogueNode("I thought that's obvious, you are trying to escape MY prison after all.", new String[]{"So will you allow me to leave?", "<<Give Letter>>"}, "good", continue2, letter1);

        question3 = new DialogueNode("Quite the heist you pulled!", new String[] {"Well if you insist..", "Am I in trouble?"}, null, continue1, trouble1);
        apology = new DialogueNode("Well anyway...", new String[] {"<<Continue>>"}, "good", question3);

        death = new DialogueNode("So you have chosen death... How foolish.", new String[] {"<<Continue>>"}, "OVER", END);

        fight = new DialogueNode("You are on thin ice, kid. I could vaporize you in an instant", new String[] {"Try me.", "Ok, I am sorry."}, "very bad", death, apology);

        stalker = new DialogueNode("Am THE WARDEN kid. This is my realm you roam, my dominion you're trespassing.", new String[] {"My bad.", "You wish."}, "bad", apology, fight);
        question2 = new DialogueNode("Yeah well, you got caught " + gamePanel.getPlayer().getDeathCount() + " times... And stolen " + gamePanel.getInventory().getCoinCounter() + " coins...", new String[] {"You were stalking me?", "..."}, null, stalker, question3);
        question1 = new DialogueNode("Of course! You made a quite a name for yourself.", new String[] {"So it would seem.", "How do you of me?"}, null, question2, question2);

        greeting = new DialogueNode("You have made quite an impression on me!", new String[] {"Do you know me?", "And here I thought I did terribly."}, null, question1, question2);

        intro = new DialogueNode("Hail prisoner.", new String[] {"Hail Warden."}, null, greeting);

        root = intro;
        current = intro;
    }

    @Override
    public void getEntityImage() {
        standing1 = "/Warden/warden1.png";
        standing2 = "/Warden/warden2.png";
        standing3 = "/Warden/warden3.png";
        standing4 = "/Warden/warden4.png";
    }

    /**
     * Advances the warden’s standing animation.
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

    public void chooseImageId() {
        String imageId = null;

        if (spriteNum == 1) imageId = standing1;
        if (spriteNum == 2) imageId = standing2;
        if (spriteNum == 3 || spriteNum == 4) imageId = standing3;
        if (spriteNum == 5) imageId = standing4;

        ImageId.WARDEN.setFileName(imageId);
    }
}
