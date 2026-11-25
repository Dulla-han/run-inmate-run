package game.items;

import game.gameControll.ImageId;

/**
 * A letter collectible that may contain lore or clues.
 */
public class Letter extends Item {

    /**
     * Constructs a Letter with the given sprite ID.
     *
     * @param imageId the ImageId for the letter sprite
     */
    public Letter(ImageId imageId) {
        super(imageId);
    }

}
