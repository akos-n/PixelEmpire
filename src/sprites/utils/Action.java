package sprites.utils;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Action {
    private String type;
    private int x;
    private int y;
    private int width;
    private int height;
    private Image image;

    /**
     * Initialize action with the input type.
     */
    public Action(String type) {
        this.type = type;
        String absPath = (new File("")).getAbsolutePath()+"\\src\\resources";
        absPath += "\\"+ type + ".png";
        loadImage(absPath);
        getImageDimensions();
    }

    protected void getImageDimensions() {
        width = 50;
        height = 50;
    }

    protected void loadImage(String imagePath) {
        ImageIcon ii = new ImageIcon(imagePath);
        image = ii.getImage();
        image = image.getScaledInstance(50,50,Image.SCALE_DEFAULT);
    }

    /**
     * Returns the action's rectangle bounds.
     */
    public Rectangle getBounds(){
        return new Rectangle(x,y,width,height);
    }

    /**
     * Returns the action's type.
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the action's x coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the action's y coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the action's width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the action's height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the action's image.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Set the action's x coordinate.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Set the action's y coordinate.
     */
    public void setY(int y) {
        this.y = y;
    }
}
