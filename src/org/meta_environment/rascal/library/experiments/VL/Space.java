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
	void bbox(int left, int top){
		this.left = left;
		this.top = top;
		width = getWidthProperty();
		height = getHeightProperty();
		if(inside != null){
			int hgap = getHGapProperty();
			int vgap = getVGapProperty();
			inside.bbox();
			if(width == 0 && height == 0){
				width = inside.width + hgap;
				height = inside.height + vgap;
			} 
		}
		if(debug)System.err.println("space.bbox: width=" + width + ", height=" + height);
	}

	@Override
	void draw() {
		applyProperties();
		if(debug)System.err.println("space.draw: left=" + left + ", top=" + top + ", width=" + width + ", height=" + height+ ", color=" + getFillColorProperty());

		if(height > 0 && width > 0){
			if(inside != null){
				if(inside.width <= width && inside.height <= height){
					int hgap = getHGapProperty();
					int vgap = getVGapProperty();
					float xi;
					if(inside.isLeftAligned())
						xi = left + hgap/2;
					else if(inside.isRightAligned())
						xi = left + (width - inside.width - hgap/2);
					else 
						xi = left + (width - inside.width )/2;
					float yi;
					if(inside.isTopAligned())
						yi = top + vgap/2;
					else if(inside.isBottomAligned())
						yi = top + (height - inside.height - vgap/2);
					else 
						yi = top + (height - inside.height)/2;

					inside.draw(xi, yi);
				}
			}
		}
	}
}
