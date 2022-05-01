package lawnlayer;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    public void testAppLoadResources(){
        App app = new App();

        // Tell PApplet to create the worker threads for the program
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();

        //to give time to initialise stuff before drawing begins
        app.delay(1000); 

        //Font is not null.
        assertTrue(app.font!=null);        

        //All enemies and players images are not null.
        assertTrue(app.playerImage!=null);
        assertTrue(app.whaleImage!=null);
        assertTrue(app.sharkImage!=null);
        assertTrue(app.dinoImage!=null);
        assertTrue(app.frogImage!=null);

        //All tiles images are not null.
        assertTrue(app.solidImage!=null);
        assertTrue(app.filledImage!=null);
        assertTrue(app.emptyImage!=null);
        assertTrue(app.pathGreenImage!=null);
        assertTrue(app.pathRedImage!=null);

        //All power ups images are not null.
        assertTrue(app.speedUpImage!=null);        
        assertTrue(app.slowDownImage!=null);

        //All UI instances are not null.
        assertTrue(app.titleText!=null);        
        assertTrue(app.levelText!=null);
        assertTrue(app.progressText!=null);
        assertTrue(app.powerUpTimerText!=null);
        assertTrue(app.livesText!=null);
        assertTrue(app.loseText!=null);
        assertTrue(app.winText!=null);

        //Grid map instance is not null.
        assertTrue(app.gridMap!=null);

        //Player instance is not null.
        assertTrue(app.player!=null);
    }

    @Test
    public void testAppPowerUps(){
        App app = new App();

        // Tell PApplet to create the worker threads for the program
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();

        //to give time to initialise stuff before drawing begins
        app.delay(1000); 

        float originalEnemySpeed = app.enemies[0].getSecondsPerTile();
        //Check if enemy speed is changed correctly.
        app.enemiesChangeSpeed(0.5f, 100);
        assertTrue(app.enemies[0].getSecondsPerTile() == originalEnemySpeed*0.5);
        assertTrue(app.currentPowerUpTimer>0);
        //Check if enemy speed is reverted.
        app.enemiesRevertSpeed();
        assertTrue(app.enemies[0].getSecondsPerTile() == originalEnemySpeed);

        float originalPlayerSpeed = app.player.getSecondsPerTile();
        //Check if player speed is changed correctly.
        app.playerChangeSpeed(0.5f, 100);
        assertTrue(app.player.getSecondsPerTile() == originalPlayerSpeed*0.5);
        assertTrue(app.currentPowerUpTimer>0);
        //Check if player speed is reverted.
        app.playerRevertSpeed();
        assertTrue(app.player.getSecondsPerTile() == originalPlayerSpeed);
    }
}
