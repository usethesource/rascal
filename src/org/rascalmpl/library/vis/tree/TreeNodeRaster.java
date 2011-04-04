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
package org.rascalmpl.library.vis.tree;

import processing.core.PApplet;

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
	public void add(float position, float top, float width, float height){
		int itop = PApplet.round(top);
		int ibot = PApplet.round(top + height);
		if(ibot > RMAX){
			extend(ibot);
		}
		int l = PApplet.round(position + width/2);
		for(int i = itop; i < ibot; i++){
			last[i] = l;
		}
	}
	
	/*
	 * leftMostPosition -- place an element at the left most position that is possible
	 * Returns the x position of the center of the element
	 */
	
	public float leftMostPosition(float position, float top, float width, float height, float gap){
		int itop = PApplet.round(top);
		float l = position >= 0 ? position : 0;
		int ibot = PApplet.round(top + height);
		if(ibot > RMAX){
			extend(ibot);
		}
		for(int i = itop; i < ibot; i++){
			l = PApplet.max(l, (last[i] == 0) ? width/2 : last[i] + gap + width/2);
//			l = PApplet.max(l, last[i] + gap + width/2);
		}
		return l;
	}
		

}
