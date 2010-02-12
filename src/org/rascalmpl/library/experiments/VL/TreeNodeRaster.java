package org.rascalmpl.library.experiments.VL;

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
	
	public void clear(){
		for(int i = 0; i < RMAX; i++)
			last[i] = 0;
	}
	
	private void extend(int n){
		n += n/5;
		int newLast[] = new int[n];
		for(int i = 0; i < n; i++){
			newLast[i] = i < RMAX ? last[i] : 0;
		}
		RMAX = n;
		last = newLast;
	}
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
	
	public float leftMostPosition(float position, float top, float width, float height, float gap){
		int itop = PApplet.round(top);
		float l = position;
		int ibot = PApplet.round(top + height);
		if(ibot > RMAX){
			extend(ibot);
		}
		for(int i = itop; i < ibot; i++){
			l = PApplet.max(l, last[i] + gap + width/2);
		}
		return l;
	}
		

}
