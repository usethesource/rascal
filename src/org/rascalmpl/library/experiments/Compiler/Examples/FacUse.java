package org.rascalmpl.library.experiments.Compiler.Examples;

import org.rascalmpl.interpreter.utils.Timing;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class FacUse  {
	
	static long jfac(long n) {
		return n <= 1 ? 1 : n * jfac(n - 1);
	}
	
	static long do_jfac(int n, int m){
		long before = Timing.getCpuTime();
		for(int i = 0; i < m; i++){
			jfac(n);
		}
		long t = (Timing.getCpuTime() - before)/1000;
		System.out.println("jfac: " +  t + " nsec");
		return t;
	}
	
	static long do_rfac(int n, int m){
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		Fac myFac = new Fac(vf);
		long before = Timing.getCpuTime();
		for(int i = 0; i < m; i++){
			myFac.fac(vf.integer(n));
		}
		long t = (Timing.getCpuTime() - before)/1000;
		System.out.println("rfac: " + t + " nsec");
		return t;
	}
	
	public static void main(String[] args) {
		int n = 20;
		
		boolean use1 = false;

		if(use1){
			IValueFactory vf = ValueFactoryFactory.getValueFactory();
			
			Fac myFac = new Fac(vf);					// <===
			
			long before = Timing.getCpuTime();
			
			IInteger result = myFac.fac(vf.integer(n));	// <===

			System.out.println("result = " + result + "\n" + (Timing.getCpuTime() - before)/1000 + " nsec");
			before = Timing.getCpuTime();
			System.out.println("jfac: " + jfac(n)+ "\n" + (Timing.getCpuTime() - before)/1000 + " nsec");
		} else {
			long jtime = do_jfac(n, 1000000);
			long rtime = do_rfac(n, 1000000);
			System.out.println("java speedup: " + rtime/jtime);
		}
	}
}
