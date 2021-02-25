package sprites.buildings;

import sprites.Sprite;
import sprites.utils.Action;

import java.util.ArrayList;

public abstract class Building extends Sprite {
    protected int health;
    protected ArrayList<Action> actions;

    /**
     * Initialize the Building object with x and y coordinates.
     */
    public Building(int x, int y) {
        super(x, y);
    }


    /**
     * Checks that building's health is bigger than zero.
     */
    public boolean isAlive(){
        return health>0;
    }


    /**
     * Change the building's health with the input damage.
     */
    public void changeHealth(int damage){
        health -= damage;
    }

    abstract public ArrayList<Action> getActions();

    abstract protected void initActions();

    abstract public String doAction(String type);


    /**
     * Set the building's actions to the input x and y coordinates.
     */
    public void setActionsCoordsTo(int x, int y) {
        if (actions != null) {
            for (int i = 0; i < actions.size(); ++i) {
                actions.get(i).setX(x);
                x += actions.get(i).getBounds().width + 20;
                actions.get(i).setY(y);
            }
        }
    }

    abstract public Action doLastAction();
}
