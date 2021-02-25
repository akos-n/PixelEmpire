package sprites.units;

import sprites.Sprite;
import sprites.buildings.Building;
import sprites.features.Mine;
import sprites.features.Tree;
import sprites.utils.Action;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class Worker extends Unit {
    private boolean isBuilding;
    private int buildTime;
    private boolean isBusy;

    /**
     * Initialize the Worker object with x and y coordinates.
     */
    public Worker(int x, int y) {
        super(x, y);
        File pathFinder = new File("");
        loadImage(pathFinder.getAbsolutePath()+"\\src\\resources\\Worker.png");
        pathFinder.delete();
        getImageDimensions();
        initActions();
        isBusy = false;
        buildTime = 1;
        cost.gold = 100;
        cost.food = 1;
        health = 150;
        maxHealth = health;
        damage = 10;
        range = 20;
        isBuilding = false;
    }

    /**
     * Returns the Worker's actions.
     */
    @Override
    public ArrayList<Action> getActions() {
        return actions;
    }

    @Override
    protected void initActions() {
        actions = new ArrayList<>();
        actions.add(new Action("Build"));
    }

    private void initBuildActions() {
        actions = new ArrayList<>();
        actions.add(new Action("Build Headquarter"));
        actions.add(new Action("Build Barrack"));
        actions.add(new Action("Build Farm"));
        actions.add(new Action("Build Storage"));
        actions.add(new Action("Build Brothel"));
        actions.add(new Action("Build Tower"));
        actions.add(new Action("Back to main actions"));
    }

    /**
     * Returns the action type or the building type at build().
     */
    @Override
    public String doAction(String type) {
        switch (type) {
            case "Build":
                initBuildActions();
                return null;
            case "Back to main actions":
                initActions();
                isBuilding = false;
                return "Back to main actions";
            default:
                isBusy = true;
                return type.substring(6);
        }
    }

    /**
     * The worker moves to x and y coordinates.
     */
    @Override
    public void move(int x, int y){
        lastAttackedSprite = null;
        isBuilding = false;
        isHealing = false;
        isBusy = false;
        goToX = x;
        goToY = y;
        move();
    }

    /**
     * Returns null. The object move() if he can or doesn't do anything else.
     */
    @Override
    public Action doLastAction(boolean canMove){
        if (canMove && lastAttackedSprite == null && !isBuilding && !isBusy) move();
        return null;
    }

    /**
     * Returns if attack(), build() or heal() returns true.
     */
    @Override
    public boolean doLastAction(Sprite sprite, boolean canMove){
        if (isHealing && sprite.getClass().getTypeName().equals("sprites.buildings.Brothel"))
            return heal((Building) sprite, canMove);
        else if (isBuilding) {
            isBusy = true;
            return build((Building) lastAttackedSprite, canMove);
        } else if (sprite.equals(lastAttackedSprite)) {
            return attack(sprite, canMove);
        }
        return false;
    }

    private boolean isInRange(Tree tree){
        Rectangle rangeRect = new Rectangle(x-range,y-range,width+(2*range),height+(2*range));
        if(rangeRect.contains(tree.getBounds()) || rangeRect.intersects(tree.getBounds())){
            return true;
        }
        return false;
    }

    private boolean isInRange(Mine mine){
        Rectangle rangeRect = new Rectangle(x-range,y-range,width+(2*range),height+(2*range));
        if(rangeRect.contains(mine.getBounds()) || rangeRect.intersects(mine.getBounds())){
            return true;
        }
        return false;
    }

    private boolean attack(Tree tree, boolean canMove){
        long currentTime = System.currentTimeMillis();
        lastAttackedSprite = tree;
        if(isInRange(tree) && ((currentTime-lastAttackTime)>=1000)){
            lastAttackTime = currentTime;
            goToX = x;
            goToY = y;
            return true;
        } else if (!isInRange(tree) && canMove) {
            move(tree);
        }
        return false;
    }

    private boolean attack(Mine mine, boolean canMove){
        long currentTime = System.currentTimeMillis();
        lastAttackedSprite = mine;
        if(isInRange(mine) && ((currentTime-lastAttackTime)>=1000)){
            lastAttackTime = currentTime;
            goToX = x;
            goToY = y;
            return true;
        } else if (!isInRange(mine) && canMove) move(mine);
        return false;
    }

    /**
     * The object attack the input sprite or move to it if he can.
     */
    @Override
    public boolean attack(Sprite sprite, boolean canMove) {
        switch (sprite.getClass().getSuperclass().getTypeName()) {
            case "sprites.units.Unit":
                return attack((Unit) sprite, canMove);
            case "sprites.buildings.Building":
                return attack((Building) sprite, canMove);
            case "sprites.Sprite":
                if (sprite.getClass().getTypeName().equals("sprites.features.Tree")) return attack((Tree) sprite, canMove);
                if (sprite.getClass().getTypeName().equals("sprites.features.Mine")) return attack((Mine) sprite, canMove);
        }
        return false;
    }

    /**
     * Build the input sprite or move to it if he can.
     */
    public boolean build(Building building, boolean canMove){
        long currentTime = System.currentTimeMillis();
        lastAttackedSprite = building;
        isBuilding = true;
        if(isInRange(building) && ((currentTime-lastAttackTime)>=buildTime*1000)){
            lastAttackTime = currentTime;
            goToX = x;
            goToY = y;
            isBusy = false;
            return true;
        } else if (!isInRange(building) && canMove) move(building);
        return false;
    }

    /**
     * Set isBuilding boolean.
     */
    public void setBuilding(boolean building) {
        isBuilding = building;
    }

    /**
     * Returns isBuilsing boolean.
     */
    public boolean isBuilding() {
        return isBuilding;
    }
}
