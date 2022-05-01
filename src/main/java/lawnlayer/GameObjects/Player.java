package lawnlayer.GameObjects;

import java.util.ArrayList;
import processing.core.PImage;
import lawnlayer.GameObjectClass.DynamicObject;
import lawnlayer.GeneralClass.Direction;
import lawnlayer.GeneralClass.TileLabel;
import lawnlayer.GeneralClass.Vector2Int;

/**
 * Represents a player object on the grid map.
 */
public class Player extends DynamicObject {

    private GridMap gridMap;                                    //The grid map reference that the player is currently on.
    private boolean isAlive = true;                             //Is the player alive?
    public static final float SECONDS_PER_TILE = 0.16f;         //The second to wait before moving one tile unit. (Default speed) 2PX PER FRAME = 10 FRAME PER TILE = 0.16 SEC PER TILE.
    public static final float MOVEMENT_SMOOTHNESS = 5.8f;       //The value that control the smoothness of the movement.

    /**
     * Setup the player data.
     * @param gridPosX The position X on grid.
     * @param gridPosY The position Y on grid.
     * @param sprite The sprite reference.
     * @param gridMap The grid map reference.
     */
    public Player(int gridPosX, int gridPosY, PImage sprite, GridMap gridMap) {
        super(gridPosX, gridPosY, sprite, SECONDS_PER_TILE, MOVEMENT_SMOOTHNESS);
        this.gridMap = gridMap;
    }

    public void onGridMovement(){
        //If the player will be dead at the position.
        if(checkDead(getGridPosX(), getGridPosY())){ return; }

        //Check if player is in safe area and fill the area if needed.
        checkFill(getGridPosX(), getGridPosY());

        //If player is on solid tile. Movement is not continous.
        checkMovement(getGridPosX(), getGridPosY());
    }

    /**
     * Change the direction of the player only when the given direction is neither the same nor the opposite.
     * Note: The direction will be stored in the buffer "nextDirection". "currentDirection" can not be changed once set.
     * Every tile movement the nextDirection will override the currentDirection value to set the direction of next movement.
     * This will result in a bit delay in keyboard control, but visual smoothing can be applied.
     * @param direction The direction to set.
     */
    public void changeDirection(Direction direction){
        //Player will never be stopped by keyboard. If the direction is NONE, return.
        //If the player is dead. Player control is lost.
        if(direction == Direction.NONE || !isAlive) {return;}
        //If neither the same nor the opposite.
        if(direction != getCurrentDirection().getOpposite() && direction != getCurrentDirection()){
            setNextDirection(direction);
        }
    }

    /**
     * Apply the movement rule. Stop player on cement.
     */
    public void checkMovement(int x, int y){
        if(gridMap.getTile(x, y)==TileLabel.SOLID){
            setNextDirection(Direction.NONE);
        }
    }

    /**
     * If player is in safe area and there are path made by player. Try to fill the area within the path.
     * If player is not in safe area, create a path tile at the player's position.
     * @param x Player position X.
     * @param y Player position Y.
     */
    public void checkFill(int x, int y){
        //When the player touch the safe area.
        if(gridMap.getTile(x, y)==TileLabel.SOLID || gridMap.getTile(x, y)==TileLabel.FILLED){
            gridMap.startFloodFillAroundPath();

        //If player is not in safe area, put a path tile at the position
        }else{
            gridMap.addCurrentPathArea(new Vector2Int(x, y));
            gridMap.setTile(x, y, TileLabel.PATH_G);
        }
    }

    /**
     * Check if the player in the position will die or not.
     * @param x Position X.
     * @param y Position Y.
     * @return Return true if the player will die.
     */
    public boolean checkDead(int x, int y){
        ArrayList<Vector2Int> path = gridMap.getCurrentPathArea();
        TileLabel currentTileLabel = gridMap.getTile(x, y);

        //When the player touch the path made by itself. Kill the player.
        if(currentTileLabel==TileLabel.PATH_G || currentTileLabel==TileLabel.PATH_R){
            kill();
            return true;
        //When the path tile player just created is red, means the path is destroyed. Kill the player.
        }else if(path.size()>0){
            Vector2Int lastPathTile = path.get(path.size()-1);
            TileLabel lastPathTileLabel = gridMap.getTile(lastPathTile.x, lastPathTile.y);
            if(lastPathTileLabel==TileLabel.PATH_R){
                kill();
                return true;
            }
        //When the player collided with the enemy. Kill the player.
        }else if(gridMap.isEnemyAt(x, y)){
            kill();
            return true;
        }
        return false;
    }

    /**
     * Get the player's current grid map reference.
     * @return Current grid map.
     */
    public GridMap getGridMap() {
        return gridMap;
    }

    /**
     * Set the player's current grid map reference.
     * @param gridMap Current grid map.
     */
    public void setGridMap(GridMap gridMap) {
        this.gridMap = gridMap;
    }

    /**
     * Is the player alive?
     * @return Current player's life status.
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Kill the player and reset the data.
     */
    public void kill() {
        //Stop the player movement.
        stop();
        //Remove all temporary tiles like path and flood filled tiles.
        gridMap.removeTemporaryTiles();
        //Clear player path list.
        gridMap.clearCurrentPathArea();
        //Set alive false, which will block player's logic section.
        this.isAlive = false;
    }

    /**
     * Revive the player.
     */
    public void revive() {
        this.isAlive = true;
    }

}
