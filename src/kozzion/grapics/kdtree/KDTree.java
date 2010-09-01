package kozzion.grapics.kdtree;

import java.util.Arrays;

import kozzion.graphic.sift.SIFTFeature;




public class KDTree {
	private static final String __LINE__ = "no line";
	
	public static KDNode kdtree_build( SIFTFeature[] features, int n )
	{
		KDNode kd_root;

		if( features == null  ||  n <= 0 )
		{
			System.err.format("Warning: kdtree_build(): no features, %s, line %d\n",
					"KDTree", __LINE__ );
			return null;
		}

		kd_root = kd_node_init( features, 0, n );
		expand_kd_node_subtree( kd_root );

		return kd_root;
	}
	
	public static int kdtree_bbf_knn( KDNode kd_root, SIFTFeature feat, int k,
			SIFTFeature nbrs[], int max_nn_chks )
	{
		KDNode expl;
		MinPriOueue min_pq;
		SIFTFeature tree_feat, _nbrs[];
		BBFData bbf_data;
		int i, t = 0, n = 0;
		
		if( nbrs == null  ||  feat == null  ||  kd_root == null )
		{
			System.err.format("Warning: null pointer error, %s, line %d\n",
					"KDTree", __LINE__ );
			return -1;
		}

		_nbrs = new SIFTFeature[k];
		min_pq = new MinPriOueue();
		min_pq.minpq_insert( kd_root, 0 );
		boolean fail = false;
		while( min_pq.n > 0  &&  t < max_nn_chks )
		{
			expl = (KDNode)min_pq.minpq_extract_min(  );
			if( expl == null )
			{
				System.err.format("Warning: PQ unexpectedly empty, %s line %d\n",
						"KDTree", __LINE__ );
				fail = true;
				break;
			}
			
			expl = explore_to_leaf( expl, feat, min_pq );
			if( expl == null )
			{
				System.err.format("Warning: PQ unexpectedly empty, %s line %d\n",
						"KDTree", __LINE__ );
				fail = true;
				break;
			}
			
			for( i = 0; i < expl.n; i++ )
			{
				tree_feat = expl.features[i];
				bbf_data = new BBFData();
				if( bbf_data == null )
				{
					System.err.format("Warning: unable to allocate memory, %s line %d\n", "KDTree", __LINE__ );
					fail = true;
					break;
				}
				bbf_data.old_data = tree_feat.feature_data;
				bbf_data.d = SIFTFeature.descr_dist_sq(feat, tree_feat);
				tree_feat.feature_data = bbf_data;
				n += insert_into_nbr_array( tree_feat, _nbrs, n, k );
			}
			t++;
		}

		if(!fail) {
			for( i = 0; i < n; i++ )
			{
				bbf_data = (BBFData)(_nbrs[i].feature_data);
				_nbrs[i].feature_data = bbf_data.old_data;
			}
			for (int j = 0; j < _nbrs.length; j++) {
				nbrs[j] = _nbrs[j];
			}
			return n;
		} else {
			for( i = 0; i < n; i++ )
			{
				bbf_data = (BBFData)(_nbrs[i].feature_data);
				_nbrs[i].feature_data = bbf_data.old_data;
			}
			return -1;
		}
	}
	
	int kdtree_bbf_spatial_knn( KDNode kd_root, SIFTFeature feat,
			   int k, SIFTFeature[] nbrs, int max_nn_chks,
			   DoubleRectangle rect, int model )
	{	
		SIFTFeature all_nbrs[] = null, sp_nbrs[];
		DoubleVector pt;
		int i, n, t = 0;
	
		n = kdtree_bbf_knn( kd_root, feat, max_nn_chks, all_nbrs, max_nn_chks );
		sp_nbrs = new SIFTFeature[k]; //calloc( k, sizeof( SIFTFeature[] ) );
		for( i = 0; i < n; i++ )
		{
			if( model > 0 )
				pt = all_nbrs[i].mdl_pt;
			else
				pt = all_nbrs[i].img_pt;
	
			if( within_rect( pt, rect ) > 0 )
			{
				sp_nbrs[t++] = all_nbrs[i];
				if( t == k )
					break;
			}
		}

		nbrs = Arrays.copyOf(sp_nbrs,sp_nbrs.length);
		return t;
	}

