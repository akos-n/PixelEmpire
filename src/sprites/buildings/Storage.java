package sprites.buildings;

import sprites.utils.Action;

import java.io.File;
import java.util.ArrayList;

public class Storage extends Building {

    /**
     * Initialize the Storage object with x and y coordinates.
     */
    public Storage(int x, int y) {
        super(x, y);
        File pathFinder = new File("");
        loadImage(pathFinder.getAbsolutePath()+"\\src\\resources\\Storage.png");
        pathFinder.delete();
        getImageDimensions();
        health = 300;
        cost.gold = 80;
        cost.wood = 30;
    }

    /**
     * Returns null.
     */
    @Override
    public ArrayList<Action> getActions() {
        return null;
    }

    @Override
    protected void initActions() {

    }

    /**
     * Returns null.
     */
    @Override
    public String doAction(String type) {
        return null;
    }

    /**
     * Returns null.
     */
    @Override
    public Action doLastAction() {
        return null;
    }
}
