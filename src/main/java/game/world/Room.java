package game.world;

import game.entity.*;
import game.gameControll.GamePanel;
import game.gameControll.ImageId;
import game.entity.*;
import game.items.Coins;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents one room in the game world.
 * Holds its TileMap, all contained entities, projectiles and items.
 */
public class Room {
    private TileMap tileMap;
    private final int entranceRow, entranceCol;
    private final int exitRow, exitCol;
    private final List<PatrollingNPC> patrolls;
    private final List<Entity> entities;
    private final List<Coins> coins;
    private final List<PrisonerNPC> prisonNPCs;
    private final List<Warden> warden;
    private final List<Fireball> fireballs;
    private final List<Keymaster> keymasters;
    private final List<Door> doors;

    /**
     * Loads this room’s layout, entities and items from its JSON or map file.
     *
     * @param mapFilePath the path to the room definition resource
     * @param gamePanel
     */
    public Room(String mapFilePath, GamePanel gamePanel) {
        this.tileMap = new TileMap(mapFilePath);
        this.entranceRow = tileMap.getEntranceRow();
        this.entranceCol = tileMap.getEntranceCol();
        this.exitRow = tileMap.getExitRow();
        this.exitCol = tileMap.getExitCol();
        this.patrolls = new ArrayList<>();
        this.entities = new ArrayList<>();
        this.coins = new ArrayList<>();
        this.prisonNPCs = new ArrayList<>();
        this.warden = new ArrayList<>();
        this.fireballs = new ArrayList<>();
        this.keymasters = new ArrayList<>();
        this.doors = new ArrayList<>();

        int ts = gamePanel.getTileSize();
        for (int r = 0; r < tileMap.getRows(); r++) {
            for (int c = 0; c < tileMap.getCols(); c++) {
                if (tileMap.isPatrollingNPC(r, c)) {
                    int wx = c * ts;
                    int wy = r * ts;
                    PatrollingNPC npc = new PatrollingNPC(gamePanel,this, ImageId.PATROLL, wx, wy, gamePanel.getPatrollScale(), 5);
                    patrolls.add(npc);
                    entities.add(npc);
                }
                if (tileMap.isCoin(r, c)) {
                    int wx = c * ts;
                    int wy = r * ts;
                    Coins coin = new Coins(ImageId.COINS, wx, wy, gamePanel);
                    coins.add(coin);
                }
                if (tileMap.isNpcCell(r, c)) {
                    int wx = c * ts;
                    int wy = r * ts;
                    PrisonerNPC prisonerNPC = new PrisonerNPC(gamePanel, ImageId.NPCCELL, wx, wy, ts);
                    prisonNPCs.add(prisonerNPC);
                    entities.add(prisonerNPC);
                }
                if (tileMap.isWarden(r, c)) {
                    int wx = c * ts;
                    int wy = r * ts;
                    Warden wardenFound = new Warden(gamePanel, ImageId.WARDEN, wx, wy, gamePanel.getWardenScale());
                    warden.add(wardenFound);
                    entities.add(wardenFound);
                }
                if (tileMap.isFireball(r, c)) {
                    int wx = c * ts;
                    int wy = r * ts;
                    Fireball fireball = new Fireball(gamePanel, this, ImageId.FIREBALL, wx, wy, gamePanel.getFireBallScale(), 8);
                    fireballs.add(fireball);
                    entities.add(fireball);
                }
                if (tileMap.isKeymaster(r, c)) {
                    int wx = c * ts;
                    int wy = r * ts;
                    Keymaster keymaster = new Keymaster(gamePanel, ImageId.KEYMASTER, wx, wy, gamePanel.getKeymasterScale());
                    keymasters.add(keymaster);
                    entities.add(keymaster);
                }
                if (tileMap.isDoor(r, c)) {
                    int wx = c * ts;
                    int wy = r * ts;
                    Door door = new Door(gamePanel, wx, wy);
                    doors.add(door);
                }
            }
        }
    }

    public TileMap getTileMap() { return tileMap; }
    public void setTileMap(TileMap tileMap) { this.tileMap = tileMap; }
    public int getEntranceRow() { return entranceRow; }
    public int getEntranceCol() { return entranceCol; }
    public int getExitRow() { return exitRow; }
    public int getExitCol() { return exitCol; }
    public List<PatrollingNPC> getPatrolls() { return patrolls; }
    public List<Coins> getCoins() { return coins; }
    public List<Entity> getEntities() { return entities; }
    public List<PrisonerNPC> getPrisonNPCs() { return prisonNPCs; }
    public List<Warden> getWardens() { return warden; }
    public List<Fireball> getFireballs() { return fireballs; }
    public List<Keymaster> getKeymasters() { return keymasters; }
    public List<Door> getDoors() { return doors; }
}


