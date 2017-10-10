public class TestFrequencies{
	public static void main(String args[]){
		CMS c = new CMS(Integer.MAX_VALUE);
		CMLS cl = new CMLS(Short.MAX_VALUE);
		int maxx = 1000;
		for(int i=0;i<maxx;i++){
			cl.add(i,i);
		}
		for(int i=0;i<maxx;i++){
			System.out.format("fhat[%6d]=%6d%n", i,cl.get(i));
		}
	}
}