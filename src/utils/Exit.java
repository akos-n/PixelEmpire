package utils;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Exit extends WindowAdapter{
    
    /**
     * Override the window closing method with System.exit(0)
     */
    @Override
    public void windowClosing(WindowEvent e){ System.exit(0); }

}
