package game.gameControll;

import com.google.gson.*;
import game.ability.Ability;
import game.entity.*;
import game.gameControll.screen.*;
import game.items.*;
import game.entity.*;
import game.gameControll.screen.*;
import game.items.*;
import game.world.RoomManager;
import game.world.Room;
import game.world.TileMap;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.Map;

/**
 * The primary game panel: sets up the canvas, loads all assets,
 * runs the game loop, handles input, game‐state switching,
 * entity updates and rendering.
 */
public class GamePanel extends StackPane {

    private final Map<ImageId, Image> images = new EnumMap<>(ImageId.class);

    private static final int ORIGINAL_TILE_SIZE = 32;
    private static final int SCALE = 5;
    private static final int FPS = 45;

    protected int tileSize = ORIGINAL_TILE_SIZE * SCALE;

    private final int maxScreenCol = 11;
    private final int maxScreenRow = 7;
    private final int screenWidth  = tileSize * maxScreenCol;
    private final int screenHeight = tileSize * maxScreenRow;

    private final double playerScale = 0.9;
    private final double patrollScale = 1.5;
    private final double wardenScale = 1.8;
    private final double fireBallScale = 1.0;
    private final double keymasterScale = 1.5;

    private final Player player;

    private double envOffsetX = 0;
    private double envOffsetY = 0;

    protected final Controlls keyH = new Controlls();

    private final Inventory inventory;
    private final Lamp lamp;
    private final Key key;
    private final Clock clock;
    private final Letter letter;

    protected final Ability ability;

    protected final RoomManager roomManager;
    private Room curRoom;

    private final Canvas canvas;
    private final GraphicsContext gc;

    private GameState state = GameState.MAIN_MENU;

    private String npcName = null;
    private String npcLine = null;
    private String[] currentOptions = null;

    private final MainMenu mainMenu;
    private final RespawnMenu respawnMenu;
    private final PauseMenu pauseMenu;
    private final GameOver gameOver;
    private final GameWon gameWon;

    private static final Path SAVE_FILE = Paths.get(System.getProperty("user.home"), ".gameon-save.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


    /**
     * Constructs the GamePanel, binds its canvas to this pane,
     * loads images, and starts the animation timer.
     */
    public GamePanel () {
        loadImages();

        canvas = new Canvas(screenWidth, screenHeight);

        gc = canvas.getGraphicsContext2D();

        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());

        getChildren().add(canvas);
        roomManager = new RoomManager();
        player = new Player(this, ImageId.PLAYER, screenWidth / 2 - tileSize / 2, screenHeight / 2 - tileSize / 2, 6);
        inventory = new Inventory(this, keyH);

        roomManager.getRoomFiles(this);
        ability = new Ability(this);

        lamp = new Lamp(ImageId.LAMP, this, keyH, player);
        clock = new Clock(ImageId.CLOCK, 15, this);
        inventory.addToInventory(lamp);
        inventory.addToInventory(clock);

        key = new Key(ImageId.KEY);
        letter = new Letter(ImageId.LETTER);

        gameWon = new GameWon(this);

        mainMenu = new MainMenu(this);
        respawnMenu = new RespawnMenu(this);
        pauseMenu = new PauseMenu(this);
        gameOver = new GameOver(this);

        mainMenu.show();
        centerCameraOn(player.getX(), player.getY());

        curRoom = roomManager.getCurrentRoom();
        player.setCurrentRoom(curRoom);
        clock.start();
        startGameLoop();
    }

