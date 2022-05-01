package lawnlayer.GeneralClass;

/**
 * This is an enum that represents the different types of a tile.
 */
public enum TileLabel {
    EMPTY,         //EMPTY is the empty that can be filled. Enemies can only move in empty spaces.
    FILLED,        //FILLED is the filled area by player.
    SOLID,         //SOLID is a wall that blocks enemies and bounds the map.
    PATH_G,        //PATH_G is a temporary tile that represents Green path - in progress.
    PATH_R,        //PATH_R is a temporary tile that represents Red path - hit by enemy.
    FLOODFILLED;   //FLOODFILLED is a temporary tile. To avoid duplicate fill when doing multiple times of flood filling.
}
