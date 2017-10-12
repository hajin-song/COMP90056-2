// Counter.java
// Abstract Counter Object
// Created By - Ha Jin Song
// Created On - 11 Oct 2017
// Modified By - Ha Jin Song
// Last Modified - 11 Oct 2017
// Using Tutorial code listed on LMS as starting point


public interface Counter {
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

	public static int zeros(int v){
		return Integer.numberOfTrailingZeros(v);
	}
	
	/**
	 * leftOne : Get the most significant 1-bit of given value
	 * @param v : {int} value being checked
	 * @return {int} most significant 1-bit of the value
	 */
	public static int leftOne(int v) {
		return Integer.numberOfLeadingZeros(v) ;
	}

	/**
	 * ones : Get number of trailing ones of given value
	 * @param v : {int} value being checked
	 * @return {int} number of consecutive 1s from the right hand side
	 */
	public static int ones(int v) {
		return Integer.numberOfTrailingZeros(v ^ Integer.MAX_VALUE);
	}

	/**
	 * getUpToNthBit : From current value, get upto N value only
	 * @param n : {int} bit limit
	 * @param v : {int} value being checked
	 * @return {int} chopped off value
	 */
	public static int getUpToNthBit(int n, int v) {
		return v & ((1 << n) - 1);
		// return v & ((int)Math.pow(2, n) - 1); <- This made the loop go for extra 10 seconds in the third trial!
	}
	
	
	/**
	 * getFromNthBit : From current value, get from N value only
	 * @param n : {int} bit start position
	 * @param v : {int} value being checked
	 * @return {int} chopped off value
	 */
	public static int getFromNthBit(int n, int v) {
		return v >> n;
	}
}
