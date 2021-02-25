package engine;

import sprites.Sprite;
import sprites.buildings.*;
import sprites.features.Mine;
import sprites.features.Tree;
import sprites.units.*;
import sprites.utils.Action;
import sprites.utils.Cost;

import java.awt.*;
import java.util.ArrayList;

public class GameEngine {
    private ArrayList<Player> players;
    private ArrayList<Tree> trees;
    private ArrayList<Mine> mines;

    private int panelWidth;
    private int panelHeight;
    private int mapWidth;
    private int mapHeight;

    /**
     * Initialize the game engine with the players list, trees list and mines list.
     */
    public GameEngine(ArrayList<Player> players, ArrayList<Tree> trees, ArrayList<Mine> mines) {
        this.players = players;
        this.trees = trees;
        this.mines = mines;
    }

    /**
     * Update trees, buildings, units.
     */
    public void update(){
        updateTrees();
        updateBuildings();
        updateUnits();
    }

    private void updateTrees() {
        for (int i = 0; i <trees.size(); ++i){
            if (!trees.get(i).isAlive()){
                trees.remove(trees.get(i));
                i--;
            }
        }
    }

    private void updateUnits() {
        int playerCounter = 0;
        for (Player player : players) {
            playerCounter++;
            for (int i = 0; i < player.getUnits().size(); ++i) {
                if (!player.getUnits().get(i).isAlive()) {
                    if (player.getAssignedSpritesIndeces() != null) {
                        if (player.getAssignedType().equals("Unit")
                                && player.getAssignedSpritesIndeces()
                                .contains(player.getUnits()
                                        .indexOf(player.getUnits().get(i)))) {
                            int assignIndex = player.getAssignedSpritesIndeces().indexOf(player.getUnits()
                                    .indexOf(player.getUnits().get(i)));
                            player.getAssignedSpritesIndeces().remove(assignIndex);
                            fixAssignedIndeces(player);
                            if (player.getAssignedSpritesIndeces().size() < 1) {
                                player.setAssignedType("");
                            } else {
                                setCurrentActionsCoord(playerCounter);
                            }
                        }
                    }
                    player.removeUnit(player.getUnits().get(i));
                    i--;
                } else if (!checkUnitAttack(player, player.getUnits().get(i),!checkCollisions(player.getUnits().get(i)))) {
                    Unit tmp = player.getUnits().get(i);
                    if (tmp.isHealing()) {
                        player.getUnits().get(i).heal((Building) tmp.getLastAttackedSprite(), !checkCollisions(tmp));
                    } else {
                        if ((tmp.getClass().getTypeName().equals("sprites.units.Worker"))
                                && (((Worker) player.getUnits().get(i)).isBuilding())) {
                            (player.getUnits().get(i)).doLastAction(!checkCollisions(player.getUnits().get(i))
                                    && underBuildingCollision((Worker) player.getUnits().get(i)));
                        } else {
                            player.getUnits().get(i).doLastAction(!checkCollisions(player.getUnits().get(i)));
                        }
                    }
                }
            }
        }
    }

