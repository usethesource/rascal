/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.util;

public class MaxFontAscent {
	
	double maxDescent;
	double maxAscent;
	
	int lastComputeClock;
	
	public MaxFontAscent() {
		maxAscent = 0;
		maxDescent=0;
	}
	
	public void updateToClock(int computeClock){
		if(computeClock != lastComputeClock){
			System.out.printf("Resetting!\n");
			maxAscent = 0;
			maxDescent = 0;
			lastComputeClock = computeClock;
		}
	}
	
	public void set(double fontAscent,double fontDescent){
		System.out.printf("Registering %f %f\n",fontAscent,fontDescent);
		maxAscent = Math.max(maxAscent, fontAscent);
		maxDescent = Math.max(maxDescent,fontDescent);

	}
	
	public double getFontAscent(){
		return maxAscent;
	}
	
	public double getFontDescent(){
		return maxDescent;
	}
	

}
