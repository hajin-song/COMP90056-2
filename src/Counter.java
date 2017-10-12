// Counter.java
// Abstract Counter Object
// Created By - Ha Jin Song
// Created On - 11 Oct 2017
// Modified By - Ha Jin Song
// Last Modified - 11 Oct 2017
// Using Tutorial code listed on LMS as starting point


public abstract class Counter {
	protected Hash hfs[];
	protected int d, w, m;
	protected int n;
	protected double accruacy = 1 - 0.9;
	protected double badEstimate = 1 - 0.9;
	protected int[][] T;
	
	protected RangeTree freqRange;
	protected int treeDepth;
	
	public Counter(int n){
		this.n = n;
		this.m = (int) Math.ceil((10000  * Math.log(accruacy)) / Math.log(1.0/Math.pow(2.0,  Math.log(2.0))));
		this.d = (int) Math.ceil(Math.log(2) * m / 10000);
		this.w = (int) (m/d);
		hfs = new Hash[d];
		for(int i=0;i<d;i++){
			hfs[i] = new Hash(n,w);
		}
	}
	
	/**
	 * add : void
	 * @param x {int} - Value being added
	 * @param count {int} - Frequency of value being added
	 */
	public abstract void add(int x, int count);
	
	/**
	 * get : int
	 * @param y {int} - Value being searched
	 * @return Frequency of the value calculated by the Counter
	 */
	public abstract int get(int y);
	
	/**
	 * get : int
	 * @param low {int} - Start of the range being searched
	 * @param high {int} - End of teh range being searched
	 * @return Sum of frequencies of the values in range
	 */
	public abstract int get(int low, int high);

	protected static int zeros(int v){
		return Integer.numberOfTrailingZeros(v);
	}
	
	/**
	 * leftOne : Get the most significant 1-bit of given value
	 * @param v : {int} value being checked
	 * @return {int} most significant 1-bit of the value
	 */
	protected static int leftOne(int v) {
		return Integer.numberOfLeadingZeros(v) ;
	}

	/**
	 * ones : Get number of trailing ones of given value
	 * @param v : {int} value being checked
	 * @return {int} number of consecutive 1s from the right hand side
	 */
	protected static int ones(int v) {
		return Integer.numberOfTrailingZeros(v ^ Integer.MAX_VALUE);
	}

	/**
	 * getUpToNthBit : From current value, get upto N value only
	 * @param n : {int} bit limit
	 * @param v : {int} value being checked
	 * @return {int} chopped off value
	 */
	protected static int getUpToNthBit(int n, int v) {
		return v & ((1 << n) - 1);
		// return v & ((int)Math.pow(2, n) - 1); <- This made the loop go for extra 10 seconds in the third trial!
	}
	
	
	/**
	 * getFromNthBit : From current value, get from N value only
	 * @param n : {int} bit start position
	 * @param v : {int} value being checked
	 * @return {int} chopped off value
	 */
	protected static int getFromNthBit(int n, int v) {
		return v >> n;
	}
}
