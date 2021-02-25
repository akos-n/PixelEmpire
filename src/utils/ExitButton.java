package utils;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ExitButton extends JButton {
    
    /**
     * Add new AbstractAction to the super() what's overriding window closing.
     */
    public ExitButton(String name) {
        super(new AbstractAction(name) {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Exit().windowClosing(null);
            }
        });
    }
}
