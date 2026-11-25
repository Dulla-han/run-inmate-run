package game.world;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * The 2D grid underlying a Room: tracks walls, passages, doors, and empty cells.
 */
public class TileMap {
    private int rows;
    private int cols;
    private boolean[][] solidBlocks;
    private boolean[][] emptyCells;
    private boolean[][] doors;
    private boolean[][] npcCells;
    private boolean[][] passageNext;
    private boolean[][] passageBack;
    private boolean[][] patrollingNPC;
    private boolean[][] coins;
    private boolean[][] warden;
    private boolean[][] fireball;
    private boolean[][] keymaster;

    private int entranceRow;
    private int entranceCol;
    private int exitRow;
    private int exitCol;

    /**
     * Constructs a TileMap by loading its layout from the given map definition file.
     *
     * @param filename the resource path to the tile‐map file
     */
    public TileMap(String filename) {
        try (InputStream is = getClass().getResourceAsStream(filename);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            int r = 0;
            java.util.List<String> lines = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            rows = lines.size();
            cols = (rows > 0) ? lines.get(0).length() : 0;

            solidBlocks = new boolean[rows][cols];
            passageNext = new boolean[rows][cols];
            passageBack = new boolean[rows][cols];
            emptyCells = new boolean[rows][cols];
            doors = new boolean[rows][cols];
            npcCells = new boolean[rows][cols];
            patrollingNPC = new boolean[rows][cols];
            coins = new boolean[rows][cols];
            warden = new boolean[rows][cols];
            fireball = new boolean[rows][cols];
            keymaster = new boolean[rows][cols];

            for (int i = 0; i < rows; i++) {
                line = lines.get(i);
                for (int c = 0; c < line.length(); c++) {           //emptyCells                     //doors                //npcCells
                    solidBlocks[i][c] = ((line.charAt(c) == '1') || (line.charAt(c) == '3') || (line.charAt(c) == '4') || (line.charAt(c) == '5'));
                    passageNext[i][c] = (line.charAt(c) == '+');
                    passageBack[i][c] = (line.charAt(c) == '-');
                    emptyCells[i][c] = (line.charAt(c) == '3');
                    doors[i][c] = (line.charAt(c) == '4');
                    npcCells[i][c] = (line.charAt(c) == '5');
                    patrollingNPC[i][c] = (line.charAt(c) == '!');
                    coins[i][c] = (line.charAt(c) == '$');
                    warden[i][c] = (line.charAt(c) == 'W');
                    fireball[i][c] = (line.charAt(c) == 'F');
                    keymaster[i][c] = (line.charAt(c) == '#');

                    if (line.charAt(c) == '+') { entranceRow = i; entranceCol = c;
                        System.out.println(entranceRow + " " + entranceCol);}
                    if (line.charAt(c) == '-') { exitRow = i; exitCol = c;
                        System.out.println(exitRow + " " + exitCol);}
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unlockDoor(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) return;
        solidBlocks[row][col] = false;
        doors[row][col] = false;
    }



    public boolean isSolid(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return true;
        }
        return solidBlocks[row][col];
    }

    public boolean isPassageNext(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return false;
        }
        return passageNext[row][col];
    }

    public boolean isPassageBack(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return false;
        }
        return passageBack[row][col];
    }

    public boolean isEmptyCell(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return false;
        }
        return emptyCells[row][col];
    }

    public boolean isDoor(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return false;
        }
        return doors[row][col];

    }

    public boolean isNpcCell(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return false;
        }
        return npcCells[row][col];

    }

    public boolean isPatrollingNPC(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return false;
        }
        return patrollingNPC[row][col];

    }

    public boolean isCoin(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return false;
        }
        return coins[row][col];
    }

    public boolean isWarden(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return false;
        }
        return warden[row][col];
    }

    public boolean isFireball(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return false;
        }
        return fireball[row][col];
    }

    public boolean isKeymaster(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return false;
        }
        return keymaster[row][col];
    }

    public int getExitCol() {
        return exitCol;
    }

    public int getExitRow() {
        return exitRow;
    }

    public int getEntranceCol() {
        return entranceCol;
    }

    public int getEntranceRow() {
        return entranceRow;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public void setIsSolid(int r, int c, boolean isSolid) {
        solidBlocks[r][c] = isSolid;
    }
}
