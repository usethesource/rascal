package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;


/**
 * A container represents a visual element that can contain another (nested) visual element called the "inside" element.
 * Typical examples are Boxes and Ellipses that may contain another element.
 * 
 * A container has the following behaviour:
 * - It has a bounding box of its own unless interaction due to a moueOver overrules it.
 * - It draws itself (using drawContainer).
 * - It draws the inside element provided that it fits in the container.
 * - It always draws the inside element on mouseOver.
 * 
 * @author paulk
 * 
 */

/**
 * @author paulk
 *
 */
public class Container extends VELEM {

	protected VELEM inside;
	private static boolean debug = true;
	float hgap;
	float vgap;

	public Container(VLPApplet vlp, PropertyManager inheritedProps, IList props, IConstructor inside, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);
		if(inside != null)
			this.inside = VELEMFactory.make(vlp, inside, this.properties, ctx);
		if(debug)System.err.printf("container.init: width=%f, height=%f, hanchor=%f, vanchor=%f\n", width, height, properties.hanchor, properties.vanchor);
	}

	@Override 
	void bbox(){
	
		VELEM insideForMouseOver = getInsideForMouseOver();
		if(vlp.isRegisteredAsMouseOver(this) && insideForMouseOver != null){
			insideForMouseOver.bbox();
			this.width = insideForMouseOver.width;
			this.height = insideForMouseOver.height;
		} else {
			int lw = getLineWidthProperty();
			width = getWidthProperty();
			height = getHeightProperty();
			if(inside != null){
				hgap = getHGapProperty();
				vgap = getVGapProperty();
				inside.bbox();
				if(width == 0 && height == 0){
					width = inside.width + 2 * hgap;
					height = inside.height + 2 * vgap;
				}
			} 
			width += 2*lw;
			height += 2*lw;
		}
		if(debug)System.err.printf("container.bbox: width=%f, height=%f, hanchor=%f, vanchor=%f\n", width, height, properties.hanchor, properties.vanchor);

	}

	@Override
	void draw(float left, float top) {
		this.left = left;
		this.top = top;
		applyProperties();
		if(debug)System.err.printf("container.draw: left=%f, top=%f, width=%f, height=%f, hanchor=%f, vanchor=%f\n", left, top, width, height, properties.hanchor, properties.vanchor);

		VELEM insideForMouseOver = getInsideForMouseOver();
		if(vlp.isRegisteredAsMouseOver(this) && insideForMouseOver != null){
			insideForMouseOver.draw(left, top);
		} else {
			if(height > 0 && width > 0){
				drawContainer();
				if(inside != null){
					if(debug)System.err.printf("container.draw2: hgap=%f, vgap=%f, inside.width=%f\n", hgap, vgap, inside.width);
					if(insideFits() || vlp.isRegisteredAsMouseOver(this))
						insideDraw();
				}
			}
		}
	}
	
	/**
	 * @return true if the inside element fits in the current container.
	 */
	boolean insideFits(){
		return inside.width + 2*hgap <= width && inside.height + 2*vgap <= height;
	}
	
	/**
	 * If the inside  element fits, draw it.
	 */
	void insideDraw(){
		inside.draw(left + hgap + properties.hanchor*(width  - inside.width  - 2 * hgap),
			    	top  + vgap + properties.vanchor*(height - inside.height - 2 * vgap));
	}
	
	
	/**
	 * drawContainer: draws the graphics associated with the container (if any). It is overridden by subclasses.
	 */
	void drawContainer(){
	}
	
	@Override
	public boolean mouseOver(int mousex, int mousey){
		
		if(vlp.isRegisteredAsMouseOver(this)){  // mouse is over this element
			VELEM imo = getInsideForMouseOver();
			if(imo != null){
				if(mousex > imo.left && mousex < imo.left + imo.width &&
						mousey > imo.top && mousey < imo.top + imo.height){
					vlp.registerMouse(this);
					return true;
				}
				vlp.unRegisterMouse();
				return false;
			}
			if(mousex > left && mousex < left + width &&
				mousey > top  && mousey < top + height){
				return true;
			} else {
				vlp.unRegisterMouse();
				return false;
			}
		}
		if(mousex > left && mousex < left + width &&
				mousey > top  && mousey < top + height){
			vlp.registerMouse(this);
			return true;
		}
		return false;
	}
}
