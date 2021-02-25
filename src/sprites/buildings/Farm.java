package sprites.buildings;

import sprites.utils.Action;

import java.io.File;
import java.util.ArrayList;

public class Farm extends Building {

    /**
     * Initialize the Farm object with the input coordinates.
     */
    public Farm(int x, int y) {
        super(x, y);
        File pathFinder = new File("");
        loadImage(pathFinder.getAbsolutePath()+"\\src\\resources\\Farm.png");
        pathFinder.delete();
        getImageDimensions();
        health = 300;
        cost.gold = 80;
        cost.wood = 20;
    }


    /**
     * Return null.
     */
    @Override
    public ArrayList<Action> getActions() {
        return null;
    }

    @Override
    protected void initActions() {

    }

    /**
     * Return null.
     */
    @Override
    public String doAction(String type) {
        return null;
    }

    /**
     * Return null.
     */
    @Override
    public Action doLastAction() {
        return null;
    }
}
