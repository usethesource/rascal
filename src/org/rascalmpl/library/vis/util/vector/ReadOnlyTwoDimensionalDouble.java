/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
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
