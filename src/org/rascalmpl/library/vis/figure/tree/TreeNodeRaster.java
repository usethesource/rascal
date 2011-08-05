/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.vis.figure.tree;

import org.rascalmpl.library.vis.util.FigureMath;


/**
 * Auxiliary class for Tree layout.
 * 
 * @author paulk
 *
 */
public class TreeNodeRaster {
	int last[];
	int RMAX = 1000;
	
	TreeNodeRaster(){
		last = new int[RMAX];
		for(int i = 0; i < RMAX; i++)
			last[i] = 0;
	}
	
	/*
	 * Clear -- reset the node raster
	 */
	public void clear(){
		for(int i = 0; i < RMAX; i++)
			last[i] = 0;
	}
	
	/*
	 * Extend -- enlarge the node raster
	 */
	
	private void extend(int n){
		n += n/5;
		int newLast[] = new int[n];
		for(int i = 0; i < n; i++){
			newLast[i] = i < RMAX ? last[i] : 0;
		}
		RMAX = n;
		last = newLast;
	}
	
	/*
	 * Add -- add an element to the raster
	 */
	public void add(double position, double top, double width, double height){
		int itop = FigureMath.round(top);
		int ibot = FigureMath.round(top + height);
		if(ibot > RMAX){
			extend(ibot);
		}
		int l = FigureMath.round(position + width/2);
		for(int i = itop; i < ibot; i++){
			last[i] = l;
		}
	}
	
	/*
	 * leftMostPosition -- place an element at the left most position that is possible
	 * Returns the x position of the center of the element
	 */
	
	public double leftMostPosition(double position, double top, double width, double height, double gap){
		int itop = FigureMath.round(top);
		double l = position >= 0 ? position : 0;
		int ibot = FigureMath.round(top + height);
		if(ibot > RMAX){
			extend(ibot);
		}
		for(int i = itop; i < ibot; i++){
			l = FigureMath.max(l, (last[i] == 0) ? width/2 : last[i] + gap + width/2);
//			l = FigureApplet.max(l, last[i] + gap + width/2);
		}
		return l;
	}
		

}
