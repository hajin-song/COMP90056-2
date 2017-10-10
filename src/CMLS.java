import java.util.Random;

// CMLS.java
// CMLS Count Min Sketch for COMP90056
// Created By - Ha Jin Song
// Created On - Oct 2017
// Modified By - Ha Jin Song
// Last Modified - 10 Oct 2017
// Using Tutorial code listed on LMS as starting point

public class CMLS extends CMS{
	
	// Sadly, no easy way to get unsigned short max
	// apart from around about way of using char in byte format
	private int storageTypeMax;
	private float logBase;
	private Random rand;
	public CMLS(int n){
		super(n);
		this.rand = new Random();
		this.storageTypeMax = Short.MAX_VALUE;
		this.logBase = (float) 1.00025 ;
	}
	
	// ideally take Object
	// we'll stick with int items for now
	public void add(int x, int count){
		int currentFreq = T[0][hfs[0].hash(x)];
		for(int i=1;i<d;i++){
			currentFreq = Math.min(currentFreq, T[i][hfs[i].hash(x)]);
		}
		
		
		for(int freqCount = 0 ; freqCount < count ; freqCount++) {
			boolean updated = false;
			if(this.shouldIncrease(currentFreq)) {
				for(int i=0;i<d;i++){
					if(T[i][hfs[i].hash(x)] == currentFreq) {
						updated = true;
						T[i][hfs[i].hash(x)] += 1;
						//System.err.format("hash[%2d](%6d)=%5d%n",i,x,hfs[i].hash(x));
					}
				}
			}
			if(updated) {
				currentFreq += 1;
			}
		}
		
	}
	
	
	// count-min
	public int get(int y){
		int m = T[0][hfs[0].hash(y)];
		for(int i=1;i<d;i++){
			m = Math.min(m,T[i][hfs[i].hash(y)]);
		}
		return Math.round(this.value(m));
	}
	
	private boolean shouldIncrease(int currentFreq) {
		return this.rand.nextDouble() < 1/Math.pow(this.logBase, currentFreq);
	}
	
	private float pointValue(int count) {
		return (float) ((count==0) ? 0.00 : Math.pow(this.logBase, (float)count-1));
	}
	
	private float value(int count) {
		if(count <= 1) { return this.pointValue(count); }
		float v = this.pointValue(count + 1);
		return (1-v) / (1-this.logBase);
	}
}