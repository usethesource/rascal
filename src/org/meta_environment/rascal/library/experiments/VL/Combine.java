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
			width += velems.size() * getGapProperty();
		else
			height += velems.size() * getGapProperty();
		return new BoundingBox(width, height);
	}
	
	@Override
	void draw(float x, float y){
		applyProperties();
		printProperties();
		this.x = x;
		this.y = y;
		int gap = getGapProperty();
		if(isHorizontal()){
			float top = y - height/2;
			float bottom = y + height/2;
			float left = x - width/2;
			float vey;
			for(VELEM ve : velems){
				BoundingBox bb = ve.bbox();
				if(isTop())
					vey = top + bb.getHeight()/2;
				else if(isCenter())
					vey = top + (height - bb.getHeight())/2 + bb.getHeight()/2;
				else
					vey = bottom - bb.getHeight()/2;
				float w = bb.getWidth();
				ve.draw(left + w/2, vey);
				left += w + gap;
			}
		} else {
			float left =  x - width/2;
			float right = x + width/2;
			float bottom = y + height/2;
			float vex;
			for(VELEM ve : velems){
				BoundingBox bb = ve.bbox();
				if(isRight())
					vex = right - bb.getWidth()/2;
				else if(isCenter())
					vex = left + (width - bb.getWidth())/2 + bb.getWidth()/2;
				else
					vex = left + bb.getWidth()/2;
				float h = bb.getHeight();
				ve.draw(vex, bottom - h/2);
				bottom -= h + gap;
			}
		}
	}
}
