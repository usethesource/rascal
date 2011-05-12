package org.rascalmpl.library.vis.util;

public class BoundingBox {
	
	private double width, height;
	
	public BoundingBox(double width,double height,boolean flip){
		if(flip){
			this.width = height;
			this.height = width;
		} else {
			this.width = width;
			this.height = height;
		}
	}
	
	public BoundingBox(double width,double height){
		this(width,height,false);
	}
	
	
	public double getWidth(){
		return width;
	}
	
	public double getHeight(){
		return height;
	}
	
	public double getWidth(boolean flip){
		if(flip) return height;
		else return width;
	}
	
	public double getHeight(boolean flip){
		if(flip) return width;
		else return height;
	}
	
	public double getDimension(Dimension d){
		switch(d){
		case X: return width;
		case Y: return height;
		default: return 0;
		}
	}

}
