package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PApplet;


public class VCat extends Compose {
	
	int vgap;
	private static boolean debug = false;

	VCat(VLPApplet vlp, PropertyManager inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, elems, ctx);
	}
	
	@Override
	void bbox(int left, int top){
		this.left = left;
		this.top = top;
		width = 0;
		height = 0;
		vgap = getVGapProperty();
		if(debug)System.err.printf("vertical.bbox: vgap=%d\n", vgap);
		for(VELEM ve : velems){
			ve.bbox();
			width = max(width, ve.width);
			height = height + ve.height;
		} 
		int ngaps = (velems.length - 1);
		
		height += ngaps * vgap;
		if(debug)System.err.printf("vertical.bbox: width=%f, height=%f\n", width, height);
	}
	
	@Override
	void draw(){

		applyProperties();

		float right = left + width;
		float bottom = top + height;
		float veLeft;

		// Draw from top to bottom
		for(int i = velems.length-1; i >= 0; i--){
			if(debug)System.err.printf("vertical.draw: i=%d, vgap=%d, bottom=%f\n", i, vgap, bottom);
			VELEM ve = velems[i];
			if(ve.isRightAligned())
				veLeft = right - ve.width;
			else if(ve.isLeftAligned())
				veLeft = left;
			else
				veLeft = left + (width - ve.width)/2;

			float h = ve.height;
			ve.draw(veLeft, bottom - h);
			bottom -= h + vgap;
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
