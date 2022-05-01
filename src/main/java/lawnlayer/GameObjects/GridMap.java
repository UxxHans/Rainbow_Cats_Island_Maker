package lawnlayer.GameObjects;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;

import lawnlayer.App;
import lawnlayer.GlobalSettings;
import lawnlayer.GeneralClass.Direction;
import lawnlayer.GeneralClass.TileLabel;
import lawnlayer.GeneralClass.Vector2Int;

/**
 * This class represent a grid map that is based on 2d int coordinates.
 * It is the scene for the player to move and play.
 * The object has a few handy game rule functions to inspect and modify the map.
 */
public class GridMap{

    private GridMapTile[][] gridMap;    //This is where the information of grid tiles is stored.
    private PImage emptyImage;          //Reference of the empty grid texture.
    private PImage filledImage;         //Reference of the filled grid texture.
    private PImage solidImage;          //Reference of the solid grid texture.
    private PImage pathGreenImage;      //Reference of the green path texture.
    private PImage pathRedImage;        //Reference of the red path texture.

    private int pathSpreadTimer = 0;    //Timer that record the frames before a spread of the red path tile.
    private int framesPerTile = 3;      //Frames before the spread of a red path tile.
    private App mainProgram;            //The main program reference to get enemies position.

    //This is a list to store all the flood filled grid positions of the current flood fill session.
    private ArrayList<Vector2Int> currentFloodFilledArea = new ArrayList<Vector2Int>();
    //The list to record all the positions of path tiles the player creates on the map.
    private ArrayList<Vector2Int> currentPathArea = new ArrayList<Vector2Int>();
    
    /**
     * Create a grid map with the sprite references of different tiles.
     * @param emptyImage        Reference of the empty grid texture.
     * @param filledImage       Reference of the filled grid texture.
     * @param solidImage        Reference of the solid grid texture.
     * @param pathGreenImage    Reference of the green path texture.
     * @param pathRedImage      Reference of the red path texture.
     */
    public GridMap(PImage emptyImage, PImage filledImage, PImage solidImage, PImage pathGreenImage, PImage pathRedImage, App mainProgram) {
        this.emptyImage = emptyImage;
        this.filledImage = filledImage;
        this.solidImage = solidImage;
        this.pathGreenImage = pathGreenImage;
        this.pathRedImage = pathRedImage;
        this.mainProgram = mainProgram;
        //Create an empty grid map after the map object is created.
        createMap (GlobalSettings.mapWidth, GlobalSettings.mapHeight);
    }

