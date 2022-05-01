package lawnlayer.GameObjectClass;

import lawnlayer.GlobalSettings;
import processing.core.PImage;

/**
 * Represents a sprite object positioned on the grid.
 * Modify the grid position will affect the screen position.
 * Modify the screen position will only change the display location instead of the grid position in computing logic.
 */
public abstract class GridObject extends SpriteObject {

    private int gridPosX;   //The position X on grid.
    private int gridPosY;   //The position Y on grid.

    /**
     * Setup the grid object.
     * @param gridPosX Position X on grid.
     * @param gridPosY Position Y on gird.
     * @param sprite The sprite reference.
     */
    public GridObject(int gridPosX, int gridPosY, PImage sprite) {
        super(gridPosX * GlobalSettings.mapTileSize, gridPosY * GlobalSettings.mapTileSize + GlobalSettings.topReserveHeight, sprite);
        this.gridPosX = gridPosX;
        this.gridPosY = gridPosY;
    }

    /**
     * Get the Position Y on grid.
     * @return The position Y on grid.
     */
    public int getGridPosY() {
        return gridPosY;
    }

    /**
     * Set the position Y on grid as well as on screen. Set the position on screen lower according to the reserved height.
     * @param gridPosY The position Y on grid.
     */
    public void setGridPosY(int gridPosY) {
        this.setY(gridPosY * GlobalSettings.mapTileSize + GlobalSettings.topReserveHeight);
        this.gridPosY = gridPosY;
    }

    /**
     * Get position Y on grid.
     * @return Position Y on grid.
     */
    public int getGridPosX() {
        return gridPosX;
    }
    
    /**
     * Set the position X on grid as well as on screen.
     * @param gridPosX Position X on grid.
     */
    public void setGridPosX(int gridPosX) {
        this.setX(gridPosX * GlobalSettings.mapTileSize);
        this.gridPosX = gridPosX;
    }

    /**
     * Set the position on grid as well as on screen.
     * @param gridPosX Position X on grid.
     * @param gridPosY Position Y on grid.
     */
    public void setGridPos(int gridPosX, int gridPosY) {
        this.setX(gridPosX * GlobalSettings.mapTileSize);
        this.setY(gridPosY * GlobalSettings.mapTileSize + GlobalSettings.topReserveHeight);
        this.gridPosX = gridPosX;
        this.gridPosY = gridPosY;
    }

    /**
     * Check if the object is at the same position as the given object.
     * @param gridObject The grid object to compare position.
     * @return Return true if the two grid objects are overlapped on grid.
     */
    public boolean isCollidedWith(GridObject gridObject){
        return gridObject.getGridPosX() == this.gridPosX && gridObject.getGridPosY() == this.gridPosY;
    }

    /**
     * Check if the object is at the same position as the given position.
     * @param x Position X.
     * @param y Position Y.
     * @return Return true if the object is at the exact position.
     */
    public boolean isOnGridPosition(int x, int y){
        return x == this.gridPosX && y == this.gridPosY;
    }
}
