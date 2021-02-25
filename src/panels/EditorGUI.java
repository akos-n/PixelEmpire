package panels;

import popups.SavePopup;
import sprites.Sprite;
import sprites.buildings.Building;
import sprites.buildings.Headquarter;
import sprites.editors.DeleteSprite;
import sprites.features.Mine;
import sprites.features.Tree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditorGUI extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
    private Timer timer;
    private ActionListener mainActionListener;
    private String mapName;

    private ArrayList<Sprite> editorSprites;
    private ArrayList<Tree> trees;
    private ArrayList<Mine> mines;
    private ArrayList<Building> buildings;
    private Rectangle background;

    private int mapWidth;
    private int mapHeight;
    private int viewX;
    private int viewY;
    private int clipWidth;
    private int clipHeight;

    private boolean showEditorComponents;
    private SavePopup savePopup;
    private int mouseX;
    private int mouseY;
    private boolean isMousePressed;
    private Class<? extends Sprite> actionType = new Sprite(0,0).getClass();
    private Class<? extends Sprite> tmpActionType = new Sprite(0,0).getClass();

    /**
     * Initialize the EditorGUI.
     */
    public EditorGUI(int mapWidth, int mapHeight,int clipWidth, int clipHeight, ActionListener jframeActionListener){
        super(new FlowLayout());
        this.mainActionListener = jframeActionListener;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.clipWidth = clipWidth;
        this.clipHeight = clipHeight;

        showEditorComponents = true;
        mapName = "";
        viewX = 0;
        viewY = 0;
        init();
    }

    private void init(){
        addMouseListener(this);
        addMouseMotionListener(this);
        isMousePressed = false;
        background = new Rectangle(0,0, mapWidth, mapHeight);
        initEditorSprites();
        trees = new ArrayList<>();
        mines = new ArrayList<>();
        buildings = new ArrayList<>();
        initButtons();
        timer = new Timer(10,this);
        timer.start();
    }

    private void initButtons(){
        JButton hideButton = new JButton("Hide");
        hideButton.addActionListener(this);
        hideButton.setName("Hide");
        add(hideButton);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(this);
        saveButton.setName("Save button");
        add(saveButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(mainActionListener);
        backButton.setName("Back to menu");
        add(backButton);
    }

    private void initEditorSprites(){
        editorSprites = new ArrayList<>();
        editorSprites.add(new Tree(50, 50));
        editorSprites.add(new Mine(50, 10+editorSprites.get(0).getY() + 40));
        editorSprites.add(new Headquarter(50,10+editorSprites.get(1).getY() + 40));
        editorSprites.add(new DeleteSprite(50, 10+editorSprites.get(2).getY() + 40));
    }

    private void addNewSprite(int x, int y){
        switch (actionType.getTypeName()){
            case "sprites.features.Tree":
                Tree tree = new Tree(x,y);
                tree = new Tree(x-tree.getWidth()/2,y-tree.getHeight()/2);
                if (checkCollisions(tree)) trees.add(tree);
                break;
            case "sprites.features.Mine":
                Mine mine = new Mine(x,y);
                mine = new Mine(x-mine.getWidth()/2,y-mine.getHeight()/2);
                if (checkCollisions(mine)) mines.add(mine);
                break;
            case "sprites.buildings.Headquarter":
                Headquarter headquarter = new Headquarter(x,y);
                headquarter = new Headquarter(x-headquarter.getWidth()/2, y-headquarter.getHeight()/2);
                if (checkCollisions(headquarter)) buildings.add(headquarter);
                break;
            case "sprites.editors.DeleteSprite":
                deleteSprite();
            default:
        }
    }

    private void deleteSprite(){
        for (Tree tree : trees){
            if (tree.getBounds().contains(new Point(mouseX+viewX, mouseY+viewY))) {
                trees.remove(tree);
                return;
            }
        }
        for (Mine mine : mines){
            if (mine.getBounds().contains(new Point(mouseX+viewX, mouseY+viewY))) {
                mines.remove(mine);
                return;
            }
        }
        for (Building building : buildings){
            if (building.getBounds().contains(new Point(mouseX+viewX, mouseY+viewY))) {
                buildings.remove(building);
                return;
            }
        }
    }

    /**
     * Override the paintComponent and paint the current sprites.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(0,0,0));
        g.fillRect(0-viewX,0-viewY,mapWidth,mapHeight);
        g.setColor(new Color(0,255,0));
        g.fillRect(3+background.x-viewX,3+background.y-viewY,background.width-10,background.height-10);

        drawCurrentAction(g);
        drawObjects(g);
        if (showEditorComponents) drawEditorSprites(g);
    }

    private void drawCurrentAction(Graphics g){
        for (Sprite sprite : editorSprites){
            if (actionType.getTypeName().equals(sprite.getClass().getTypeName())
                    && sprite.getClass().getTypeName().equals("sprites.editors.DeleteSprite")){
                g.drawImage(sprite.getEditorImage(), mouseX-sprite.getEditorBounds().width/2,
                                                    mouseY-sprite.getEditorBounds().height/2, this);
            } else if (sprite.getClass().getTypeName().equals(actionType.getTypeName())) {
                g.drawImage(sprite.getImage(), mouseX-sprite.getWidth()/2,
                                                                mouseY-sprite.getHeight()/2, this);
            }
        }
    }

    private void drawEditorSprites(Graphics g){
        for (Sprite sprite : editorSprites){
            g.drawImage(sprite.getEditorImage(), sprite.getX(), sprite.getY(), this);
        }
    }

    private void drawObjects(Graphics g) {
        for (Building building : buildings){
            if (inClip(building.getBounds()))
            g.drawImage(building.getImage(), building.getX()-viewX, building.getY()-viewY, this);
        }
        for (Tree tree : trees){
            if (inClip(tree.getBounds()))
            g.drawImage(tree.getImage(), tree.getX()-viewX, tree.getY()-viewY, this);
        }
        for (Mine mine : mines){
            if (inClip(mine.getBounds()))
            g.drawImage(mine.getImage(), mine.getX()-viewX, mine.getY()-viewY, this);
        }
    }

    private boolean inClip(Rectangle rectangle){
        Rectangle r = new Rectangle(viewX,viewY,clipWidth,clipHeight);
        return r.intersects(rectangle) || r.contains(rectangle);
    }

    private void moveView(){
        Point mousePoint = new Point(mouseX, mouseY);
        //Right rectangle of the panel
        if (new Rectangle(getWidth()-50,0,50,getHeight()+80).contains(mousePoint)
                            && viewX <= mapWidth-getWidth()-10) viewX+=10;
        //Left rectangle of the panel
        if (new Rectangle(0,0,70,getHeight()+80).contains(mousePoint) &&viewX >= 10) viewX-=10;
        //Top rectangle of the panel
        if (new Rectangle(0,0,getWidth(),50).contains(mousePoint) && viewY >= 10) viewY-=10;
        //Bottom rectangle of the panel
        if (new Rectangle(0,getHeight()-50,getWidth(),130).contains(mousePoint)
                            && viewY <= mapHeight-getHeight()-10) viewY+=10;
    }

    private boolean isNewAction(){
        if (showEditorComponents) {
            for (Sprite sprite : editorSprites) {
                if (sprite.getEditorBounds().contains(new Point(mouseX, mouseY))) {
                    tmpActionType = sprite.getClass();
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkCollisions(Sprite sprite){
        for (Building building : buildings){
            if ((building.getBounds().intersects(sprite.getBounds())
                    || building.getBounds().contains(sprite.getBounds()))) return false;
        }
        for (Tree tree : trees){
            if (tree.getBounds().intersects(sprite.getBounds())
                    || tree.getBounds().contains(sprite.getBounds())) return false;
        }
        for (Mine mine : mines){
            if (mine.getBounds().intersects(sprite.getBounds())
                    || mine.getBounds().contains(sprite.getBounds())) return false;
        }
        return background.contains(sprite.getBounds());
    }

    private void printMap() throws IOException {
        File pathFinder = new File("");
        PrintWriter pw;
        if  (mapName.equals("")) {
            DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH-mm-ss");
            Date date = new Date();
            pw = new PrintWriter(new FileWriter(pathFinder.getAbsoluteFile() + "\\src\\maps\\"
                    + dateFormat.format(date) + " ("+buildings.size()+").map"));
        } else {
            pw = new PrintWriter(new FileWriter(pathFinder.getAbsoluteFile() + "\\src\\maps\\"
                    + mapName + " ("+buildings.size()+").map"));
        }
        pathFinder.delete();
        pw.print("MapSize\n");
        pw.print(mapWidth + " " + mapHeight+"\n");
        pw.print("PlayerNumber\n");
        pw.print(buildings.size()+"\n");
        printObjects(pw);
        pw.close();
    }

    private void printObjects(PrintWriter pw){
        if (mines.size() > 0) pw.print("Mine "+mines.size() +"\n");
        for (Mine mine : mines){
            pw.print(mine.toString()+"\n");
        }
        if (trees.size() > 0) pw.print("Tree "+trees.size()+"\n");
        for (Tree tree : trees){
            pw.print(tree.toString()+"\n");
        }
        if (buildings.size() > 0) pw.print("Headquarter "+buildings.size()+"\n");
        for (Building building : buildings){
            pw.print(building.toString()+"\n");

        }
    }

    /**
     * Override the actionPerformed and handle the action events.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getSource().getClass().getTypeName()) {
            case "javax.swing.JButton":
                switch (((JButton) e.getSource()).getName()) {
                    case "Save button":
                        savePopup = new SavePopup(this);
                        break;
                    case "Hide":
                        showEditorComponents = !showEditorComponents;
                        break;
                    case "Cancel":
                        savePopup.dispose();
                        savePopup = null;
                        break;
                    case "Save":
                        mapName = savePopup.getNameTextField().getText();
                        System.out.println(mapName);
                        try {
                            printMap();
                        } catch (IOException ex) {
                            System.out.println("IOException at printMap()!");
                        }
                        savePopup.dispose();
                        savePopup = null;
                        break;
                }
                break;
            default:
                moveView();
                repaint();
                if (isMousePressed && !isNewAction()) addNewSprite(mouseX + viewX, mouseY + viewY);

        }
    }
    /**
     * Does nothing.
     */
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * Handle the mouse events.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        Point mousePoint = e.getPoint();
        mouseX = mousePoint.x;
        mouseY = mousePoint.y;
        if (!isNewAction()) addNewSprite(mouseX+viewX,mouseY+viewY);
        else {
            if (actionType.getTypeName().equals("sprites.Sprite")
                    || !actionType.equals(tmpActionType)) actionType = tmpActionType;
            else {
                actionType = new Sprite(0,0).getClass();
            }
        }
        isMousePressed = true;
    }

    /**
     * Handle the mouse events.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        isMousePressed = false;
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
     * Set the mouse positions from mouse event.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        Point mousePoint = e.getPoint();
        mouseX = mousePoint.x;
        mouseY = mousePoint.y;
    }

    /**
     * Set the mouse positions from mouse event.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        Point mousePoint = e.getPoint();
        mouseX = mousePoint.x;
        mouseY = mousePoint.y;
    }
}
