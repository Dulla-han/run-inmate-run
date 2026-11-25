package game.items;

import game.gameControll.ImageId;

/**
 * A key collectible used to unlock doors.
 */
public class Key extends Item{

    /**
     * Constructs a Key with the given sprite ID.
     *
     * @param imageId the ImageId for the key sprite
     */
    public Key(ImageId imageId) {
        super(imageId);
    }
}
