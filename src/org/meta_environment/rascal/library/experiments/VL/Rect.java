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
		width = getWidth();
		height = getHeight();
		System.err.println("rect: width=" + width + ", height=" + height);
	}
	
	@Override
	BoundingBox bbox(){
		width = getWidth();
		height = getHeight();
		return new BoundingBox(width, height);
	}

	@Override
	void draw(int l, int b) {
		applyProperties();
		left = l;
		bottom = b;
		width = getWidth();
		height = getHeight();
		System.err.println("rect: left=" + left + ", bottom=" + bottom + ", width=" + width + ", height=" + height);
		if(height > 0 && width > 0){
			vlp.rectMode(PConstants.CORNERS);
			vlp.rect(left, bottom-height, left + width, bottom);
		}
	}
	
	@Override
	public void mouseOver(int x, int y){
		System.err.println("MouseOver in rect: " + left  + ", " + bottom);
		if((x > left && x < left + width) &&
		   (y > bottom  && y < bottom - height)){
		   System.err.println("MouseOver in rect: " + left  + ", " + bottom);
		}
	}

}
