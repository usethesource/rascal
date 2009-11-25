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
		return new BoundingBox(getWidthProperty(), max(getHeightProperty(), getHeight2Property()));
	}

	@Override
	void draw(float x, float y) {
		this.x = x;
		this.y = y;
		applyProperties();
		int h1 = getHeightProperty();
		int h2 = getHeight2Property();
		float left = x - width/2;
		float right = x + width/2;
		float bottom = y + height/2;
		if(height > 0 && width > 0){
			vlp.rectMode(PConstants.CORNERS);
			vlp.quad(left, bottom, left,  bottom - h1, right, bottom - h2, right, bottom);
		}
	}

}
