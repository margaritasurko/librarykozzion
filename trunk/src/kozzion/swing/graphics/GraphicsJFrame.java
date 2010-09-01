package kozzion.swing.graphics;


import java.awt.Image;
import javax.swing.JFrame;

import main.ImagePanel;

public class GraphicsJFrame extends JFrame
{

    private static final long serialVersionUID = -7896254613683656184L;
    
    ImageJPanel d_imagepanel;
    
    
    public GraphicsJFrame(String name, int locx, int locy)
    {
        super(name);
        d_imagepanel = new ImageJPanel(320, 240);
        setContentPane(d_imagepanel);
        setSize(320, 240);
        setLocation(locx, locy);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    public void update()
    {
        d_imagepanel.repaint();
    }

    public void setImage(Image image)
    {
        d_imagepanel.setImage(image);
    }
    
}
