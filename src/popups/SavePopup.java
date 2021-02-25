package popups;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SavePopup extends JFrame {
    private JTextField nameTextField;

    /**
     * Initialize the SavePopup JFrame.
     */
    public SavePopup(ActionListener actionListener){
        setName("Save Map");
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(10,10,10,10);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setName("Cancel");
        cancelButton.addActionListener(actionListener);
        JButton saveButton = new JButton("Save");
        saveButton.setName("Save");
        saveButton.addActionListener(actionListener);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panel.add(new JLabel("Please give a map name!"), gridBagConstraints);

        nameTextField = new JTextField();
        nameTextField.addActionListener(actionListener);
        nameTextField.setPreferredSize(new Dimension(200,20));
        nameTextField.setMinimumSize(nameTextField.getPreferredSize());
        gridBagConstraints.gridy = 1;
        panel.add(nameTextField,gridBagConstraints);

        gridBagConstraints.gridy = 2;
        panel.add(saveButton, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        panel.add(cancelButton, gridBagConstraints);

        add(panel);
        pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(panel.getWidth(),panel.getHeight()+30);
        setLocation((dim.width/2) - (getSize().width/2), (dim.height/2) - (getSize().height/2));
        setVisible(true);
    }

    /**
     * Returns the name from the JTextField.
     */
    public JTextField getNameTextField() {
        return nameTextField;
    }
}

