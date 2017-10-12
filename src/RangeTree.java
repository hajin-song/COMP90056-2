// RangeTree.java
// Tree Object for Dyadic Storage 
// Created By - Ha Jin Song
// Created On - 12 Oct 2017
// Modified By - Ha Jin Song
// Last Modified - 13 Oct 2017

public class RangeTree {
	private int range;
	private int depth;
	private int bucketSize;
	public RangeRow[] tree;
	
	public RangeTree(int range, int depth, int bucketSize) {
		this.range = range;
		this.depth = depth;
		this.bucketSize = bucketSize;
		this.tree = new RangeRow[this.depth];
		
		// Reduce Size on each row as it gets deeper
		for(int i = 0; i < this.depth ; i++) {
			this.tree[i] = new RangeRow(this.range/(int)Math.pow(2, i), this.bucketSize);
		}
	}
}
