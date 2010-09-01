package kozzion.graphic.sift;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import kozzion.grapics.kdtree.KDNode;
import kozzion.grapics.kdtree.KDTree;


public class SIFTFeatures 
{
	private static final int    KDTREE_BBF_MAX_NN_CHKS = 200;
	private static final double NN_SQ_DIST_RATIO_THR = 0.49;
	
	int numFeatures;
	ArrayList<SIFTFeature> features;
	
	public static SIFTFeatures readFeatures(File f) throws IOException 
	{
		SIFTFeatures features = new SIFTFeatures();
		
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = br.readLine();
		StringTokenizer tokenizer = new StringTokenizer(line);
		features.numFeatures = Integer.parseInt(tokenizer.nextToken());
		int descriptorLength = Integer.parseInt(tokenizer.nextToken());
		features.features = new ArrayList<SIFTFeature>(descriptorLength);

		for (int i = 0; i < features.numFeatures; i++) 
		{
			SIFTFeature fe = new SIFTFeature();
			line = br.readLine();
			tokenizer = new StringTokenizer(line);
			fe.y = Double.parseDouble(tokenizer.nextToken());
			fe.x = Double.parseDouble(tokenizer.nextToken());
			fe.scl = Double.parseDouble(tokenizer.nextToken());
			fe.ori = Double.parseDouble(tokenizer.nextToken());
			fe.d = descriptorLength;
			
			fe.descr = new double[128];
			int count = 0;
			for (int j = 0; j < 7; j++) 
			{
				line = br.readLine();
				tokenizer = new StringTokenizer(line);
				while(tokenizer.hasMoreTokens()) {
					String next = tokenizer.nextToken();
					fe.descr[count++] = Double.parseDouble(next);
				}
			}
			features.features.add(fe);
		}
		
		return features;
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

	public int getNumFeatures() {
		return numFeatures;
	}

	public void setNumFeatures(int numFeatures) {
		this.numFeatures = numFeatures;
	}

	public ArrayList<SIFTFeature> getFeatures() {
		return features;
	}

	public void setFeatures(ArrayList<SIFTFeature> features) {
		this.features = features;
	}
}
