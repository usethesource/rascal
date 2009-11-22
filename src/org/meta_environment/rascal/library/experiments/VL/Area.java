package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PConstants;

public class Area extends VELEM {

	public Area(VLPApplet vlp, HashMap<String,IValue> inheritedProps, IList props, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);
	}
	
	@Override
	BoundingBox bbox(){
		return new BoundingBox(getWidth(), max(getHeight(), getHeight2()));
	}

	@Override
	void draw(int left, int bottom) {
		applyProperties();
		int h = getHeight();
		int h2 = getHeight2();
		int w = getWidth();
		int r = left + w;
		if(h > 0 && w > 0){
			vlp.rectMode(PConstants.CORNERS);
			vlp.quad(left, bottom, left,bottom - h, r, bottom - h2, r, bottom);
		}
	}

}
