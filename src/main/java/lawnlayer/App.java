package lawnlayer;

import java.util.Timer;
import java.util.TimerTask;
import lawnlayer.Loaders.*;
import lawnlayer.DataClass.*;
import lawnlayer.GameObjects.*;
import lawnlayer.GeneralClass.*;
import lawnlayer.GameObjectClass.TextObject;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;

/**
 * The main program of the game.
 * Java has a built-in (and mandatory) garbage collector. 
 * Once an Object is no longer reachable (in Java), the collector will free the memory.
 * So I will not delete an object after I created it, I will overwrite the variable.
 */
public class App extends PApplet {

    public PFont font;

    public PImage playerImage;
	public PImage filledImage;
    public PImage emptyImage;
    public PImage solidImage;
    public PImage pathGreenImage;
    public PImage pathRedImage;

    public PImage sharkImage;
    public PImage whaleImage;
    public PImage dinoImage;
    public PImage frogImage;

    public PImage slowDownImage;
    public PImage speedUpImage;

    public TextObject titleText;
    public TextObject levelText;
    public TextObject livesText;
    public TextObject powerUpTimerText;
    public TextObject progressText;
    public TextObject loseText;
    public TextObject winText;

    public GameData gameData;
    public int currentLives;
    public int currentLevel;
    public int currentPowerUpTimer;
    public boolean currentPowerUpSpawn;
    public float currentGoal;
    public float currentProgress;
    public GameState currentGameState;

    public GridMap gridMap;
    public Player player;
    public Enemy[] enemies;
    public PowerUps powerUps;

    public Vector2Int revivePostion = new Vector2Int(0, 0);
    public boolean debug = true;

