package lawnlayer;

import processing.core.PApplet;
import processing.core.PImage;
import lawnlayer.GameObjects.GridMap;
import lawnlayer.GameObjects.Player;
import lawnlayer.GeneralClass.Direction;
import lawnlayer.GeneralClass.TileLabel;
import lawnlayer.GeneralClass.Vector2Int;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    PImage testImage;
    GridMap emptyGridMap;

    @Test
    public void testConstructPlayer(){
        Player player = new Player(10, 20, testImage, emptyGridMap);
        assertTrue(player.getGridPosX() == 10);
        assertTrue(player.getGridPosY() == 20);
    }

    @Test
    public void testGridPosGetSet(){
        Player player = new Player(10, 20, testImage, emptyGridMap);

        player.setGridPosX(15);
        player.setGridPosY(34);

        assertTrue(player.getGridPosX()==15);
        assertTrue(player.getGridPosY()==34);

        player.setGridPos(24, 18);

        assertTrue(player.getGridPosX()==24);
        assertTrue(player.getGridPosY()==18);
    }

    @Test
    public void testSecondsPerTileGetSet(){
        Player player = new Player(10, 20, testImage, emptyGridMap);
        player.setSecondsPerTile(15);
        assertEquals(15, player.getSecondsPerTile());
    }

    @Test
    public void testDirectionGetSet(){
        Player player = new Player(10, 20, testImage, emptyGridMap);
        //Set next direction.
        player.changeDirection(Direction.RIGHT);
        assertEquals(Direction.RIGHT, player.getNextDirection());
        player.changeDirection(Direction.LEFT);
        assertEquals(Direction.LEFT, player.getNextDirection());
        player.changeDirection(Direction.UP);
        assertEquals(Direction.UP, player.getNextDirection());
        player.changeDirection(Direction.DOWN);
        assertEquals(Direction.DOWN, player.getNextDirection());

        //Stop all direction.
        player.stop();
        assertEquals(Direction.NONE, player.getCurrentDirection());
        assertEquals(Direction.NONE, player.getNextDirection());
    }

    @Test
    public void testPlayerSituationCheck(){
        App app = new App();
        
        // Tell PApplet to create the worker threads for the program.
        //app.noLoop();
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();

        //to give time to initialise stuff before drawing begins.
        app.delay(1000); 
        
        Player player = new Player(2, 2, testImage, app.gridMap);

        //Put enemy at 2,2 and check dead at 2,2 
        app.enemies[0].setGridPos(2, 2);
        assertTrue(player.checkDead(2, 2));

        //Put a path at 4,4 and check dead
        app.gridMap.setTile(4, 4, TileLabel.PATH_G);
        assertTrue(player.checkDead(4, 4));
        
        //Create a small path and add a red one at the end, check dead
        app.gridMap.addCurrentPathArea(new Vector2Int(6, 6));
        app.gridMap.addCurrentPathArea(new Vector2Int(6, 7));
        app.gridMap.setTile(6, 7, TileLabel.PATH_R);
        assertTrue(player.checkDead(0, 0));

        //Create a solid tile at 8,8 and put player at 8,8, check movement
        app.gridMap.setTile(8, 8, TileLabel.SOLID);
        app.player.setGridPos(8, 8);
        app.player.checkMovement(8, 8);
        assertTrue(player.getNextDirection()==Direction.NONE);
    }
}
