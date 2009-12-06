package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PConstants;


public class Ellipse extends VELEM {
	private VELEM inside;

	public Ellipse(VLPApplet vlp, PropertyManager inheritedProps, IList props, IConstructor inside, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);
		if(inside != null)
			this.inside = VELEMFactory.make(vlp, inside, inheritedProps, ctx);
	}

	@Override
	void bbox(float left, float top){
		this.left = left;
		this.top = top;
		int lw = getLineWidthProperty();
		width = getWidthProperty() + lw;
		height = getHeightProperty() + lw;
		if(inside != null){
			int gap = getGapProperty();
			inside.bbox();
			width = inside.width + gap;
			height = inside.height + gap;
		}
		System.err.printf("bbox.ellipse: %f, %f)\n", width, height);
	}
	
	@Override
	void draw() {
		System.err.printf("ellipse.draw: %f, %f\n", left, top);
		
		applyProperties();
		vlp.ellipseMode(PConstants.CORNERS);
		vlp.ellipse(left, top, left + width, top + height);
		if(inside != null){
			int gap = getGapProperty();
			inside.draw(left + gap/2, top + gap/2);
		}
	}

}
