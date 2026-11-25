package game.items;

import game.gameControll.Controlls;
import game.gameControll.GamePanel;
import game.gameControll.ImageId;
import game.entity.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

/**
 * A lamp item that, when activated, illuminates dark rooms.
 */
public class Lamp extends Item {

    private Controlls keyH;
    private GamePanel gamePanel;

    /**
     * Constructs a Lamp.
     *
     * @param imageId   the ImageId for the lamp sprite
     * @param gamePanel the GamePanel for rendering context
     * @param keyH      the Controlls instance to read activation input
     * @param player    the Player whose position anchors the light
     */
    public Lamp(ImageId imageId, GamePanel gamePanel, Controlls keyH, Player player) {
        super(imageId);
        this.gamePanel = gamePanel;
        this.keyH = keyH;
    }

    /**
     * Updates the lamp’s activation state and renders the light effect.
     * Should be called once per game tick.
     */
    //RadialGradient is taken from an online user
    public void update() {
        double width = gamePanel.getCanvas().getWidth();
        double height = gamePanel.getCanvas().getHeight();

        GraphicsContext gc = gamePanel.getGraphicsContext();


        if (keyH.isLampActivated() && gamePanel.getInventory().hasLamp()) {


            double centerX = gamePanel.getScreenWidth()  / 2.0;
            double centerY = gamePanel.getScreenHeight() / 2.0;

            double radius = 500;

            RadialGradient rg = new RadialGradient(
                    0,
                    0,
                    centerX,
                    centerY,
                    radius,
                    false,
                    CycleMethod.NO_CYCLE,
                    new Stop(0.0, new Color(1.0, 1.0, 0.6, 0.0)),
                    new Stop(0.3, new Color(1.0, 1.0, 0.6, 0.2)),
                    new Stop(0.6, new Color(0.0, 0.0, 0.0, 0.5)),
                    new Stop(1.0, new Color(0.0, 0.0, 0.0, 0.8)));

            gc.setFill(rg);
            gc.fillRect(0, 0, width, height);

        }
        else {

            double centerX = gamePanel.getCanvas().getWidth()  / 2.0;
            double centerY = gamePanel.getCanvas().getHeight() / 2.0;

            double radius = 300;

            RadialGradient rg = new RadialGradient(
                    0,
                    0,
                    centerX,
                    centerY,
                    radius,
                    false,
                    CycleMethod.NO_CYCLE,
                    new Stop(0.0, Color.TRANSPARENT),
                    new Stop(0.7, new Color(0, 0, 0, 0.7)),
                    new Stop(1.0, Color.BLACK)
            );

            gc.setFill(rg);
            gc.fillRect(0, 0, width, height);
        }
    }
}
