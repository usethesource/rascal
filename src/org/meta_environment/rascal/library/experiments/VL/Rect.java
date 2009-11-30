package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

public class Rect extends VELEM {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Rect(VLPApplet vlp, PropertyManager inheritedProps, IList props, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);
		int lw = getLineWidthProperty();
		width = getWidthProperty() + 2 * lw;
		height = getHeightProperty() + 2 * lw;
		System.err.println("rect: width=" + width + ", height=" + height);
	}
	
	@Override
	void bbox(){
		width = getWidthProperty();
		height = getHeightProperty();
	}

	@Override
	void draw(float left, float top) {
		this.left = left;
		this.top = top;
		applyProperties();
		System.err.println("rect: left=" + left + ", top=" + top + ", width=" + width + ", height=" + height+ ", color=" + getFillColorProperty());
		if(height > 0 && width > 0){
			vlp.rect(left, top, width, height);
		}
	}
	
	@Override
	public void mouseOver(int mousex, int mousey){
		System.err.println("MouseOver in rect: " + left  + ", " + top);
		if((mousex > left && mousex < left + width) &&
		   (mousey > top  && mousey < top + height)){
		   System.err.println("MouseOver in rect: " + left  + ", " + top);
		}
	}

}