    /**
     * Starts the JavaFX AnimationTimer that drives update/render at the target FPS.
     */
    public void startGameLoop() {
        AnimationTimer gameLoop = new AnimationTimer() {
            double drawInterval = 1_000_000_000.0 / FPS;
            double delta = 0;
            long lastTime = System.nanoTime();
            long currentTime;
            long timer = 0;
            int drawCount = 0;

            @Override
            public void handle(long now) {
                currentTime = System.nanoTime();
                timer += (currentTime - lastTime);
                delta += (currentTime - lastTime) / drawInterval;
                lastTime = currentTime;

                if (delta >= 1) {
                    update();
                    delta--;
                    drawCount++;
                }

                if (timer >= 1_000_000_000) {
                    System.out.println("FPS: " + drawCount);
                    System.out.println("PLAYER X: " + player.getX() + ", Y: " + player.getY());
                    drawCount = 0;
                    timer = 0;
                }
            }
        };
        gameLoop.start();
    }

    /**
     * Handles a key‐press event: updates Controlls flags,
     * toggles pause and inventory, or selects dialogue options.
     *
     * @param code the KeyCode of the pressed key
     */
    public void keyPressed(KeyCode code) {
        if (state == GameState.DIALOGUE) {
            switch (code) {
                case DIGIT1 -> keyH.setOpt1Pressed(true);
                case DIGIT2 -> keyH.setOpt2Pressed(true);
                case DIGIT3 -> keyH.setOpt3Pressed(true);
                case DIGIT4 -> keyH.setOpt4Pressed(true);
            }
            return;
        }

        switch (code) {
            case W -> keyH.setUpPressed(true);
            case S -> keyH.setDownPressed(true);
            case A -> keyH.setLeftPressed(true);
            case D -> keyH.setRightPressed(true);
            case I -> keyH.setInventoryPressed(!keyH.isInventoryPressed());
            case E -> keyH.setLampActivated(!keyH.isLampActivated());
            case SPACE -> keyH.setInteractPressed(true);
            case R -> keyH.setAbilityPressed(!keyH.isAbilityPressed(), ability);
            case ESCAPE -> { keyH.setPausedPressed(!keyH.isPausedPressed(), state, this); if (keyH.isPausedPressed()) pauseMenu.show(); else pauseMenu.hide(); }
        }
    }

    /**
     * Handles a key‐release event: clears movement or dialogue‐choice flags.
     *
     * @param code the KeyCode of the released key
     */
    public void keyReleased(KeyCode code) {
        if (state == GameState.DIALOGUE) {
            switch (code) {
                case DIGIT1 -> keyH.setOpt1Pressed(false);
                case DIGIT2 -> keyH.setOpt2Pressed(false);
                case DIGIT3 -> keyH.setOpt3Pressed(false);
                case DIGIT4 -> keyH.setOpt4Pressed(false);
            }
            return;
        }

        switch (code) {
            case W -> keyH.setUpPressed(false);
            case S -> keyH.setDownPressed(false);
            case A -> keyH.setLeftPressed(false);
            case D -> keyH.setRightPressed(false);
            case SPACE -> keyH.setInteractPressed(false);
        }
    }


    /**
     * Advances game logic on each tick: updates player, NPCs,
     * projectiles, dialogue state, and environment offsets.
     * Does nothing if in a non‐playing state.
     */
    public void update() {
        if ((GameState.RESPAWNING == state) || (GameState.PAUSED == state) || (GameState.MAIN_MENU == state) || state == GameState.GAME_OVER || state == GameState.GAME_WON) {
            return;
        }

        curRoom = roomManager.getCurrentRoom();

        player.update();
        clock.update();

        for (PatrollingNPC npc : curRoom.getPatrolls()) npc.update(player);
        for (Fireball fireball : curRoom.getFireballs()) fireball.update();
        for (PrisonerNPC pnpc : curRoom.getPrisonNPCs()) {
            if (state == GameState.DIALOGUE) { pnpc.tickDialogue(); pnpc.update(); }
            else pnpc.update();}
        for (Warden warden : curRoom.getWardens()) { warden.tickDialogue(); warden.update(); }
        for (Keymaster keymaster : curRoom.getKeymasters()) {
            if (state == GameState.DIALOGUE) { keymaster.tickDialogue(); keymaster.update(); }
            else keymaster.update();
        }

        //check again after player.update
        if (state == GameState.RESPAWNING || state == GameState.PAUSED || state == GameState.GAME_OVER || state == GameState.GAME_WON) {
            return;
        }
        render();
    }

