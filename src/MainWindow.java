import panels.EditorGUI;
import panels.GameGUI;
import panels.LobbyPanel;
import panels.MenuPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
public class MainWindow extends JFrame implements ActionListener {
    private JPanel panel;
    private String mapName;

    /**
     * MainWindow constructor.
     */
    public MainWindow(){
        init();
    }

    void init(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pixel Empire");
        Dimension dim  = Toolkit.getDefaultToolkit().getScreenSize();
        panel = new MenuPanel(this);
        add(panel);
        pack();
        setSize(panel.getWidth(),panel.getHeight());
        setLocation((dim.width/2) - (getSize().width/2), (dim.height/2) - (getSize().height/2));

        setVisible(true);
    }

    /**
     * Start the MainWindow JFrame.
     */
    public static void main(String[] args) {
        new MainWindow();
    }

    /**
     * Handle the action events.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        switch (source.getClass().getTypeName()){
            case "javax.swing.JButton":
                GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
                Rectangle windowBounds = env.getMaximumWindowBounds();
                switch (((JButton) source).getName()){
                    case "New Game":
                        panel.setVisible(false);
                        panel = new LobbyPanel(this, "178.12312.312.3.12");
                        setSize(panel.getWidth(), panel.getHeight());
                        add(panel);
                        pack();
                        panel.setVisible(true);
                        break;
                    case "World Editor":
                        panel.setVisible(false);
                        setLocation(0,0);
                        setSize(windowBounds.width,windowBounds.height);
                        panel = new EditorGUI(2500,1500, getWidth(), getHeight()-30, this);
                        add(panel);
                        setResizable(false);
                        panel.setVisible(true);
                        break;
                    case "Settings":
                        System.out.println("SETTINGS");
                        break;
                    case "Back to menu":
                        panel.setVisible(false);
                        panel = new MenuPanel(this);
                        add(panel);
                        pack();
                        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                        setLocation(dim.width/2 - getWidth()/2, dim.height/2 - getHeight()/2);
                        setSize(panel.getWidth(), panel.getHeight());

                        panel.setVisible(true);
                        break;
                    case "Start game":
                        panel.setVisible(false);
                        ArrayList<String> playerNames = new ArrayList<>();
                        for (JTextField textField : ((LobbyPanel) panel).getTextFields()){
                            String text = textField.getText();
                            if (text.length() > 0) playerNames.add(text);
                        }
                        setLocation(0,0);
                        setSize(windowBounds.width,windowBounds.height);
                        panel = new GameGUI(playerNames,mapName, getWidth(), getHeight()-30);
                        panel.setFocusable(true);
                        add(panel);
                        pack();
                        setResizable(false);
                        panel.setVisible(true);
                        panel.requestFocus();
                        break;
                        default:
                }
                break;
            case "javax.swing.JComboBox":
                if (((JComboBox<String>) source).getName().equals("Map List")) {
                    mapName = (String) ((JComboBox<String>) source).getSelectedItem();
                }
            default:
        }
    }
}