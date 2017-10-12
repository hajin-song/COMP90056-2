
public class RangeRow {
	private int range;
	public int[][] cell;
	
	public RangeRow(int range, int bucketSize) {
		this.range = range;
		this.cell = new int[range][bucketSize];
	}
}
