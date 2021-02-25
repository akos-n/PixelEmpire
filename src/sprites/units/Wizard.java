package sprites.units;

import sprites.utils.Action;

import java.io.File;
import java.util.ArrayList;

public class Wizard extends Unit {

    /**
     * Initialize the Wizard object with x and y coordinates.
     */
    public Wizard(int x, int y) {
        super(x, y);
        File pathFinder = new File("");
        loadImage(pathFinder.getAbsolutePath()+"\\src\\resources\\Wizard.png");
        pathFinder.delete();
        getImageDimensions();
        initActions();
        cost.gold = 400;
        cost.wood = 100;
        cost.food = 2;
        health = 150;
        maxHealth = health;
        damage = 90;
        range = 200;
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