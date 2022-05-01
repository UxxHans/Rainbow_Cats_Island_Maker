package lawnlayer.GeneralClass;

/**
 * This is a working generic class that stores a pair of values
 * Because the game uses int grid to record position.
 * I choose to create another Vector2Int class instead, just to be precise.
 */
public class Vector2<T> {
    //It is easier to access the value through vector2.x or vector.y.
    public T x;
    public T y;

    public Vector2(T x, T y){
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object other){
        //If the given value's type is vector2
        if(other instanceof Vector2){
            Vector2<?> vector2 = (Vector2<?>) other;
            //If the type in the vectors are different, they are not equal.
            if(!vector2.x.getClass().equals(x.getClass())){
                return false;
            //If the type in the vectors are same, compare the value.
            }else{
                return vector2.x.equals(x) && vector2.y.equals(y);
            }
        }
        return false;
    }
}
