package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

public class Box extends VELEM {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VELEM inside;
	private static boolean debug = false;

	public Box(VLPApplet vlp, PropertyManager inheritedProps, IList props, IConstructor inside,IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);
		if(inside != null)
			this.inside = VELEMFactory.make(vlp, inside, this.properties, ctx);
		if(debug)System.err.printf("box.init: width=%f, height=%f, hanchor=%f, vanchor=%f\n", width, height, properties.hanchor, properties.vanchor);
	}

	@Override 
	void bbox(int left, int top){
		this.left = left;
		this.top = top;
		VELEM insideForMouseOver = getInsideForMouseOver();
		if(vlp.isRegisteredAsMouseOver(this) && insideForMouseOver != null){
			insideForMouseOver.bbox(left, top);
			this.width = insideForMouseOver.width;
			this.height = insideForMouseOver.height;
		} else {
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
		}
		if(debug)System.err.printf("box.bbox: width=%f, height=%f, hanchor=%f, vanchor=%f\n", width, height, properties.hanchor, properties.vanchor);

	}

	@Override
	void draw() {
		applyProperties();
		if(debug)System.err.printf("box.draw: left=%d, top=%d, width=%f, height=%f, hanchor=%f, vanchor=%f\n", left, top, width, height, properties.hanchor, properties.vanchor);

		VELEM insideForMouseOver = getInsideForMouseOver();
		if(vlp.isRegisteredAsMouseOver(this) && insideForMouseOver != null){
			insideForMouseOver.draw();
		} else {
			if(height > 0 && width > 0){
				vlp.rect(left, top, width, height);
				if(inside != null){
					int hgap = getHGapProperty();
					int vgap = getVGapProperty();
					if(debug)System.err.printf("box.draw2: hgap=%d, vgap=%d, inside.width=%f\n", hgap, vgap, inside.width);
					if(inside.width + 2*hgap <= width && inside.height + 2*vgap <= height){
						inside.draw(left + hgap + properties.hanchor*(width - inside.width - 2 * hgap),
								    top  + vgap + properties.vanchor*(height - inside.height - 2 * vgap));
					} else if(vlp.isRegisteredAsMouseOver(this)){
						inside.draw(left + (width - inside.width )/2, top + (height - inside.height)/2);
					}
				}
			}
		}
	}
	
	@Override
	public boolean mouseOver(int mousex, int mousey){
		VELEM imo = getInsideForMouseOver();
		if(vlp.isRegisteredAsMouseOver(this) && imo != null){

			if(mousex > imo.left && mousex < imo.left + imo.width &&
					mousey > imo.top && mousey < imo.top + imo.height){
				properties.setMouseOver(true);
				vlp.registerMouse(this);
				return true;
			}
			return false;

		}
		if(mousex > left && mousex < left + width &&
				mousey > top  && mousey < top + height){
			properties.setMouseOver(true);
			vlp.registerMouse(this);
			return true;

		}
		return false;
	}
}
