package org.meta_environment.rascal.library.experiments.VL;

import java.util.List;

import processing.core.PApplet;

public class Part {
	
	protected String name;
	protected VELEM velem;
	boolean fixed = false;
	protected float x;
	protected float y;
	protected float dx = 0f;
	protected float dy = 0f;
	
	Part(String name, VELEM velem){
		this.name = name;
		this.velem = velem;
	}
	
	public void relax(List<Part> parts){
		float ddx = 0;
		float ddy = 0;
		
		for(Part n : parts){
			if(n != this){
				float vx = x - n.x;
				float vy = y - n.y;
				float lensq = vx * vx + vy * vy;
				if(lensq == 0){
					ddx += Math.random();
					ddy += Math.random();
				} else if(lensq < 100*100){
					ddx += vx / lensq;
					ddy += vy / lensq;
				}
			}
		}
		float dlen = PApplet.mag(ddx, ddy) / 2;
		if(dlen > 0){
			dx += ddx / dlen;
			dy += ddy / dlen;
		}
	}
	
	void update(){
		if(!fixed){
			x += PApplet.constrain (dx, -5, 5);
			y += PApplet.constrain (dy, -5, 5);
			
			x =  PApplet.constrain (x, 0, 600);
			y =  PApplet.constrain (y, 0, 600);
		}
		dx /= 2;
		dy /= 2;
	}

	void draw() {
		velem.bbox();
		int l = PApplet.round(x - velem.width/2);
		int b = PApplet.round(y + velem.height/2);
		velem.draw(l, b);
	}
}
