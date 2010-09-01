package kozzion.grapics.kdtree;

import java.util.Arrays;

public class MinPriOueue {
	MinPriNode[] pq_array;    /* array containing priority queue */
	int nallocd;                 /* number of elements allocated */
	int n;                       /**< number of elements in pq */
	
	private static final String __LINE__ = "no line";
	private static final String __FILE__ = "MinPriQueue";
	private static final int MINPQ_INIT_NALLOCD = 512;
	
	private static int parent( int i )
	{
		return ( i - 1 ) / 2;
	}


	/* returns the array index of element i's right child */
	private static int right( int i )
	{
		return 2 * i + 2;
	}


	/* returns the array index of element i's left child */
	private static int left( int i )
	{
		return 2 * i + 1;
	}


	/********************** Functions prototyped in minpq.h **********************/


	/*
	Creates a new minimizing priority queue.
	*/
	public MinPriOueue() {
		this.pq_array = new MinPriNode[MINPQ_INIT_NALLOCD];
		this.nallocd = MINPQ_INIT_NALLOCD;
		this.n = 0;
	}


	/**
	Inserts an element into a minimizing priority queue.

	@param min_pq a minimizing priority queue
	@param data the data to be inserted
	@param key the key to be associated with \a data

	@return Returns 0 on success or 1 on failure.
	*/
	int minpq_insert( Object data, int key )
	{
		int n = this.n;

		/* double array allocation if necessary */
		if( this.nallocd == n )
		{
			
			this.pq_array = Arrays.copyOf(this.pq_array, this.pq_array.length * 2);
			this.nallocd = this.pq_array.length;
			
			if( this.nallocd == 0 )
			{
				System.err.format("Warning: unable to allocate memory, %s, line %d\n",
						__FILE__, __LINE__ );
				return 1;
			}
		}

		this.pq_array[n] = new MinPriNode();
		this.pq_array[n].data = data;
		this.pq_array[n].key = Integer.MAX_VALUE;
		decrease_pq_node_key( this.pq_array, this.n, key );
		this.n++;

		return 0;
	}



	/*
	Returns the element of a minimizing priority queue with the smallest key
	without removing it from the queue.

	@param min_pq a minimizing priority queue

	@return Returns the element of \a min_pq with the smallest key or null
	if \a min_pq is empty
	*/
	Object minpq_get_min(  )
	{
		if( this.n < 1 )
		{
			System.err.format("Warning: PQ empty, %s line %d\n", __FILE__, __LINE__ );
			return null;
		}
		return this.pq_array[0].data;
	}



	/*
	Removes and returns the element of a minimizing priority queue with the
	smallest key.

	@param min_pq a minimizing priority queue

	@return Returns the element of \a min_pq with the smallest key of null
	if \a min_pq is empty
	*/
	Object minpq_extract_min(  )
	{
		Object data;

		if( this.n < 1 )
		{
			System.err.format("Warning: PQ empty, %s line %d\n", __FILE__, __LINE__ );
			return null;
		}
		data = this.pq_array[0].data;
		this.n--;
		this.pq_array[0] = this.pq_array[this.n];
		restore_minpq_order( this.pq_array, 0, this.n );

		return data;
	}


	/*
	De-allocates the memory held by a minimizing priorioty queue

	@param min_pq pointer to a minimizing priority queue
	*/
//	void minpq_release( MinPriQueue* min_pq )
//	{
//		if( min_pq == null )
//		{
//			System.err.format("Warning: null pointer error, %s line %d\n", __FILE__,
//					__LINE__ );
//			return;
//		}
//		if( *min_pq  &&  (*min_pq).pq_array )
//		{
//			free( (*min_pq).pq_array );
//			free( *min_pq );
//			*min_pq = null;
//		}
//	}


	/************************ Functions prototyped here **************************/

	/*
	Decrease a minimizing pq element's key, rearranging the pq if necessary

	@param pq_array minimizing priority queue array
	@param i index of the element whose key is to be decreased
	@param key new value of element <EM>i</EM>'s key; if greater than current
		key, no action is taken
	*/
	void decrease_pq_node_key( MinPriNode[] pq_array, int i, int key )
	{
		MinPriNode tmp;

		if( key > pq_array[i].key )
			return;

		pq_array[i].key = key;
		while( i > 0  &&  pq_array[i].key < pq_array[parent(i)].key )
		{
			tmp = pq_array[parent(i)];
			pq_array[parent(i)] = pq_array[i];
			pq_array[i] = tmp;
			i = parent(i);
		}
	}



	/*
	Recursively restores correct priority queue order to a minimizing pq array

	@param pq_array a minimizing priority queue array
	@param i index at which to start reordering
	@param n number of elements in \a pq_array
	*/
	void restore_minpq_order( MinPriNode[] pq_array, int i, int n )
	{
		MinPriNode tmp;
		int l, r, min = i;

		l = left( i );
		r = right( i );
		if( l < n )
			if( pq_array[l].key < pq_array[i].key )
				min = l;
		if( r < n )
			if( pq_array[r].key < pq_array[min].key )
				min = r;

		if( min != i )
		{
			tmp = pq_array[min];
			pq_array[min] = pq_array[i];
			pq_array[i] = tmp;
			restore_minpq_order( pq_array, min, n );
		}
	}

}