	void kdtree_release( KDNode kd_root )
	{
		if( kd_root == null )
			return;
		kdtree_release( kd_root.kd_left );
		kdtree_release( kd_root.kd_right );
	}

	private static KDNode kd_node_init( SIFTFeature[] features, int from, int n )
	{
		KDNode kd_node;

		kd_node = new KDNode();
		kd_node.ki = -1;
		kd_node.features = Arrays.copyOfRange(features, from, from+n);
		kd_node.n = n;

		return kd_node;
	}

	private static void expand_kd_node_subtree( KDNode kd_node )
	{
		/* base case: leaf node */
		if( kd_node.n == 1  ||  kd_node.n == 0 )
		{
			kd_node.leaf = 1;
			return;
		}

		assign_part_key( kd_node );
		partition_features( kd_node );

		if( kd_node.kd_left != null )
			expand_kd_node_subtree( kd_node.kd_left );
		if( kd_node.kd_right != null )
			expand_kd_node_subtree( kd_node.kd_right );
	}

	private static void assign_part_key( KDNode kd_node )
	{
		SIFTFeature[] features;
		double kv, x, mean, var, var_max = 0;
		double tmp[];
		int d, n, i, j, ki = 0;

		features = kd_node.features;
		n = kd_node.n;
		d = features[0].d;

		/*
		 * partition key index is that along which descriptors have most
		 * variance
		 */
		for( j = 0; j < d; j++ )
		{
			mean = var = 0;
			for( i = 0; i < n; i++ )
				mean += features[i].descr[j];
			mean /= n;
			for( i = 0; i < n; i++ )
			{
				x = features[i].descr[j] - mean;
				var += x * x;
			}
			var /= n;

			if( var > var_max )
			{
				ki = j;
				var_max = var;
			}
		}

		/* partition key value is median of descriptor values at ki */
		tmp = new double[n]; // calloc( n, sizeof( double ) );
		for( i = 0; i < n; i++ )
			tmp[i] = features[i].descr[ki];
		kv = median_select( tmp, n );
//		free( tmp );

		kd_node.ki = ki;
		kd_node.kv = kv;
	}
	
	static double median_select( double array[], int n )
	{
		return rank_select( array, n, (n - 1) / 2 );
	}

	static double rank_select( double array[], int n, int r )
	{
		double tmp[], med;
		int gr_5, gr_tot, rem_elts, i, j;

		/* base case */
		if( n == 1 )
			return array[0];

		/* divide array into groups of 5 and sort them */
		gr_5 = n / 5;
		gr_tot = (int)Math.ceil( n / 5.0 );
		rem_elts = n % 5;
//		tmp = Arrays.copyOf(array,5);
		for( i = 0; i < gr_5; i++ )
		{
//			insertion_sort( tmp, 5 );
			Arrays.sort(array, i*5, Math.min(i*5+5,array.length));
//			tmp += 5;
		}
//		insertion_sort( tmp, rem_elts );

		/* recursively find the median of the medians of the groups of 5 */
		tmp = new double[gr_tot]; //calloc( gr_tot, sizeof( double ) );
		for( i = 0, j = 2; i < gr_5; i++, j += 5 )
			tmp[i] = array[j];
		if( rem_elts > 0 )
			tmp[i++] = array[n - 1 - rem_elts/2];
		med = rank_select( tmp, i, ( i - 1 ) / 2 );
//		free( tmp );

		/*
		 * partition around median of medians and recursively select if
		 * necessary
		 */
		j = partition_array( array, n, med );
		if( r == j )
			return med;
		else if( r < j )
			return rank_select( array, j, r );
		else
		{
			return rank_select( Arrays.copyOfRange(array,j+1,n), ( n - j - 1 ), ( r - j - 1 ) );
		}
	}
	
