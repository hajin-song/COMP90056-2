import java.util.Random;

// CMLS.java
// CMLS Count Min Sketch
// Created By - Ha Jin Song
// Created On - Oct 2017
// Modified By - Ha Jin Song
// Last Modified - 11 Oct 2017
// Based on the paper provided and GoLang version of the implementation
// - https://github.com/seiflotfy/count-min-log


public class CMLS extends Counter{
	protected short[][] T;
		
	// Sadly, no easy way to get unsigned short max
	// apart from around about way of using char in byte format
	private double logBase;
	private Random rand;
	public CMLS(int n){
		super(n);
		this.w *= 2;
		T = new short[d][w];
		hfs = new Hash[d];
		for(int i=0;i<d;i++){
			hfs[i] = new Hash(n,w);
		}
		this.rand = new Random();
		this.logBase = 1.00025; //magic number from paper
		this.treeDepth = (int)Math.ceil((Math.log(n) / Math.log(2)));
		this.freqRange = new RangeTree(n, treeDepth);
	}
	
	@Override
	public void add(int x, int count){
		int currentFreq = T[0][hfs[0].hash(x)];
		for(int i=1;i<d;i++){
			currentFreq = Math.min(currentFreq, T[i][hfs[i].hash(x)]);
		}
		
		for(int freqCount = 0 ; freqCount < count ; freqCount++) {
			if(this.shouldIncrease(currentFreq)) {
				boolean updated = false;
				for(int i=0;i<d;i++){
					if(T[i][hfs[i].hash(x)] == currentFreq) {
						updated = true;
						T[i][hfs[i].hash(x)] += 1;
						//System.err.format("hash[%2d](%6d)=%5d%n",i,x,hfs[i].hash(x));
					}
				}
				if(updated) {
					currentFreq += 1;
				}
			}

		}
		// Update the Frequency Lists
		for(int j = 0 ; j < treeDepth ; j++) {
			this.freqRange.tree[j].cell[(int) (x/(Math.pow(2, j)))] += this.value(currentFreq);
		}
	}
	
	@Override
	public int get(int y){
		int m = T[0][hfs[0].hash(y)];
		for(int i=1;i<d;i++){
			m = Math.min(m,T[i][hfs[i].hash(y)]);
		}
		return (int) Math.round(this.value(m));
	}
	
	@Override
	public int get(int low, int high) {
		return this.getRange(low,  high);
	}
	
	
	/**
	 * getRange : int
	 * Recursively process the range and fetch the low and high summation
	 * @param low {int} - Start of the range being searched
	 * @param high {int} - End of teh range being searched
	 * @return Sum of frequencies of the values in range
	 */
	private int getRange(int low, int high) {
		// Single Range
		if(low == high) {
			//System.out.printf("\t%d ~ %d : %d\n", low, high,
			//		this.freqRange.tree[0].cell[low]);
			return this.freqRange.tree[0].cell[low];
		}
		
		// Convert Low to next factor of 2 (2^n)
		// Possible Optimisation - take the previous factor of 2 (2^n)
		// and use the closest of the two to collect the missing values
		if((low != 0) && ((low & (low - 1)) != 0)) {
			int newLow = (int) Math.ceil(Math.log(low) / Math.log(2));
			newLow = (int) Math.pow(2, newLow);
			int left = 0;
			// Collecitng missing values
			for( ; low < newLow ; low++) {
				left += this.freqRange.tree[0].cell[low];
			}
			int right = getRange(newLow, high);
			System.out.printf("\t%d ~ %d / %d ~ %d: %d, %d\n", 
					low, low,
					low + 1, high,
					left, right);
			return left + right;
		}
		
		// What is the largest factor 2 I can fit into the difference?
		int step = (int) (Math.log(high-low) / Math.log(2));
		// Where am I to start from?
		int start = (int) (low / Math.pow(2, step));
		
		// Debug Purpose variables and print statemetns
		int left = this.freqRange.tree[step].cell[start];
		int right = getRange((int)(low+Math.pow(2, step)), high);
		System.out.printf("\t STEP: %d, START: %d --- %d ~ %d / %d ~ %d: %d, %d\n", 
			step, start,
			low, (int)(low+Math.pow(2, step)),
			(int)(low+Math.pow(2, step)) + 1, high,
			left, right);

		return left + right;
	}
	
	/**
	 * shouldIncrease : boolean
	 * Using Count-Min-Log, the update happens on probability 
	 * based on the log base and current frequency
	 * @param currentFreq {int} - Current frequency
	 * @return True if update should occur
	 */
	private boolean shouldIncrease(int currentFreq) {
		return this.rand.nextDouble() < 1/Math.pow(this.logBase, currentFreq);
	}
	
	/**
	 * pointValue : float
	 * @param currentFreq {int} - current frequency
	 * @return 0 if current frequency is 0, else reverse log the current frequency - 1
	 */
	private double pointValue(int currentFreq) {
		return ((currentFreq==0) ? 0.00 : Math.pow(this.logBase, (float)currentFreq-1));
	}
	
	
	/**
	 * value : float
	 * @param currentFreq {int} - current frequency
	 * @return True Count
	 */
	private double value(int currentFreq) {
		if(currentFreq <= 1) { return this.pointValue(currentFreq); }
		double v = this.pointValue(currentFreq + 1);
		return (1-v) / (1-this.logBase);
	}
}