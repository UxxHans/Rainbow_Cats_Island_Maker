package lawnlayer.GameObjectClass;

import processing.core.PImage;
import lawnlayer.GeneralClass.Color;
import processing.core.PApplet;

/**
 * Represents a sprite object that can be drawn on screen in game.
 */
public abstract class SpriteObject {
    
    private int x;                              //The Shape's x-coordinate.
    private int y;                              //The Shape's y-coordinate.
    private PImage sprite;                      //The Shape's sprite.
    private Color tintColor = Color.WHITE;      //The tint of the image.
    
    /**
     * Creates a new Shape object.
     * @param x Position X.
     * @param y Position Y.
     * @param sprite Sprite reference.
     */
    public SpriteObject(int x, int y, PImage sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    /**
     * Sets the shape's sprite.
     * @param sprite The sprite reference.
     */
    public void setSprite(PImage sprite) {
        this.sprite = sprite;
    }

    /**
     * Updates the shape every frame. Handles the logics.
     */
    public abstract void tick();

    /**
     * Draws the shape to the screen. Handles the graphics
     * @param app The main program.
     */
    public void draw(PApplet app) {
        // The image() method is used to draw PImages onto the screen.
        // The first argument is the image, the second and third arguments are coordinates
        app.tint(tintColor.R, tintColor.G, tintColor.B);
        app.image(this.sprite, this.x, this.y);
    }

    /**
     * Returns the x-coordinate.
     * @return Position X on screen in pixel.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Returns the y-coordinate.
     * @return Position Y on screen in pixel.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Sets the x-coordinate.
     * @param x The position x on screen in pixel.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate.
     * @param y The position y on screen in pixel.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Sets the position on screen.
     * @param x Position X.
     * @param y Position Y.
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the tint color of the image
     * @param tintColor Tint color of the image
     */
    public void setTintColor(Color tintColor) {
        this.tintColor = tintColor;
    }
}
