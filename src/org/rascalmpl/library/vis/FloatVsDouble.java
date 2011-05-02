package org.rascalmpl.library.vis;

import java.util.Arrays;

import processing.core.PApplet;

public class FloatVsDouble {

	public static final int NR_TESTS = 50000000;
	public static final int NR_REPEATS = 50;
	
	static double testConv(){
		double[] testValues = new double[NR_TESTS];
		for(int i = 0 ; i < NR_TESTS ; i++){
			testValues[i] = Math.random();
		}
		
		float[] testValuesF = new float[NR_TESTS];
		System.out.print("Starting double 2 float convertions");
		long start = System.nanoTime();
		for(int i = 0 ; i < NR_TESTS ; i++){
			testValuesF[i] = (float)testValues[i];
		}
		long dur = System.nanoTime() - start;
		System.out.printf("Double took: %d\n", dur);
		double[] testValues2 = new double[NR_TESTS];
		System.out.print("Starting without convertions");
		start = System.nanoTime();
		for(int i = 0 ; i < NR_TESTS ; i++){
			testValues2[i] = testValues[i];
		}
		long dur2 = System.nanoTime() - start;
		System.out.printf("without took: %d\n", dur2);
		double diff =  (1.0 - (double)dur2   / (double)dur)*100;
		System.out.printf("without was %f  %% faster\n", diff);
		return diff;
		
	}
	
	static double testDiff(){
		double[] testValues = new double[NR_TESTS];
		for(int i = 0 ; i < NR_TESTS ; i++){
			testValues[i] = Math.random();
		}
		float[] testValuesF = new float[NR_TESTS];
		for(int i = 0 ; i < NR_TESTS ; i++){
			testValuesF[i] = (float)testValues[i];
		}
		System.out.print("Starting double calulations");
		long start = System.nanoTime();
		double tmp;
		for(int i = 0 ; i < NR_TESTS ; i++){
			tmp = Math.sin(testValues[i]);
			tmp += Math.sqrt(testValues[i]);
			tmp += Math.exp(testValues[i]);
			tmp += Math.log(testValues[i] + 1.0);
			tmp += Math.toRadians(testValues[i]);
		}
		long dur = System.nanoTime() - start;
		System.out.printf("Double took: %d\n", dur);
		System.out.print("Starting float calulations");
		float tmpF;
		start = System.nanoTime();
		for(int i = 0 ; i < NR_TESTS ; i++){
			tmpF = PApplet.sin(testValuesF[i]);
			tmpF += PApplet.sqrt(testValuesF[i]);
			tmpF += PApplet.exp(testValuesF[i]);
			tmpF += PApplet.log(testValuesF[i] + 1.0f);
			tmpF += PApplet.radians(testValuesF[i]);
		}
		long dur2 = System.nanoTime() - start;
		System.out.printf("float took: %d\n", dur2);
		double diff =  (1.0 - (double)dur2   / (double)dur)*100;
		System.out.printf("float was %f  %% faster\n", diff);
		return diff;
	}
	
	public static void main(String[] argv){
		double[] diff = new double[NR_REPEATS];
		for(int i = 0 ; i < NR_REPEATS ; i++){
			diff[i] = testConv();
		}
		Arrays.sort(diff);
		System.out.printf("Median is: %f", diff[NR_REPEATS/2-1]);
	}
}
