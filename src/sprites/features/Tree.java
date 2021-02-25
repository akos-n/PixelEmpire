package sprites.features;

import sprites.Sprite;

import java.io.File;

public class Tree extends Sprite {
    private int health;

    /**
     * Initialize the Tree object with x and y coordinates.
     */
    public Tree(int x, int y) {
        super(x, y);
        health = 10;
        File pathFinder = new File("");
        loadImage(pathFinder.getAbsolutePath()+"\\src\\resources\\Tree.png");
        pathFinder.delete();
        getImageDimensions();
    }

    /**
     * Change the tree's health with the input damage.
     */
    public void changeHealth(int damage){
        health -= damage;
    }

    /**
     * Returns true if the tree's health is bigger than zero.
     */
    public boolean isAlive(){
        return health>0;
    }

    /**
     * Returns the tree's health.
     */
    public int getHealth(){
        return health;
    }

}
