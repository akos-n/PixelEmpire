package panels;

import utils.ExitButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {

    /**
     * Initialize the MenuPanel.
     */
    public MenuPanel(ActionListener actionListener) {
        super(new GridBagLayout());
        init(actionListener);
    }

    void init(ActionListener actionListener) {
        setPreferredSize(new Dimension(220,260));
        JButton newGameButton = new JButton("New Game");
        JButton worldEditorButton = new JButton(("World Editor"));
        JButton settingsButton = new JButton("Setting");
        ExitButton exitButton = new ExitButton("Exit");
        newGameButton.setName("New Game");
        worldEditorButton.setName("World Editor");
        settingsButton.setName("Settings");
        newGameButton.addActionListener(actionListener);
        worldEditorButton.addActionListener(actionListener);
        settingsButton.addActionListener(actionListener);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        this.add(newGameButton, gridBagConstraints);
        gridBagConstraints.gridy = 1;
        this.add(worldEditorButton, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        this.add(settingsButton, gridBagConstraints);
        gridBagConstraints.insets = new Insets(40, 10, 10, 10);
        gridBagConstraints.gridy = 3;
        this.add(exitButton, gridBagConstraints);
    }
}
