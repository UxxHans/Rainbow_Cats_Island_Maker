package lawnlayer.GameObjects;

import java.util.ArrayList;
import lawnlayer.GameObjectClass.DynamicObject;
import lawnlayer.GeneralClass.Direction;
import lawnlayer.GeneralClass.TileLabel;
import processing.core.PImage;

/**
 * Represents an enemy object.
 */
public class Enemy extends DynamicObject {

    private GridMap gridMap;                                                //The grid map reference that the enemy is currently on.
    private TileLabel[] moveableArea = new TileLabel[]{ TileLabel.EMPTY };  //The area that the enemy can move.
    private boolean canMakeRedPath = true;                                  //Determine if the enemy can create red path on the player's green path.
    private boolean canEatFilledTile = true;                                //Determine if the enemy can turn the filled tile to empty tile.
    public static final float SECONDS_PER_TILE = 0.16f;                     //The second to wait before moving one tile unit. (Default speed) 2PX PER FRAME = 10 FRAME PER TILE = 0.16 SEC PER TILE.
    public static final float MOVEMENT_SMOOTHNESS = 5.8f;                   //The value that control the smoothness of the movement.

    /**
     * Setup the enemy data.
     * @param gridPosX The position X on grid.
     * @param gridPosY The position Y on grid.
     * @param sprite The sprite reference.
     * @param gridMap The grid map reference.
     */
    public Enemy(int gridPosX, int gridPosY, PImage sprite, GridMap gridMap) {
        super(gridPosX, gridPosY, sprite, SECONDS_PER_TILE, MOVEMENT_SMOOTHNESS);
        this.setNextDirection(Direction.DIAGONAL[(int) (Math.random() * 4.0)]);
        this.gridMap = gridMap;
    }

    /**
     * The implementation of the abstract method that will be excuted each time enemy moves in the grid.
     */
    public void onGridMovement(){
        if(checkWallCollision()){
            checkFillCollision();
            checkPathCollision();
        }
    }

    /**
     * Check if the enemy is going to collide with green path.
     */
    public void checkPathCollision(){
        //Return if red path can not be made.
        if(!canMakeRedPath){ return; }

        Direction cDirection = getCurrentDirection();
        int nextGridPosX = getGridPosX() + cDirection.X;
        int nextGridPosY = getGridPosY() + cDirection.Y;
        TileLabel nextTileLabel = null;
        if(!isOutOfMap(nextGridPosX, nextGridPosY)){
            nextTileLabel = gridMap.getTile(nextGridPosX, nextGridPosY);
        }

        //When the enemy touch the path made by player. Put a red path at the collision point.
        if(nextTileLabel == TileLabel.PATH_G){
            gridMap.setTile(nextGridPosX, nextGridPosY, TileLabel.PATH_R);
        }
    }

    /**
     * Check if the enemy is going to collide with filled tile.
     */
    public void checkFillCollision(){
        //Return if enemy can not eat filled tiles.
        if(!canEatFilledTile){ return; }
        boolean hasEatenTile = false;
        Direction[] cDirection = getCurrentDirection().getSeparate();
        for(int i=0; i<cDirection.length; i++){
            int nextGridPosX = getGridPosX() + cDirection[i].X;
            int nextGridPosY = getGridPosY() + cDirection[i].Y;
            TileLabel nextTileLabel = null;
            if(!isOutOfMap(nextGridPosX, nextGridPosY)){
                nextTileLabel = gridMap.getTile(nextGridPosX, nextGridPosY);
            }
            //When the enemy touch the filled tiles made by player. turn the tile back to empty.
            if(nextTileLabel == TileLabel.FILLED){
                hasEatenTile = true;
                gridMap.setTile(nextGridPosX, nextGridPosY, TileLabel.EMPTY);
            }
        }
        //Eat diagonal tile only if there is no collision on the side.
        if(!hasEatenTile){
            int nextGridPosX = getGridPosX() + getCurrentDirection().X;
            int nextGridPosY = getGridPosY() + getCurrentDirection().Y;
            TileLabel nextTileLabel = null;
            if(!isOutOfMap(nextGridPosX, nextGridPosY)){
                nextTileLabel = gridMap.getTile(nextGridPosX, nextGridPosY);
            }
            //When the enemy touch the filled tiles made by player. turn the tile back to empty.
            if(nextTileLabel == TileLabel.FILLED){
                hasEatenTile = true;
                gridMap.setTile(nextGridPosX, nextGridPosY, TileLabel.EMPTY);
            }
        }
    }