    /**
     * Renders the entire scene: floor, walls, entities, GUI overlays,
     * ability effects, lighting, clock, inventory, and dialogue.
     */
    public void render() {

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setImageSmoothing(false);
        reloadEntityImage(ImageId.PLAYER);

        double sx = canvas.getWidth()  / screenWidth;
        double sy = canvas.getHeight() / screenHeight;
        double s  = Math.min(sx, sy);

        gc.save();
        gc.scale(s, s);
        gc.translate(envOffsetX, envOffsetY);

        drawTileMap();
        reloadEntityImage(ImageId.NPCCELL);

        // coins
        for (Coins coin : curRoom.getCoins()) {
            if (!coin.isOnGround()) continue;
            Image img = images.get(ImageId.COINS);
            double size = tileSize * 0.12 * coin.getValue() / 2;
            gc.drawImage(img, coin.getX(), coin.getY(), size, size);
        }

        // patrollers
        for (PatrollingNPC npc : curRoom.getPatrolls()) {
            Image img = npc.getCurrentImg();
            if (img == null) continue;
            drawPatrollRadius(npc);
            gc.drawImage(img, npc.getX(), npc.getY(), patrollScale * tileSize, patrollScale * tileSize);}

        // fireballs
        for (Fireball fireball : curRoom.getFireballs()) {
            Image img = fireball.getCurrentImg();
            if (img == null) continue;
            drawFireballEffect(fireball);
            gc.drawImage(img, fireball.getX(), fireball.getY(), fireBallScale * tileSize, fireBallScale * tileSize);
        }

        // wardens
        for (Warden warden : curRoom.getWardens()) {
            Image img = images.get(ImageId.WARDEN);
            if (img == null) continue;
            drawWardenEffect(warden);
            gc.drawImage(img, warden.getX(), warden.getY(), wardenScale * tileSize, wardenScale * tileSize);
            reloadEntityImage(ImageId.WARDEN);
        }

        // keymasters
        for (Keymaster keymaster : curRoom.getKeymasters()) {
            Image img = images.get(ImageId.KEYMASTER);
            if (img == null) continue;
            gc.drawImage(img, keymaster.getX(), keymaster.getY(), keymasterScale * tileSize, keymasterScale * tileSize);
            reloadEntityImage(ImageId.KEYMASTER);
        }

        Image playerImg = images.get(ImageId.PLAYER);
        gc.drawImage(playerImg, player.getX(), player.getY(), playerScale * tileSize, playerScale * tileSize);

        gc.restore();
        if (ability.isActive()) drawAbility();
        lamp.update();
        clock.draw(gc);
        inventory.drawInventory(images);
        ability.cooldownUpdate(gc, images);

        if (currentOptions != null && currentOptions.length > 0) {
            drawDialogue();
        }
    }

    /**
     * Loads all sprite sheets and tile images into the internal map.
     * Skips any ImageId with a null or missing resource path.
     */
    private void loadImages() {
        for (ImageId imageId : ImageId.values()) {
            String fileName = imageId.getFileName();
            if (fileName != null && !fileName.isEmpty()) {
                var resourceUrl = getClass().getResource(fileName);
                if (resourceUrl != null) {
                    Image image = new Image(resourceUrl.toExternalForm());
                    imageId.setWidth(image.getWidth());
                    imageId.setHeight(image.getHeight());
                    images.put(imageId, image);
                } else {
                    System.out.println("Resource not found: " + fileName);
                }
            } else {
                System.out.println("Skipping " + imageId + " because filename is null or empty.");
            }
        }
    }

