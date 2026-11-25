package game.gameControll;

public enum GameState {
    /** Normal gameplay in progress. */
    PLAYING,
    /** Game is currently paused. */
    PAUSED,
    /** Player is waiting to respawn after death. */
    RESPAWNING,
    /** The player has died and sees the “Game Over” screen. */
    GAME_OVER,
    /** The player has won and sees the “Victory” screen. */
    GAME_WON,
    /** In‐dialogue state, player must choose dialogue options. */
    DIALOGUE,
    /** Initial main‐menu screen. */
    MAIN_MENU
}

