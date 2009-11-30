package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;


public class Combine extends Compose {

	Combine(VLPApplet vlp, PropertyManager inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, elems, ctx);
	}
	
	@Override
	void bbox(){
		width = 0;
		height = 0;
		for(VELEM ve : velems){
			ve.bbox();
			if(isHorizontal()){
				width += ve.width;
				height = max(height, ve.height);
			} else {
				width = max(width, ve.width);
				height = height + ve.height;
			}
		} 
		int gaps = (velems.length - 1) * getGapProperty();
		if(isHorizontal())
			width += gaps;
		else
			height += gaps;
	}
	
	@Override
	void draw(float left, float top){
		this.left = left;
		this.top = top;
		applyProperties();
		int gap = getGapProperty();
		System.err.println("Combine.draw: isHor=" + isHorizontal());
		System.err.println("Combine.draw: isTop=" + isTop());
		System.err.println("Combine.draw: isBottom=" + isBottom());
		if(isHorizontal()){
			float bottom = top + height;
			float veTop;
			for(VELEM ve : velems){
				if(isTop())
					veTop = top;
				else if(isBottom())
					veTop = bottom - ve.height;
				else
					veTop = top + (height - ve.height)/2;
				
				ve.draw(left, veTop);
				left += ve.width + gap;
			}
		} else {
			float right = left + width;
			float bottom = top + height;
			float veLeft;
			for(VELEM ve : velems){
				if(isRight())
					veLeft = right - ve.width;
				else if(isLeft())
					veLeft = left;
				else
					veLeft = left + (width - ve.width)/2;
			
				float h = ve.height;
				ve.draw(veLeft, bottom - h);
				bottom -= h + gap;
			}
		}
	}
}
