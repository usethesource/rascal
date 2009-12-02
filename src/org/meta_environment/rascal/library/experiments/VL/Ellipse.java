package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PConstants;


public class Ellipse extends VELEM {

	public Ellipse(VLPApplet vlp, PropertyManager inheritedProps, IList props, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);
	}

	@Override
	void bbox(){
		int lw = getLineWidthProperty();
		width = getWidthProperty() + lw;
		height = getHeightProperty() + lw;
		System.err.printf("bbox.ellipse: %f, %f)\n", width, height);
	}
	
	@Override
	void draw(float left, float top) {
		System.err.printf("ellipse.draw: %f, %f\n", left, top);
		this.left = left;
		this.top = top;
		applyProperties();
		vlp.ellipseMode(PConstants.CORNERS);
		vlp.ellipse(left, top, left + width, top + height);
	}

}
