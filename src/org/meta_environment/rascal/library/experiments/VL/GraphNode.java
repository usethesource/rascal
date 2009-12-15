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
				
				float dlensq = deltax * deltax + deltay * deltay;
				if(dlensq == 0){
					dispx += Math.random();
					dispy += Math.random();
				} else {
					dispx -= deltax * G.springConstant2 / dlensq;
					dispy -= deltay * G.springConstant2 / dlensq;
				}
			}
		
		}
	}
	
	void update(Graph G){
		float dlen = PApplet.mag(dispx, dispy);
		System.err.printf("update %s, dispx=%f, dispy=%f, dlen=%f", name, dispx, dispy, dlen);

		if(dlen > 0){
			
			float dx = (dispx / dlen) * PApplet.min(PApplet.abs(dispx), G.temperature);
			float dy = (dispy / dlen) * PApplet.min(PApplet.abs(dispy), G.temperature);
            x += dx;
            y += dy;
            
			x =  PApplet.constrain (x, velem.width/2, G.width-velem.width/2);
			y =  PApplet.constrain (y, velem.height/2, G.height-velem.height/2);
			System.err.printf(", dx=%f, dy=%f, x=%f, y=%f\n", dx, dy, x, y);
		} else
			System.err.printf(", dlen=0\n");
	}

	void draw() {
		velem.bbox();
		velem.draw(x - velem.width/2, y - velem.height/2);
	}
}
