package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;


public class Overlay extends Compose {
	
	private static boolean debug = false;

	Overlay(VLPApplet vlp, PropertyManager inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, elems, ctx);
	}
	
	@Override
	void bbox(int left, int top){
		this.left = left;
		this.top = top;
		width = 0;
		height = 0;
		for(VELEM ve : velems){
			ve.bbox();
			height = max(height, ve.height);
			width = max(width, ve.width);
		}
		if(debug)System.err.printf("overlay.bbox: width=%f, height=%f\n", width, height);
	}
	
	@Override
	void draw() {
		
		applyProperties();
		if(debug)System.err.printf("overlay.draw: left=%d, top=%d\n", left, top);
		for(VELEM ve : velems){
			float veLeft;
			float veTop;
			
			if(ve.isRightAligned())
				veLeft = left + width - ve.width;
			else if(ve.isLeftAligned())
				veLeft = left;
			else
				veLeft = left + (width - ve.width)/2;
			
			if(ve.isTopAligned())
				veTop = top;
			else if (ve.isBottomAligned())
				veTop = top + height - ve.height;
			else
				veTop = top + (height - ve.height)/2;
	
			ve.draw(veLeft, veTop);
		}
	}

}
