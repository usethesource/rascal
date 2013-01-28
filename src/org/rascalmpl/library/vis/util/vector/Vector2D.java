/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.util.vector;

public class Vector2D  {

	private double x;
	private double y;

	public Vector2D(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public Vector2D(Vector2D V){
		this.x = V.x;
		this.y = V.y;
	}
	
	public Vector2D(){
		x = 0;
		y = 0;
	}
	

	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public void setX(double d) {
		x = d;
	}
	
	public void setY(double d) {
		y = d;
	}
	

	public double angle(Vector2D other){
		//Vector2D n1 = new Vector2D(this).normalize();
		//Vector2D n2 = new Vector2D(other).normalize();
		//return Math.acos(n1.dot(n2));
		double a = Math.atan2(other.y, other.x) - Math.atan2(y, x);
		if(a < 0)
			a += 2 * Math.PI;
		return a;
		
//		double dotProduct = this.dot(other);
//		double len1 = length();
//		double len2 = other.length();
//		double denom = len1 * len2;
//		double product = denom != 0 ? dotProduct / denom : 0;
//		return Math.acos(product);
		
	}
	
	public double dot(Vector2D other){
		return x * other.x + y * other.y;
	}
	
	public Vector2D add(Vector2D other){
		x += other.x;
		y += other.y;
		return this;
	}
	
	public Vector2D sub(Vector2D other){
		x -= other.x;
		y -= other.y;
		return this;
	}
	
	public Vector2D negate(){
		x = -x;
		y = -y;
		return this;
	}
	
	public Vector2D mul(Vector2D other){
		x *= other.x;
		y *= other.y;
		return this;
	}
	
	public Vector2D mul(double d){
		x *= d;
		y *= d;
		return this;
	}
	
	public Vector2D div(double d){
		if(d != 0){
			x /= d;
			y /= d;
		}
		return this;
	}
	
	public Vector2D div(Vector2D other){
		if(other.x != 0)
			x /= other.x;
		if(other.y != 0)
			y /= other.y;
		return this;
	}
	
	public double length(){
		return  Math.sqrt(x * x + y * y);
	}
	
	public Vector2D normalize(){
		
		//System.err.print("normalize: (" + x + " ," + y + ")");
		double len = length();
		if(len > 0){
			x /= len;
			y /= len;
		}
		//System.err.println("len = " + len + ";  => (" + x + " ," + y + ")");
		return this;
	}
	
	public double distance(Vector2D other){
		return Math.sqrt(distance2(other));
	}
	
	public double distance2(Vector2D other){
		double dx = x - other.getX();
		double dy = y - other.getY();
		return dx * dx + dy * dy;
	}
	
	public String toString(){
		return "(" + x + ", " + y + ")";
	}

	
}