    /**
     * Initialise the setting of the window size.
    */
    public void settings() {
        size(GlobalSettings.canvasWidth, GlobalSettings.canvasHeight);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
    */
    public void setup() {
        //Set frame rate of the game.
        frameRate(GlobalSettings.frameRate);
        
        //Read all values from the json file and map file and interpret to a game data object.
        gameData = ConfigReader.read(loadJSONObject(GlobalSettings.configPath), debug);

        //Load font.
        this.font = createFont(this.getClass().getResource("Quicksand.ttf").getPath(), 128);

        //Load tiles images.
        this.playerImage = loadImage(this.getClass().getResource("Player.png").getPath());
		this.filledImage = loadImage(this.getClass().getResource("FilledTile.png").getPath());
        this.emptyImage = loadImage(this.getClass().getResource("EmptyTile.png").getPath());
        this.solidImage = loadImage(this.getClass().getResource("SolidTile.png").getPath());
        this.pathGreenImage = loadImage(this.getClass().getResource("PathGreen.png").getPath());
        this.pathRedImage = loadImage(this.getClass().getResource("PathRed.png").getPath());

        //Load power ups images.
        this.slowDownImage = loadImage(this.getClass().getResource("SlowDownPowerUp.png").getPath());
        this.speedUpImage = loadImage(this.getClass().getResource("SpeedUpPowerUp.png").getPath());

        //Load enemy images.
        this.sharkImage = loadImage(this.getClass().getResource("Shark.png").getPath());
        this.whaleImage = loadImage(this.getClass().getResource("Whale.png").getPath());
        this.dinoImage = loadImage(this.getClass().getResource("Dino.png").getPath());
        this.frogImage = loadImage(this.getClass().getResource("Frog.png").getPath());

        //Create the UI of the game.
        int screenCenterX = GlobalSettings.canvasWidth / 2;
        int screenCenterY = GlobalSettings.canvasHeight / 2;
        int topCenterY = GlobalSettings.topReserveHeight / 2;
        titleText = new TextObject(GlobalSettings.canvasWidth/7*1, topCenterY, 20, "ISLAND MAKER!", Color.WHITE, font);
        levelText = new TextObject(GlobalSettings.canvasWidth/7*5, topCenterY, 18, "Level UI NaN", Color.WHITE, font);
        progressText = new TextObject(GlobalSettings.canvasWidth/7*4, topCenterY, 18, "Progress UI NaN", Color.WHITE, font);
        powerUpTimerText = new TextObject(GlobalSettings.canvasWidth/7*6, topCenterY, 18, "Power Up UI NaN", Color.WHITE, font);
        livesText = new TextObject(GlobalSettings.canvasWidth/7*3, topCenterY, 18, "Lives UI NaN", Color.WHITE, font);
        loseText = new TextObject(screenCenterX, screenCenterY, 60, "YOU LOSE!", Color.WHITE, font);
        winText = new TextObject(screenCenterX, screenCenterY, 60, "YOU WIN!", Color.WHITE, font);

        //Create a grid map scene.
        gridMap = new GridMap(emptyImage, filledImage, solidImage, pathGreenImage, pathRedImage, this);

        //Create a player.
        player = new Player(0, 0, playerImage, gridMap);

        //Load the first level.
        loadLevel(0);
    }

    /**
     * Load and reset the level.
     * @param levelIndex The level to load.
     */
    public void loadLevel(int levelIndex){
        //Get and load data from the read data object.
        LevelData levelData = gameData.getLevels()[levelIndex];
        gridMap.loadMap(levelData.getLevelMap());
        currentLives = gameData.getLives();
        currentGoal = levelData.getGoal();
        currentGameState = GameState.PLAYING;
        currentPowerUpSpawn = false;
        currentPowerUpTimer = 0;

        //Load and spawn enemies.
        EnemyData[] enemiesData = levelData.getEnemies();
        enemies = new Enemy[enemiesData.length];
        for (int i=0; i<enemiesData.length; i++) {
            //Get required data for each enemy.
            int type = enemiesData[i].getType();
            int spawnX = enemiesData[i].getSpawnX();
            int spawnY = enemiesData[i].getSpawnY();
            boolean isRandomSpawn = enemiesData[i].isSpawnRandom();

            //Spawn if the position is in map.
            if(!GridMap.isOutOfMap(spawnX, spawnY)){
                enemies[i] = spawnEnemy(type, isRandomSpawn, spawnX, spawnY);
            }else{
                System.out.println("The enemy" + i + "spawn location is out of map.");
                System.exit(1);
            }
        }

        //Spawn and reset the player.
        resetPlayer();
        //Remove power ups.
        removePowerUps();
    }


    /**
     * Called every frame if a key is down.
     */
    public void keyPressed() {
       
        //Setup a direction variable.
        Direction pressedDirection = Direction.NONE;
        boolean enableWASD = false;
        System.out.println(this.keyCode);
        System.out.println(this.key);
        //Get the current key pressing and record it.
        if(enableWASD){
            //WASD control scheme is added to add convenience.
            switch(this.keyCode){
                case 65:
                    pressedDirection = Direction.LEFT;
                    break;
                case 87:
                    pressedDirection = Direction.UP;
                    break;
                case 68:
                    pressedDirection = Direction.RIGHT;
                    break;
                case 83:
                    pressedDirection = Direction.DOWN;
                    break;
            }
        }else{
            // Left: 37 | Up: 38 | Right: 39 | Down: 40
            switch(this.keyCode){
                case 37:
                    pressedDirection = Direction.LEFT;
                    break;
                case 38:
                    pressedDirection = Direction.UP;
                    break;
                case 39:
                    pressedDirection = Direction.RIGHT;
                    break;
                case 40:
                    pressedDirection = Direction.DOWN;
                    break;
            }
        }
        
        //Change the player direction with the recorded pressed direction.
        player.changeDirection(pressedDirection);
    }
    
    /**
     * Spawns an enemy using the data given.
     * @param type The enemy type.
     * @param randomSpawn If the enemy spawn randomly.
     * @param spawnX Spawn position X, will be ignored if randomSpawn is true.
     * @param spawnY Spawn position Y, will be ignored if randomSpawn is true.
     * @return Returns an enemy instance.
     */
    public Enemy spawnEnemy(int type, boolean randomSpawn, int spawnX, int spawnY){

        //Set default value of the enemy.
        int posX = spawnX;
        int posY = spawnY;
        PImage sprite = sharkImage;
        boolean canMakeRedPath = true;
        boolean canEatFilledTile = true;
        TileLabel[] moveableArea = new TileLabel[]{TileLabel.EMPTY};

        //Set the value according to the enemy type.
        switch(type){
            //A shark enemy that will simply kill player by placing red path.
            case 0:
                sprite = sharkImage;
                canMakeRedPath = true;
                canEatFilledTile = false;
                moveableArea = new TileLabel[]{TileLabel.EMPTY};
                break;
            //A whale enemy that will also eat the filled tile player made.
            case 1:
                sprite = whaleImage;
                canMakeRedPath = true;
                canEatFilledTile = true;
                moveableArea = new TileLabel[]{TileLabel.EMPTY};
                break;
            //A frog enemy that will enter both the filled area and empty area. But can not create red path.
            case 2:
                sprite = frogImage;
                canMakeRedPath = false;
                canEatFilledTile = false;
                moveableArea = new TileLabel[]{TileLabel.EMPTY, TileLabel.FILLED};
                break;
            //A dino enemy that will enter both the filled area and empty area.
            case 3:
                sprite = dinoImage;
                canMakeRedPath = true;
                canEatFilledTile = false;
                moveableArea = new TileLabel[]{TileLabel.EMPTY, TileLabel.FILLED};
                break;
        }

        //Get random position according to enemy's moveable area.
        if(randomSpawn){
            Vector2Int spawnPos = gridMap.getRandomPosition(moveableArea);
            posX = spawnPos.x;
            posY = spawnPos.y;
        }

        //Create the enemy.
        Enemy enemy = new Enemy(posX, posY, sprite, gridMap);
        enemy.setMoveableArea(moveableArea);
        enemy.setCanMakeRedPath(canMakeRedPath);
        enemy.setCanEatFilledTile(canEatFilledTile);
        return enemy;
    }
    
    /**
     * If there is no power ups and also no power ups going to spawn, start the delayed spawn.
     */
    public void checkPowerUpSpawn(){
        if(powerUps==null && !currentPowerUpSpawn){
            spawnPowerUpsDelay();
            currentPowerUpSpawn = true;
        }
    }

    /**
     * Spawn a power up in random time range.
     */
    public void spawnPowerUpsDelay(){
        double maxTimeSec = 10;
        double minTimeSec = 5;
        //If min is invalid, set default min.
        if(minTimeSec > maxTimeSec) {minTimeSec = 0;}
        double randomTimeSec = minTimeSec + Math.random() * (maxTimeSec - minTimeSec);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                spawnRandomPowerUps();
                System.out.println("Power Up Spawned.");
            }
            
        }, (int)(randomTimeSec * 1000));
    }

    /**
     * Spawn a random power up.
     */
    public void spawnRandomPowerUps(){
        int totalPowerUps = 2;
        int randomIndex = (int)(Math.random() * (double)(totalPowerUps));
        Vector2Int spawnPos = gridMap.getRandomPosition(new TileLabel[]{TileLabel.EMPTY});

        switch(randomIndex){
            case 0:
                powerUps = new EnemySlowPowerUp(spawnPos.x, spawnPos.y, slowDownImage, this);
                break;
            case 1:
                powerUps = new PlayerFastPowerUp(spawnPos.x, spawnPos.y, speedUpImage, this);
                break;
        }
    }

    /**
     * Remove power up from the map.
     */
    public void removePowerUps(){
        powerUps = null;
        currentPowerUpSpawn = false;
    }

    /**
     * Put player back to the spawn position.
     * Enable player control.
     * Stop all movements.
     */
    public void resetPlayer(){
        player.setGridPos(revivePostion.x, revivePostion.y);
        player.stop();
        player.revive();
    }

    /**
     * Draw the screen when playing.
     */
    public void gameScreen(){
        background(150, 100, 50);

        // First update all the game objects.
        for (Enemy enemy : enemies) {enemy.tick();}
        if (powerUps != null) {this.powerUps.tick();}
        this.gridMap.tick();
        this.player.tick();
        
        //Check if game state need to be changed.
        checkPlayerDead();
        checkFillProgress();
        checkPowerUpSpawn();
        powerUpsTimerCountDown();

        //Update UI content.
        setProgressText();
        setLivesText();
        setLevelText();
        setPowerUpTimerText();
        
        // Then draw all the game objects
        this.gridMap.draw(this);
        this.titleText.draw(this);
        this.levelText.draw(this);
        this.progressText.draw(this);
        this.livesText.draw(this);
        if (currentPowerUpTimer > 0) {this.powerUpTimerText.draw(this);} 
        if (powerUps != null) {this.powerUps.draw(this);}
        for (Enemy enemy : enemies) {enemy.draw(this);}
        this.player.draw(this);
    }

    /**
     * Draw the screen when lose.
     */
    public void loseScreen(){
        background(150, 120, 80);
        this.loseText.draw(this);
    }

    /**
     * Draw the screen when win.
     */
    public void winScreen(){
        background(150, 120, 80);
        this.winText.draw(this);
    }

    /**
     * Draw all elements in the game by current frame.
    */
    public void draw() {
        switch(currentGameState){
            case WIN:
                winScreen();
                break;
            case LOSE:
                loseScreen();
                break; 
            case PLAYING:
                gameScreen();
                break;
        }
    }

    /**
     * Check if the player is dead in the game.
     * Revive player if live is above zero.
     * Lose the game if live is under zero.
     */
    public void checkPlayerDead(){
        //If player is in dead state.
        if(!player.isAlive()){
            //Lose the game if no lives left.
            if(currentLives - 1 < 0){
                currentGameState = GameState.LOSE;
            }
            //Otherwise, revive and cost one life.
            else{
                currentLives -= 1;
                resetPlayer();
            }
        }
    }

    /**
     * If the fill has reached the level goal, load the next level.
     */
    public void checkFillProgress(){
        currentProgress = gridMap.getFilledProgress();
        //If the current progress has reached the goal.
        if(currentProgress >= currentGoal){
            //If there are no more levels, win the game.
            int totalLevels = gameData.getLevels().length;
            if(currentLevel + 1 >= totalLevels){
                currentGameState = GameState.WIN;
            //Otherwise, advance one level.
            }else{
                currentLevel += 1;
                loadLevel(currentLevel);
            }
        }
    }

    /**
     * Count down the timer if it is above zero.
     */
    public void powerUpsTimerCountDown(){
        if(currentPowerUpTimer>0){
            currentPowerUpTimer -= (int)(1000 / (float)GlobalSettings.frameRate);
        }
    }

    /**
     * Set the Progress UI.
     */
    public void setProgressText(){
        int roundValue = 1;
        float progressPercentage = currentProgress * 100;
        float goalPercentage = currentGoal * 100;

        String progressStr = String.format(java.util.Locale.ROOT, "%."+roundValue+"f", progressPercentage);
        String goalStr = String.format(java.util.Locale.ROOT, "%."+roundValue+"f", goalPercentage);

        progressText.setText("Progress: "+ progressStr + " % | " + goalStr + " %");
    }

    /**
     * Set the power up timer UI.
     */
    public void setPowerUpTimerText(){
        int roundValue = 2;
        float timerSec = (float)currentPowerUpTimer / 1000;
        String timeStr = String.format(java.util.Locale.ROOT, "%."+roundValue+"f", timerSec);
        powerUpTimerText.setText("Power Up Timer: " + timeStr);
    }

    /**
     * Set the lives UI.
     */
    public void setLivesText(){
        livesText.setText("Lives: " + Integer.toString(currentLives));
    }

    /**
     * Set the level UI.
     */
    public void setLevelText(){
        levelText.setText("Level " + Integer.toString(currentLevel + 1));
    }

    /**
     * Check if the enemy is at the specific position
     * @param x Position X.
     * @param y Position Y.
     * @return If the enemy is at the specific position
     */
    public boolean isEnemyAt(int x, int y){
        for (Enemy enemy : enemies) {
            if(x==enemy.getGridPosX() && y==enemy.getGridPosY()){
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the player is at the specific position.
     * @param x Position X.
     * @param y Position Y.
     * @return If the player is at the specific position.
     */
    public boolean isPlayerAt(int x, int y){
        if(x==player.getGridPosX() && y==player.getGridPosY()){
            return true;
        }
        return false;
    }

    /**
     * Change the enemy speed for a while.
     */
    public void enemiesChangeSpeed(float multiplyBy, int milliseconds){
        for (Enemy enemy : enemies) {
            enemy.setTintColor(Color.RED);
            enemy.setSecondsPerTile(Enemy.SECONDS_PER_TILE * multiplyBy);
        }
        System.out.println("Change Enemy Speed.");
        currentPowerUpTimer = milliseconds;

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //Revert speed after a few seconds.
                enemiesRevertSpeed();
                System.out.println("Revert Enemy Speed.");
            }
            
        },milliseconds);
    }

    /**
     * Change enemy speed back to normal, is usually called with a delay.
     */
    public void enemiesRevertSpeed(){
        for (Enemy enemy : enemies) {
            enemy.setTintColor(Color.WHITE);
            enemy.setSecondsPerTile(Enemy.SECONDS_PER_TILE);
        }
    }

    /**
     * Change the player speed for a while.
     */
    public void playerChangeSpeed(float multiplyBy, int milliseconds){
        player.setTintColor(Color.BLUE);
        player.setSecondsPerTile(Player.SECONDS_PER_TILE * multiplyBy);
        System.out.println("Change Player Speed.");
        currentPowerUpTimer = milliseconds;

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //Revert speed after a few seconds.
                playerRevertSpeed();
                System.out.println("Revert Player Speed.");
            }
            
        },milliseconds);
    }

    /**
     * Change player speed back to normal, is usually called with a delay.
     */
    public void playerRevertSpeed(){
        player.setTintColor(Color.WHITE);
        player.setSecondsPerTile(Player.SECONDS_PER_TILE);
    }

    /** 
     * @param args
     */
    public static void main(String[] args) {
        PApplet.main("lawnlayer.App");
    }
}
