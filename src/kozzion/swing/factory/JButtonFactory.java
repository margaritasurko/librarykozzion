package kozzion.swing.factory;

import java.awt.event.ActionListener;

import javax.swing.JButton;

public class JButtonFactory
{
    public static JButton makeJButton(String label, String action, ActionListener listener)
    {
        JButton button = new JButton(label);
        button.setActionCommand(action);
        button.addActionListener(listener);
        return button;
    }
}
