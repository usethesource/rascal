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
