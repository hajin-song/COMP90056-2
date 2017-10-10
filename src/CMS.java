// CMS.java
// CMS Count Min Sketch for COMP90056
// Created By - awirth
// Created On - Oct 2017
// Modified By - Ha Jin Song
// Last Modified - 10 Oct 2017
// Using Tutorial code listed on LMS as starting point

public class CMS{
	protected int[][] T; // Yan is happy
	protected Hash hfs[];
	protected int d=5,w=211;
	
	protected int n;
	
	public CMS(int n){
		this.n = n;
		T = new int[d][w]; // in Java arrays
			// are initialized by default to 0
		hfs = new Hash[d];
		for(int i=0;i<d;i++){
			hfs[i] = new Hash(n,w);
		}
	}
	// ideally take Object
	// we'll stick with int items for now
	public void add(int x, int count){
		for(int i=0;i<d;i++){
			T[i][hfs[i].hash(x)]+=count;
			System.err.format("hash[%2d](%6d)=%5d%n",i,x,hfs[i].hash(x));
		}
	}
	// count-min
	public int get(int y){
		int m = T[0][hfs[0].hash(y)];
		for(int i=1;i<d;i++){
			m = Math.min(m,T[i][hfs[i].hash(y)]);
		}
		return m;
	}
	
	public int get(int low, int high) {
		int freq = 0;
		while(low < high) {
			freq += get(low);
			low+=1;
		}
		return freq;
	}
}