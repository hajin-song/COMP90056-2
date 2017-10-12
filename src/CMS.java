public class CMS implements Counter{
	private int[][] T; // Yan is happy
	private Hash hfs[];
	private int d=12,w=500000;
	
	private int n;
	
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
			//System.err.format("hash[%2d](%6d)=%5d%n",i,x,hfs[i].hash(x));
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
	@Override
	public int get(int low, int high) {
		// TODO Auto-generated method stub
		return 0;
	}
}