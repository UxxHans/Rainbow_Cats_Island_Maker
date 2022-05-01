package lawnlayer.Loaders;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * This is a map reader that read the txt file and convert the data into a 2d boolean array.
 */
public class MapReader {

    /**
     * Read the txt file and convert the map into a 2d boolean array. (Solid and Empty)
     * @param filePath The path of the map text file.
     * @param width The width of the map.
     * @param height The height of the map.
     * @param debug When this is set to true, the reader will print out the whole 2d boolean array in console.
     * @return Return a 2d boolean array that represent the read txt file. (Solid - true | Empty - false)
     */
    public static boolean[][] read(String filePath, int width, int height, boolean debug){
        try{
            boolean[][] map = new boolean[height][width];
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            String[] mapLines = new String[height];

            //Read each line in the file and add them in the array.
            for(int i=0; i<height; i++){
                //If the scanner can not scan any more lines, height is not enough.
                if(!scanner.hasNextLine()){
                    System.out.println("Map height invalid!");
                    //Close the reader to avoid memory leak.
                    scanner.close();
                    return null;
                }

                //Read the line.
                String input = scanner.nextLine();

                //If the length is not equals to the width, return error.
                if(input.length() != width){
                    System.out.println("Map width invalid!");
                    //Close the reader to avoid memory leak.
                    scanner.close();
                    return null;
                }

                //If passed, add the line to the map line array.
                mapLines[i] = input;
            }
            
            //Close the reader after scanning.
            scanner.close();

            //Convert each tile in to a boolean in a 2d array.
            for(int y=0; y<height; y++){
                //Convert each line into a char array.
                char[] charLine = mapLines[y].toCharArray();
                
                //Check each character in each line.
                for(int x=0; x<width; x++){
                    //If the char is X, it is a wall.
                    if(charLine[x]=='X'){
                        map[y][x] = true;
                    //If the char is space, it is an empty space.
                    }else if(charLine[x]==' '){
                        map[y][x] = false;
                    //If it is a char that is not specified, return error.
                    }else{
                        System.out.println("Map symbols invalid!");
                        return null;
                    }
                }
            }
            //Print the map if debugging is enabled
            if(debug){ debugPrintMap(map); }
            //Check if the map is bounded with walls, if not, return error.
            if(!isMapValid(map)){
                System.out.println("Map invalid! Need boundary!");
                return null;
            }
            return map;

        }catch(FileNotFoundException e){
            System.out.println("Level map file not found!");
            return null;
        }
    }

    /**
     * Check if the map is bounded with walls. (4 sides as a rect around the map)
     * @param levelMap The map data, 2d boolean array.
     * @return If there is a bounding rect around the map, the map is valid and return true. Otherwise, return false.
     */
    public static boolean isMapValid(boolean[][] levelMap){
        //For each boolean in the 2d array.
        for(int y=0; y<levelMap.length; y++){
            for(int x=0; x<levelMap[0].length; x++){
                //If this is the first or last row or column, everything must be true.
                if(y == 0 || y == levelMap.length - 1 || x == 0 || x == levelMap[0].length - 1){
                    if(levelMap[y][x]==false){
                        return false;
                    }
                }
            }
        }
        //Passed the check, return true.
        return true;
    }

    /**
     * Print the 2d boolean array into a map in terminal.
     * @param map The 2d boolean array that represent a map.
     */
    public static void debugPrintMap(boolean[][] map){
        System.out.printf("%nLevelMapReaderDebug:%n");
        //Read and print each element in the 2d boolean
        for(int y=0; y<map.length; y++){
            for(int x=0; x<map[0].length; x++){
                //If the grid is true, print an X
                if(map[y][x]){
                    System.out.printf("X");
                //If the grid is false, print a space
                }else{
                    System.out.printf(" ");
                }
            }
            //Change line
            System.out.printf("%n");
        }
    }
}
