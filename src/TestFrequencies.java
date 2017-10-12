public class TestFrequencies{
	public static void main(String args[]){
		CMS c = new CMS(Integer.MAX_VALUE);
		CMLS cl = new CMLS(10000);
		int maxx = 10000;
		for(int i=0;i<maxx;i++){
			cl.add(i,i);
			//c.add(i,i);
		}
		for(int i=0;i<maxx;i++){
			System.out.format("fhat[%6d]=%6d %n", i,cl.get(i));
		}
		
		System.out.format("foofofo  %6d ~ %6d %n", cl.get(200), cl.get(300));
		System.out.format("foofofo  %6d ~ %6d = %6d%n", 41, 160, cl.get(41, 160));
	}
}