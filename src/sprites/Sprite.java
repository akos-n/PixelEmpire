package sprites;

import sprites.utils.Cost;

import javax.swing.*;
import java.awt.*;

public class Sprite {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    private boolean visible;
    private Image image;
    private Image editorImage;
    protected Cost cost;

    /**
     * Initialize the sprite with x and y coordinates.
     */
    public Sprite(int x, int y) {
        cost = new Cost();
        this.x = x;
        this.y = y;
        visible = true;
    }

    protected void getImageDimensions() {

        width = image.getWidth(null);
        height = image.getHeight(null);
    }

    protected void loadImage(String imagePath) {

        ImageIcon ii = new ImageIcon(imagePath);
        image = ii.getImage();
        editorImage = ii.getImage().getScaledInstance(40,40,Image.SCALE_DEFAULT);
    }

    /**
     * Set the sprite's x coordinate.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Set the sprite's y coordinate.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Returns the sprite's image.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Returns the sprite's editor image.
     */
    public Image getEditorImage() {
        return editorImage;
    }

    /**
     * Returns the sprite's x coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the sprite's y coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the sprite's width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the sprite's height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns visible.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Set visible.
     */
    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    /**
     * Returns the sprite's rectangle bounds.
     */
    public Rectangle getBounds() {
        return new Rectangle(x-2, y-2, width+4, height+4);
    }

    /**
     * Returns the sprite's rectangle editor bounds.
     */
    public Rectangle getEditorBounds() {
        return new Rectangle(x, y, editorImage.getWidth(null), editorImage.getHeight(null));
    }

    /**
     * Returns the sprite's cost.
     */
    public Cost getCost() {
        return cost;
    }

    /**
     * Returns the string of the sprite (x + " " + y).
     */
    @Override
    public String toString() {
        return x + " " + y;
    }
}