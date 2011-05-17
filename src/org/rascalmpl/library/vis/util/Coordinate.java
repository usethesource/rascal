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

package org.rascalmpl.library.vis.util;

public class Coordinate {
	
	private  double x,y;
	
	public Coordinate(double x, double y,boolean flip){
		if(flip){
			this.x = y;
			this.y = x;
		} else {
			this.x = x;
			this.y = y;
		}
	}
	
	public Coordinate(double x, double y){
		this(x,y,false);
	}
	
	public double getX(){ return x; }
	public double getY() { return y; }
	public double getX(boolean flip) {
		if(flip) return y;
		else return x;
	}
	public double getY(boolean flip) {
		if(flip) return x;
		else return y;
	}
	
	public double getDimension(Dimension d){
		switch(d){
		case X: return x;
		case Y: return y;
		default: return 0;
		}
	}
	

}