    private boolean checkUnitAttack(Player activePlayer,Unit unit, boolean canMove){
        if (unit.getClass().getTypeName().equals("sprites.units.Worker")) {
            if (((Worker) unit).isBuilding()) {
                if (unit.doLastAction(unit.getLastAttackedSprite(),canMove)) {
                    int playerIndex = players.indexOf(activePlayer);
                    players.get(playerIndex).addBuilding((Building) unit.getLastAttackedSprite());
                    ((Worker) unit).setBuilding(false);
                    return true;
                }
            } else {
                for (Tree tree : trees) {
                    if (unit.doLastAction(tree, canMove)) {
                        tree.changeHealth(unit.getDamage());
                        activePlayer.addWood(unit.getDamage());
                        return true;
                    }
                }
                for (Mine mine : mines) {
                    if (unit.doLastAction(mine, canMove)) {
                        activePlayer.addGold(unit.getDamage());
                        return true;
                    }
                }
            }
        }
        for (Player player : players) {
            if (!player.getUnits().contains(unit)) {
                for (Unit enemyUnit : player.getUnits()) {
                    if (unit.doLastAction(enemyUnit, canMove)) {
                        enemyUnit.changeHealth(unit.getDamage());
                        return true;
                    }
                }
                for (Building enemyBuilding : player.getBuildings()) {
                    if (unit.doLastAction(enemyBuilding, canMove)) {
                        enemyBuilding.changeHealth(unit.getDamage());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean underBuildingCollision(Worker worker){
        Rectangle workerRangeRect = new Rectangle(worker.getBounds());
        workerRangeRect.x -= worker.getRange();
        workerRangeRect.width += 2*worker.getRange();
        workerRangeRect.y -= worker.getRange();
        workerRangeRect.height += 2*worker.getRange();
        return workerRangeRect.contains(worker.getLastAttackedSprite().getBounds())
            || workerRangeRect.intersects(worker.getLastAttackedSprite().getBounds());
    }

    private void fixAssignedIndeces(Player player){
        ArrayList<Integer> tmpIndeces = new ArrayList<>();
        for (Integer index : player.getAssignedSpritesIndeces()) {
            tmpIndeces.add(index-1);
        }
        player.addAssignedSpriteIndeces(tmpIndeces);
    }

    private void updateBuildings() {
        int playerCounter = 0;
        for (Player player : players) {
            playerCounter++;
            int storageCounter = 0, farmCounter = 0;
            for (int i = 0; i < player.getBuildings().size(); ++i) {
                if (!player.getBuildings().get(i).isAlive()) {
                    if (player.getAssignedType().equals("Building")
                            && player.getAssignedSpritesIndeces()
                            .contains(player.getBuildings()
                            .indexOf(player.getBuildings().get(i)))) {
                        int assignIndex = player.getAssignedSpritesIndeces().indexOf(player.getBuildings()
                                .indexOf(player.getBuildings().get(i)));
                        player.getAssignedSpritesIndeces().remove(assignIndex);
                        fixAssignedIndeces(player);
                        if (player.getAssignedSpritesIndeces().size() < 1) {
                            player.setAssignedType("");
                        } else {
                            setCurrentActionsCoord(playerCounter);
                        }

                    }
                    player.removeBuilding(player.getBuildings().get(i));
                    i--;
                } else {

                    Action action = player.getBuildings().get(i).doLastAction();
                    switch (player.getBuildings().get(i).getClass().getTypeName()) {
                        case "sprites.buildings.Headquarter":
                            if (action != null && action.getType().equals("Train Worker")) {
                                Building tmp = player.getBuildings().get(i);
                                Worker worker = new Worker(tmp.getX()+tmp.getWidth()+10
                                                        , tmp.getY()+tmp.getHeight()+10);
                                worker.move(((Headquarter)tmp).getOutX(), ((Headquarter)tmp).getOutY());
                                player.addUnit(worker);
                            }
                            break;
                        case "sprites.buildings.Barrack":
                            if (action != null && action.getType().equals("Train Archer")) {
                                Building tmp = player.getBuildings().get(i);
                                Archer archer = new Archer(tmp.getX()+tmp.getWidth()+10
                                        , tmp.getY()+tmp.getHeight()+10);
                                archer.move(((Barrack)tmp).getOutX(), ((Barrack)tmp).getOutY());
                                player.addUnit(archer);
                            } else if (action != null && action.getType().equals("Train Knight")) {
                                Building tmp = player.getBuildings().get(i);
                                Knight knight = new Knight(tmp.getX()+tmp.getWidth()+10
                                        , tmp.getY()+tmp.getHeight()+10);
                                knight.move(((Barrack)tmp).getOutX(), ((Barrack)tmp).getOutY());
                                player.addUnit(knight);
                            } else if (action != null && action.getType().equals("Train Wizard")) {
                                Building tmp = player.getBuildings().get(i);
                                Wizard wizard = new Wizard(tmp.getX()+tmp.getWidth()+10
                                        , tmp.getY()+tmp.getHeight()+10);
                                wizard.move(((Barrack)tmp).getOutX(), ((Barrack)tmp).getOutY());
                                player.addUnit(wizard);
                            }
                            break;
                        case "sprites.buildings.Storage":
                            storageCounter++;
                            break;
                        case "sprites.buildings.Farm":
                            farmCounter++;
                            break;
                        case "sprites.buildings.Tower":
                            inTowerRange(player,(Tower) player.getBuildings().get(i));
                            break;
                    }
                }
            }
            if (player.getMaxFood() != farmCounter*10 +10) {
                player.setMaxFood(farmCounter*10 + 10);
            }
            if (player.getMaxGold() != (storageCounter*250)+500) {
                player.setMaxGold(storageCounter*250 +500);
                player.setMaxWood(storageCounter*250 + 100);
            }
        }
    }

    private void inTowerRange(Player player, Tower tower){
        for (Player enemyPlayer : players) {
            if (!player.equals(enemyPlayer)) {
                for (Unit unit : enemyPlayer.getUnits()) {
                    if (tower.isInRange(unit)) {
                        if (((Tower) player.getBuildings().get(player.getBuildings().indexOf(tower)))
                                .doLastAction(unit)) {
                            unit.changeHealth(tower.getDamage());
                        }
                    }
                }
                for (Building building : enemyPlayer.getBuildings()) {
                    if (tower.isInRange(building)) {
                        if (((Tower) player.getBuildings().get(player.getBuildings().indexOf(tower)))
                                .doLastAction(building)){
                            building.changeHealth(tower.getDamage());
                        }
                    }
                }
            }
        }
    }

    /**
     * Set players[playerIndex]'s mouse positions.
     */
    public void setPlayerMousePos(int playerIndex, int mouseX, int mouseY){
        players.get(playerIndex).setMouseX(mouseX);
        players.get(playerIndex).setMouseY(mouseY);
    }

    /**
     * Update player[playerIndex]'s view.
     */
    public void updatePlayerView(int playerIndex){
        Point mousePoint = new Point(players.get(playerIndex).getMouseX(), players.get(playerIndex).getMouseY());
        //Right rectangle of the panel
        if (new Rectangle(panelWidth-50,0,50,panelHeight+80).contains(mousePoint)
                && players.get(playerIndex).getViewX() <= mapWidth-panelWidth-10) {
            players.get(playerIndex).changeViewX(10);
        }
        //Left rectangle of the panel
        if (new Rectangle(0,0,50,panelHeight+80).contains(mousePoint)
                && players.get(playerIndex).getViewX() >= 10) {
            players.get(playerIndex).changeViewX(-10);
        }
        //Top rectangle of the panel
        if (new Rectangle(0,0,panelWidth,50).contains(mousePoint)
                && players.get(playerIndex).getViewY() >= 10) {
            players.get(playerIndex).changeViewY(-10);
        }
        //Bottom rectangle of the panel
        if (new Rectangle(0,panelHeight-50,panelWidth,130).contains(mousePoint)
                && players.get(playerIndex).getViewY() <= mapHeight-panelHeight-10) {
            players.get(playerIndex).changeViewY(10);
        }
    }

    /**
     * Handle the active player's left mouse button actions.
     */
    public void checkGameMechanics(int activePlayer, Rectangle assignRect){
        if (isActionBox(activePlayer,assignRect)) {
            String tmpAction = players.get(activePlayer).doAction(whichAction(activePlayer,assignRect));
            if (tmpAction != null) {
                switch (tmpAction) {
                    case "Back to main actions":
                        players.get(activePlayer).setBuilding(false);
                        players.get(activePlayer).setBuildType("");
                        break;
                    case "Heal":
                        break;
                    default:
                        if (players.get(activePlayer).hasEnoughResource(getResourcesNeed(tmpAction))) {
                            players.get(activePlayer).setBuilding(true);
                            players.get(activePlayer).setBuildType(tmpAction);
                            setUnderBuilding(activePlayer);
                        }
                }
            }

            setCurrentActionsCoord(activePlayer);
        } else if (players.get(activePlayer).isBuilding()) {
            buildUnderBuilding(activePlayer,assignRect);
        } else {
            setAssignedObject(activePlayer,assignRect);
        }
    }

    /**
     * Returns the cost of the sprite type.
     */
    static Cost getResourcesNeed(String type){
        switch (type) {
            case "Headquarter":
                return  new Headquarter(0,0).getCost();
            case "Barrack":
                return new Barrack(0,0).getCost();
            case "Farm":
                return new Farm(0,0).getCost();
            case "Storage":
                return new Storage(0,0).getCost();
            case "Brothel":
                return new Brothel(0,0).getCost();
            case "Tower":
                return new Tower(0,0).getCost();
            case "Archer":
                return new Archer(0,0).getCost();
            case "Worker":
                return new Worker(0,0).getCost();
            case "Knight":
                return new Knight(0,0).getCost();
            case "Wizard":
                return new Wizard(0,0).getCost();
            default:
                return new Cost();
        }
    }

    private void buildUnderBuilding(int activePlayer, Rectangle assignRect){
        players.get(activePlayer).getUnderBuilding().setX(
                assignRect.x-players.get(activePlayer).getUnderBuilding().getWidth()/2);
        players.get(activePlayer).getUnderBuilding().setY(
                assignRect.y-players.get(activePlayer).getUnderBuilding().getHeight()/2);
        if (!checkCollisions(players.get(activePlayer).getUnderBuilding())) {
            ((Worker) players.get(activePlayer).getUnits()
                    .get(players.get(activePlayer).getAssignedSpritesIndeces().get(0)))
                    .build(players.get(activePlayer).getUnderBuilding(), false);
            players.get(activePlayer).removeResources(getResourcesNeed(players.get(activePlayer).getBuildType()));
            ArrayList<Integer> assignedSpritesIndeces = new ArrayList<>();
            players.get(activePlayer).setAssignedType("");
            players.get(activePlayer).addAssignedSpriteIndeces(assignedSpritesIndeces);
            players.get(activePlayer).setBuilding(false);
            players.get(activePlayer).setBuildType("");
        }
    }

    private void setUnderBuilding(int activePlayer){
        switch (players.get(activePlayer).getBuildType()) {
            case "Headquarter":
                players.get(activePlayer).setUnderBuilding(new Headquarter(0,0));
                break;
            case "Barrack":
                players.get(activePlayer).setUnderBuilding(new Barrack(0,0));
                break;
            case "Farm":
                players.get(activePlayer).setUnderBuilding(new Farm(0,0));
                break;
            case "Storage":
                players.get(activePlayer).setUnderBuilding(new Storage(0,0));
                break;
            case "Brothel":
                players.get(activePlayer).setUnderBuilding(new Brothel(0,0));
                break;
            case "Tower":
                players.get(activePlayer).setUnderBuilding(new Tower(0,0));
                break;
        }
    }

    private Action whichAction(int activePlayer, Rectangle assignRect) {
        Player player = players.get(activePlayer);
        Rectangle checkActionsRect = new Rectangle(assignRect.x-players.get(activePlayer).getViewX()
                ,assignRect.y-players.get(activePlayer).getViewY(), assignRect.width, assignRect.height);
        for (Action action : player.getAssignedActions()) {
            if (action.getBounds().contains(checkActionsRect)) {
                return action;
            }
        }
        return null;
    }

    private boolean isActionBox(int activePlayer, Rectangle assignRect){
        if (players.get(activePlayer).getAssignedActions() != null &&
            players.get(activePlayer).getAssignedActions().size() > 0) {
            Rectangle checkActionsRect = new Rectangle(assignRect.x-players.get(activePlayer).getViewX()
                    ,assignRect.y-players.get(activePlayer).getViewY(), assignRect.width, assignRect.height);
            for (Action action : players.get(activePlayer).getAssignedActions()) {
                if (action.getBounds().contains(checkActionsRect)) return true;
            }
        }
        return false;
    }

    private void setAssignedObject(int activePlayer, Rectangle assignRect){


        ArrayList<Integer> assignedSpritesIndeces = new ArrayList<>();
        for (int i = 0; i < players.get(activePlayer).getUnits().size(); ++i) {
            Unit unit = players.get(activePlayer).getUnits().get(i);
            if (assignRect.contains(unit.getBounds())
                    || assignRect.intersects(unit.getBounds())) {
                assignedSpritesIndeces.add(i);
            }
        }
        if (!assignedSpritesIndeces.isEmpty()) {
            players.get(activePlayer).setAssignedType("Unit");
            players.get(activePlayer).addAssignedSpriteIndeces(assignedSpritesIndeces);
            setCurrentActionsCoord(activePlayer);
            return;
        }
        for (int i = 0; i < players.get(activePlayer).getBuildings().size(); ++i) {
            Building building = players.get(activePlayer).getBuildings().get(i);
            if (assignRect.contains(building.getBounds())
                    || assignRect.intersects(building.getBounds())) {
                assignedSpritesIndeces.add(i);
            }
        }
        if (assignedSpritesIndeces.isEmpty()) players.get(activePlayer).setAssignedType("");
        else {
            players.get(activePlayer).setAssignedType("Building");
            players.get(activePlayer).addAssignedSpriteIndeces(assignedSpritesIndeces);
            setCurrentActionsCoord(activePlayer);
        }
    }

    private boolean checkCollisions(Sprite sprite){
        for (Player player : players){
            Rectangle currentSpriteR = sprite.getBounds();
            if (checkTreeCollisions(currentSpriteR)
                    || checkMineCollisions(currentSpriteR)
                    || checkBuildingCollisions(currentSpriteR, player.getBuildings())
                    || checkUnitCollisions(currentSpriteR, player.getUnits())
                    ) {
                return true;
            }
        }
        return false;
    }

    private boolean checkTreeCollisions(Rectangle currentSpriteR){
        for (Tree tree : trees){
            Rectangle treeR = tree.getBounds();
            if (currentSpriteR.intersects(treeR) || currentSpriteR.contains(treeR)){
                return true;
            }
        }
        return false;
    }

    private boolean checkMineCollisions(Rectangle currentSpriteR){
        for (Mine mine : mines){
            Rectangle mineR = mine.getBounds();
            if (currentSpriteR.intersects(mineR) || currentSpriteR.contains(mineR)){
                return true;
            }
        }
        return false;
    }

    private boolean checkBuildingCollisions(Rectangle currentSpriteR, ArrayList<Building> buildings){
        for (Building building : buildings){
            if (!currentSpriteR.equals(building.getBounds())) {
                Rectangle buildingR = building.getBounds();
                if (currentSpriteR.intersects(buildingR) || currentSpriteR.contains(buildingR)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkUnitCollisions(Rectangle currentSpriteR, ArrayList<Unit> units){
        for (Unit otherUnit : units){
            if (!currentSpriteR.equals(otherUnit.getBounds())) {
                Rectangle otherUnitR = otherUnit.getBounds();
                if (currentSpriteR.intersects(otherUnitR) || currentSpriteR.contains(otherUnitR)){
                    return true;
                }
            }
        }
        return false;
    }

    private void setCurrentActionsCoord(int activePlayer){
        switch (players.get(activePlayer).getAssignedType()){
            case "":
                break;
            case "Unit":
                if (players.get(activePlayer).getUnits().size() > 0)
                players.get(activePlayer).getUnits()
                        .get(players.get(activePlayer).getAssignedSpritesIndeces().get(0))
                        .setActionsCoordsTo(80,panelHeight-110);
                break;
            case "Building":
                if (players.get(activePlayer).getBuildings().size() > 0)
                players.get(activePlayer).getBuildings()
                        .get(players.get(activePlayer).getAssignedSpritesIndeces().get(0))
                        .setActionsCoordsTo(80, panelHeight-110);
                break;
        }
    }

    /**
     * Handle the active player's right mouse button actions.
     */
    public void catchButton3(int activePlayer, Point p){
        switch (players.get(activePlayer).getAssignedType()) {
            case "Building":
                for (Building building : players.get(activePlayer).getBuildings()) {
                    switch (building.getClass().getTypeName()) {
                        case "sprites.buildings.Headquarter":
                            ((Headquarter) building).changeOutPos(p.x, p.y);
                            break;
                        case "sprites.buildings.Barrack":
                            ((Barrack) building).changeOutPos(p.x,p.y);
                            break;
                    }
                }
                break;
            case "Unit":
                for (Unit unit : players.get(activePlayer).getUnits()) {
                    if (players.get(activePlayer).assignedSpritesContains(unit)) {
                        boolean isAttacking = false;
                        for (Player player : players) {
                            if (!players.get(activePlayer).equals(player)) {
                                for (Unit enemyUnit : player.getUnits()) {
                                    if (enemyUnit.getBounds().contains(p)) {
                                        unit.attack(enemyUnit,false);
                                        isAttacking = true;
                                    }
                                }
                                for (Building enemyBuilding : player.getBuildings()) {
                                    if (enemyBuilding.getBounds().contains(p)) {
                                        unit.attack(enemyBuilding,false);
                                        isAttacking = true;
                                    }
                                }
                            } else {
                                for (Building friendlyBuilding : player.getBuildings()) {
                                    if (friendlyBuilding.getBounds().contains(p)
                                            && friendlyBuilding.getClass().getTypeName()
                                            .equals("sprites.buildings.Brothel")) {
                                        unit.heal(friendlyBuilding,false);
                                        isAttacking = true;
                                    }
                                }
                            }
                        }
                        if (unit.getClass().getTypeName().equals("sprites.units.Worker")) {
                            for (Tree tree : trees) {
                                if (tree.getBounds().contains(p)) {
                                    ((Worker) unit).attack(tree,false);
                                    isAttacking = true;
                                }
                            }
                            for (Mine mine : mines) {
                                if (mine.getBounds().contains(p)) {
                                    ((Worker) unit).attack(mine,false);
                                    isAttacking = true;
                                }
                            }
                        }
                        if (!isAttacking) unit.move(p.x, p.y);
                    }
                }
        }
    }

    /**
     * Returns players list.
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Returns trees list.
     */
    public ArrayList<Tree> getTrees() {
        return trees;
    }

    /**
     * Returns mines list.
     */
    public ArrayList<Mine> getMines() {
        return mines;
    }

    /**
     * Set panel width.
     */
    public void setPanelWidth(int panelWidth) {
        this.panelWidth = panelWidth;
    }

    /**
     * Set panel height.
     */
    public void setPanelHeight(int panelHeight) {
        this.panelHeight = panelHeight;
    }

    /**
     * Set map's width.
     */
    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    /**
     * Set map's height.
     */
    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }
}