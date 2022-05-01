package lawnlayer.GameObjects;

import lawnlayer.App;
import lawnlayer.GameObjectClass.GridObject;
import processing.core.PImage;

/**
 * Represent a power up that can do something when player hits it.
 * Object that inherit it must implement onPlayerEat method that will be called when hit by player.
 */
public abstract class PowerUps extends GridObject{
    protected App mainProgram;

    public PowerUps(int gridPosX, int gridPosY, PImage sprite, App mainProgram) {
        //Spawn the power up at a random map position in empty space.
        super(gridPosX, gridPosY, sprite);
        this.mainProgram = mainProgram;
    }

    public abstract void onPlayerEat();

    @Override
    public void tick(){
        if(mainProgram.isPlayerAt(this.getGridPosX(), this.getGridPosY())){
            onPlayerEat();
        }
    }
}
