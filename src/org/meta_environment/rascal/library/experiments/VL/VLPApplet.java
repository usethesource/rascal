package org.meta_environment.rascal.library.experiments.VL;

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

	VLPApplet(IConstructor elem, IEvaluatorContext ctx){
		this.velem = VELEMFactory.make(elem, null, ctx);
	}

	@Override
	public void setup(){
		size(width, height);
		//noLoop();
	}
	
	@Override
	public void draw(){
		velem.draw(this, 0, height);
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