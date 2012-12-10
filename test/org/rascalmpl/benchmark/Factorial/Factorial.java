/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.benchmark.Factorial;

import java.math.BigInteger;

/**
 * NOTE: You may not be testing what you thing you are testing (HotSpot may mess the test up).
 */
public class Factorial{
	
	public static int fac(int n){
	   if(n <= 1)
	   		return 1;
	   return n * fac(n - 1);
	}
	
	public static BigInteger BigFac(BigInteger n){
	   if(n.compareTo(BigInteger.ONE) <= 0)
	   		return BigInteger.ONE;
	   return n.multiply(BigFac(n.subtract(BigInteger.ONE)));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int n = 500;
		int iterations = 10000;
		
		// Warmup
		for(int i = 0; i < 20000; i++){
			 fac(n);
		}
		
		// Test Integer
		long start1 = System.currentTimeMillis();
		for(int i = 0; i < iterations; i++){
			fac(n);
		}
		long end1 = System.currentTimeMillis();

		// Warmup
		String nAsString = new Integer(n).toString();
		for(int i = 0; i < 20000; i++){
			 BigFac(new BigInteger(nAsString));
		}
		
		// Test Big Integer
		long start2 = System.currentTimeMillis();
		for(int i = 0; i < iterations; i++){
			 BigFac(new BigInteger(nAsString));
		}
		long end2 = System.currentTimeMillis();
		
		System.err.println(iterations+"x fac(" + n + ")    = " + fac(n) + " (" + (end1 - start1) + " millis)");
		System.err.println(iterations+"x BigFac(" + n + ") = " + BigFac(new BigInteger(nAsString)) + " (" + (end2 - start2) + " millis)");
	}
}
