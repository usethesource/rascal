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

public final class Coordinate {
	
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
	
	public Coordinate(){
		this(0,0);
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
	
	public void setX(double val){
		x = val;
	}
	
	public void setY(double val){
		y = val;
	}

	public void setX(boolean flip, double val) {
		if(flip) y = val;
		else x = val;
	}
	
	public void setY(boolean flip, double val) {
		if(flip) x = val;
		else y = val;
	}
	
	public void set(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public void set(boolean flip,double x, double y){
		if(flip) set(y,x);
		else set(x,y);
	}
	
	public void set(Coordinate coordinate){
		this.x = coordinate.x;
		this.y = coordinate.y;
	}
	
	public void clear(){
		x = y = 0;
	}
	
	public void addX(boolean flip, double val){
		if(flip) y += val;
		else x += val;
	}
	
	public boolean isEq(Coordinate c){
		return x == c.x && y == c.y;
	}
	
	public void addY(boolean flip, double val){
		if(flip) x += val;
		else y += val;
	}
	
	public void addX( double val){
		x += val;
	}
	
	public void addY( double val){
		y += val;
	}
	
	public void add(Coordinate c){
		x += c.x;
		y += c.y;
	}
	
	public String toString(){
		return "x:"+x+"y:"+y;
	}
}
