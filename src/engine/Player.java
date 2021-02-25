package engine;

import sprites.Sprite;
import sprites.buildings.Building;
import sprites.units.Unit;
import sprites.utils.Action;
import sprites.utils.Cost;

import java.util.ArrayList;

public class Player {
    private String name;
    private int gold;
    private int wood;
    private int food;
    private int maxGold, maxWood, maxFood;
    private ArrayList<Building> buildings;
    private ArrayList<Unit> units;

    private boolean isBuilding;
    private String buildType;
    private Building underBuilding;

    private String assignedType;
    private ArrayList<Integer> assignedSpritesIndeces;
    private int mouseX;
    private int mouseY;
    private int viewX;
    private int viewY;

    /**
     * Initialize the Player object with the input name.
     */
    public Player(String name){
        this.name = name;
        buildings = new ArrayList<>();
        units = new ArrayList<>();
        gold = 100;
        wood = 0;
        food = 0;
        maxFood = 10;
        maxWood = 100;
        maxGold = 500;
        assignedType = "";
        assignedSpritesIndeces = new ArrayList<>();
        isBuilding = false;
        buildType = "";
    }

    /**
     * Returns that the player has any building or unit.
     */
    public boolean isInGame(){
        return buildings.size() + units.size() > 0;
    }

    /**
     * Add unit to the player's units list.
     */
    public void addUnit(Unit unit){
        units.add(unit);
    }

    /**
     * Add building to the player's buildings list.
     */
    public void addBuilding(Building building){
        buildings.add(building);
    }
    
    /**
     * Remove building from the player's buildings list.
     */
    public void removeBuilding(Building building){
        buildings.remove(building);
    }
    
    /**
     * Remove unit from the player's units list.
     */
    public void removeUnit(Unit unit){
        units.remove(unit);
    }

    /**
     * Returns the player's buildings list.
     */
    public ArrayList<Building> getBuildings(){
        return buildings;
    }

    /**
     * Returns the  player's units list.
     */
    public ArrayList<Unit> getUnits(){
        return units;
    }

    /**
     * Add gold to the player's gold variable.
     */
    public void addGold(int gold){
        if (this.gold + gold <= maxGold && this.gold+gold >=0) {
            this.gold += gold;
        }
    }
    
    /**
     * Add wood to the player's wood variable.
     */
    public void addWood(int wood){
        if (this.wood + wood <= maxWood && this.wood+wood >=0) {
            this.wood += wood;
        }
    }
    
    /**
     * Add food to the player's food variable.
     */
    public void addFood(int food){
        if (this.food+food <=maxFood && this.food+food >=0) {
            this.food += food;
        }
    }
    
    /**
     * Remove the input cost from the player's variables gold, wood, food.
     */
    public void removeResources(Cost cost) {
        addGold(-cost.gold);
        addWood(-cost.wood);
        addFood(cost.food);
    }

    /**
     * Returns that the player has enough resources to the cost.
     */
    public boolean hasEnoughResource(Cost cost){
        if (cost.food != 0) {
            return (hasEnoughGold(cost.gold) && hasEnoughWood(cost.wood)) && hasEnoughFood(cost.food);
        }
        return (hasEnoughGold(cost.gold) && hasEnoughWood(cost.wood));
    }

    /**
     * Set assignedSpritesIndeces.
     */
    public void addAssignedSpriteIndeces(ArrayList<Integer> indeces){
        assignedSpritesIndeces = indeces;
    }

    /**
     * Returns player's assignedSpritesIndeces.
     */
    public ArrayList<Integer> getAssignedSpritesIndeces() {
        return assignedSpritesIndeces;
    }

    /**
     * Add change to the player's viewX.
     */
    public void changeViewX(int change){
        viewX+= change;
    }

    /**
     * Add change to the player's viewY.
     */
    public void changeViewY(int change){
        viewY+= change;
    }

    /**
     * Returns that food can be added to the player's food.
     */
    public boolean hasEnoughFood(int food){
        return this.food + food < maxFood;
    }

    /**
     * Returns that gold equals or less than the player's gold.
     */
    public boolean hasEnoughGold(int gold){
        return this.gold >= gold;
    }

    /**
     * Returns that wood equals or less than the player's wood.
     */
    public boolean hasEnoughWood(int wood){
        return this.wood >= wood;
    }

    /**
     * Returns player's gold.
     */
    public int getGold() {
        return gold;
    }

    /**
     * Returns player's wood.
     */
    public int getWood() {
        return wood;
    }

