package lawnlayer;

import lawnlayer.GameObjects.Enemy;
import lawnlayer.GameObjects.GridMap;
import lawnlayer.GeneralClass.TileLabel;
import lawnlayer.GeneralClass.Vector2Int;
import processing.core.PApplet;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GridMapTest {

    @Test
    public void testGridMapConstruct(){
        App app = new App();
        
        // Tell PApplet to create the worker threads for the program
        //app.noLoop();
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();

        //to give time to initialise stuff before drawing begins
        app.delay(1000); 

        GridMap gridMap = new GridMap(app.emptyImage, app.filledImage, app.solidImage, app.pathGreenImage, app.pathRedImage, app);
        for(int x=0; x<GlobalSettings.mapWidth; x++){
            for(int y=0; y<GlobalSettings.mapHeight; y++){
                assertTrue(gridMap.getTile(x, y)==TileLabel.EMPTY);
            }
        }
    }

    @Test
    public void testGridMapFloodFill(){
        App app = new App();
        
        // Tell PApplet to create the worker threads for the program.
        //app.noLoop();
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();

        //to give time to initialise stuff before drawing begins.
        app.delay(1000); 
        
        //Put enemy at the bottom.
        for(Enemy enemy: app.enemies){
            enemy.setGridPos(20, 10);
            enemy.stop();
        }

        //Draw a horizontal path on the map and start flood fill.
        for(int x=0; x<GlobalSettings.mapWidth; x++){
            if(x!=0 && x!=GlobalSettings.mapWidth-1){
                app.gridMap.setTile(x, 5, TileLabel.PATH_G);
                app.gridMap.addCurrentPathArea(new Vector2Int(x, 5));
            }
        }
        app.gridMap.startFloodFillAroundPath();

        //If the area is filled, test success.
        for(int i=2; i<6; i++){
            for(int t=1; t<GlobalSettings.mapWidth; t++){
                if(t!=GlobalSettings.mapWidth-1){
                    assertTrue(app.gridMap.getTile(t, i)==TileLabel.FILLED);
                }
            }
        }
        app.delay(1000);
    }

    @Test
    public void testRedPathPropagate(){
        App app = new App();
        
        // Tell PApplet to create the worker threads for the program.
        //app.noLoop();
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();

        //to give time to initialise stuff before drawing begins.
        app.delay(1000); 

        //Put enemy at the bottom.
        for(Enemy enemy: app.enemies){
            enemy.setGridPos(20, 10);
            enemy.stop();
        }

        app.delay(200); 

        //Draw a horizontal path on the map and put a red path.
        for(int x=0; x<GlobalSettings.mapWidth; x++){
            if(x!=0 && x!=GlobalSettings.mapWidth-1){
                if(x==8){
                    app.gridMap.setTile(x, 5, TileLabel.PATH_R);
                }else{
                    app.gridMap.setTile(x, 5, TileLabel.PATH_G);
                }
                app.gridMap.addCurrentPathArea(new Vector2Int(x, 5));
            }
        }
        app.delay(5000);

        //Check if the path has turned to red
        for(int x=0; x<GlobalSettings.mapWidth; x++){
            if(x!=0 && x!=GlobalSettings.mapWidth-1){
                assertTrue(app.gridMap.getTile(x, 5) == TileLabel.PATH_R);
            }
        }
    }
}
