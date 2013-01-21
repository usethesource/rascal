/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.util.vector;

public final class TransformMatrix {
	
	double x11,x12, x13, x21,x22, x23;
	double totalAngle;
	
	public TransformMatrix() { x11 = x22  = 1.0; x12 = x13 = x21 = x23 = 0.0;}
	
	public TransformMatrix(TransformMatrix b){
		x11 = b.x11;
		x12 = b.x12;
		x13 = b.x13;
		x21 = b.x21;
		x22 = b.x22;
		x23 = b.x23;
	}
	
	void translate(double x, double y){
		double tx, ty;
		tx = x11 * x + x12 * y + x13;
		ty = x21 * x + x22 * y + x23;
		x13 = tx;
		x23 = ty;
	}
	
	void rotate(double angle){ // in radians
		double r1 , r2;
		r1 = Math.cos(angle);
		r2 = Math.sin(angle);
		double tmp = x11 * r1 - x12 * r2;
		x12 = x11 * r2 + x12 * r1;
		x11 = tmp;
		tmp = x21 * r1 - x22 * r2;
		x22 = x21 * r2 + x22 * r1;
		x21 = tmp;
		totalAngle+=angle;
	}
	
	void scale(double x, double y){
		x11*=x;
		x21*=y;
		x21*=x;
		x22*=y;
	}
	
	void noRotate(){
		rotate(-totalAngle);
	}
	
	void noScale(){
		x11 = x22 = 1.0;
		x12 = x21 = 0.0;
		rotate(totalAngle);
	}
	
	void noScaleNoRotate(){
		x11 = x22 = 1.0;
		x12 = x21 = 0.0;
	}
	
	double getTotalAngle(){
		return totalAngle;
	}
	
	public void transformCoordinate(Coordinate c){
		double x,y;
		x = x11 * c.x + x12 * c.y + x13;
		y = x21 * c.x + x22 * c.y + x23;
		c.set(x, y);
	}
	
	public void transformBoundingBox(BoundingBox b){
		double w,h;
		w = Math.abs(x11) * b.getX() + Math.abs(x12) * b.getY()/2;
		h = Math.abs(x21) * b.getX() + Math.abs(x22) * b.getY()/2;
		b.set(w, h);
	}

	/* Shear is not used
	public void shearX(double x){
		x12+= x * x11;
		x22+= x* x21;
	}
	
	public void shearY(double y){
		x11+=y*x12;
		x21+=y*x22;
	}
	*/
}
