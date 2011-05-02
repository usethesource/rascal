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

	private float minimum, maximum;
	
	public Extremes(float ... values){
		minimum = Float.MAX_VALUE;
		maximum = Float.MIN_VALUE;
		for(float val : values){
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
	
	private void processLow(float value){
		minimum = Figure.min(minimum, value);
	}
	
	private void processHigh(float value){
		maximum = Figure.max(maximum, value);
	}
	
	private void processValue(float value){
		processLow(value);
		processHigh(value);
	}
	
	public boolean gotData(){
		return minimum < maximum;
	}

	
	public float getMinimum(){
		return minimum;
	}
	
	public float getMaximum(){
		return maximum;
	}

}
