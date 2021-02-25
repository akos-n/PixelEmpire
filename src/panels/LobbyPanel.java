package panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class LobbyPanel extends JPanel {
    private String address;
    private int playerNumber = 4;
    private String mapName;
    private ArrayList<JTextField> textFields;

    /**
     * Initialize the LobbyPanel.
     */
    public LobbyPanel(ActionListener actionListener, String address){
        super(new GridBagLayout());
        this.address = address;
        init(actionListener);
    }

    void init(ActionListener actionListener){
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(10,10,10,10);

        initAddressLabel(gridBagConstraints);
        initComboBox(gridBagConstraints,actionListener);
        initTextFields(gridBagConstraints,actionListener);
        initStartButton(gridBagConstraints,playerNumber,actionListener);
        initBackButton(gridBagConstraints, playerNumber, actionListener);
    }

    private void initAddressLabel(GridBagConstraints gridBagConstraints){
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        JLabel addressLabel = new JLabel("IP Address: "+address);
        this.add(addressLabel, gridBagConstraints);
    }

    private void initComboBox(GridBagConstraints gridBagConstraints, ActionListener actionListener){
        File pathFinder = new File("");
        final File mapsFolder = new File(pathFinder.getAbsolutePath()+"\\src\\maps");
        pathFinder.delete();

        JComboBox<String> mapList = new JComboBox<>();
        mapList.setName("Map List");
        mapList.addActionListener(actionListener);
        mapList.addItem("Choose a map!");
        for (File fileEntry : mapsFolder.listFiles()) {
            String filename = fileEntry.getName();
            filename = filename.substring(0,filename.length()-4);
            mapList.addItem(filename);
        }
        mapsFolder.delete();
        gridBagConstraints.gridx = 1;
        this.add(mapList, gridBagConstraints);
    }

    private void initTextFields(GridBagConstraints gridBagConstraints, ActionListener actionListener){
        textFields = new ArrayList<>();

        gridBagConstraints.gridx = 0;
        String textFieldName = "Player ";
        int playerIndex = 0;
        while (playerIndex < playerNumber){
            gridBagConstraints.gridy = playerIndex+1;
            JTextField textField = new JTextField();
            textField.setPreferredSize(new Dimension(200,20));
            textField.setMinimumSize(textField.getPreferredSize());
            textField.setName(textFieldName+(playerIndex+1));
            this.add(textField, gridBagConstraints);
            textField.addActionListener(actionListener);
            textFields.add(textField);
            playerIndex++;
        }
    }

    private void initStartButton(GridBagConstraints gridBagConstraints, int playerIndex, ActionListener actionListener){
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = playerIndex + 2;
        JButton startButton = new JButton("Start");
        startButton.setName("Start game");
        startButton.addActionListener(actionListener);
        add(startButton,gridBagConstraints);
    }

    private void initBackButton(GridBagConstraints gridBagConstraints, int playerIndex, ActionListener actionListener){
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = playerIndex + 2;
        JButton backButton = new JButton("Back");
        backButton.setName("Back to menu");
        backButton.addActionListener(actionListener);
        add(backButton,gridBagConstraints);
    }

    /**
     * Set the player number.
     */
    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    /**
     * Returns the address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns the player number.
     */
    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     * Returns the map name.
     */
    public String getMapName() {
        return mapName;
    }

    /**
     * Returns the JTextFields.
     */
    public ArrayList<JTextField> getTextFields() {
        return textFields;
    }
}