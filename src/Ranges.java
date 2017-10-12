// SolRanges.java
// ** SAMPLE ** solution code for week 08
// Not checked carefully, nor any particular guarantee
// awirth
// Sep/Oct 2017

public class Ranges{
	int levels; // not really used
	private int powers2[];
	private CMLS[] cs;
	private int TT=32;
	
	public Ranges(int n){
		powers2 = new int[TT];
		powers2[0] = 1;
		for(int i= 1;i<TT;i++){
			powers2[i] = powers2[i-1]*2;
		}
		cs = new CMLS[TT];
		for(int i=0;i<TT;i++){
			cs[i] = new CMLS(n);
		}
		
	}
	public void add(int x, int count){
		for(int j = 0;j<TT;j++){
			cs[j].add(x, count);
			x /= 2;
		}
	}
	
	/** For Yadeesha **/
	
	public int rangeFreq(int lo, int hi){	// upper bound non-incl
		int start;
		int freq=0;
		int diff,z;
		
		if(lo != 0){
			// largest power of two that is a factor of lo
			z = Integer.numberOfTrailingZeros(lo);
			start = lo/powers2[z];	// which interval of length 2^z
			while(lo + powers2[z] <= hi){				
				freq += cs[z].get(start);
				lo += powers2[z];
				z = Integer.numberOfTrailingZeros(lo);
				start = lo/powers2[z];
			}
		}
		// now have largest power of two, contracting again
		diff = hi - lo;
		// could use highestOneBit()
		for(z=0;z<TT;z++){
			if(powers2[z]> diff){
				break;
			}
		}
		z--;
		while(lo < hi){
			start = lo/powers2[z];
			//System.err.format("Contracting: querying level %6d start %6d lo %6d%n",z,start*powers2[z],lo);
			freq += cs[z].get(start);
		
			lo += powers2[z];
			diff = hi-lo;
			for(z=0;z<TT;z++){
				if(powers2[z]> diff){
					break;
				}
			}
			z--;
		}
		return freq;
	}
	
}
