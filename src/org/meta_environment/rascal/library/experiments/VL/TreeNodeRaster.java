package org.meta_environment.rascal.library.experiments.VL;

import processing.core.PApplet;

class Range {
	float from;
	float to;
	float right;
	Range(float f, float t){
		from = f;
		to = t;
		right = 0;
	}
}

public class TreeNodeRaster {
	static int last[];
	
	
	TreeNodeRaster(){
		last = new int[1000];
		for(int i = 0; i < 1000; i++)
			last[i] = 0;
	}
	
	public void add(TreeNode tn){
		int itop = PApplet.round(tn.top);
		int ibot = PApplet.round(tn.top + tn.height);
		int l = PApplet.round(tn.left + tn.width);
		for(int i = itop; i < ibot; i++){
			last[i] = l;
		}
	}
	
	public float leftMostPosition(float left, float top, float width, float height,  float gap){
		int itop = PApplet.round(top);
		float l = left;
		int ibot = PApplet.round(top + height);
		for(int i = itop; i < ibot; i++){
			l = PApplet.max(l, last[i] + gap + width/2);
		}
		return l;
	}
		

}
