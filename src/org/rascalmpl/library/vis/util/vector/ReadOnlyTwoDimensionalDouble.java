package org.rascalmpl.library.vis.util.vector;

public abstract class ReadOnlyTwoDimensionalDouble {
	
	public abstract double getX();
	
	public abstract double getY();
	
	public double get(Dimension d){
		switch(d){
		case X: return getX();
		case Y: return getY();
		}
		return 0;
	}
	
	public boolean isEq(ReadOnlyTwoDimensionalDouble rhs){
		return rhs!=null && getX() == rhs.getX() && getY() == rhs.getY();
	}
}
