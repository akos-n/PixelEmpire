package sprites.units;

import sprites.utils.Action;

import java.io.File;
import java.util.ArrayList;

public class Knight extends Unit {

    /**
     * Initialize the Knight object with x and y coordinates.
     */
    public Knight(int x, int y) {
        super(x, y);
        File pathFinder = new File("");
        loadImage(pathFinder.getAbsolutePath()+"\\src\\resources\\Knight.png");
        pathFinder.delete();
        getImageDimensions();
        initActions();
        cost.gold = 220;
        cost.food = 2;
        health = 500;
        maxHealth = health;
        damage = 50;
        range = 20;
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
        actions = new ArrayList<>();
    }

    /**
     * Returns null.
     */
    @Override
    public String doAction(String type) {
        return null;
    }
}
