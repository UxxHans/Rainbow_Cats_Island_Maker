package lawnlayer.GeneralClass;

/**
 * Color enum in the text object class.
 * Because set color RGB values is ineffecient, I set up a color palette for the text.
 */
public enum Color{
    BLACK (0,0,0),
    WHITE (255,255,255),
    RED   (255,0,0),
    GREEN (0,255,0),
    BLUE  (0,0,255);

    public final int R;
    public final int G;
    public final int B;

    Color(int R, int G, int B){
        this.R = R;
        this.G = G;
        this.B = B;
    }
}