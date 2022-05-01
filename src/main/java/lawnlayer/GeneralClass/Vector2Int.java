package lawnlayer.GeneralClass;

/**
 * This is a class that stores a pair of int as coordinates.
 * I did not use the other generic class I wrote, because we will mostly use int coordinates in this specific project.
 */
public class Vector2Int {
    public int x;
    public int y;

    public Vector2Int(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object other){
        //Before comparing two objects, make sure the input is a Vector2Int object.
        if(other instanceof Vector2Int){
            Vector2Int vector2Int = (Vector2Int)other;
            return vector2Int.x == x && vector2Int.y == y;
        }
        //If not the same type, return false.
        return false;
    }
}
