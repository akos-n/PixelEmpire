package engine;

import exceptions.EmptyFileException;
import exceptions.MaxPlayerException;
import sprites.buildings.Headquarter;
import sprites.features.Mine;
import sprites.features.Tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Map {
    private int countHeadquarters = 0;
    private int mapWidth, mapHeight, maxPlayer;

    private ArrayList<Tree> trees;
    private ArrayList<Mine> mines;
    private ArrayList<Player> players;

    /**
     * Initialize the Map.
     */
    public Map(String filename, ArrayList<Player> players){
        this.players = players;
        trees = new ArrayList<>();
        mines = new ArrayList<>();
        try {
            init(filename);
        } catch (FileNotFoundException e) {
            System.out.println("The map's file is not found!");
        } catch (MaxPlayerException e) {
            System.out.println("Max players number exceed the maximum!");
        } catch (EmptyFileException e){
            System.out.println("The map's file is empty!");
        }
    }

    private void init(String filename) throws EmptyFileException, FileNotFoundException, MaxPlayerException {
        File pathFinder = new File("");
        final File mapFile = new File(pathFinder.getAbsolutePath()+"\\src\\maps\\"+filename+".map");
        Scanner sc = new Scanner(mapFile);

        String type = sc.next();
        if (type.equals("")) throw new EmptyFileException();
        if (type.equals("MapSize")) {
            mapWidth = sc.nextInt();
            mapHeight = sc.nextInt();
        }
        type = sc.next();
        if (type.equals("PlayerNumber")) maxPlayer = sc.nextInt();
        checkPlayerNumber();
        while (sc.hasNext()){
            type = sc.next();
            int numberOfType = sc.nextInt();
            for (int i = 0; i < numberOfType; ++i) addObject(type, sc);
        }
    }

    private void addObject(String type, Scanner sc){
        switch (type){
            case "Tree":
                addTree(sc.nextInt(), sc.nextInt());
                break;
            case "Mine":
                addMine(sc.nextInt(), sc.nextInt());
                break;
            case "Headquarter":
                addHeadquarter(sc.nextInt(), sc.nextInt());
                countHeadquarters++;
                break;
            default:
        }
    }

    private void addHeadquarter(int x, int y){
        if (countHeadquarters < players.size()) players.get(countHeadquarters).addBuilding(new Headquarter(x,y));
    }

    private void addMine(int x,int y){
        mines.add(new Mine(x,y));
    }

    private void addTree(int x, int y){
        trees.add(new Tree(x,y));
    }

    private void checkPlayerNumber() throws MaxPlayerException {
        if(maxPlayer < players.size()) throw new MaxPlayerException();
    }

    /**
     * Returns the map width.
     */
    public int getMapWidth() {
        return mapWidth;
    }

    /**
     * Returns the map height.
     */
    public int getMapHeight() {
        return mapHeight;
    }

    /**
     * Returns the trees ArrayList.
     */
    public ArrayList<Tree> getTrees() {
        return trees;
    }

    /**
     * Returns the mines ArrayList.
     */
    public ArrayList<Mine> getMines() {
        return mines;
    }

    /**
     * Returns the players ArrayList.
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }
}
