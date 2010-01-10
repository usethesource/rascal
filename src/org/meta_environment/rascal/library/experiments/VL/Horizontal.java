package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PApplet;


public class Horizontal extends Compose {
	
	int hgap;

	Horizontal(VLPApplet vlp, PropertyManager inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, elems, ctx);
	}
	
	@Override
	void bbox(int left, int top){
		this.left = left;
		this.top = top;
		width = 0;
		height = 0;
		hgap = getHGapProperty();
		for(VELEM ve : velems){
			ve.bbox();
			width += ve.width;
			height = max(height, ve.height);
		} 
		int ngaps = (velems.length - 1);
		
		width += ngaps * hgap;
		
	}
	
	@Override
	void draw(){

		applyProperties();

		float bottom = top + height;
		float veTop;

		// Draw from left to right
		for(VELEM ve : velems){
			if(ve.isTopAligned())
				veTop = top;
			else if(ve.isBottomAligned())
				veTop = bottom - ve.height;
			else
				veTop = top + (height - ve.height)/2;

			ve.draw(left, veTop);
			left += ve.width + hgap;
		}

	}

	@Override
	void bbox() {
		bbox(0,0);
	}

	@Override
	void draw(float left, float top) {
		this.left = PApplet.round(left);
		this.top =  PApplet.round(top);
		draw();
	}
	

}
