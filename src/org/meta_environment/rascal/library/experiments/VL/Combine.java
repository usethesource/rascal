package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;


public class Combine extends Compose {

	Combine(VLPApplet vlp, HashMap<String,IValue> inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, elems, ctx);
	}
	
	@Override
	BoundingBox bbox(){
		width = 0;
		height = 0;
		for(VELEM ve : velems){
			BoundingBox bb = ve.bbox();
			if(isHorizontal()){
				width += bb.getWidth();
				height = max(height, bb.getHeight());
			} else {
				width = max(width, bb.getWidth());
				height = height + bb.getHeight();
			}
		} 
		if(isHorizontal())
			width += velems.size() * getGap();
		else
			height += velems.size() * getGap();
		return new BoundingBox(width, height);
	}
	
	@Override
	void draw(int left, int bottom){
		applyProperties();
		printProperties();
		int gap = getGap();
		if(isHorizontal()){
			int l = left;
			int b = bottom;
			for(VELEM ve : velems){
				BoundingBox bb = ve.bbox();
				if(isTop())
					b = bottom - (height - bb.getHeight());
				else if(isCenter())
					b = bottom - (height - bb.getHeight())/2;
				else
					b = bottom;
				ve.draw(l, b);
				l += bb.getWidth() + gap;
			}
		} else {
			int l = left;
			int b = bottom;
			for(VELEM ve : velems){
				BoundingBox bb = ve.bbox();
				if(isRight())
					l = left + (width - bb.getWidth());
				else if(isCenter())
					l = left + (width - bb.getWidth())/2;
				else
					l = left;
				ve.draw(l, b);
				b -= bb.getHeight() + gap;
			}
		}
	}
}
