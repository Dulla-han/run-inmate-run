package game.gameControll;

import game.ability.Ability;


/**
 * Tracks the state of all player input controls (movement,
 * interaction, inventory, abilities, pause and dialogue options).
 */
public class Controlls {
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean inventoryPressed;
    private boolean lampActivated;
    private boolean interactPressed;
    private boolean abilityPressed;
    private boolean pausedPressed;
    private boolean opt1Pressed;
    private boolean opt2Pressed;
    private boolean opt3Pressed;
    private boolean opt4Pressed;

    //setters
    public void setUpPressed(boolean upPressed) { this.upPressed = upPressed; }
    public void setDownPressed(boolean downPressed) { this.downPressed = downPressed; }
    public void setLeftPressed(boolean leftPressed) { this.leftPressed = leftPressed; }
    public void setRightPressed(boolean rightPressed) { this.rightPressed = rightPressed; }
    public void setInventoryPressed(boolean inventoryPressed) { this.inventoryPressed = inventoryPressed; }
    public void setLampActivated(boolean lampActivated) { this.lampActivated = lampActivated; }
    public void setInteractPressed(boolean interactPressed) { this.interactPressed = interactPressed; }

    /**
     * Notifies the assigned Ability instance that its activation key
     * has been pressed (starting cooldown).
     *
     * @param ability the Ability to trigger
     */
    public void setAbilityPressed(boolean abilityPressed, Ability ability) { if (ability.isAbilityReady() && abilityPressed) {
                                                                                this.abilityPressed = abilityPressed;
                                                                                ability.setActive(true);
                                                                            } else if (!ability.isAbilityReady() && abilityPressed) {
                                                                                System.out.println("ability is on cooldown!!!");
                                                                            } else if (ability.isAbilityReady() && !abilityPressed) {
                                                                                this.abilityPressed = abilityPressed;
                                                                                ability.inicializeCooldown();
                                                                                ability.setActive(false);
                                                                            }}
    /**
     * Toggles pause when the ESC key is pressed,
     * switching between PLAYING and PAUSED states.
     *
     * @param pausedPressed true to pause the game, false to resume
     */
    public void setPausedPressed(boolean pausedPressed, GameState state, GamePanel gamePanel) { this.pausedPressed = pausedPressed;
                                                                                                if (state == GameState.PAUSED) gamePanel.setGameState(GameState.PLAYING);
                                                                                                else gamePanel.setGameState(GameState.PAUSED);}
    public void setOpt1Pressed(boolean opt1Pressed) { this.opt1Pressed = opt1Pressed; }
    public void setOpt2Pressed(boolean opt2Pressed) { this.opt2Pressed = opt2Pressed; }
    public void setOpt3Pressed(boolean opt3Pressed) { this.opt3Pressed = opt3Pressed; }
    public void setOpt4Pressed(boolean opt4Pressed) { this.opt4Pressed = opt4Pressed; }


    //getters
    public boolean isUpPressed() { return upPressed; }
    public boolean isDownPressed() { return downPressed; }
    public boolean isLeftPressed() { return leftPressed; }
    public boolean isRightPressed() { return rightPressed; }
    public boolean isInventoryPressed() { return inventoryPressed; }
    public boolean isLampActivated() { return lampActivated; }
    public boolean isInteractPressed() { return interactPressed; }
    public boolean isAbilityPressed() { return abilityPressed; }
    public boolean isPausedPressed() { return pausedPressed; }
    public boolean isOpt1Pressed() { return opt1Pressed; }
    public boolean isOpt2Pressed() { return opt2Pressed; }
    public boolean isOpt3Pressed() { return opt3Pressed; }
    public boolean isOpt4Pressed() { return opt4Pressed; }
}

