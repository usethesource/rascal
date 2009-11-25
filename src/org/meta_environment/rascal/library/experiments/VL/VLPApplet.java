package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PApplet;

public class VLPApplet extends PApplet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6074377218243765483L;
	
	private int width = 600;
	private int height = 600;
	private VELEM  velem;
	private HashMap<String,Part> registered;

	public VLPApplet(IConstructor elem, IEvaluatorContext ctx){
		registered = new HashMap<String,Part>();
		this.velem = VELEMFactory.make(this, elem, null, ctx);
	}
	
	public void register(String name, Part nd){
		registered.put(name, nd);
	}
	
	public Part getRegistered(String name){
		return registered.get(name);
	}
	
	public void connect(String from, String to){
		
	}

	@Override
	public void setup(){
		size(width, height);
		textFont(createFont("Helvetica", 12));
		smooth();
		//noLoop();
	}
	
	@Override
	public void draw(){
		background(255);
		BoundingBox bb = velem.bbox();
		velem.draw(bb.getWidth()/2, height - bb.getHeight()/2 - 30);
	}
	
	@Override
	public void mouseMoved(){
		System.err.println("mouseMoved: " + mouseX + ", " + mouseY);
		velem.mouseOver(mouseX, mouseY);
	}
	
	@Override
	public void mousePressed(){
		System.err.println("mousePressed: " + mouseX + ", " + mouseY);
		velem.mouseOver(mouseX, mouseY);
	}
	
	/*
	@Override
	public void draw(){
		int left = 0;
		int bottom = height;
		
		int deltax = 0;
		int deltay = 0;
		
		rectMode(CORNERS);
		for(int i = 0; i < nmax; i++){
			bottom = height;
			for(VELEM ve : velems){
				if(ve.draw(this, i, left, bottom)){
					BoundingBox bb = ve.bbox(i);
					BoundingBox ibb = ve.innerBbox(i);
					deltax = max(deltax, ibb.getWidth());
					deltay = max(deltay, ibb.getHeight());
					if(hcomposition)
						left += bb.getWidth();
					else
						bottom -= ibb.getHeight();
				}
			}
			if(!hcomposition)
				left += deltax + gap;
		}
	}
	*/
/*
	@Override
	public void draw(){
		//stroke(255);
		//background(192, 64, 0);
		//rect(25, 0, 50, 80);
		//line(150, 25, mouseX, mouseY);
		for(VELEM ve : velems){
			ve.draw(this);
		}
	}
*/
}