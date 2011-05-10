/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Atze van der Ploeg - Atze.van.der.Ploeg@cwi.nl (CWI)
*******************************************************************************/

package org.rascalmpl.library.vis;

// immutable extremes (minimum and maximum) helper class
public class Extremes {

	private double minimum, maximum;
	
	public Extremes(double ... values){
		minimum = Double.MAX_VALUE;
		maximum = Double.MIN_VALUE;
		for(double val : values){
			processValue(val);
		}
	}
	
	static public Extremes merge(Extremes ... extremesList){
		Extremes result = new Extremes();
		for(Extremes extremes : extremesList){
			result.processLow(extremes.minimum);
			result.processHigh(extremes.maximum);
		}
		return result;
	}
	
	private void processLow(double value){
		minimum = Figure.min(minimum, value);
	}
	
	private void processHigh(double value){
		maximum = Figure.max(maximum, value);
	}
	
	private void processValue(double value){
		processLow(value);
		processHigh(value);
	}
	
	public boolean gotData(){
		return minimum < maximum;
	}

	
	public double getMinimum(){
		return minimum;
	}
	
	public double getMaximum(){
		return maximum;
	}

}
