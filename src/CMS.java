// CMS.java
// CMS Count Min Sketch for COMP90056
// Created By - awirth
// Created On - Oct 2017
// Modified By - Ha Jin Song
// Last Modified - 11 Oct 2017
// Using Tutorial code listed on LMS as starting point

public class CMS extends Counter{
	protected int[][] T;
	
	public CMS(int n){
		super(n);
		T = new int[d][w];
	}

	public void add(int x, int count){
		for(int i=0;i<d;i++){
			T[i][hfs[i].hash(x)]+=count;
			//System.err.format("hash[%2d](%6d)=%5d%n",i,x,hfs[i].hash(x));
		}
	}
	
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