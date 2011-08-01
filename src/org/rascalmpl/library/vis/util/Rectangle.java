package org.rascalmpl.library.vis.util;

public final class Rectangle {

	private double x;
	private double y;
	private double width;
	private double height;
	private double xHigh;
	private double yHigh;
	
	public Rectangle(double x, double y , double width , double height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		xHigh = x + width;
		yHigh = y + height;
	}
	
	public Rectangle(int x, int y , int width , int height){
		this((double)x,(double)y,(double)width,(double)height);
	}
	
	public Rectangle(Coordinate c, BoundingBox b){
		this(c.getX(),c.getY(),b.getWidth(),b.getHeight());
	}
	
	public boolean contains(Rectangle rhs){
		return x <= rhs.x && xHigh >= rhs.xHigh && y <= rhs.y && yHigh >= rhs.yHigh;
	}
	
	public boolean contains(Coordinate location, BoundingBox size){
		return x <= location.getX() && xHigh >= location.getX() + size.getWidth() 
			&& y <= location.getY() && yHigh >= location.getY() + size.getHeight();
	}
	
	public boolean overlapsWith(Rectangle rhs){
		return !(xHigh <= rhs.x || x >= rhs.xHigh || yHigh <= rhs.y || y >= rhs.yHigh);
	}
	
	public boolean overlapWith(Coordinate location, BoundingBox size){
		return !(xHigh <= location.getX() || x >= location.getX() + size.getWidth() 
				|| yHigh <= location.getY() || y >= location.getY() + size.getHeight());
	}
	
	// assumption: they overlap, check with overlapsWith
	public Rectangle getOverlap(Rectangle rhs){
		double nx =  Math.max(x, rhs.x);
		double ny = Math.max(y, rhs.y);
		double nWidth = Math.min(xHigh, rhs.xHigh) - nx;
		double nHeight =  Math.min(yHigh, rhs.yHigh) - ny;
		return new Rectangle(nx,ny,nWidth,nHeight );
	}
	
	
	public double getX() { return x;}
	public double getY() { return y;}
	public double getX(boolean flip) { if(flip) return getY(); else return getX();}
	public double getWidth() { return width;}
	public double getHeight() { return height;}
	public double getWidth(boolean flip) { if(flip) return getHeight(); else return getWidth();}
	public double getXRight(){ return xHigh;}
	public double getYDown() { return yHigh; }
	public double getXRight(boolean flip) { if(flip) return getYDown(); else return getXRight();}
	
	
	public String toString(){
		return String.format("Rectangle(x:%f y:%f w: %f h: %f)",x,y,width,height);
	}
}
