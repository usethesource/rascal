package org.rascalmpl.library.vis.util.vector;

import static org.rascalmpl.library.vis.util.vector.Dimension.*;

public abstract class TwoDimensionalDouble extends ReadOnlyTwoDimensionalDouble{
	
	double x, y;
	
	public TwoDimensionalDouble(){
		x = y = 0;
	}
	
	public TwoDimensionalDouble(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public TwoDimensionalDouble(TwoDimensionalDouble rhs) {
		this.x = rhs.x;
		this.y = rhs.y;
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public void setX(double d){
		x = d;
	}
	public void setY(double d){
		y = d;
	}
	
	public void set(double x, double y){
		setX(x);
		setY(y);
	}
	
	public void set(TwoDimensionalDouble d){
		setX(d.getX());
		setY(d.getY());
	}
	

	public void set(Dimension d, double val){
		switch(d){
		case X: setX(val); break;
		case Y: setY(val); break;
		}
	}
	
	// some convenience functions

	public void clear(){
		x = y = 0.0;
	}
	
	
	
	public void add(ReadOnlyTwoDimensionalDouble rhs){
		setX(rhs.getX() + getX());
		setY(rhs.getY() + getY());
	}

	
	public void setMax(ReadOnlyTwoDimensionalDouble rhs){
		setMax(X,rhs.getX());
		setMax(Y,rhs.getY());
	}
	
	public void setMax(Dimension d, double val){
		set(d, Math.max(val, get(d)));
	}
	
	public void setMin(Dimension d, double val){
		set(d, Math.min(val, get(d)));
	}
	
	public void setMinMax(Dimension d, double min, double max){
		set(d, Math.max(min,Math.min(max, get(d))));
	}
	
	public void add(Dimension d, double val){
		set(d, val + get(d));
	}

}
