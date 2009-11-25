package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;


public class Overlay extends Compose {

	Overlay(VLPApplet vlp, HashMap<String,IValue> inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, elems, ctx);
	}
	
	@Override
	BoundingBox bbox(){
		width = 0;
		height = 0;
		for(VELEM ve : velems){
			BoundingBox bb = ve.bbox();
			height = max(height, bb.getHeight());
			width = max(width, bb.getWidth());
		}
		System.err.printf("overlay.bbox: width=%f, height=%f\n", width, height);
		return new BoundingBox(width, height);
	}
	
	@Override
	void draw(float x, float y) {
		this.x = x;
		this.y = y;
		applyProperties();
		float left = x - width/2;
		float top = y - height/2;
		System.err.printf("overlay.draw: x=%f, y=%f, left=%f, top=%f", x, y, left, top);
		for(VELEM ve : velems){
			float vex;
			float vey;
			BoundingBox bb = ve.bbox();
			
			if(isRight())
				vex = left + (width - bb.getWidth()/2);
			else if(isLeft())
				vex = left + bb.getWidth()/2;
			else
				vex = left + (width - bb.getWidth())/2 + bb.getWidth()/2;
			
			if(isTop())
				vey = top + bb.getHeight()/2;
			else if (isBottom())
				vey = top + (height - bb.getHeight()/2);
			else
				vey = top + (height - bb.getHeight())/2 + bb.getHeight()/2;
	
			ve.draw(vex, vey);
		}
	}
}
