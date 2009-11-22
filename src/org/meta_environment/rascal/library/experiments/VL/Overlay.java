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
		return new BoundingBox(width, height);
	}
	
	@Override
	void draw(int l, int b) {
		applyProperties();
		left = l;
		bottom = b;
		for(VELEM ve : velems){
			BoundingBox bb = ve.bbox();
			if(isTop())
				b = bottom - (height - bb.getHeight());
			else if(isCenter())
				b = bottom - (height - bb.getHeight())/2;
			else
				b = bottom;
			if(isRight())
				l = left + (width - bb.getWidth());
			else if(isCenter())
				l = left + (width - bb.getWidth())/2;
			else
				l = left;
			ve.draw(l, b);
		}
	}
}
