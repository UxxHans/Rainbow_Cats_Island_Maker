package lawnlayer.DataClass;

/**
 * This represents a in game data object that stores the data loaded from the json file.
 */
public class GameData{
    private LevelData[] levels;
    private int lives;

    public GameData(LevelData[] levels, int lives) {
        this.levels = levels;
        this.lives = lives;
    }
    
    public LevelData[] getLevels() {
        return levels;
    }
    public void setLevels(LevelData[] levels) {
        this.levels = levels;
    }
    public int getLives() {
        return lives;
    }
    public void setLives(int lives) {
        this.lives = lives;
    }
}