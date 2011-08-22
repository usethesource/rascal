package org.rascalmpl.library.vis.util.vector;

public enum Dimension {
	
	X, Y;

	public static final Dimension[] HOR_VER = {X,Y};
	
	public Dimension other(){
		if(this == X){
			return Y;
		} else {
			return X;
		}
	}

}
