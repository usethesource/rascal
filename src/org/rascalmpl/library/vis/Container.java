package org.rascalmpl.library.vis;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;

import processing.core.PConstants;


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

public class Container extends Figure {

	protected Figure inside;
	boolean insideVisible = true;
	private static boolean debug = false;
	float hgap;
	float vgap;

	public Container(FigurePApplet fpa, PropertyManager inheritedProps, IList props, IConstructor inside, IEvaluatorContext ctx) {
		super(fpa, inheritedProps, props, ctx);
		if(inside != null){
			this.inside = FigureFactory.make(fpa, inside, this.properties, ctx);
			insideVisible = isContentsVisible();
		}
		if(debug)System.err.printf("container.init: width=%f, height=%f, hanchor=%f, vanchor=%f\n", width, height, properties.hanchor, properties.vanchor);
	}

	@Override 
	void bbox(){
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
		if(debug)System.err.printf("container.bbox: width=%f, height=%f, hanchor=%f, vanchor=%f\n", width, height, properties.hanchor, properties.vanchor);

	}

	@Override
	void draw(float left, float top) {
		this.left = left;
		this.top = top;
		left += leftDragged;
		top += topDragged;
		applyProperties();
		if(debug)System.err.printf("container.draw: left=%f, top=%f, width=%f, height=%f, hanchor=%f, vanchor=%f\n", left, top, width, height, properties.hanchor, properties.vanchor);

		if(height > 0 && width > 0){
			drawContainer();
			if(inside != null){
				if(debug)System.err.printf("container.draw2: hgap=%f, vgap=%f, inside.width=%f\n", hgap, vgap, inside.width);
				if(insideFits() && insideVisible)
					insideDraw();
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
		inside.draw(left + leftDragged + hgap + properties.hanchor*(width  - inside.width  - 2 * hgap),
			    	top  + topDragged  + vgap + properties.vanchor*(height - inside.height - 2 * vgap));
	}
	
	
	/**
	 * drawContainer: draws the graphics associated with the container (if any). It is overridden by subclasses.
	 */
	void drawContainer(){
	}
	
	@Override
	public boolean mouseOver(int mousex, int mousey){
		if(inside != null && insideVisible &&  inside.mouseOver(mousex, mousey))
				return true;
		if(mouseInside(mousex, mousey)){
			fpa.registerFocus(this);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean mousePressed(int mousex, int mousey){
		if(inside != null && insideVisible && inside.mousePressed(mousex, mousey))
				return true;
		if(mouseInside(mousex, mousey)){
			fpa.registerFocus(this);
			if(fpa.mouseButton == PConstants.RIGHT)
				insideVisible = false;
			else
				insideVisible = true;
			return true;
		}
		return false;
	}
}
