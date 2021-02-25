package sprites.units;

import sprites.utils.Action;

import java.io.File;
import java.util.ArrayList;

public class Archer extends Unit {

    /**
     * Initialize the Archer object with x and y coordinates.
     */
    public Archer(int x, int y) {
        super(x, y);
        File pathFinder = new File("");
        loadImage(pathFinder.getAbsolutePath()+"\\src\\resources\\Archer.png");
        pathFinder.delete();
        getImageDimensions();
        initActions();
        cost.gold = 220;
        cost.wood = 50;
        cost.food = 1;
        health = 200;
        damage = 40;
        maxHealth = health;
        range = 120;
    }

    @Override
    public ArrayList<Action> getActions() {
        return null;
    }

    @Override
    protected void initActions() {
        actions = new ArrayList<>();
    }

    @Override
    public String doAction(String type) {
        return null;
    }
}
