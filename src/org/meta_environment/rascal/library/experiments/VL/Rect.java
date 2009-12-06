package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

public class Rect extends VELEM {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VELEM inside;

	public Rect(VLPApplet vlp, PropertyManager inheritedProps, IList props, IConstructor inside,IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);
		int lw = getLineWidthProperty();
		width = getWidthProperty() + 2 * lw;
		height = getHeightProperty() + 2 * lw;
		if(inside != null)
			this.inside = VELEMFactory.make(vlp, inside, inheritedProps, ctx);
		System.err.println("rect.init: width=" + width + ", height=" + height);
	}

	@Override
	void bbox(float left, float top){
		this.left = left;
		this.top = top;
		width = getWidthProperty();
		height = getHeightProperty();
		if(inside != null){
			int gap = getGapProperty();
			inside.bbox();
			width = inside.width + gap;
			height = inside.height + gap;
		}
		System.err.println("rect.bbox: width=" + width + ", height=" + height);
	}

	@Override
	void draw() {
		applyProperties();
		System.err.println("rect.draw: left=" + left + ", top=" + top + ", width=" + width + ", height=" + height+ ", color=" + getFillColorProperty());
		if(height > 0 && width > 0){
			vlp.rect(left, top, width, height);
			if(inside != null){
				int gap = getGapProperty();
				inside.draw(left + gap/2, top + gap/2);
			}
		}
	}
}
