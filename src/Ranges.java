public class Ranges{
	int levels;
	int powers2[];
	CMS[] cs;
	int TT=32;
	
	public Ranges(int n){
		powers2 = new int[TT];
		powers2[0] = 1;
		for(int i= 1;i<TT;i++){
			powers2[i] = powers2[i-1]*2;
		}
		cs = new CMS[TT];
		for(int i=0;i<TT;i++){
			cs[i] = new CMS(n);
		}
		
	}
	public void add(int x, int count){
		for(int j = 0;j<TT;j++){
			cs[j].add(x, count);
			x /= 2;
		}
	}
}