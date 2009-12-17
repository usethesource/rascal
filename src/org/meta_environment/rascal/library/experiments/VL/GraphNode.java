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
		float deltax = 0;
		float deltay = 0;
		
	//	dispx = dispy = 0;
		
		for(GraphNode n : G.nodes){
			if(n != this){
				deltax = x - n.x;
				deltay = y - n.y;
				
				float dlensq = deltax * deltax + deltay * deltay;
				
				// Inline version of repel(dlen) = SpringCon^2/dlen
				
//				if(deltax < 5)
//					dispx += velem.vlp.random(10);
//				else
//					dispx += deltax * G.springConstant2 / dlensq;
//				if(deltay < 5)
//					dispy += velem.vlp.random(10);
//				 else
//					dispy += deltay * G.springConstant2 / dlensq;
				
				if(dlensq == 0){
					dispx += velem.vlp.random(1);
					dispy += velem.vlp.random(1);
				} else {
					dispx += deltax * G.springConstant2 / dlensq;
					dispy += deltay * G.springConstant2 / dlensq;
				}
			}
			float dlen = PApplet.mag(dispx, dispy);
			if(dlen > 0){
				dispx += deltax / dlen;
				dispy += deltay/ dlen;
			}
		}
		
		System.err.printf("Node %s, dispx = %f, dispy =%f\n", name, dispx, dispy);
	}
	
	void update(Graph G){
		
		x += PApplet.constrain(dispx, -G.temperature, G.temperature);
		y += PApplet.constrain(dispy, -G.temperature, G.temperature);
		x =  PApplet.constrain (x, velem.width/2, G.width-velem.width/2);
		y =  PApplet.constrain (y, velem.height/2, G.height-velem.height/2);
		
		dispx /= 2;
		dispy /= 2;
		
//		float dlen = PApplet.mag(dispx, dispy);
//		System.err.printf("update %s, dispx=%f, dispy=%f, dlen=%f", name, dispx, dispy, dlen);
//
//		if(dlen > 0){
//			
//			float dx = (dispx / dlen) * PApplet.min(dispx, G.temperature);
//			float dy = (dispy / dlen) * PApplet.min(dispy, G.temperature);
//			
//			//dx = PApplet.constrain(dx, -5, 5);
//			//dy = PApplet.constrain(dy, -5, 5);
//			
//            x += dx;
//            y += dy;
//            
//			x =  PApplet.constrain (x, velem.width/2, G.width-velem.width/2);
//			y =  PApplet.constrain (y, velem.height/2, G.height-velem.height/2);
//			System.err.printf(", dx=%f, dy=%f, x=%f, y=%f\n", dx, dy, x, y);
//		} else
//			System.err.printf(", dlen=0\n");
	}

	void draw() {
		velem.bbox();
		velem.draw(x - velem.width/2, y - velem.height/2);
	}
}
