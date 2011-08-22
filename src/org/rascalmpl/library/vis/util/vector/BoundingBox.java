/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Atze van der Ploeg - Atze.van.der.Ploeg@cwi.nl (CWI)
*******************************************************************************/

package org.rascalmpl.library.vis.util.vector;


public final class BoundingBox extends TwoDimensionalDouble{
	 
	public BoundingBox(double x, double y){
		super(x,y);
	}
	
	public BoundingBox(){
		super();
	}
	
	public BoundingBox(BoundingBox rhs){
		super(rhs);
	}


	public String toString(){
		return String.format("(w:%f h: %f)",getX(),getY());
	}
	
	public boolean contains(BoundingBox rhs){
		return x >= rhs.x && y >= rhs.y;
	}
}
