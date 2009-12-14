package org.meta_environment.rascal.library.experiments.VL;

import processing.core.PApplet;

public class GraphNode {
	
	protected String name;
	protected VELEM velem;
	protected float x;
	protected float y;
	protected float dispx = 0f;
	protected float dispy = 0f;
	
	GraphNode(String name, VELEM velem){
		this.name = name;
		this.velem = velem;
	}
	
	public void relax(Graph G){
		
		dispx = dispy = 0f;
		
		for(GraphNode n : G.nodes){
			if(n != this){
				float deltax = x - n.x;
				float deltay = y - n.y;
				
				float dlen = PApplet.dist(x, y, n.x, n.y);
				
				if(dlen > 0.001f){
					float rep = G.repel(dlen);
					dispx += (deltax / dlen) * rep;
					dispy += (deltay / dlen) * rep;
				} else {
					dispx = (float) Math.random() * G.width;
					dispy =  (float) Math.random() * G.height;
				}
			}
		}
	}
	
	void update(Graph G){

		float dlen = PApplet.mag(dispx, dispy);

		x += (dispx / dlen) * PApplet.min(dispx, G.temperature);
		y += (dispy / dlen) * PApplet.min(dispy, G.temperature);

		x =  PApplet.constrain (x, velem.width/2, G.width-velem.width/2);
		y =  PApplet.constrain (y, velem.height/2, G.height-velem.height/2);
	}

	void draw() {
		velem.bbox();
		velem.draw(x - velem.width/2, y - velem.height/2);
	}
}
