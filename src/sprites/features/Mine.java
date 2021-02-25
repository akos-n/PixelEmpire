package sprites.features;

import sprites.Sprite;

import java.io.File;

public class Mine extends Sprite {

    /**
     * Initialize the Mine object with x and y coordinates.
     */
    public Mine(int x, int y) {
        super(x, y);
        File pathFinder = new File("");
        loadImage(pathFinder.getAbsolutePath()+"\\src\\resources\\Mine.png");
        pathFinder.delete();
        getImageDimensions();
    }
}
