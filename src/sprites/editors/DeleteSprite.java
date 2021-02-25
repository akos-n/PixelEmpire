package sprites.editors;

import sprites.Sprite;

import java.io.File;

public class DeleteSprite extends Sprite {

    /**
     * Initialize the delete editor sprite with x and y coordinates.
     */
    public DeleteSprite(int x, int y) {
        super(x, y);
        File pathFinder = new File("");
        loadImage(pathFinder.getAbsolutePath()+"\\src\\resources\\Delete Icon.png");
        pathFinder.delete();
        getImageDimensions();
    }
}
