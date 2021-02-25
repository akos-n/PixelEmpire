package sprites.buildings;

import sprites.Sprite;
import sprites.units.Unit;
import sprites.utils.Action;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class Tower extends Building {
    private int range;
    private int damage;
    private long lastAttackTime;

    /**
     * Initialize the Tower object with x and y coordinates.
     */
    public Tower(int x, int y) {
        super(x, y);
        File pathFinder = new File("");
        loadImage(pathFinder.getAbsolutePath()+"\\src\\resources\\Tower.png");
        pathFinder.delete();
        getImageDimensions();
        initActions();
        health = 500;
        cost.gold = 120;
        cost.wood = 20;
        damage = 30;
        range = 300;
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

    /**
     * Returns null.
     */
    @Override
    public Action doLastAction() {
        return null;
    }

    /**
     * The tower object attack's the input sprite and returns true if he can.
     */
    public boolean doLastAction(Sprite sprite) {
        return attack(sprite);
    }

    /**
     * Returns true if the input buildings is in range of the tower.
     */
    public boolean isInRange(Building building){
        Rectangle rangeRect = new Rectangle(x-range,y-range,width+(2*range),height+(2*range));
        if(rangeRect.contains(building.getBounds()) || rangeRect.intersects(building.getBounds())){
            return true;
        }
        return false;
    }

    /**
     * Returns true if the input unit is in range of the tower.
     */
    public boolean isInRange(Unit unit){
        Rectangle rangeRect = new Rectangle(x-range,y-range,width+(2*range),height+(2*range));
        if(rangeRect.contains(unit.getBounds()) || rangeRect.intersects(unit.getBounds())){
            return true;
        }
        return false;
    }

    private boolean attack(Sprite sprite) {
        switch (sprite.getClass().getSuperclass().getTypeName()) {
            case "sprites.units.Unit":
                return attack((Unit) sprite);
            case "sprites.buildings.Building":
                return attack((Building) sprite);
        }
        return false;
    }

    private boolean attack(Building building){
        long currentTime = System.currentTimeMillis();
        if(isInRange(building) && ((currentTime-lastAttackTime)>=1000)){
            lastAttackTime = currentTime;
            return true;
        }
        return false;
    }

    private boolean attack(Unit unit){
        long currentTime = System.currentTimeMillis();
        if(isInRange(unit) && ((currentTime-lastAttackTime)>=1000)){
            lastAttackTime = currentTime;
            return true;
        }
        return false;
    }

    /**
     * Set the tower's damage.
     */
    public int getDamage() {
        return damage;
    }
}
