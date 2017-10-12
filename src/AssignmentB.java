import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

// AssignmentB.java
// Assignment B
// Created By - Ha Jin Song
// Created On - 13 Oct 2017
// Modified By - Ha Jin Song
// Last Modified - 13 Oct 2017



public class AssignmentB{
	public static void main(String args[]){
		Counter cl = new CMLS((int)(Math.pow(2, 30) - 1));
		Counter base = new CMS((int)(Math.pow(2, 30) - 1));
		//runDebugMode(cl);
		if(args.length == 0){
			System.err.println("No fileName argument.");
			System.exit(1);
		}
		run(cl, args[0]);
		//runDebugMode(cl, base);
	}
	public static void run(Counter c, String fileName) {
		Scanner scanner;
		
		try{
			File f = new File(fileName);
			scanner = new Scanner(f);
			String s;
			List<String> input;
			while(scanner.hasNextLine()){
				s = scanner.nextLine();
				input = new ArrayList<String>(Arrays.asList(s.replaceAll("\\s+",  " ").trim().split(" ")));
				if(input.get(0).equals("f")) {
					int value =  Integer.parseInt(input.get(1));
					System.out.format("%d\n",c.get(value));
				}else if(input.get(0).equals("r")) {
					int low = Integer.parseInt(input.get(1));
					int high = Integer.parseInt(input.get(2));
					System.out.format("%d\n", c.get(low, high));
				}else {
					int val = Integer.parseInt(input.get(0));
					int count = Integer.parseInt(input.get(1));
					c.add(val, count);
				}
				//System.out.println(input);
			}
			scanner.close();
	
		} catch (FileNotFoundException ex) {
			System.err.println("No file: "+fileName);
		}
	}
	public static void runDebugMode(Counter c, Counter base) {
		Random rand = new Random();
		int maxx = 100000;
		double cErr = 0.00;
		double baseErr = 0.00;
		
		//Used when testing run time duration
		long start = System.currentTimeMillis();

		for(int i=0;i<maxx;i++){
			c.add(i,i%10000);
			base.add(i, i);
		}
		
		
		int cResult;
		int baseResult;
		
		for(int j = 0 ; j < 1000 ; j++) {
			int val = rand.nextInt(maxx);
			cResult = c.get(val);
			baseResult = base.get(val);
			cErr += ((val%10000) - cResult) / (double)(val%10000);
			baseErr += (val - baseResult) / (double)val;
		}
		
		System.out.format("Time Elapsed: %d%n", System.currentTimeMillis() - start);
		System.out.format("Base: %.4f \n", baseErr);
		System.out.format("CMLS: %.4f \n", cErr);
		
		System.out.format("foofofo  %6d ~ %6d %n", c.get(200), c.get(300));
		System.out.format("foofofo  %6d ~ %6d = %6d%n", 9994, 15200, c.get(9994, 15200));
	}
}