    /**
     * Returns player's food.
     */
    public int getFood() {
        return food;
    }

    /**
     * Returns player's name.
     */
    public String getName(){ return name; }

    /**
     * Returns player's mouseX.
     */
    public int getMouseX() {
        return mouseX;
    }

    /**
     * Returns player's mouseY.
     */
    public int getMouseY() {
        return mouseY;
    }

    /**
     * Returns player's viewX.
     */
    public int getViewX() {
        return viewX;
    }

    /**
     * Returns player's viewY.
     */
    public int getViewY() {
        return viewY;
    }

    /**
     * Set player's viewX.
     */
    public void setViewX(int viewX) {
        this.viewX = viewX;
    }

    /**
     * Set player's viewY.
     */
    public void setViewY(int viewY) {
        this.viewY = viewY;
    }

    /**
     * Set player's mouseX.
     */
    public void setMouseX(int mouseX) {
        this.mouseX = mouseX;
    }

    /**
     * Set player's mouseY.
     */
    public void setMouseY(int mouseY) {
        this.mouseY = mouseY;
    }

    /**
     * Returns player's first assigned unit's actions.
     */
    public ArrayList<Action> getAssignedActions(){
        if (assignedSpritesIndeces.size() > 0) {
            switch (assignedType) {
                case "Building":
                    if (buildings.size()-1 >= assignedSpritesIndeces.get(0)) {
                        return buildings.get(assignedSpritesIndeces.get(0)).getActions();
                    } else return null;
                case "Unit":
                    if (units.size()-1 >= assignedSpritesIndeces.get(0)) {
                        return units.get(assignedSpritesIndeces.get(0)).getActions();
                    } else return null;
                default:
                    return null;
            }
        }
        return null;
    }

    /**
     * Returns true if the sprite is assigned.
     */
    public boolean assignedSpritesContains(Sprite sprite) {
        ArrayList<Sprite> tmp = new ArrayList<>();
        for (Integer index : assignedSpritesIndeces) {
            if (assignedType.equals("Building"))
                tmp.add(buildings.get(index));
            else if (assignedType.equals("Unit"))
                tmp.add(units.get(index));
        }
        if (tmp.contains(sprite)) return true;
        return false;
    }

    /**
     * Handle and does the input action if possible.
     */
    public String doAction(Action action){
        switch (assignedType){
            case "Building":
                if (action.getType().length() > 6) {
                    if (hasEnoughResource(GameEngine.getResourcesNeed(action.getType().substring(6)))) {
                        removeResources(GameEngine.getResourcesNeed(action.getType().substring(6)));
                        return buildings.get(assignedSpritesIndeces.get(0)).doAction(action.getType());
                    }
                } else {
                    return buildings.get(assignedSpritesIndeces.get(0)).doAction(action.getType());
                }
            case "Unit":
                return units.get(assignedSpritesIndeces.get(0)).doAction(action.getType());
            default:
                return null;
        }
    }

    /**
     * Returns assigned type.
     */
    public String getAssignedType() {
        return assignedType;
    }

    protected void setAssignedType(String assignedType) {
        this.assignedType = assignedType;
    }

    /**
     * Set isBuilding.
     */
    public void setBuilding(boolean building) {
        isBuilding = building;
    }

    /**
     * Set buildType.
     */
    public void setBuildType(String buildType) {
        this.buildType = buildType;
    }

    /**
     * Returns isBuilding.
     */
    public boolean isBuilding() {
        return isBuilding;
    }

    /**
     * Returns buildType.
     */
    public String getBuildType() {
        return buildType;
    }

    /**
     * Returns underBuilding building.
     */
    public Building getUnderBuilding() {
        return underBuilding;
    }

    /**
     * Set underBuilding building.
     */
    public void setUnderBuilding(Building underBuilding) {
        this.underBuilding = underBuilding;
    }

    /**
     * Returns max food.
     */
    public int getMaxFood() {
        return maxFood;
    }

    /**
     * Set max food.
     */
    public void setMaxFood(int maxFood) {
        this.maxFood = maxFood;
    }

    /**
     * Returns max gold.
     */
    public int getMaxGold() {
        return maxGold;
    }

    /**
     * Returns max wood.
     */
    public int getMaxWood() {
        return maxWood;
    }

    /**
     * Set max gold.
     */
    public void setMaxGold(int maxGold) {
        this.maxGold = maxGold;
    }

    /**
     * Set max wood.
     */
    public void setMaxWood(int maxWood) {
        this.maxWood = maxWood;
    }
}
