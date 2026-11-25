package game.gameControll;


/**
 * A single node in a branching dialogue tree.
 * Holds the NPC’s line, the player’s choice options,
 * and pointers to subsequent DialogueNode(s).
 */
public class DialogueNode {
    public final String npcLine;
    public final String[] playerChoices;
    public final DialogueNode[] next;
    public String note;

    /**
     * Creates a new dialogue node.
     *
     * @param npcLine       the NPC’s dialogue text
     * @param playerChoices array of player choice strings
     * @param note          optional note (e.g., reward flag)
     * @param next          subsequent DialogueNode(s), in order
     */
    public DialogueNode(String npcLine, String[] playerChoices, String note, DialogueNode... next) {
        this.npcLine = npcLine;
        this.playerChoices = playerChoices;
        this.next = next;
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
