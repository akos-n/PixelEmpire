package sprites.buildings;

import sprites.editors.OutPosSprite;
import sprites.utils.Action;

import java.io.File;
import java.util.ArrayList;

public class Headquarter extends Building {
    private OutPosSprite out;
    private int outX;
    private int outY;
    private String unitType;
    private double startTime;
    private double currentTime;
    private int trainingTime = 1;

    private Action lastAction;

    /**
     * Initialize the Headquarter object with the input x and y coordinates.
     */
    public Headquarter(int x, int y) {
        super(x, y);
        health = 1000;
        File pathFinder = new File("");
        loadImage(pathFinder.getAbsolutePath()+"\\src\\resources\\Headquarter.png");
        pathFinder.delete();
        getImageDimensions();
        initActions();
        cost.gold = 300;
        cost.wood = 250;
        outX = x + width + 30;
        outY = y + height + 30;
        out = new OutPosSprite(outX,outY);
    }

    private boolean train(){
        if (!unitType.equals("")) {
            currentTime = System.currentTimeMillis();
            return currentTime - startTime >= trainingTime*1000;
        }
        return false;
    }

    private void train(String unitType){
        if (unitType.equals("Worker")) {
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
     * Returns the trained unit's type.
     */
    public String getUnitType() {
        return unitType;
    }

    /**
     * Returns the out position x.
     */
    public int getOutX() {
        return outX;
    }

    /**
     * Returns the out position y.
     */
    public int getOutY() {
        return outY;
    }

    /**
     * Returns the Headquarter's actions.
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
        actions.add(new Action("Train Worker"));
        actions.add(new Action("Back to main actions"));
    }

    /**
     * Do the input action and return null.
     */
    @Override
    public String doAction(String type) {
        if (type.equals("Train")) {
            initTrainActions();
        } else if (type.equals("Back to main actions")) {
            initActions();
        } else if (type.equals("Train Worker") && startTime == 0) {
            lastAction = new Action(type);
            train("Worker");
        }
        return null;
    }


    /**
     * Change the out position x, y and OutPosSprite coordinates.
     */
    public void changeOutPos(int outX, int outY) {
        this.outX = outX;
        this.outY = outY;
        out = new OutPosSprite(outX, outY);
    }

    /**
     * Returns true if the train() has ended.
     */
    @Override
    public Action doLastAction() {
        if (startTime != 0 && train()) {
            endTraining();
            return lastAction;
        }
        return null;
    }

    /**
     * Returns the OutPosSprite out object.
     */
    public OutPosSprite getOut() {
        return out;
    }
}
