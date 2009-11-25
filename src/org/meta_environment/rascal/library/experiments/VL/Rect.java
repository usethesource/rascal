package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PConstants;

public class Rect extends VELEM {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Rect(VLPApplet vlp, HashMap<String,IValue> inheritedProps, IList props, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);
		width = getWidthProperty();
		height = getHeightProperty();
		System.err.println("rect: width=" + width + ", height=" + height);
	}
	
	@Override
	BoundingBox bbox(){
		width = getWidthProperty();
		height = getHeightProperty();
		return new BoundingBox(width, height);
	}

	@Override
	void draw(float x, float y) {
		applyProperties();
		this.x = x;
		this.y = y;
		System.err.println("rect: x=" + x + ", y=" + y + ", width=" + width + ", height=" + height);
		if(height > 0 && width > 0){
			vlp.rectMode(PConstants.CENTER);
			vlp.rect(x, y, width, height);
		}
	}
	
	@Override
	public void mouseOver(int mousex, int mousey){
		System.err.println("MouseOver in rect: " + x  + ", " + y);
		if((mousex > x - width/2 && mousex < x + width/2) &&
		   (mousey > y - height/2  && mousey < y + height/2)){
		   System.err.println("MouseOver in rect: " + x  + ", " + y);
		}
	}

}
