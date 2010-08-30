package kozzion.swing.layout;


import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;;

@SuppressWarnings("serial")
public abstract class GridBagJFrame extends JFrame implements ActionListener 
{
   
    GridBagConstraints d_gridbagconstraints;   


    public GridBagJFrame()
    {   
        d_gridbagconstraints = new GridBagConstraints();
        getContentPane().setLayout(new GridBagLayout());
    }    
    
    protected void addComponentToPane(Component component, int x , int y, int w, int h)
    {
        d_gridbagconstraints.fill = GridBagConstraints.HORIZONTAL;

        d_gridbagconstraints.gridx = x;
        d_gridbagconstraints.gridy = y;   

        d_gridbagconstraints.gridwidth = w;
        d_gridbagconstraints.gridheight = h;
        
        d_gridbagconstraints.weightx = 1.0;
        getContentPane().add(component, d_gridbagconstraints);
    }
    
    abstract public void actionPerformed(ActionEvent arg0);  
}


