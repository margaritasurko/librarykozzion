package kozzion.swing.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

import main.GraphicsTools;

class ImageJPanel extends JPanel 
{

    private static final long serialVersionUID = -2457642053349904340L;
    Image d_image;
  
    public ImageJPanel(int width, int height) 
    {
        setSize(width,height);
    }

    public void paint(Graphics g) 
    {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        if(d_image != null)
            GraphicsTools.drawImage(g2, (BufferedImage)d_image, getWidth(), getHeight());
    }

    public void setImage(Image image)
    {
        d_image = image;
        repaint();    
    }
}
