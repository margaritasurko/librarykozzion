package kozzion.swing.graphics;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class GraphicsTools
{
    public static void drawImage(Graphics2D g2, BufferedImage image, int sizex, int sizey)
    {
        double sclx = (double)sizex / (double)image.getWidth();
        double scly = (double)sizey / (double)image.getHeight();
        
        AffineTransform affine  = AffineTransform.getTranslateInstance(0 , 0);
        affine.scale(sclx, scly);

        g2.drawImage(image, affine, null);
    }
    
    public static void copy(BufferedImage orgiginal, BufferedImage target)
    {
        Graphics2D graphics2d = (Graphics2D)target.getGraphics();
        GraphicsTools.drawImage(graphics2d, (BufferedImage)orgiginal, target.getWidth(), target.getHeight());
    }



    public static boolean compatible(BufferedImage image1, BufferedImage image2)
    {   
        if (image1 == null)
            return false;
        if (image2 == null)
            return false;
        if (image1.getHeight() != image2.getHeight())
            return false;
        if (image1.getWidth() != image2.getWidth())
            return false;
        if (image1.getType() != image2.getType())
            return false;
        return true;
    }

}
