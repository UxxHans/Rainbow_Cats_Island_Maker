package lawnlayer.GameObjects;

import lawnlayer.App;
import processing.core.PImage;

/**
 * This is a power up that can make player move faster.
 */
public class PlayerFastPowerUp extends PowerUps{

    public static final int EFFECT_TIME = 4800; //Effect time in milliseconds.

    public PlayerFastPowerUp(int gridPosX, int gridPosY, PImage sprite, App mainProgram) {
        super(gridPosX, gridPosY, sprite, mainProgram);
    }

    /**
     * Is called if the player hits the power up.
     */
    @Override
    public void onPlayerEat() {
        this.mainProgram.playerChangeSpeed(0.5f, EFFECT_TIME);
        System.out.println("Player Speed Up Power Up Eaten.");
        this.mainProgram.removePowerUps();
    }
}