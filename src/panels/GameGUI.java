package panels;

import engine.GameEngine;
import engine.Map;
import engine.Player;
import sprites.buildings.Barrack;
import sprites.buildings.Building;
import sprites.buildings.Headquarter;
import sprites.features.Mine;
import sprites.features.Tree;
import sprites.units.Unit;
import sprites.utils.Action;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GameGUI extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener{

    private Timer timer;
    private GameEngine gameEngine;

    private int activePlayer;
    private Point pressedView;
    private Point releasedView;

    private boolean isButton1Pressed;
    private Point mouseButton1Pressed;
    private Point mouseButton1Released;

    private int mapWidth;
    private int mapHeight;

    /**
     * Initialize the GameGui.
     */
    public GameGUI(ArrayList<String> playerNames, String mapName, int clipWidth, int clipHeight){
        super(new BorderLayout());
        setPreferredSize(new Dimension(clipWidth,clipHeight));
        setSize(getPreferredSize().width,getPreferredSize().height);
        init(playerNames, mapName);
        activePlayer = 0;
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
    }

    private void init(ArrayList<String> playerNames, String mapName){
        ArrayList<Player> players = new ArrayList<>();
        for (String playerName : playerNames) {
            players.add(new Player(playerName));
        }
        Map map = new Map(mapName ,players);
        mapWidth = map.getMapWidth();
        mapHeight = map.getMapHeight();
        gameEngine = new GameEngine(map.getPlayers(), map.getTrees(), map.getMines());
        gameEngine.setPanelWidth(getWidth());
        gameEngine.setPanelHeight(getHeight());
        gameEngine.setMapWidth(mapWidth);
        gameEngine.setMapHeight(mapHeight);
        timer = new Timer(10, this);
        timer.start();
    }

    /**
     * Override the paint component to paint the current sprites.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        g.fillRect(0,0,mapWidth,mapHeight);
        String winner = drawWinner();
        if (winner != null) {
            g.setColor(Color.BLACK);
            g.drawString(winner + " has won!", getWidth()/2, getHeight()/2);
        } else {
            drawObjects(g);
            if (gameEngine.getPlayers().get(activePlayer).isBuilding()) drawUnderBuilding(g);
            else if (isButton1Pressed) drawAssignRect(g);
            drawAssingedActions(g);
            drawPlayerResources(g);
        }
    }

    private void drawObjects(Graphics g) {
        Player actPlayer = gameEngine.getPlayers().get(activePlayer);
        for (Player player : gameEngine.getPlayers()) {
            for (Unit unit : player.getUnits()) {
                if (isInPlayerView(unit.getBounds()))
                    g.drawImage(unit.getImage(), unit.getX() - actPlayer.getViewX(),
                            unit.getY() - actPlayer.getViewY(), this);
            }
            for (Building building : player.getBuildings()) {
                drawOutPositions(g,building);
                if (isInPlayerView(building.getBounds())) {
                    g.drawImage(building.getImage(), building.getX() - actPlayer.getViewX(),
                            building.getY() - actPlayer.getViewY(), this);
                }
            }
        }
        for (Tree tree : gameEngine.getTrees()){
            if (isInPlayerView(tree.getBounds()))
            g.drawImage(tree.getImage(), tree.getX()-actPlayer.getViewX(),
                    tree.getY()-actPlayer.getViewY(), this);
        }
        for (Mine mine : gameEngine.getMines()){
            if (isInPlayerView(mine.getBounds()))
            g.drawImage(mine.getImage(), mine.getX()-actPlayer.getViewX(),
                    mine.getY()-actPlayer.getViewY(), this);
        }
    }

    private void drawOutPositions(Graphics g, Building building) {
        Player player = gameEngine.getPlayers().get(activePlayer);
        if (player.getAssignedType().equals("Building")) {
            switch (building.getClass().getTypeName()) {
                case "sprites.buildings.Headquarter":
                    for (Integer index : player.getAssignedSpritesIndeces()) {
                        if (player.getBuildings().get(index).equals(building)) {
                            ((Headquarter) building).getOut();
                            if (isInPlayerView(((Headquarter) building).getOut().getBounds())) {
                                g.drawImage(((Headquarter) building).getOut().getImage()
                                        , ((Headquarter) building).getOut().getX() - player.getViewX()
                                        , ((Headquarter) building).getOut().getY() - player.getViewY(), this);
                            }
                        }
                    }
                    break;
                case "sprites.buildings.Barrack":
                    for (Integer index : player.getAssignedSpritesIndeces()) {
                        if (player.getBuildings().get(index).equals(building)) {
                            ((Barrack) building).getOut();
                            if (isInPlayerView(((Barrack) building).getOut().getBounds())) {
                                g.drawImage(((Barrack) building).getOut().getImage()
                                        , ((Barrack) building).getOut().getX() - player.getViewX()
                                        , ((Barrack) building).getOut().getY() - player.getViewY(), this);
                            }
                        }
                    }
                    break;
            }
        }
    }

    private void drawAssignRect(Graphics g){
        g.setColor(Color.BLACK);
        Player player = gameEngine.getPlayers().get(activePlayer);
        if (player != null && mouseButton1Pressed != null && pressedView != null) {
            g.drawRect((mouseButton1Pressed.x - (player.getViewX() - pressedView.x)),
                    (mouseButton1Pressed.y - (player.getViewY() - pressedView.y)),
                    ((player.getMouseX() - mouseButton1Pressed.x) + (player.getViewX() - pressedView.x)),
                    ((player.getMouseY() - mouseButton1Pressed.y) + (player.getViewY() - pressedView.y)));
        }
    }

    private void drawUnderBuilding(Graphics g){
        Player player = gameEngine.getPlayers().get(activePlayer);
        Building building = player.getUnderBuilding();
        g.drawImage(building.getImage(),player.getMouseX()-building.getWidth()/2
                    ,player.getMouseY()-building.getHeight()/2, this);
    }

    private void drawAssingedActions(Graphics g){
        if (gameEngine.getPlayers().get(activePlayer).getAssignedActions() != null) {
            g.setColor(new Color(0,0,0));
            g.fillRect(50,getHeight()-140,530,90);
            g.setColor(new Color(153,76,0));
            g.fillRect(60,getHeight()-130,510,70);
            for (Action action : gameEngine.getPlayers().get(activePlayer).getAssignedActions()) {
                g.drawImage(action.getImage(), action.getX(), action.getY(), null);
            }
        }
    }

    private void drawPlayerResources(Graphics g){
        Player player = gameEngine.getPlayers().get(activePlayer);
        g.setColor(new Color(0,0,0));
        g.fillRect(getWidth()-350,getHeight()-100,300,50);
        g.setColor(new Color(153,76,0));
        g.fillRect(getWidth()-340,getHeight()-90,280,30);
        g.setColor(Color.BLACK);
        g.drawString("Gold: " + player.getGold()+"/"+player.getMaxGold(), getWidth()-330,getHeight()-70);
        g.drawString("Wood: " + player.getWood()+"/"+player.getMaxWood(), getWidth()-230,getHeight()-70);
        g.drawString("Food: " + player.getFood()+"/"+player.getMaxFood(), getWidth()-130,getHeight()-70);
    }

    private boolean isInPlayerView(Rectangle rectangle){
        Player player = gameEngine.getPlayers().get(activePlayer);
        Rectangle r = new Rectangle( player.getViewX(),player.getViewY(),getWidth(),getHeight());
        return r.intersects(rectangle) || r.contains(rectangle);
    }

    private String drawWinner(){
        String winner = "";
        int ingameCounter = 0;
        for (Player player : gameEngine.getPlayers()){
            if (player.isInGame()){
                ingameCounter++;
                winner = player.getName();
            }
        }
        if (ingameCounter == 1) return winner;
        return null;
    }

    private Rectangle assignRect(){
        Rectangle assignRect = new Rectangle((mouseButton1Pressed.x+pressedView.x),
                (mouseButton1Pressed.y+pressedView.y),
                ((mouseButton1Released.x-mouseButton1Pressed.x)+(releasedView.x-pressedView.x)),
                ((mouseButton1Released.y-mouseButton1Pressed.y)+(releasedView.y-pressedView.y)));
        if (assignRect.width == 0) assignRect.width = 1;
        if (assignRect.height == 0) assignRect.height = 1;
        if (assignRect.width < 0) assignRect.width = 0;
        if (assignRect.height < 0) assignRect.height = 0;

        mouseButton1Pressed = null;
        mouseButton1Released = null;
        return assignRect;
    }

    /**
     * Handle the action events.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (drawWinner() == null) {
            gameEngine.update();
            gameEngine.updatePlayerView(activePlayer);
            if (mouseButton1Pressed != null && mouseButton1Released != null) {
                if (!gameEngine.getPlayers().get(activePlayer).isBuilding()) {
                    gameEngine.checkGameMechanics(activePlayer, assignRect());
                } else {
                    gameEngine.checkGameMechanics(activePlayer, assignRect());
                }
            }
        }
        repaint();
    }

    /**
     * Does nothing.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Handle the BUTTON1 pressed events.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        Point mousePoint = e.getPoint();
        gameEngine.setPlayerMousePos(activePlayer, mousePoint.x, mousePoint.y);
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                Player player = gameEngine.getPlayers().get(activePlayer);
                pressedView = new Point(player.getViewX(), player.getViewY());
                isButton1Pressed = true;
                mouseButton1Pressed = mousePoint;
                break;
        }
    }

    /**
     * Handle the BUTTON1 and BUTTON3 released events.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        Player player = gameEngine.getPlayers().get(activePlayer);
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                releasedView = new Point(player.getViewX(), player.getViewY());
                isButton1Pressed = false;
                mouseButton1Released = e.getPoint();
                break;
            case MouseEvent.BUTTON3:
                if (!gameEngine.getPlayers().get(activePlayer).isBuilding()) {
                    Point p = new Point(e.getPoint().x + player.getViewX(), e.getPoint().y + player.getViewY());
                gameEngine.catchButton3(activePlayer, p);
                }
                break;
        }
    }

    /**
     * Does nothing.
     */
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Does nothing.
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Set the active player's mouse position from mouse event.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        Point mousePoint = e.getPoint();
        gameEngine.setPlayerMousePos(activePlayer, mousePoint.x, mousePoint.y);
    }

    /**
     * Set the active player's mouse position from mouse event.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        Point mousePoint = e.getPoint();
        gameEngine.setPlayerMousePos(activePlayer, mousePoint.x, mousePoint.y);
    }

    /**
     * Does nothing.
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * Does nothing.
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Handle the button VK_1, VK_2, VK_3, VK_4 key events.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key){
            case KeyEvent.VK_1:
                activePlayer = 0;
                break;
            case KeyEvent.VK_2:
                activePlayer = 1;
                break;
            case KeyEvent.VK_3:
                activePlayer = 2;
                break;
            case KeyEvent.VK_4:
                activePlayer = 3;
                break;
        }
    }
}