	void insertion_sort( double array[], int n )
	{
		double k;
		int i, j;

		for( i = 1; i < n; i++ )
		{
			k = array[i];
			j = i-1;
			while( j >= 0  &&  array[j] > k )
			{
				array[j+1] = array[j];
				j -= 1;
			}
			array[j+1] = k;
		}
	}

	static int partition_array( double array[], int n, double pivot )
	{
		double tmp;
		int p = 0, i, j;

		i = -1;
		for( j = 0; j < n; j++ )
			if( array[j] <= pivot )
			{
				tmp = array[++i];
				array[i] = array[j];
				array[j] = tmp;
				if( array[i] == pivot )
					p = i;
			}
		array[p] = array[i];
		array[i] = pivot;

		return i;
	}

	private static void partition_features( KDNode kd_node )
	{
		SIFTFeature features[], tmp;
		double kv;
		int n, ki, p = 0, i, j = -1;

		features = kd_node.features;
		n = kd_node.n;
		ki = kd_node.ki;
		kv = kd_node.kv;
		for( i = 0; i < n; i++ )
			if( features[i].descr[ki] <= kv )
			{
				tmp = features[++j];
				features[j] = features[i];
				features[i] = tmp;
				if( features[j].descr[ki] == kv )
					p = j;
			}
		tmp = features[p];
		features[p] = features[j];
		features[j] = tmp;

		/* if all records fall on same side of partition, make node a leaf */
		if( j == n - 1 )
		{
			kd_node.leaf = 1;
			return;
		}

		kd_node.kd_left = kd_node_init( features, 0, j + 1 );
		kd_node.kd_right = kd_node_init( features, j + 1,  n - j - 1 );
	}

	private static KDNode explore_to_leaf( KDNode kd_node, SIFTFeature feat, MinPriOueue min_pq )
	{
		KDNode unexpl, expl = kd_node;
		double kv;
		int ki;
		
		while( expl != null  &&  expl.leaf == 0 )
		{
			ki = expl.ki;
			kv = expl.kv;
			
			if( ki >= feat.d )
			{
				System.err.format("Warning: comparing imcompatible descriptors, %s line %d\n", "KDTree", __LINE__ );
				return null;
			}
			if( feat.descr[ki] <= kv )
			{
				unexpl = expl.kd_right;
				expl = expl.kd_left;
			}
			else
			{
				unexpl = expl.kd_left;
				expl = expl.kd_right;
			}
			
			if( min_pq.minpq_insert( unexpl, (int)Math.abs( kv - feat.descr[ki] ) ) > 0 )
			{
				System.err.format("Warning: unable to insert into PQ, %s, line %d\n",
						"KDTree", __LINE__ );
				return null;
			}
		}
		
		return expl;
	}

	public static int insert_into_nbr_array( SIFTFeature feat, SIFTFeature[] nbrs,
			  int n, int k )
	{
		BBFData fdata, ndata;
		double dn, df;
		int i, ret = 0;
		
		if( n == 0 )
		{
			nbrs[0] = feat;
			return 1;
		}

		/* check at end of array */
		fdata = (BBFData)feat.feature_data;
		df = fdata.d;
		ndata = (BBFData)nbrs[n-1].feature_data;
		dn = ndata.d;
		if( df >= dn )
		{
			if( n == k )
			{
				feat.feature_data = fdata.old_data;
				return 0;
			}
			nbrs[n] = feat;
			return 1;
		}
		
		/* find the right place in the array */
		if( n < k )
		{
			nbrs[n] = nbrs[n-1];
			ret = 1;
		}
		else
		{
			nbrs[n-1].feature_data = ndata.old_data;
		}
		i = n-2;
		while( i >= 0 )
		{
			ndata = (BBFData)nbrs[i].feature_data;
			dn = ndata.d;
			if( dn <= df )
			break;
			nbrs[i+1] = nbrs[i];
			i--;
		}
		i++;
		nbrs[i] = feat;
		
		return ret;
	}

	public static int within_rect( DoubleVector pt, DoubleRectangle rect )
	{
		if( pt.x < rect.x  ||  pt.y < rect.y )
			return 0;
		if( pt.x > rect.x + rect.width  ||  pt.y > rect.y + rect.height )
			return 0;
		return 1;
	}

}
