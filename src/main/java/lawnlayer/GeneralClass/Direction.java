package lawnlayer.GeneralClass;

/**
 * This is an enum that represents the four perpendicular and four diagonal directions.
 */
public enum Direction{
    //Represents the up direction.
    UP(0, -1),
    //Represents the down direction.          
    DOWN(0, 1),
    //Represents the left direction.             
    LEFT(-1, 0),
    //Represents the right direction.            
    RIGHT(1, 0),

    //Represents the top right direction.
    TOPRIGHT(1, -1),
    //Represents the top left direction.        
    TOPLEFT(-1, -1),
    //Represents the bottom right direction.        
    BOTTOMRIGHT(1, 1),
    //Represents the bottom left direction.     
    BOTTOMLEFT(-1, 1),

    //None is a state when stopped.
    NONE(0, 0);

    public final int X;                 //Position X.
    public final int Y;                 //Position Y.
    
    Direction(int X, int Y){
        this.X = X;
        this.Y = Y;
    }

    /**
     * The four perpendicular directions.
     */
    public static final Direction[] PERPENDICULAR = new Direction[]{
        UP,
        DOWN, 
        LEFT, 
        RIGHT
    };

    /**
     * The four diagonal directions.
     */
    public static final Direction[] DIAGONAL = new Direction[]{
        TOPRIGHT,
        TOPLEFT,
        BOTTOMRIGHT, 
        BOTTOMLEFT
    };

    /**
     * The full eight direction.
     */
    public static final Direction[] ALL = new Direction[]{
        UP,
        DOWN, 
        LEFT, 
        RIGHT,
        TOPRIGHT,
        TOPLEFT,
        BOTTOMRIGHT, 
        BOTTOMLEFT
    };

    /**
     * Get the opposite direction.
     * @param direction Direction to be compared with.
     * @return Return the opposite direction.
     */
    public Direction getOpposite(){
        switch(this){
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            case TOPRIGHT:
                return BOTTOMLEFT;
            case TOPLEFT:
                return BOTTOMRIGHT;
            case BOTTOMRIGHT:
                return TOPLEFT;
            case BOTTOMLEFT:
                return TOPRIGHT;
            default:
                return NONE;
        }
    }

    /**
     * Get the separate direction.
     * @param direction Direction to be separated.
     * @return Return the separated directions.
     */
    public Direction[] getSeparate(){
        switch(this){
            case TOPRIGHT:
                return new Direction[]{UP, RIGHT};
            case TOPLEFT:
                return new Direction[]{UP, LEFT};
            case BOTTOMRIGHT:
                return new Direction[]{DOWN, RIGHT};
            case BOTTOMLEFT:
                return new Direction[]{DOWN, LEFT};
            default:
                return null;
        }
    }
}
