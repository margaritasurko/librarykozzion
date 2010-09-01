package superresolution;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Vector;

public class SuperResolutionImage
{
    

    Vector<SubImage>  d_subimages;
    
    
    public SuperResolutionImage(List<BufferedImage> inputimages, int iterationcount, double maxtransform)
    {

        //make sub-images
        d_subimages = new Vector<SubImage>();
        for(int index = 0; index < inputimages.size(); index++)
            d_subimages.add(new SubImage(inputimages.get(index), 0, 0));
        
        //glue sub-images
        for(int index = 0; index < d_subimages.size() - 1; index++)
        {
            IntegerCorrelationTransformationEstimation(d_subimages.get(index),d_subimages.get(index + 1), maxtransform); 
        }
        
        //build super-image
        renderSuperResolutionImage();
        
        //start interation process
        
        
    }
    
    
    private void IntegerCorrelationTransformationEstimation(SubImage subimage1, SubImage subimage2, double maxtransform)
    {
        // TODO Auto-generated method stub
        
    }


    private void renderSuperResolutionImage()
    {
        //reset transformation origin 
        
    }


    //TODO remove this function when done
    public static void testSuperResolution(BufferedImage original, int imagecount, double scale, double subsampleing, double maxtransform)
    {
        
        //show original
        
        //subsample image
        
        //show subsampling
        
        //make superimage and show
    }
    
    private class SubImage
    {
        public double           d_x;
        public double           d_y;
        public BufferedImage    d_subimage;
        
        public SubImage(BufferedImage subimage, double x, double y)
        {
            d_subimage  = subimage;
            d_x         = x;
            d_y         = y;
        }
    } 
}
