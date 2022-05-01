package lawnlayer.GeneralClass;

/**
 * Represent the different game states that could occur in game.
 * By using enum, the game states can be written more precisely than isWin, isLose boolean.
 * We can also implement different complex game states if needed.
 */
public enum GameState {
    WIN,        //The player wins the game.
    LOSE,       //The player lose the game.
    PLAYING;    //The player is playing the game.
}
