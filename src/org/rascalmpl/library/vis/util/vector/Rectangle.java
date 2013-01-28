/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.util.vector;

public final class Rectangle{

	Coordinate location;
	BoundingBox size;
	Coordinate rightDown;
	
	public Rectangle(double x, double y , double width , double height){
		this(new Coordinate(x, y),new BoundingBox(width, height));
	}
	
	public Rectangle(int x, int y , int width , int height){
		this((double)x,(double)y,(double)width,(double)height);
	}
	
	public Rectangle(Coordinate c, BoundingBox b){
		location =c;
		size = b;
		rightDown = new Coordinate(location.getX() + size.getX(), location.getY() + size.getY());
	}
	
	public void update(){
		rightDown.setX(location.getX() + size.getX());
		rightDown.setY(location.getY() + size.getY());
	}
	
	public boolean contains(Rectangle rhs){
		return 	location.getX() <= rhs.location.getX() 
				&& rightDown.getX() >= rhs.rightDown.getX() 
				&& location.getY() <= rhs.location.getY()  
				&& rightDown.getY() >= rhs.rightDown.getY(); 
	}
	
	public boolean contains(Coordinate location, BoundingBox size){
		return contains(new Rectangle(location, size));
	}
	
	public boolean overlapsWith(Rectangle rhs){
		return !(
				rightDown.getX() <= rhs.location.getX() 
				|| location.getX() >= rhs.rightDown.getX() 
				|| rightDown.getY() <= rhs.location.getY() 
				|| location.getY() >= rhs.rightDown.getY() );
	}
	
	
	public boolean overlapsWith(Coordinate location, BoundingBox size){
		return overlapsWith(new Rectangle(location, size));
	}
	
	public Coordinate getLocation(){
		return location;
	}
	
	public BoundingBox getSize(){
		return size;
	}
	
	public Coordinate getRightDown(){
		return rightDown;
	}
	
	public String toString(){
		return String.format("Rectangle(x:%f y:%f w: %f h: %f)",location.getX(),location.getY(),size.getX(),size.getY());
	}
}