    /**
     * Check if the enemy is going to collide with anything.
     */
    public boolean checkWallCollision(){
        Direction nDirection = getNextDirection();
        int nextGridPosX = getGridPosX() + nDirection.X;
        int nextGridPosY = getGridPosY() + nDirection.Y;
        TileLabel nextTileLabel = null;
        if(!isOutOfMap(nextGridPosX, nextGridPosY)){
            nextTileLabel = gridMap.getTile(nextGridPosX, nextGridPosY);
        }

        //When the enemy is going to touch the wall.
        if(nextTileLabel==null || !isMoveableArea(nextTileLabel)){

            //Get the list of diagonal directions that is empty around the enemy.
            ArrayList<Direction> possibleNextDirections = gridMap.getSurroundTilesDirection(getGridPosX(), getGridPosY(), Direction.DIAGONAL, moveableArea);
            //Get wall collisions on perpendicular directions according to the next direction. 
            ArrayList<Direction> collisions = gridMap.getSurroundTilesDirection(getGridPosX(), getGridPosY(), nDirection.getSeparate(), moveableArea);
            //If there is no choice, means enemy is stuck, stop.   
            if(possibleNextDirections.size() == 0){setNextDirection(Direction.NONE); return false;}
            //Remove the possible directions according to walls absence on any perpendicular direction.
            for (Direction collision : collisions) {
                //If there is no wall on the top, the bottom diagonal direction is not possible.
                if(collision == Direction.UP){
                    possibleNextDirections.remove(Direction.BOTTOMLEFT);
                    possibleNextDirections.remove(Direction.BOTTOMRIGHT);
                }
                //If there is no wall on the bottom, the top diagonal direction is not possible.
                if(collision == Direction.DOWN){
                    possibleNextDirections.remove(Direction.TOPLEFT);
                    possibleNextDirections.remove(Direction.TOPRIGHT);
                }
                //If there is no wall on the left, the right diagonal direction is not possible.
                if(collision == Direction.LEFT){
                    possibleNextDirections.remove(Direction.TOPRIGHT);
                    possibleNextDirections.remove(Direction.BOTTOMRIGHT);
                }
                //If there is no wall on the right, the left diagonal direction is not possible.
                if(collision == Direction.RIGHT){
                    possibleNextDirections.remove(Direction.TOPLEFT);
                    possibleNextDirections.remove(Direction.BOTTOMLEFT);
                }
            }
            //Finally decide the next direction of the enemy.
            if(possibleNextDirections.size() == 1){
                //If there is only one choice, which should be the opposite direction, choose it.
                setNextDirection(possibleNextDirections.get(0));
            }else if(possibleNextDirections.size() == 0){
                //If there is no choice, which should mean that enemy is hitting angle.
                //This will happen when enemy is set to spawn at a bad place in config.
                setNextDirection(nDirection.getOpposite());
            }else{
                //If there are more than one choice, remove the opposite direction and random pick one.
                possibleNextDirections.remove(nDirection.getOpposite());
                int randomDirectionIndex = (int) (Math.random() * possibleNextDirections.size());
                setNextDirection(possibleNextDirections.get(randomDirectionIndex));
            }
            return true;
        }
        return false;
    }
        
    /**
     * Get the enemy's current grid map reference.
     * @return Current grid map.
     */
    public GridMap getGridMap() {
        return gridMap;
    }

    /**
     * Set the enemy's current grid map reference.
     * @param gridMap Current grid map.
     */
    public void setGridMap(GridMap gridMap) {
        this.gridMap = gridMap;
    }

    /**
     * Check if the given tile type is an area for the enemy to move.
     * @param checkTile The tile to check.
     * @return Return true if the given tile type is an area for the enemy to move.
     */
    public boolean isMoveableArea(TileLabel checkTile){
        for (TileLabel tileLabel : moveableArea) {
            if(tileLabel == checkTile){
                return true;
            }
        }
        return false;
    }

    /**
     * Get moveable area of the enemy.
     * @return Moveable area of the enemy.
     */
    public TileLabel[] getMoveableArea() {
        return moveableArea;
    }

    /**
     * Set moveable area of the enemy.
     * @moveableArea Moveable area of the enemy.
     */
    public void setMoveableArea(TileLabel[] moveableArea) {
        this.moveableArea = moveableArea;
    }

    /**
     * Get if enemy can make red path.
     * @return If enemy can make red path.
     */
    public boolean canMakeRedPath() {
        return canMakeRedPath;
    }

    /**
     * Get if enemy can make red path.
     * @canMakeRedPath If enemy can make red path.
     */
    public void setCanMakeRedPath(boolean canMakeRedPath) {
        this.canMakeRedPath = canMakeRedPath;
    }

    /**
     * Get if enemy can eat filled tile.
     * @return If enemy can eat filled tile.
     */
    public boolean canEatFilledTile() {
        return canEatFilledTile;
    }

    /**
     * Set if enemy can eat filled tile.
     * @canEatFilledTile If enemy can eat filled tile.
     */
    public void setCanEatFilledTile(boolean canEatFilledTile) {
        this.canEatFilledTile = canEatFilledTile;
    }
}
