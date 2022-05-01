package lawnlayer.GameObjects;

import lawnlayer.App;
import processing.core.PImage;

/**
 * This is a power up that can make enemy move slower.
 */
public class EnemySlowPowerUp extends PowerUps{

    public static final int EFFECT_TIME = 6000; //Effect time in milliseconds.

    public EnemySlowPowerUp(int gridPosX, int gridPosY, PImage sprite, App mainProgram) {
        super(gridPosX, gridPosY, sprite, mainProgram);
    }

    /**
     * Is called if the player hits the power up.
     */
    @Override
    public void onPlayerEat() {
        this.mainProgram.enemiesChangeSpeed(2.5f, EFFECT_TIME);
        System.out.println("Enemy Slow Down Power Up Eaten.");
        this.mainProgram.removePowerUps();
    }
}