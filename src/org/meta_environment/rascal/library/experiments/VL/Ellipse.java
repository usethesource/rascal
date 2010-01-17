package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PConstants;


public class Ellipse extends VELEM {
	private VELEM inside;
	private static boolean debug = false;

	public Ellipse(VLPApplet vlp, PropertyManager inheritedProps, IList props, IConstructor inside, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);
		if(inside != null)
			this.inside = VELEMFactory.make(vlp, inside, inheritedProps, ctx);
	}

	@Override
	void bbox(int left, int top){
		this.left = left;
		this.top = top;
		int lw = getLineWidthProperty();
		width = getWidthProperty() + lw;
		height = getHeightProperty() + lw;
		if(inside != null){
			int hgap = getHGapProperty();
			int vgap = getVGapProperty();
			inside.bbox();
			width = inside.width + hgap;
			height = inside.height + vgap;
		}
		if(debug)System.err.printf("bbox.ellipse: %f, %f)\n", width, height);
	}
	
	@Override
	void draw() {
		if(debug)System.err.printf("ellipse.draw: %d, %d\n", left, top);
		
		applyProperties();
		vlp.ellipseMode(PConstants.CORNERS);
		vlp.ellipse(left, top, left + width, top + height);
		if(inside != null){
			int hgap = getHGapProperty();
			int vgap = getVGapProperty();
			inside.draw(left + hgap/2, top + vgap/2);
		}
	}

}
