package lawnlayer.GameObjectClass;

import processing.core.PImage;
import lawnlayer.GlobalSettings;
import lawnlayer.GeneralClass.Direction;

/**
 * Represents a dynamic object that can smoothly move on the grid map.
 */
public abstract class DynamicObject extends GridObject {

    private int movementTimer = 0;                              //The timer to set the time to wait before moving one tile unit.
    private Direction currentDirection = Direction.NONE;        //Current moving direction of the object. Can not be changed.
    private Direction nextDirection = Direction.NONE;           //Next moving direction of the object. Can be changed.
    private float secondsPerTile = 0.08f;                        //The second to wait before moving one tile unit.

    private float movementSmoothness = 3;                        //The value that control the smoothness of the movement, 1 is the minimum value which means no smoothing.
                                                                //If the value is too high, it will be too slow to transit from one position to another. 
                                                                //Once its time to move in logic section, it will be brought to the position immediately, which result in no smoothing.
    /**
     * Setup the object data.
     * @param gridPosX The position X on grid.
     * @param gridPosY The position Y on grid.
     * @param sprite The sprite reference.
     * @param gridMap The grid map reference.
     */
    public DynamicObject(int gridPosX, int gridPosY, PImage sprite, float secondsPerTile, float movementSmoothness) {
        super(gridPosX, gridPosY, sprite);
        this.secondsPerTile = secondsPerTile;
        this.movementSmoothness = movementSmoothness;
    }

    /**
     * The abstract method that will be called each time the object is moved for one grid.
     */
    public abstract void onGridMovement();

    /**
     * Lerp the position of the object on screen, according to the current direction.
     * Each time the object actually move in logic, its position on screen will be set to the right and precise position.
     */
    public void smoothMovement(){
        //Stop smoothing if the value is invalid.
        if(movementSmoothness < 1){ return; }

        //Get current screen position.
        int currentX = this.getX();
        int currentY = this.getY();

        //Get next screen position.
        int nextGridX = this.getGridPosX() + currentDirection.X;
        int nextGridY = this.getGridPosY() + currentDirection.Y;
        int nextScreenX = nextGridX * GlobalSettings.mapTileSize;
        int nextScreenY = nextGridY * GlobalSettings.mapTileSize + GlobalSettings.topReserveHeight;

        //Calculate the difference.
        int deltaX = nextScreenX - currentX;
        int deltaY = nextScreenY - currentY;

        //Move the object using the difference.
        this.setX(currentX + (int)((float)deltaX / movementSmoothness));
        this.setY(currentY + (int)((float)deltaY / movementSmoothness));
    }

    /**
     * Move the object in the grid.
     */
    public void gridMovement(){
        //If more frames have passed than the number of seconds x the framerate, move the object.
        int framesToMove = (int)(secondsPerTile * (float)GlobalSettings.frameRate);
        if (this.movementTimer > framesToMove) {
            //If current direction is not none, move the object and do the function.
            if(currentDirection != Direction.NONE) {
                //Move the object with its current direction.
                int currentGridPosX = this.getGridPosX() + currentDirection.X;
                int currentGridPosY = this.getGridPosY() + currentDirection.Y;

                //Set object position.
                this.setGridPosX(currentGridPosX);
                this.setGridPosY(currentGridPosY);

                //Call the function each time object moves on the grid.
                onGridMovement();
            }
            
            //Set the current direction next direction.
            currentDirection = nextDirection;

            //The timer is reset to 0.
            this.movementTimer = 0;
        }
    }

    /**
     * Handles the logic for each frame.
     */
    @Override
    public void tick() {
        //Increments the timer
        this.movementTimer++;

        //If the object is going to move out of map, stop the object.
        if(isOutOfMap(currentDirection)){
            stop();
        //Else apply smooth movement to the direction.
        }else{
            smoothMovement();
        }

        //After a few ticks, the object actually move in grid.
        gridMovement();
    }
    
    /**
     * If the object keep moving on the direction, will it move out of map?
     * This is an overload method of isOutOfMap. So we can check with other parameters.
     * @param direction The direction object is going to move.
     * @return Return true if the object will move out of the map if it continue one movement on the direction.
     */
    public boolean isOutOfMap(Direction direction){
        int nextGridX = this.getGridPosX() + direction.X;
        int nextGridY = this.getGridPosY() + direction.Y;

        //If the object is going to move out of map, return true.
        if(isOutOfMap(nextGridX, nextGridY)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Check if the given position is out of map.
     * @param x Position X.
     * @param y Position Y.
     * @return Return true if the position is out of map.
     */
    public boolean isOutOfMap(int x, int y){
        return x < 0 || y < 0 || x >= GlobalSettings.mapWidth || y >= GlobalSettings.mapHeight;
    }

    /**
     * Stop the object.
     */
    public void stop(){
        currentDirection = Direction.NONE;
        nextDirection = Direction.NONE;
    }

    /**
     * Get the direction to move in the current grid movement.
     * @return Direction to move in the current grid movement.
     */
    public Direction getCurrentDirection(){
        return currentDirection;
    }

    /**
     * Get the direction to move in the next grid movement.
     * @return Direction to move in the next grid movement.
     */
    public Direction getNextDirection(){
        return nextDirection;
    }

    /**
     * Setup the next direction.
     * @param direction Direction to move in the next grid movement.
     */
    protected void setNextDirection(Direction direction){
        nextDirection = direction;
    }

    /**
     * Get the movement speed of object.
     * @return Seconds to move a tile unit.
     */
    public float getSecondsPerTile() {
        return secondsPerTile;
    }
    
    /**
     * Set the movement speed of object.
     * @secondsPerTile Seconds to move a tile unit.
     */
    public void setSecondsPerTile(float secondsPerTile) {
        this.secondsPerTile = secondsPerTile;
    }

}
