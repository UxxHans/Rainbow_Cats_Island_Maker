package lawnlayer;

import processing.core.PImage;
import lawnlayer.GameObjects.Enemy;
import lawnlayer.GameObjects.GridMap;
import lawnlayer.GeneralClass.Direction;
import lawnlayer.GeneralClass.TileLabel;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EnemyTest {
    PImage testImage;
    GridMap gridMap;

    @Test
    public void testConstructEnemy(){
        Enemy enemy = new Enemy(10, 20, testImage, gridMap);
        assertTrue(enemy.getGridPosX() == 10);
        assertTrue(enemy.getGridPosY() == 20);
    }

    @Test
    public void testGridPosGetSet(){
        Enemy enemy = new Enemy(10, 20, testImage, gridMap);

        enemy.setGridPosX(15);
        enemy.setGridPosY(34);

        assertTrue(enemy.getGridPosX()==15);
        assertTrue(enemy.getGridPosY()==34);

        enemy.setGridPos(24, 18);

        assertTrue(enemy.getGridPosX()==24);
        assertTrue(enemy.getGridPosY()==18);
    }

    @Test
    public void testMoveableAreaGetSet(){
        Enemy enemy = new Enemy(10, 20, testImage, gridMap);
        TileLabel[] moveableArea = new TileLabel[]{TileLabel.EMPTY, TileLabel.FILLED};

        enemy.setMoveableArea(moveableArea);

        assertEquals(moveableArea, enemy.getMoveableArea());
    }

    @Test
    public void testNextDirectionGetSet(){
        Enemy enemy = new Enemy(10, 20, testImage, gridMap);
        enemy.stop();

        assertEquals(Direction.NONE, enemy.getNextDirection());
        assertEquals(Direction.NONE, enemy.getCurrentDirection());
    }

    @Test
    public void testCanEatFilledTileGetSet(){
        Enemy enemy = new Enemy(10, 20, testImage, gridMap);
        enemy.setCanEatFilledTile(true);
        assertTrue(enemy.canEatFilledTile());
    }

    @Test
    public void testCanMakeRedPathGetSet(){
        Enemy enemy = new Enemy(10, 20, testImage, gridMap);
        enemy.setCanMakeRedPath(false);
        assertTrue(!enemy.canMakeRedPath());
    }
}
