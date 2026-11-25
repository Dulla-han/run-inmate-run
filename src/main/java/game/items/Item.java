package game.items;

import game.gameControll.ImageId;

/**
 * Base class for all collectible items.
 * Holds the sprite identifier and basic info.
 */
public class Item {
    protected ImageId imageId;

    /**
     * Constructs an Item with the given sprite ID.
     *
     * @param imageId the ImageId for rendering this item
     */
    public Item(ImageId imageId) {
        this.imageId = imageId;
    }

    public ImageId getImageId() {
        return imageId;
    }

    public String getItem() {
        return imageId.toString();
    }
}
