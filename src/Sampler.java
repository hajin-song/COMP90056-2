// Sampler.java
// Generate Random Values for COMP90056
// Created By - awirth
// Created On - Oct 2017
// Using Tutorial code listed on LMS

import java.util.Random;

public class Sampler {

	// Elements so far
	private int m;
	// Currently Sampled element
	private int current;
	
	// Random Number generator for sample decision
	private Random rand;
	
	public Sampler() {
		this.m = 0;
		this.current = 0;
		this.rand = new Random();
	}
	
	public void Sample(int next) {
		float probSample = this.rand.nextFloat();
		float threshold = (float)this.m / (this.m + 1);
		if(probSample > threshold) {
			this.current = next;
		}
		this.m += 1;
	}
}
