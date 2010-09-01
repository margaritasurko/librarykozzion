package kozzion.grapics.kdtree;

import kozzion.graphic.sift.SIFTFeature;


public class KDNode 
{
    int 			ki;          /**< partition key index */
    double 			kv;          /**< partition key value */
    int 			leaf;        /**< 1 if node is a leaf, 0 otherwise */
    SIFTFeature[] 	features;    /**< features at this node */
    int 			n;           /**< number of features */
    KDNode 			kd_left;     /**< left child */
    KDNode 			kd_right;    /**< right child */
}
