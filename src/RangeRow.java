// RangeRow.java
// Row Object for the Dyadic Storage
// Created By - Ha Jin Song
// Created On - 12 Oct 2017
// Modified By - Ha Jin Song
// Last Modified - 13 Oct 2017

public class RangeRow {
	private int range;
	public int[][] cell;
	
	public RangeRow(int range, int bucketSize) {
		this.range = range;
		// each cell has N buckets
		this.cell = new int[range+1][bucketSize];
	}
}
