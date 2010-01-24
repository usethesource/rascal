package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

public class Space extends VELEM {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VELEM inside;
	private static boolean debug = false;

	public Space(VLPApplet vlp, PropertyManager inheritedProps, IList props, IConstructor inside,IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);
		if(inside != null)
			this.inside = VELEMFactory.make(vlp, inside, inheritedProps, ctx);
		if(debug)System.err.println("space.init: width=" + width + ", height=" + height);
	}

	@Override 
	void bbox(float left, float top){
		this.left = left;
		this.top = top;
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
		if(debug)System.err.println("space.bbox: width=" + width + ", height=" + height);
	}

	@Override
	void draw() {
		applyProperties();
		if(debug)System.err.println("space.draw: left=" + left + ", top=" + top + ", width=" + width + ", height=" + height+ ", color=" + getFillColorProperty());

		if(height > 0 && width > 0){
			int hgap = getHGapProperty();
			int vgap = getVGapProperty();
			if(inside != null){
				if(inside.width +2*hgap <= width && inside.height + 2*vgap <= height){
				   inside.draw(left + hgap + properties.hanchor*(width - inside.width - 2 * hgap),
						       top  + vgap + properties.vanchor*(height - inside.height - 2 * vgap));
				}
			}
		}
	}
}
