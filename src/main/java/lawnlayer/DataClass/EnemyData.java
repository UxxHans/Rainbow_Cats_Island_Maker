package lawnlayer.DataClass;

/**
 * This represents a in game data object that stores the data loaded from the json file.
 */
public class EnemyData{
    private int type;
    private boolean spawnRandom;
    private int spawnX;
    private int spawnY;

    public EnemyData(int type, int spawnX, int spawnY) {
        this.type = type;
        this.spawnRandom = false;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
    }

    public EnemyData(int type, boolean spawnRandom) {
        this.type = type;
        this.spawnRandom = true;
    }
    
    public boolean isSpawnRandom() {
        return spawnRandom;
    }

    public void setSpawnRandom(boolean spawnRandom) {
        this.spawnRandom = spawnRandom;
    }

    public int getSpawnY() {
        return spawnY;
    }

    public void setSpawnY(int spawnY) {
        this.spawnY = spawnY;
    }

    public int getSpawnX() {
        return spawnX;
    }

    public void setSpawnX(int spawnX) {
        this.spawnX = spawnX;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}