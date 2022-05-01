package lawnlayer.DataClass;

/**
 * This represents a in game data object that stores the data loaded from the json file.
 */
public class LevelData{
    private String outlay;
    private boolean[][] levelMap;
    private EnemyData[] enemies;
    private float goal;

    public LevelData(String outlay, boolean[][] levelMap, EnemyData[] enemies, float goal) {
        this.outlay = outlay;
        this.levelMap = levelMap;
        this.enemies = enemies;
        this.goal = goal;
    }

    public boolean[][] getLevelMap() {
        return levelMap;
    }

    public void setLevelMap(boolean[][] levelMap) {
        this.levelMap = levelMap;
    }

    public String getOutlay() {
        return outlay;
    }

    public void setOutlay(String outlay) {
        this.outlay = outlay;
    }

    public EnemyData[] getEnemies() {
        return enemies;
    }

    public void setEnemies(EnemyData[] enemies) {
        this.enemies = enemies;
    }

    public float getGoal() {
        return goal;
    }

    public void setGoal(float goal) {
        this.goal = goal;
    } 
}