    /**
     * Reloads a single entity image at runtime (for animated sprites).
     *
     * @param imageId the identifier of the image to refresh
     */
    private void reloadEntityImage(ImageId imageId) {
        String fileName = imageId.getFileName();
        if (fileName != null) {
            var resourceUrl = getClass().getResource(fileName);
            if (resourceUrl != null) {
                Image image = new Image(resourceUrl.toExternalForm());
                imageId.setWidth(image.getWidth());
                imageId.setHeight(image.getHeight());
                images.put(imageId, image);
            } else {
                System.out.println("Resource not found: " + fileName);
            }
        }
    }




    /**
     * Renders the tile map grid for the active room.
     */
    public void drawTileMap() {
        Room room = roomManager.getCurrentRoom();
        if (room == null) return;

        TileMap tm = room.getTileMap();
        for (int r = 0; r < tm.getRows(); r++) {
            for (int c = 0; c < tm.getCols(); c++) {
                int posX = c * tileSize;
                int posY = r * tileSize;

                if (room.getTileMap().isPassageNext(r, c) || room.getTileMap().isPassageBack(r, c)) {
                    Image passageImage = images.get(ImageId.PASSAGE);
                    if (passageImage != null) {
                        gc.drawImage(passageImage, posX, posY, tileSize, tileSize);
                    }
                } else if (room.getTileMap().isEmptyCell(r, c)) {
                    Image cellImage = images.get(ImageId.CELL);
                    if (cellImage != null) {
                        gc.drawImage(cellImage, posX, posY, tileSize, tileSize);
                    }
                } else if (room.getTileMap().isDoor(r, c)) {
                    Image doorImage = images.get(ImageId.DOOR);
                    if (doorImage != null) {
                        gc.drawImage(doorImage, posX, posY, tileSize, tileSize);
                    }
                } else if (room.getTileMap().isNpcCell(r, c)) {
                    Image npcCellImage = images.get(ImageId.NPCCELL);
                    if (npcCellImage != null) {
                        gc.drawImage(npcCellImage, posX, posY, tileSize, tileSize);
                    }
                } else if (room.getTileMap().isSolid(r, c)) {
                    Image wallImage = images.get(ImageId.WALL);
                    if (wallImage != null) {
                        gc.drawImage(wallImage, posX, posY, tileSize, tileSize);
                    }
                } else {
                    Image floorImage = images.get(ImageId.FLOOR);
                    if (floorImage != null) {
                        gc.drawImage(floorImage, posX, posY, tileSize, tileSize);
                    }
                }
            }
        }
    }

    /**
     * Shifts the entire world offset (camera) by the given delta.
     *
     * @param dx change in X offset (pixels)
     * @param dy change in Y offset (pixels)
     */
    public void shiftEnvironment(double dx, double dy) {
        envOffsetX += dx;
        envOffsetY += dy;
    }

