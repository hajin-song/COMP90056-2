
public class RangeTree {
	private int range;
	private int depth;
	public RangeRow[] tree;
	
	public RangeTree(int range, int depth) {
		this.range = range;
		this.depth = depth;
		this.tree = new RangeRow[this.depth];
		
		for(int i = 0; i < this.depth ; i++) {
			this.tree[i] = new RangeRow(this.range/(int)Math.pow(2, i));
		}
	}
}