    /**
     * Creates an empty map with given size.
     * @param width Map width.
     * @param height Map height.
     */
    private void createMap(int width, int height){
        //Create an empty 2d array.
        gridMap = new GridMapTile[height][width];
        //For each tile in the array.
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                //Setup each tile with the given empty tile label and position.
                gridMap[y][x] = new GridMapTile(x, y, getSprite(TileLabel.EMPTY), TileLabel.EMPTY);
            }
        }
    }
    
    /**
     * Setup the map with given boolean 2d array.
     * @param mapData The boolean 2d array that represents wall and empty space.
     */
    public void loadMap(boolean[][] mapData){
        //Exit if the size of two map is different.
        if(mapData==null || mapData.length != gridMap.length || mapData[0].length != gridMap[0].length){
            System.out.println("Load map failed, map data is invaild. Validate the map file.");
            System.exit(1);
        }

        //Set the tile solid or empty according to the 2d boolean array.
        for(int y=0; y<gridMap.length; y++){
            for(int x=0; x<gridMap[0].length; x++){
                //If the tile in map data is true, it is a solid tile
                if(mapData[y][x]){
                    gridMap[y][x].setTileLabel(TileLabel.SOLID);
                //If the tile in map data is not true, it is an empty tile.
                }else{
                    gridMap[y][x].setTileLabel(TileLabel.EMPTY);
                }
            }
        }
    }

    /**
     * Recrusive function that fills the area with FLOODFILL tiles if the start position is valid.
     * The function will not stop halfway if enemy is found at the filling position.
     * @param x The x position of the starting point.
     * @param y The y position of the starting point.
     * @return Return false if enemy was found or the flood fill started from a invalid tile.
     */
    private boolean floodFill(int x, int y)
    {
        //If the very start fill position is not empty, return false.
        if(gridMap[y][x].getTileLabel() != TileLabel.EMPTY){
            return false;
        }

        //Set true if the enemy is not at the filling position.
        boolean isAreaClear = !isEnemyAt(x, y);

        //Set the current tile to flood filled.
        gridMap[y][x].setTileLabel(TileLabel.FLOODFILLED);
        //System.out.println("Flood Filled Tile: <" + x + ", " + y + ">");

        //Add current tile to current filled area.
        currentFloodFilledArea.add(new Vector2Int(x, y));

        //Flood fill any empty area on 4 perpendicular directions, if any fill process found an enemy, the whole flood fill will return false.
        for(int i=0; i<Direction.PERPENDICULAR.length; i++){
            int vectorX = Direction.PERPENDICULAR[i].X;
            int vectorY = Direction.PERPENDICULAR[i].Y;
            if(!isOutOfMap(x+vectorX, y+vectorY) && gridMap[y+vectorY][x+vectorX].getTileLabel() == TileLabel.EMPTY){
                if(!floodFill(x+vectorX, y+vectorY)){
                    isAreaClear = false;
                }
            }
        }

        //Return false if enemy found in any of the filling tile or the flood fill started from a invalid tile.
        return isAreaClear;
    }
    
    /**
     * Do a complete flood fill at the position.
     * If fill succeed, set the filled area to FILLED and clear other FLOODFILLED tiles.
     * If fill failed (Enemy in fill area | Wrong starting position), leave the FLOODFILLED tiles on map.
     * @param x The position X.
     * @param y The position Y.
     * @return Return false if failed, true if succeed.
     */
    public boolean startFloodFill(int x, int y){
        //Do a complete flood fill and if there is no enemy in the filled area. Fill is complete.
        if(floodFill(x, y)){
            //Set area that recorded in the list from FLOODFILLED to FILLED.
            //Clear the list and the temporary tiles.
            setFloodFillTiles();
            return true;
        }
        //If there is enemy in the filled area or position already filled.
        //Clean the recorded flood filled area list for the next fill.
        currentFloodFilledArea.clear();
        return false;
    }
    
    /**
     * Set the current flood filling area to filled area
     * Called when the flood filling process succeed with no enemy overlap.
     */
    public void setFloodFillTiles(){
        //Set the tile labels in the area.
        for(Vector2Int floodFillTile: currentFloodFilledArea){
            gridMap[floodFillTile.y][floodFillTile.x].setTileLabel(TileLabel.FILLED);
        }
        //Clean the list and other temporary tiles for next flood fill.
        currentFloodFilledArea.clear();
    }

    /**
     * Start flood fill around the path made by player.
     */
    public void startFloodFillAroundPath(){
        ArrayList<Vector2Int> currentPathArea = getCurrentPathArea();
        //If there are paths drawn by player.
        if(currentPathArea != null && currentPathArea.size() > 0){
            //For each path tile.
            for(Vector2Int currentPathTile: currentPathArea){
                //Get any surrounding empty tiles if available.
                ArrayList<Vector2Int> surroundTiles = new ArrayList<Vector2Int>();
                for(int i=0; i<Direction.PERPENDICULAR.length; i++){
                    int checkPosX = currentPathTile.x + Direction.PERPENDICULAR[i].X;
                    int checkPosY = currentPathTile.y + Direction.PERPENDICULAR[i].Y;
                    if(!isOutOfMap(checkPosX, checkPosY) && gridMap[checkPosY][checkPosX].getTileLabel() == TileLabel.EMPTY){
                        surroundTiles.add(new Vector2Int(checkPosX, checkPosY));
                    }
                }
                //If there are empty tiles around the current path tile.
                if(surroundTiles!=null && surroundTiles.size() > 0){
                    //For every empty tile surrounded around.
                    for(Vector2Int currentTile: surroundTiles){
                        //Do a complete flood fill. Fill with "FLOODFILLED" tiles. These tiles will not be filled again.
                        //If there is no enemy in this flood fill. Set the tiles of this flood fill as "FILLED". 
                        //The fill will be restarted at other position around the path.
                        startFloodFill(currentTile.x, currentTile.y);
                    }
                }
            }
            //Just fill the path if no where to fill or enemy found on both side.
            fillPathArea();
            //Remove all temporary tiles in the map. Remove all "FLOODFILLED" tiles.
            removeTemporaryTiles();
            //Print message.
            System.out.println("Flood Filled Area.");
        }
    }

    /**
     * Fill the path (trail) that the player creates on the map with "FILLED" tiles.
     */
    public void fillPathArea(){
        //Fill each of the path tile.
        for(Vector2Int currentPathTile: currentPathArea){
            setTile(currentPathTile.x, currentPathTile.y, TileLabel.FILLED);
        }
        //Remove all path record.
        currentPathArea.clear();
    }

    /**
     * Remove all temporary tiles in the map.
     */
    public void removeTemporaryTiles(){
        removeTiles(
            new TileLabel[]{
                TileLabel.FLOODFILLED,
                TileLabel.PATH_G,
                TileLabel.PATH_R
        });
    }

    /**
     * Remove a tile label in map and replace them with empty.
     */
    public void removeTiles(TileLabel[] tiles){
        //Go through each of the tile in the map.
        for(int y=0; y<gridMap.length; y++){
            for(int x=0; x<gridMap[0].length; x++){
                //For each tile in the array.
                //If current tile checking is the tile to be removed, set tile to empty.
                for(int i=0; i<tiles.length; i++){
                    if(gridMap[y][x].getTileLabel() == tiles[i]){
                        gridMap[y][x].setTileLabel(TileLabel.EMPTY);
                    }
                }
            }
        }
    }
    
    /**
     * Is tile labels at these direction?
     * Return a list of direction found around the check position that has the specified tile label. 
     * @param x Position X of the tile.
     * @param y Position Y of the tile.
     * @param checkList The directions to check.
     * @param tileLabel The tile label to match.
     * @return Return a list of direction from the tiles around the check position that has the specified tile label.
     */
    public ArrayList<Direction> getSurroundTilesDirection(int gridPositionX, int gridPositionY, Direction[] checkList, TileLabel[] tileLabel){
        
        ArrayList<Direction> result = new ArrayList<Direction>();

        for(int i=0; i<checkList.length; i++){
            int checkPosX = gridPositionX + checkList[i].X;
            int checkPosY = gridPositionY + checkList[i].Y;
            for(int t=0; t<tileLabel.length; t++){
                if(!isOutOfMap(checkPosX, checkPosY) && gridMap[checkPosY][checkPosX].getTileLabel() == tileLabel[t]){
                    result.add(checkList[i]);
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Spread any red path in the map if found. Spread on two sides on the red path.
     */
    public void spreadRedPath(){
        for (int i=0; i<currentPathArea.size(); i++) {

            Vector2Int current = currentPathArea.get(i);

            //If this is not the last one
            if(i < currentPathArea.size()-1){
                Vector2Int next = currentPathArea.get(i+1);
                //If this tile is red and the next is green, set the green to red.
                if(getTile(current.x, current.y)==TileLabel.PATH_R && getTile(next.x, next.y)==TileLabel.PATH_G){
                    setTile(next.x, next.y, TileLabel.PATH_R);
                    //Skip one to stop the spread.
                    i+=1;
                }
            }

            //If this is not the first one
            if(i > 0){
                Vector2Int previous = currentPathArea.get(i-1);
                //If this tile is red and the previous one is green, set the green to red.
                if(getTile(current.x, current.y)==TileLabel.PATH_R && getTile(previous.x, previous.y)==TileLabel.PATH_G){
                    setTile(previous.x, previous.y, TileLabel.PATH_R);
                }
            }
        }
    }

    /**
     * Get the progress of the filled tiles in the map.
     * @return The progress of the filled tiles in the map. Ranged from 0 to 1.
     */
    public float getFilledProgress(){
        int filledTiles = 0;
        int otherTiles = 0;
        //Go through each of the tile in the map.
        for(int y=0; y<gridMap.length; y++){
            for(int x=0; x<gridMap[0].length; x++){
                //Get the filled tiles count
                if(gridMap[y][x].getTileLabel() == TileLabel.FILLED){
                    filledTiles++;
                }
                //Get all other tiles count except the wall.
                else if(gridMap[y][x].getTileLabel() != TileLabel.SOLID){
                    otherTiles++;
                }
            }
        }
        return (float)filledTiles / (float)(filledTiles + otherTiles);
    }

    /**
     * Get a random position in the tile types on the map.
     */
    public Vector2Int getRandomPosition(TileLabel[] spawnArea){
        ArrayList<Vector2Int> allPositions = getTiles(spawnArea);
        if(allPositions.size()<=0){
            System.out.println("No available position for to spawn.");
            return new Vector2Int(0, 0);
        }
        int randomIndex = (int)(Math.random() * (double)(allPositions.size()));
        return allPositions.get(randomIndex);
    }

    /**
     * Draw the whole map by setting and drawing each tile.
     * @param app The main program.
     */
    public void tick(){
        pathSpreadTimer++;
        if(pathSpreadTimer >= framesPerTile){
            spreadRedPath();
            pathSpreadTimer = 0;
        }
    }

    /**
     * Draw the whole map by setting and drawing each tile.
     * @param app The main program.
     */
    public void draw(PApplet app){
        //For each tile in the map.
        for(int y = 0; y < gridMap.length; y++){
            for(int x = 0; x < gridMap[0].length; x++){
                //Get the tile label of the current tile.
                TileLabel currentTileLabel = gridMap[y][x].getTileLabel();
                //Get the tile image using the tile label.
                PImage currentTileImage = getSprite(currentTileLabel);
                gridMap[y][x].setSprite(currentTileImage);
                //Draw this tile.
                gridMap[y][x].draw(app);
            }
        }
    }

    /**
     * Get the sprite reference according to the tile label.
     * @param tileLabel The tile label to get sprite reference.
     * @return Sprite reference of the tile label.
     */
    public PImage getSprite(TileLabel tileLabel){
        switch(tileLabel){
            case EMPTY:
                return emptyImage;
            case FILLED:
                return filledImage;
            case SOLID:
                return solidImage;
            case PATH_G:
                return pathGreenImage;
            case PATH_R:
                return pathRedImage;
            default:
                return solidImage;
        }
    }

    /**
     * Set a tile label of a tile object in the map.
     * @param x Position X.
     * @param y Position Y.
     * @param tile The tile object.
     */
    public void setTile(int x, int y, TileLabel tile){
        gridMap[y][x].setTileLabel(tile);
    }

    /**
     * Get a tile label from a tile object in the map.
     * @param x Position X.
     * @param y Position Y.
     * @return The tile object.
     */
    public TileLabel getTile(int x, int y){
        return gridMap[y][x].getTileLabel();
    }

    /**
     * Get all tiles position of the tile type found.
     * @param tiles The tile label that we want to search positions
     * @return A list of positions that represents all tiles with the labels in map.
     */
    public ArrayList<Vector2Int> getTiles(TileLabel[] tiles){
        ArrayList<Vector2Int> allPositions = new ArrayList<Vector2Int>();
        //Go through each of the tile in the map.
        for(int y=0; y<gridMap.length; y++){
            for(int x=0; x<gridMap[0].length; x++){
                //For each tile in the array.
                //If current tile checking is the tile, record it.
                for(int i=0; i<tiles.length; i++){
                    if(gridMap[y][x].getTileLabel() == tiles[i]){
                        allPositions.add(new Vector2Int(x, y));
                    }
                }
            }
        }
        return allPositions;
    }

    /**
     * Add path tile to the list of path tiles position.
     * @pathTile The path tiles to be added to the list.
     */
    public void addCurrentPathArea(Vector2Int pathTile){
        currentPathArea.add(pathTile);
    }

    /**
     * Get current path list.
     * @return A list of path tiles position.
     */
    public ArrayList<Vector2Int> getCurrentPathArea(){
        return currentPathArea;
    }

    /**
     * Clear current path list.
     */
    public void clearCurrentPathArea(){
        currentPathArea.clear();
    }

    /**
     * Is enemy at the grid position?
     * @param gridPositionX The position X on grid.
     * @param gridPositionY The position Y on grid.
     * @return Return true is enemy is on the grid position.
     */
    public boolean isEnemyAt(int gridPositionX, int gridPositionY){
        return mainProgram.isEnemyAt(gridPositionX, gridPositionY);
    }


    /**
     * Check if the position is out of the map or not.
     * @param gridPositionX The position X.
     * @param gridPositionY The position Y.
     * @return Return false if the position is in the map, true if the position is out of the map.
     */
    public static boolean isOutOfMap(int gridPositionX, int gridPositionY){
        return gridPositionX < 0 || gridPositionY < 0 || gridPositionX >= GlobalSettings.mapWidth || gridPositionY >= GlobalSettings.mapHeight;
    }

}
