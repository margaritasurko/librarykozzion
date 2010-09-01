package kozzion.graphic.sift;

import kozzion.grapics.kdtree.DoubleVector;


public class SIFTFeature 
{
	public double 		x;                      /**< x coord */
	public double 		y;                      /**< y coord */
	public int 			d;                         /**< descriptor length */
	public double 		descr[];                /**< descriptor */
	public double 		scl;                    /**< scale of a Lowe-style feature */
	public double 		ori;                    /**< orientation of a Lowe-style feature */
	public Object 		feature_data;
	public DoubleVector mdl_pt;
	public DoubleVector img_pt;
	
	public SIFTFeature fwd_match;
	
	public static double descr_dist_sq( SIFTFeature f1, SIFTFeature f2 )
	{
		double diff, dsq = 0;
		double descr1[], descr2[];
		int i, d;

		d = f1.d;
		if( f2.d != d )
			return Double.MAX_VALUE;
		descr1 = f1.descr;
		descr2 = f2.descr;

		for( i = 0; i < d; i++ )
		{
			diff = descr1[i] - descr2[i];
			dsq += diff*diff;
		}
		return dsq;
	}
}
