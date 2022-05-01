package lawnlayer.Loaders;

import lawnlayer.GlobalSettings;
import lawnlayer.DataClass.*;
import processing.data.JSONObject;
import processing.data.JSONArray;

/**
 * Read the config json file and load the level data and game rule to the game data object.
 */
public class ConfigReader {

    /**
     * Read the config and create a game data object.
     * @param values The whole json object to convert.
     * @param debug When this is set to true, reader will print out all data from the generated game data object to the console.
     * @return Return a game data object that stores the information of the game levels and rules.
     */
    public static GameData read(JSONObject values, boolean debug){

        //Get values from the first layer.
        int lives = values.getInt("lives");
        JSONArray levels = values.getJSONArray("levels");

        //Create an array to store all levels data.
        LevelData[] allLevelsData = new LevelData[levels.size()];

        //Read the level data one by one.
        for (int i = 0; i < levels.size(); i++) {
            //Read in one level as the current level reading.
            JSONObject currentLevel = levels.getJSONObject(i);

            //Read values from current level reading.
            String outlay = currentLevel.getString("outlay");
            float goal = currentLevel.getFloat("goal");
            JSONArray enemies = currentLevel.getJSONArray("enemies");

            //Read the txt file from the path read
            boolean[][] levelMap = MapReader.read(outlay, GlobalSettings.mapWidth, GlobalSettings.mapHeight, debug);

            //Create an array to store all enemies data.
            EnemyData[] allEnemiesData = new EnemyData[enemies.size()];

            //Read the enemies data one by one.
            for(int t = 0; t < enemies.size(); t++){
                //Read in one enemy as the current enemy reading.
                JSONObject currentEnemy = enemies.getJSONObject(t);

                //Read values from the current enemy reading.
                int type = currentEnemy.getInt("type");
                String spawn = currentEnemy.getString("spawn");

                //Convert the data to coordinates or random bool.
                if(spawn.equals("random")) {
                    //Create an enemy data to store the values just read.
                    EnemyData enemyData = new EnemyData(type, true);
                    //Put the enemy data into the array.
                    allEnemiesData[t] = enemyData;

                }else{
                    //Get the coordinates
                    String[] coordinates = spawn.split(",");
                    
                    //If the coordinate is invalid return error.
                    if(coordinates.length!=2){
                        System.out.println("LevelIndex: " + i + " EnemyIndex: " + t + " | Enemy coordinates must be <x,y>!");
                        return null;
                    }
                    
                    try{
                        //Try to convert read data into int.
                        int x = Integer.parseInt(coordinates[0]);
                        int y = Integer.parseInt(coordinates[1]);
                        //Create an enemy data to store the values just read.
                        EnemyData enemyData = new EnemyData(type, x, y);
                        //Put the enemy data into the array.
                        allEnemiesData[t] = enemyData;

                    }catch(NumberFormatException e){
                        System.out.println("LevelIndex: " + i + " EnemyIndex: " + t + " | Enemy coordinates must be integer!");
                        return null;
                    }
                }
            }

            //Create a new level data to store the values just read.
            LevelData levelData = new LevelData(outlay, levelMap, allEnemiesData, goal);

            //Put the levels data into the array.
            allLevelsData[i] = levelData;
        }


        //Create a new game data object to store all game data from json file.
        GameData gameData = new GameData(allLevelsData, lives);

        //For debugging, Print all the data in the game data object that is just generated.
        if(debug){ debugPrintGameData(gameData); }

        //Return the data.
        return gameData;
    }

    /**
     * Print all data in a game data object to the console.
     * @param gameData The game data object to debug.
     */
    public static void debugPrintGameData(GameData gameData){
        //Get and print lives.
        int lives = gameData.getLives();
        System.out.printf("%nConfigReaderDebug:%nLives: " + lives);

        //Get levels data and store as an array.
        LevelData[] levels = gameData.getLevels();

        //Read an print the level data one by one.
        for(int i=0; i<levels.length; i++){
            //Get and print outlay and goal.
            String outlay = levels[i].getOutlay();
            float goal = levels[i].getGoal();

            System.out.printf("%n%nLevelIndex %d | Outlay:%s | goal:%.2f", i, outlay, goal);
            EnemyData[] enemies = levels[i].getEnemies();
            
            //Get and print enemies data one by one
            for(int t=0; t<enemies.length; t++){

                boolean spawnRandom = enemies[t].isSpawnRandom();
                int spawnX = enemies[t].getSpawnX();
                int spawnY = enemies[t].getSpawnY();
                int type = enemies[t].getType();
                
                //Print "Random" if boolean spawnRandom is true, otherwise print the xy coordinates.
                if(!spawnRandom){
                    System.out.printf("%nEnemyIndex %d | Type:%d | Spawn:%d,%d", t, type, spawnX, spawnY);
                }else{
                    System.out.printf("%nEnemyIndex %d | Type:%d | Spawn:Random", t, type);
                }
            }
        }
        System.out.printf("%n%n");
    }
}