    /**
     * Draws a red translucent field‐of‐view arc or circle
     * around the given PatrollingNPC.
     *
     * @param npc the PatrollingNPC whose vision cone to render
     */
    private void drawPatrollRadius(PatrollingNPC npc) {
        double centerX = npc.getCenterX();
        double centerY = npc.getCenterY();
        double radius = keyH.isLampActivated() ? tileSize * 3 : tileSize * 1.5;
        double fov = 110;

        gc.setFill(Color.color(1, 0, 0, 0.3));

        if ("standing".equals(npc.getDirection())) {
            radius = keyH.isLampActivated() ? tileSize * 2 : tileSize * 1.5;
            gc.fillOval(centerX - (radius / 2 * 1.2), centerY - (radius / 2 * 1.2), radius * 1.2, radius * 1.2);
            return;
        }
        double dirAngle;
        switch (npc.getDirection()) {
            case "up":
                dirAngle = 90;
                break;
            case "down":
                dirAngle =  270;
                break;
            case "left":
                dirAngle = 180;
                break;
            case "right":
                dirAngle =   0;
                break;
            default:
                return;
        }

        double startAngle = dirAngle - fov / 2;

        gc.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, startAngle, fov, ArcType.ROUND);
    }

    /**
     * Draws the glowing pulsing ability effect centered on the player.
     */
    private void drawAbility() {
        double centerX = screenWidth / 2.0;
        double centerY = screenHeight / 2.0;
        //base radius just around the player
        double baseRadius = tileSize * playerScale;
        //subtle pulsing
        double pulse = 1 + 0.05 * Math.sin(System.nanoTime() / 200_000_000.0);
        double radius = baseRadius * pulse;

        RadialGradient rg = new RadialGradient(
                0, 0,
                centerX, centerY,
                radius,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.color(1, 0.5, 0, 0.0)),
                new Stop(0.3, Color.color(1, 0.6, 0, 0.4)),
                new Stop(0.6, Color.color(1, 0.2, 0, 0.6)),
                new Stop(0.9, Color.color(0.8, 0, 0, 0.8))
        );

        gc.save();
        gc.setGlobalBlendMode(BlendMode.ADD);
        gc.setFill(rg);
        gc.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

        //small drifting sparkles
        for (int i = 0; i < 10; i++) {
            double angle = 2 * Math.PI * i / 6 + System.nanoTime() / 1_000_000_000.0;
            double sx = centerX + Math.cos(angle) * radius * 0.6;
            double sy = centerY + Math.sin(angle) * radius * 0.6;
            gc.setGlobalAlpha(0.3 + 0.2 * Math.cos(System.nanoTime() / 150_000_000.0 + i));
            gc.fillOval(sx, sy, 16, 16);
        }
        gc.restore();
    }

    /**
     * Draws a fiery glow around a Fireball entity.
     *
     * @param fireball the Fireball whose glow to render
     */
    private void drawFireballEffect(Fireball fireball) {
        double fireballWidth = fireBallScale * tileSize;
        double fireballHeight = fireBallScale * tileSize;
        double centerX = fireball.getX() + fireballWidth/2;
        double centerY = fireball.getY() + fireballHeight/2;

        double glowRadius = fireballWidth * 1.2;
        RadialGradient glow = new RadialGradient(
                0,0, centerX, centerY, glowRadius, false, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.color(1.0, 0.6, 0.0, 0.8)),
                new Stop(0.6, Color.color(1.0, 0.2, 0.0, 0.4)),
                new Stop(1.0, Color.color(1.0, 0.0, 0.0, 0.0))
        );

        gc.save();
        gc.setGlobalBlendMode(BlendMode.ADD);
        gc.setFill(glow);
        gc.fillOval(centerX - glowRadius, centerY - glowRadius, glowRadius * 2, glowRadius * 2);
        gc.restore();
    }

    /**
     * Draws a purple aura around a Warden entity.
     *
     * @param warden the Warden whose glow to render
     */
    private void drawWardenEffect(Warden warden) {
        double wardenWidth = wardenScale * tileSize;
        double wardenHeight = wardenScale * tileSize;
        double centerX = warden.getX() + wardenWidth / 2;
        double centerY = warden.getY() + wardenHeight / 2;

        double glowRadius = wardenWidth * 1.4;
        RadialGradient purpleGlow = new RadialGradient(
                0, 0, centerX, centerY, glowRadius, false, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.color(0.6, 0.0, 0.8, 0.7)),
                new Stop(0.7, Color.color(0.4, 0.0, 0.6, 0.3)),
                new Stop(1.0, Color.color(0.2, 0.0, 0.4, 0.0))
        );

        gc.save();
        gc.setGlobalBlendMode(BlendMode.ADD);
        gc.setFill(purpleGlow);
        gc.fillOval(centerX - glowRadius, centerY - glowRadius, glowRadius * 2, glowRadius * 2);
        gc.restore();
    }

    /**
     * Draws the current dialogue box, NPC name, line and player options.
     */
    private void drawDialogue() {

        double boxWidth = canvas.getWidth()  * 0.6;
        double lineHeight = tileSize * 0.6;
        double boxHeight = lineHeight * currentOptions.length + 20;
        double boxX = (canvas.getWidth() - boxWidth) / 2;
        double boxY = canvas.getHeight() - boxHeight - tileSize * 0.5;

        int lineLength = 45;

        gc.save();
        gc.setGlobalAlpha(0.85);
        gc.setFill(Color.rgb(0, 0, 0, 0.75));
        gc.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20);

        gc.setFill(Color.ORANGE);
        gc.setFont(getFont(35));
        gc.fillText(npcName + ":", boxX - 30, boxY - 120);
        gc.setFill(Color.WHITE);
        String wrapped = wordWrap(npcLine, lineLength);

        gc.setFont(getFont(25));
        gc.fillText(wrapped, boxX - 20, boxY - 60);
        gc.setFont(getFont(22));
        for (int i = 0; i < currentOptions.length; i++) {
            wrapped = wordWrap(currentOptions[i], lineLength);
            String line = (i + 1) + ") " + wrapped;
            gc.fillText(line, boxX + 20, boxY + 16 + (i + 1) * lineHeight);
        }
        gc.restore();
    }

    /**
     * Simple word‐wrap utility: inserts newlines when a word would exceed maxChars.
     *
     * @param text     the raw text to wrap
     * @param maxChars maximum characters per line
     * @return the wrapped text with inserted line breaks
     */
    private static String wordWrap(String text, int maxChars) {
        StringBuilder out  = new StringBuilder();
        int line = 0;

        for (String word : text.split(" ")) {
            if (line + word.length() > maxChars) {
                out.append('\n');
                out.append('\n');
                line = 0;
            } else if (line > 0) {
                out.append(' ');
                line++;
            }
            out.append(word);
            line += word.length();
        }
        return out.toString();
    }

    /**
     * Saves the current game state (player position, inventory, clock, room index)
     * as a JSON file in the user’s home directory.
     */
    public void saveGame() {
        try {
            JsonObject root = new JsonObject();

            root.addProperty("playerX", player.getX());
            root.addProperty("playerY", player.getY());
            root.addProperty("coins", inventory.getCoinCounter());
            root.addProperty("clock", clock.getRemainingSeconds());
            root.addProperty("room", roomManager.getCurrentRoomIndex());

            JsonArray inv = new JsonArray();
            for (Item it : inventory.getItems()) {
                inv.add(it.getClass().getSimpleName());
            }
            root.add("inventory", inv);

            Files.createDirectories(SAVE_FILE.getParent());
            try (Writer w = Files.newBufferedWriter(SAVE_FILE)) {
                GSON.toJson(root, w);
            }
            System.out.println("Game saved to " + SAVE_FILE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Loads a previously saved game state from the JSON save file,
     * repositioning the player, restoring inventory and clock.
     */
    public void loadGame() {
        if (Files.notExists(SAVE_FILE)) {
            System.out.println("No save file – nothing to load.");
            return;
        }

        try (Reader r = Files.newBufferedReader(SAVE_FILE)) {
            JsonObject root = JsonParser.parseReader(r).getAsJsonObject();

            roomManager.setCurrentRoomIndex(root.get("room").getAsInt());
            curRoom = roomManager.getCurrentRoom();
            player.setCurrentRoom(roomManager.getCurrentRoom());
            player.teleportTo(root.get("playerX").getAsDouble(), root.get("playerY").getAsDouble());
            centerCameraOn(player.getX(), player.getY());

            inventory.setCoinCounter(root.get("coins").getAsInt());
            clock.setRemainingSeconds(root.get("clock").getAsInt());

            JsonArray inv = root.getAsJsonArray("inventory");
            for (JsonElement e : inv) {
                switch (e.getAsString()) {
                    case "Letter" -> inventory.addToInventory(letter);
                    case "Key" -> inventory.addToInventory(key);
                }
            }
            System.out.println("Game loaded from " + SAVE_FILE);

        } catch (IOException | IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Ends any active dialogue, clears options, and resets all input flags.
     */
    public void finishDialogue() {
        clearOptions();
        getKeyH().setInteractPressed(false);
        setGameState(GameState.PLAYING);
        keyH.setInteractPressed(false);
        keyH.setDownPressed(false);
        keyH.setUpPressed(false);
        keyH.setRightPressed(false);
        keyH.setLeftPressed(false);
    }

    /**
     * Centers the camera on the given world‐coordinates.
     *
     * @param worldX the X coordinate of the focal point
     * @param worldY the Y coordinate of the focal point
     */
    public void centerCameraOn(double worldX, double worldY) {
        envOffsetX = canvas.getWidth()  / 2.0 - worldX - tileSize / 2;
        envOffsetY = canvas.getHeight() / 2.0 - worldY - tileSize / 2;
    }

    /**
     * Queues up a single‐line message and speaker name for the dialogue overlay.
     *
     * @param msg  the NPC’s line
     * @param name the NPC’s display name
     */
    public void showMessage(String msg, String name) {
        npcName = name;
        npcLine = msg;
    }

    /**
     * Presents an array of player‐choice options (1–4) in the dialogue box.
     *
     * @param opts the array of option strings
     */
    public void showOptions(String[] opts) {
        currentOptions = opts;
    }

    public void clearOptions() {
        currentOptions = null;
    }

    /**
     * Polls which dialogue‐choice key was pressed (1–4),
     * then resets that flag so it’s only returned once.
     *
     * @return the choice index (1–4), or 0 if none pressed
     */
    public int pollChoiceKey() {
        if (keyH.isOpt1Pressed()) { keyH.setOpt1Pressed(false); return 1; }
        if (keyH.isOpt2Pressed()) { keyH.setOpt2Pressed(false); return 2; }
        if (keyH.isOpt3Pressed()) { keyH.setOpt3Pressed(false); return 3; }
        if (keyH.isOpt4Pressed()) { keyH.setOpt4Pressed(false); return 4; }
        return 0;
    }

    public RoomManager getRoomManager() {
        return roomManager;
    }

    public GameOver getGameOver() {
        return gameOver;
    }

    public GameWon getGameWon() {
        return gameWon;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public Controlls getKeyH() {
        return keyH;
    }

    public Player getPlayer() {
        return player;
    }

    public RespawnMenu getRespawnMenu() {
        return respawnMenu;
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public Ability getAbility() {
        return ability;
    }

    public GraphicsContext getGraphicsContext() {
        return gc;
    }

    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Loads a bitmap font resource at the given point size.
     *
     * @param scale desired font size in points
     * @return the loaded Font instance
     */
    public Font getFont(int scale) {
        Font undertaleFont = Font.loadFont(getClass().getResource("/Fonts/PressStart2P-Regular.ttf").toExternalForm(), scale);
        return undertaleFont;
    }

    public Clock getClock() {
        return clock;
    }

    public Key getKey() {
        return key;
    }

    public Letter getLetter() {
        return letter;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    /**
     * Switches the game state (PLAYING, PAUSED, DIALOGUE, etc.).
     *
     * @param newState the new GameState to apply
     */
    public void setGameState(GameState newState) {
        this.state = newState;
    }


    public double getPlayerScale() {
        return playerScale;
    }

    public double getPatrollScale() {
        return patrollScale;
    }

    public double getWardenScale() {
        return wardenScale;
    }

    public double getFireBallScale() {
        return fireBallScale;
    }

    public double getKeymasterScale() {
        return keymasterScale;
    }

    public int getTileSize() {
        return tileSize;
    }

    public double getEnvOffsetX() {
        return envOffsetX;
    }

    public double getEnvOffsetY() {
        return envOffsetY;
    }

}
