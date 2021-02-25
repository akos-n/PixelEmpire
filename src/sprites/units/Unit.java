package sprites.units;

import sprites.Sprite;
import sprites.buildings.Building;
import sprites.utils.Action;

import java.awt.*;
import java.util.ArrayList;

public abstract class Unit extends Sprite {
    private int dx;
    private int dy;
    protected int health;
    int maxHealth;
    boolean isHealing;
    int damage;
    int range;

    long lastAttackTime;
    Sprite lastAttackedSprite;

    int goToX;
    int goToY;

    protected ArrayList<Action> actions;

    /**
     * Initialize the Unit object with x and y coordinates.
     */
    public Unit(int x, int y) {
        super(x, y);
        goToX = x;
        goToY = y;
        lastAttackedSprite = null;
        lastAttackTime = System.currentTimeMillis();
    }

    boolean isInRange(Building building){
        Rectangle rangeRect = new Rectangle(x-range,y-range,width+(2*range),height+(2*range));
        if(rangeRect.contains(building.getBounds()) || rangeRect.intersects(building.getBounds())){
            return true;
        }
        return false;
    }

    private boolean isInRange(Unit unit){
        Rectangle rangeRect = new Rectangle(x-range,y-range,width+(2*range),height+(2*range));
        if(rangeRect.contains(unit.getBounds()) || rangeRect.intersects(unit.getBounds())){
            return true;
        }
        return false;
    }

    boolean attack(Building building, boolean canMove){
        long currentTime = System.currentTimeMillis();
        lastAttackedSprite = building;
        if(isInRange(building) && ((currentTime-lastAttackTime)>=1000)){
            lastAttackTime = currentTime;
            goToX = x;
            goToY = y;
            return true;
        } else {
            if (!isInRange(building) && canMove) move(lastAttackedSprite);
        }
        return false;
    }

    boolean attack(Unit unit, boolean canMove){
        long currentTime = System.currentTimeMillis();
        lastAttackedSprite = unit;
        if(isInRange(unit) && ((currentTime-lastAttackTime)>=1000)){
            lastAttackTime = currentTime;
            goToX = x;
            goToY = y;
            return true;
        } else {
            if (!isInRange(unit) && canMove) move(unit);
        }
        return false;
    }

    /**
     * The unit attacks the input sprite if it is in range or move to it if the unit can move.
     */
    public boolean attack(Sprite sprite, boolean canMove) {
        switch (sprite.getClass().getSuperclass().getTypeName()) {
            case "sprites.units.Unit":
                return attack((Unit) sprite,canMove);
            case "sprites.buildings.Building":
                return attack((Building) sprite,canMove);
        }
        return false;
    }

    /**
     * If the building is in range then heal the unit, else move to the building.
     */
    public boolean heal(Building building, boolean canMove){
        long currentTime = System.currentTimeMillis();
        lastAttackedSprite = building;
        isHealing = true;
        if(isInRange(building) && ((currentTime-lastAttackTime)>=1000)){
            if (maxHealth > health) {
                if (maxHealth-health >= 10) {
                    changeHealth(-10);
                } else {
                    changeHealth(-(maxHealth-health));
                }
            } else {
                lastAttackedSprite = null;
                isHealing = false;
            }
            System.out.println("Health: "+health);
            lastAttackTime = currentTime;
            goToX = x;
            goToY = y;
            return true;
        } else {
            if (!isInRange(building) && canMove) move(building);
        }
        return false;
    }



    void move(){
        if(goToX >= 0 && goToY >= 0) {
            if (x < goToX) dx = 1;
            else if (x > goToX) dx = -1;
            else dx = 0;
            x += dx;

            if (y < goToY) dy = 1;
            else if (y > goToY) dy = -1;
            else dy = 0;
            y += dy;
        }
    }

    void move(Sprite sprite) {
        goToX = sprite.getX();
        goToY = sprite.getY();
        move();
    }

    /**
     * Unit moves to x and y coordinates.
     */
    public void move(int x, int y){
        lastAttackedSprite = null;
        isHealing = false;
        goToX = x;
        goToY = y;
        move();
    }

    /**
     * Change unit's health with the input damage.
     */
    public void changeHealth(int damage){
        health -= damage;
    }

    /**
     * Returns true if the unit is alive.
     */
    public boolean isAlive(){
        return health > 0;
    }

    void setDamage(int damage){
        this.damage = damage;
    }

    abstract public ArrayList<Action> getActions();

    abstract protected void initActions();

    abstract public String doAction(String type);

    /**
     * Set the current unit's actions to x and y coordinates.
     */
    public void setActionsCoordsTo(int x, int y) {
        for (int i = 0; i < actions.size(); ++i) {
            actions.get(i).setX(x);
            x += actions.get(i).getBounds().width + 20;
            actions.get(i).setY(y);
        }
    }

    /**
     * Returns null, but the unit moves if it can and doesn't have assigned object.
     */
    public Action doLastAction(boolean canMove){
        if (canMove && lastAttackedSprite == null) move();
        return null;
    }

    /**
     * Returns that the unit can heal(), attack(), or return false.
     */
    public boolean doLastAction(Sprite sprite, boolean canMove){
        if (sprite.equals(lastAttackedSprite)) {
            if (isHealing) return heal((Building) sprite,canMove);
            return attack(sprite,canMove);
        }
        return false;
    }

    /**
     * Returns the unit's health.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Returns the unit's damage.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Returns the unit's range.
     */
    public int getRange() {
        return range;
    }

    /**
     * Returns the unit's assign object.
     */
    public Sprite getLastAttackedSprite() {
        return lastAttackedSprite;
    }

    /**
     * Returns true if the unit is healing.
     */
    public boolean isHealing() {
        return isHealing;
    }
}
