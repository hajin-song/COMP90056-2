import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// CMLS.java
// CMLS Count Min Sketch
// Created By - Ha Jin Song
// Created On - Oct 2017
// Modified By - Ha Jin Song
// Last Modified - 11 Oct 2017
// Based on the paper provided and GoLang version of the implementation
// - https://github.com/seiflotfy/count-min-log


public class CMLS implements Counter{
	public Hash hfs[];
	public int d, w, m;
	public int n;
	public double accruacy = 1 - 0.9;
	public double badEstimate = 1 - 0.9;
	public short[][] T;
	
	public RangeTree freqRange;
	public int treeDepth;
	
		
	// Sadly, no easy way to get unsigned short max
	// apart from around about way of using char in byte format
	private double logBase;
	private Random rand;
	private int bucketSize;
	private int valueSize;
	
	
	public CMLS(int n){
		this.rand = new Random();
		this.logBase = 1.00025; //magic number from paper
		this.bucketSize = 12;
		this.valueSize = Integer.BYTES * 8 - bucketSize;
		int adjustedRange = Counter.getUpToNthBit(this.valueSize, n);
		
		this.n = n;
		// Making reference from Git repo above
		// m and d varies based on the expected size of the stream
		this.m = (int) Math.ceil((500000  * Math.log(accruacy)) / Math.log(1.0/Math.pow(2.0,  Math.log(2.0))));
		this.d = (int) Math.ceil(Math.log(2) * m / 500000);
		this.w = (int) (m/d) * 2;
		this.hfs = new Hash[d];
		for(int i=0;i<d;i++){
			hfs[i] = new Hash(adjustedRange,w);
		}
		
		this.T = new short[d][w];

		this.treeDepth = (int)Math.ceil((Math.log(adjustedRange) / Math.log(2)));
		this.freqRange = new RangeTree(adjustedRange, this.treeDepth, this.bucketSize);
		

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
		int bucketNumber = Counter.getFromNthBit(this.valueSize, x) % this.bucketSize;
		int listingNumber = Counter.getUpToNthBit(this.valueSize, x);
		//System.out.printf("\t\t%d   %d  // %d,  %d\n", bucketNumber, listingNumber, x, currentFreq);
		for(int j = 0 ; j < treeDepth ; j++) {
			this.freqRange.tree[j].cell[(int) (listingNumber/(Math.pow(2, j)))][bucketNumber] += this.value(currentFreq);
			//System.out.printf("\t\t\t\t%d   %d\n", j, this.freqRange.tree[j].cell[(int) (listingNumber/(Math.pow(2, j)))][bucketNumber]);
		}
		//System.out.printf("\t\t\t\t%d   %d\n", x, this.freqRange.tree[5].cell[(int) (listingNumber/(Math.pow(2, 5)))][0]);
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
		// Split things by bucket first
		List<int[]> buckets = new ArrayList<int[]>();
		int[] tempList = new int[2];
		int curBucket;
		tempList[0] = low;
		int prevBucket = Counter.getFromNthBit(this.valueSize, low) % this.bucketSize;
		// non-includsive
		for(int curVal = low + 1 ; curVal < high; curVal++) {
			curBucket = Counter.getFromNthBit(this.valueSize, curVal) % this.bucketSize;
			if(prevBucket != curBucket) {
				tempList[1] = curVal - 1;
				buckets.add(tempList);
				tempList = new int[2];
				tempList[0] = curVal;
				prevBucket = curBucket;
			}else if(curVal==high-1) {
				tempList[1] = curVal;
				buckets.add(tempList);
			}
		}
		int freq = 0;
		for(int[] bucket : buckets) {
			freq += this.getRange(bucket[0],  bucket[1]);
		}
		return freq;
	}
	
	/**
	 * getRange : int
	 * Recursively process the range and fetch the low and high summation
	 * @param low {int} - Start of the range being searched
	 * @param high {int} - End of teh range being searched
	 * @return Sum of frequencies of the values in range
	 */
	private int getRange(int low, int high) {

		if(low >= high) {
			//System.out.printf("\t%d ~ %d : %d\n", low, high,
			//		this.freqRange.tree[0].cell[low]);
			int bucketLow = Counter.getFromNthBit(this.valueSize, high) % this.bucketSize;
			int listingLow = Counter.getUpToNthBit(this.valueSize, high);
			return this.freqRange.tree[0].cell[listingLow][bucketLow];
		}
		if(low == 0) {
			int newLow = (int) Math.ceil(Math.log(1) / Math.log(2));
			int bucketLow = Counter.getFromNthBit(this.valueSize, low) % this.bucketSize;
			int listingLow = Counter.getUpToNthBit(this.valueSize, low);
			newLow = (int) Math.pow(2, newLow);
			int left = this.freqRange.tree[0].cell[listingLow][bucketLow];
			int right = getRange(newLow, high);
			//System.out.printf("\t TMATA ZERO %d ~ %d / %d ~ %d: %d, %d\n", 
			//		low, low,
			//		low, high,
			//		left, right);
			return left + right;
		}
		// Convert Low to next factor of 2 (2^n)
		// Possible Optimisation - take the previous factor of 2 (2^n)
		// and use the closest of the two to collect the missing values
		if((low != 0) && ((low & (low - 1)) != 0)) {
			int newLow = (int) Math.ceil(Math.log(low) / Math.log(2));
			newLow = (int) Math.pow(2, newLow);
			if(newLow > high) {
				newLow = high;
			}
			int left = 0;
			// Collecitng missing values
			for( ; low < newLow ; low++) {
				int bucketLow = Counter.getFromNthBit(this.valueSize, low) % this.bucketSize;
				int listingLow = Counter.getUpToNthBit(this.valueSize, low);
				left += this.freqRange.tree[0].cell[listingLow][bucketLow];
			}
			int right = getRange(newLow, high);
			//System.out.printf("\t TMATA %d ~ %d / %d ~ %d: %d, %d\n", 
			//		low, newLow-1,
			//		low, high,
			//		left, right);
			return left + right;
		}

		
		int bucket = Counter.getFromNthBit(this.valueSize, low) % this.bucketSize;
		int listingLow = Counter.getUpToNthBit(this.valueSize, low);
		int listinghigh = Counter.getUpToNthBit(this.valueSize, high);
		
		
		// What is the largest factor 2 I can fit into the difference?
		int step = (int) (Math.log(listinghigh-listingLow) / Math.log(2));
		// Where am I to start from?
		int start = (int) (listingLow / Math.pow(2, step));
		
		// Debug Purpose variables and print statemetns
		int left = this.freqRange.tree[step].cell[start][bucket];
		int right = getRange((int)(low+Math.pow(2, step)), high);
		//System.out.printf("\t STEP: %d, START: %d --- %d ~ %d / %d ~ %d: %d, %d\n", 
		//	step, start,
		//	low, (int)(low+Math.pow(2, step)) - 1,
		//	(int)(low+Math.pow(2, step)), high,
		//	left, right);

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