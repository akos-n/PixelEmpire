package sprites.editors;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class OutPosSprite {
    protected int x;
    protected int y;
    protected Image image;

    /**
     * Initialize the Out position object with x and y coordinates.
     */
    public OutPosSprite(int x, int y) {
        this.x = x;
        this.y = y;
        File pathFinder = new File("");
        loadImage(pathFinder.getAbsolutePath()+"\\src\\resources\\Out Position.png");
    }

    protected void loadImage(String imagePath) {
        ImageIcon ii = new ImageIcon(imagePath);
        Image tmp = ii.getImage();
        image = tmp.getScaledInstance(10,10,Image.SCALE_DEFAULT);
    }

    /**
     * Returns the rectangle bounds of the sprite.
     */
    public Rectangle getBounds(){
        return new Rectangle(x,y,10,10);
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
     * Returns the Out pos sprite's image.
     */
    public Image getImage() {
        return image;
    }
}