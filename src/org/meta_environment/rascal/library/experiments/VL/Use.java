package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

public class Use extends VELEM {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VELEM inside;

	public Use(VLPApplet vlp, PropertyManager inheritedProps, IList props, IConstructor inside,IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);
		if(inside != null){
			this.inside = VELEMFactory.make(vlp, inside, this.properties, ctx);
		}
		System.err.println("use.init: width=" + width + ", height=" + height);
	}

	@Override 
	void bbox(int left, int top){
		this.left = left;
		this.top = top;
		inside.bbox(left, top);
		width = inside.width;
		height = inside.height;
		System.err.println("use.bbox: width=" + width + ", height=" + height);
	}

	@Override
	void draw() {
		applyProperties();
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
	
	@Override
	public boolean mouseOver(int mousex, int mousey){
		return inside.mouseOver(mousex, mousey);
	}
}
