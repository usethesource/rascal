package org.rascalmpl.library.vis.util;

public enum Dimension { 
	X, Y; 
	
	public static Dimension flip(Dimension d){
		switch(d){
		case X: return Y;
		case Y: return X;
		default: return X;
		}
	}
	
	
	public static Dimension flip(boolean f,Dimension d){
		if(f){
			return flip(d);
		} else {
			return d;
		}
	}
}

