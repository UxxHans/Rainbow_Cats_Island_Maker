package lawnlayer.GameObjects;

import lawnlayer.GameObjectClass.GridObject;
import lawnlayer.GeneralClass.TileLabel;
import processing.core.PImage;

/**
 * Represents a simple tile object in the grid map.
 * The tile type is defined by the tile label.
 */
public class GridMapTile extends GridObject {

    //Define the type of the tile for the map script to manage.
    private TileLabel tileLabel;

    //Setup the map tile as a grid object.
    public GridMapTile(int gridPosX, int gridPosY, PImage sprite, TileLabel tileLabel) {
        super(gridPosX, gridPosY, sprite);
        this.tileLabel = tileLabel;
    }
    
    
    /** 
     * @return The current label of this tile.
     */
    public TileLabel getTileLabel() {
        return tileLabel;
    }
    
    
    /** 
     * @param tileLabel The current label of this tile.
     */
    public void setTileLabel(TileLabel tileLabel) {
        this.tileLabel = tileLabel;
    }

    //Map tiles are static.
    public void tick(){}

}
