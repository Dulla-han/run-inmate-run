package game.gameControll;

/**
 * Identifiers for all loaded images (tiles, entities, items).
 * Each enum constant holds its resource path and runtime dimensions.
 */
public enum ImageId {
    //Environment
    WALL("/Environment/wallbrick.png"),
    FLOOR("/Environment/floorbrick.png"),
    DOOR("/Environment/door.png"),
    PASSAGE("/Environment/passage.png"),
    CELL("/Environment/emptycell.png"),

    //Items
    CLOCK("/Items/clock.png"),
    LAMP("/Items/lamp.png"),
    KEY("/Items/key.png"),
    LETTER("/Items/letter.png"),
    COINS("/Items/coins.png"),

    //ability
    ABILITY("/Ability/ability.png"),

    //animated
    NPCCELL("/Environment/npccell1.png"),
    PLAYER("/Player/standing1.png"),
    PATROLL("/Patroll/NPCstanding1.png"),
    FIREBALL("/Fireball/fireballright1.png"),
    KEYMASTER("/Keymaster/keymaster1.png"),
    WARDEN("/Warden/warden1.png");


    private String fileName;
    private double width;
    private double height;

    ImageId(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
