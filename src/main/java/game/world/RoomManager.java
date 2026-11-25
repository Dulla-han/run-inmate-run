package game.world;

import game.gameControll.GamePanel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages all rooms in the game: loading, switching,
 * and retrieving the current active room.
 */
public class RoomManager {
    private List<Room> rooms;
    private int currentRoomIndex = 0;

    /**
     * Constructs the RoomManager with an empty room list.
     */
    public RoomManager() {
        rooms = new ArrayList<Room>();
    }

    /**
     * @return the currently active Room, or null if none
     */
    public Room getCurrentRoom() {
        return rooms.isEmpty() ? null : rooms.get(currentRoomIndex);
    }

    /**
     * Advances to the next room in the list.
     *
     * @return true if successful, false if already at last room
     */
    public boolean nextRoom() {
        if (currentRoomIndex + 1 >= rooms.size()) return false;
        currentRoomIndex += 1;
        return true;
    }

    /**
     * Moves to the previous room in the list.
     *
     * @return true if successful, false if already at first room
     */
    public boolean previousRoom() {
        if (currentRoomIndex - 1 < 0) return false;
        currentRoomIndex -= 1;
        return true;
    }

    /**
     * Adds a new room to the manager’s list.
     *
     * @param room the Room to add
     */
    public void add(Room room) {
        rooms.add(room);
    }

    /**
     * Loads room files from resources.
     *
     * @param gp the GamePanel for asset paths
     */
    public void getRoomFiles(GamePanel gp) {
        String folderPath = "/Rooms";
        File dir = new File(getClass().getResource(folderPath).getPath());
        String filePaths;

        File[] files = dir.listFiles();
        for (File file : files) {
            filePaths = "/" + file.getName();
            Room room = new Room("/Rooms" + filePaths, gp);
            System.out.println(file.getName() + " room added.");
            this.add(room);
        }
    }

    public int getCurrentRoomIndex() {
        return currentRoomIndex;
    }

    public void setCurrentRoomIndex(int currentRoomIndex) {
        this.currentRoomIndex = currentRoomIndex;
    }
}
