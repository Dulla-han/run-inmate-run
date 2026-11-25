package game.items;

import game.gameControll.GamePanel;
import game.gameControll.ImageId;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * A countdown clock item that tracks remaining time
 * and renders it on screen.
 */
public class Clock extends Item {

    private long remainingSeconds;
    private long lastNano;
    private boolean timeRunning = false;
    private final GamePanel gamePanel;

    /**
     * Constructs a Clock with the given starting duration.
     *
     * @param imageId   the ImageId for the clock sprite
     * @param minutes   the initial countdown value in minutes
     * @param gamePanel the GamePanel for timing and rendering
     */
    public Clock(ImageId imageId, long minutes, GamePanel gamePanel) {
        super(imageId);
        this.remainingSeconds = minutes * 60_000;
        this.gamePanel = gamePanel;
    }

    /**
     * Starts the countdown timer.
     */
    public void start() {
        timeRunning = true;
        lastNano = System.nanoTime();
    }

    /**
     * Updates the clock’s internal timer each tick.
     */
    public void update() {
        if (!timeRunning) return;
        long now = System.nanoTime();
        long elapsed = (now - lastNano) / 1_000_000;
        remainingSeconds = Math.max(0, remainingSeconds - elapsed);
        lastNano = now;
        if (remainingSeconds <= 0) {
            remainingSeconds = 0;
            stopClock();
            gamePanel.getGameOver().show();
        }
    }

    /**
     * Draws the clock’s current time.
     *
     * @param gc the GraphicsContext used for drawing
     */
    public void draw(GraphicsContext gc) {
        int totalSec = (int)(remainingSeconds / 1000);
        int mins = totalSec / 60;
        int secs = totalSec % 60;
        String text = String.format("%02d:%02d", mins, secs);
        gc.setFont(gamePanel.getFont(100));
        if (6 <= mins && mins < 11) gc.setFill(Color.ORANGE);
        else if (mins < 6) gc.setFill(Color.RED);
        else gc.setFill(Color.YELLOW);
        gc.fillText(text, gamePanel.getRoomManager().getCurrentRoom().getExitCol() + gamePanel.getCanvas().getWidth() / 2, gamePanel.getRoomManager().getCurrentRoom().getExitRow() + gamePanel.getEnvOffsetY() + 160);
    }

    /**
     * @return the remaining time in whole seconds
     */
    public int getRemainingSeconds() {
        return (int)(remainingSeconds / 1000);
    }

    /**
     * Sets the remaining time.
     *
     * @param remainingSeconds time in seconds
     */
    public void setRemainingSeconds(long remainingSeconds) {
        this.remainingSeconds = remainingSeconds * 1000;
    }

    /**
     * Decreases the remaining time by the given amount,
     * never allowing it to go below zero.
     *
     * @param secs number of seconds to subtract
     */
    public void decreaseSeconds(int secs) {
        long newSec = Math.max(0L, (remainingSeconds / 1000) - secs);
        remainingSeconds = newSec * 1000;
    }

    /**
     * Stops the clock from counting down.
     */
    public void stopClock() {
        timeRunning = false;
    }
}
