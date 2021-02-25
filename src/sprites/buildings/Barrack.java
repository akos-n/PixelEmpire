package sprites.buildings;

import sprites.editors.OutPosSprite;
import sprites.utils.Action;

import java.io.File;
import java.util.ArrayList;

public class Barrack extends Building {
    private int outX;
    private int outY;
    private OutPosSprite out;

    private String unitType;
    private double startTime;
    private double currentTime;
    private int trainingTime;
    private Action lastAction;

    /**
     * Barrack constructor what initialize the object.
     */
    public Barrack(int x, int y) {
        super(x, y);
        File pathFinder = new File("");
        loadImage(pathFinder.getAbsolutePath()+"\\src\\resources\\Barrack.png");
        pathFinder.delete();
        getImageDimensions();
        initActions();
        health = 400;
        cost.gold = 250;
        outX = x + width + 30;
        outY = y + height + 30;
        trainingTime = 60;
        out = new OutPosSprite(outX,outY);
    }


    /**
     * Returns the Barrack's actions ArrayList what contains the barrack's actions.
     */
    @Override
    public ArrayList<Action> getActions() {
        return actions;
    }

    @Override
    protected void initActions() {
        actions = new ArrayList<>();
        actions.add(new Action("Train"));
    }

    private void initTrainActions(){
        actions = new ArrayList<>();
        actions.add(new Action("Train Archer"));
        actions.add(new Action("Train Knight"));
        actions.add(new Action("Train Wizard"));
        actions.add(new Action("Back to main actions"));
    }

    /**
     * With the input String type the barrack start the action and return null.
     */
    @Override
    public String doAction(String type) {
        if  (startTime == 0) {
            switch (type){
                case "Train":
                    initTrainActions();
                    break;
                case "Train Archer":
                    lastAction = actions.get(0);
                    train("Archer");
                    break;
                case "Train Knight":
                    lastAction = actions.get(1);
                    train("Knight");
                    break;
                case "Train Wizard":
                    lastAction = actions.get(2);
                    train("Wizard");
                    break;
                case "Back to main actions":
                    initActions();
                    break;
            }
        }
        return null;
    }

    /**
     * Change the barrack's out position where go the trained units.
     */
    public void changeOutPos(int outX, int outY) {
        this.outX = outX;
        this.outY = outY;
        out = new OutPosSprite(outX, outY);
    }

    /**
     * If the train() returns true, then this returns tha last Action what is Train Unit*, else it returns null.
     */
    @Override
    public Action doLastAction() {
        if (startTime != 0 && train()) {
            endTraining();
            return lastAction;
        }
        return null;
    }

    private boolean train(){
        if (!unitType.equals("")) {
            currentTime = System.currentTimeMillis();
            return currentTime - startTime >= trainingTime;
        }
        return false;
    }

    private void train(String unitType){
        if (unitType.equals("Archer") || unitType.equals("Knight") || unitType.equals("Wizard")) {
            this.unitType = unitType;
            startTime = System.currentTimeMillis();
        }
    }

    private void endTraining(){
        startTime = 0;
        currentTime = 0;
        unitType = "";
    }

    /**
     * Returns training unit type.
     */
    public String getUnitType() {
        return unitType;
    }

    /**
     * Returns out position x.
     */
    public int getOutX() {
        return outX;
    }

    /**
     * Returns out position y.
     */
    public int getOutY() {
        return outY;
    }

    /**
     * Return OutPosSprite out.
     */
    public OutPosSprite getOut() {
        return out;
    }
}
