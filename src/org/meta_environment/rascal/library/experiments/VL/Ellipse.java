package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PConstants;


public class Ellipse extends VELEM {
	private VELEM inside;
	private static boolean debug = true;

	public Ellipse(VLPApplet vlp, PropertyManager inheritedProps, IList props, IConstructor inside, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);
		if(inside != null)
			this.inside = VELEMFactory.make(vlp, inside, this.properties, ctx);
	}

	@Override
	void bbox(float left, float top){
		this.left = left;
		this.top = top;
		int lw = getLineWidthProperty();
		width = getWidthProperty();
		height = getHeightProperty();
		if(inside != null){
			int hgap = getHGapProperty();
			int vgap = getVGapProperty();
			inside.bbox();
			if(width == 0 && height == 0){
				width = inside.width + 2 * hgap;
				height = inside.height + 2 * vgap;
			}
		}
		width += 2*lw;
		height += 2*lw;
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
			inside.draw(left + hgap + properties.hanchor*(width - inside.width - 2 * hgap),
				        top  + vgap + properties.vanchor*(height - inside.height - 2 * vgap));
		}
	}

}
