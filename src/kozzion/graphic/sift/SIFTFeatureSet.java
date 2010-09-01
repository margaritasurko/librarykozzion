package kozzion.graphic.sift;

import java.awt.image.BufferedImage;
import java.util.ArrayList;


import kozzion.grapics.kdtree.KDNode;
import kozzion.grapics.kdtree.KDTree;

public class SIFTFeatureSet 
{
	private static final int    KDTREE_BBF_MAX_NN_CHKS = 200;
	private static final double NN_SQ_DIST_RATIO_THR = 0.49;
	ArrayList<SIFTFeature> 		d_features;
	
	public SIFTFeatureSet(BufferedImage image1)
	{
		
	}
	
	public static int _match_features_with_kdtree(SIFTFeature[] feat1, int n1, KDNode kd_root) 
	{
		SIFTFeature[] nbrs = new SIFTFeature[2];
		SIFTFeature feat;
		double d0, d1;
		int i,k,m = 0;

		for( i = 0; i < n1; i++ )
		{
			feat = feat1[i];
			k = KDTree.kdtree_bbf_knn( kd_root, feat, 2, nbrs, KDTREE_BBF_MAX_NN_CHKS );
			if( k == 2 )
			{
				d0 = SIFTFeature.descr_dist_sq( feat, nbrs[0] );
				d1 = SIFTFeature.descr_dist_sq( feat, nbrs[1] );
				if( d0 < d1 * NN_SQ_DIST_RATIO_THR )
				{
					m++;
					feat1[i].fwd_match = nbrs[0];
				}
			}
		}

		return m;
	}
	
	public static int _match_with_features(
			SIFTFeature[] feat1, int n1, 
			SIFTFeature[] feat2, int n2) {
		KDNode kd_root = KDTree.kdtree_build( feat2, n2 );
		return _match_features_with_kdtree(feat1, n1, kd_root);
	}

}
