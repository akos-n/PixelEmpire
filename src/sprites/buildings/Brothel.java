package sprites.buildings;

import sprites.utils.Action;

import java.io.File;
import java.util.ArrayList;

public class Brothel extends Building {
    private int heal = 10;

    /**
     * Initialize the object with the input x, y coordinates.
     */
    public Brothel(int x, int y) {
        super(x, y);
        File pathFinder = new File("");
        loadImage(pathFinder.getAbsolutePath()+"\\src\\resources\\Brothel.png");
        pathFinder.delete();
        getImageDimensions();
        health = 300;
        cost.gold = 100;
        cost.wood = 100;
    }

    /**
     * Returns the Brothel actions.
     */
    @Override
    public ArrayList<Action> getActions() {
        return actions;
    }

    @Override
    protected void initActions() {
        actions = new ArrayList<>();
        actions.add(new Action("Heal"));
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
