package org.rascalmpl.library.vis;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;


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

	protected Figure inner;
	private static boolean debug = false;
	float hgap;
	float vgap;

	public Container(FigurePApplet fpa, PropertyManager inheritedProps, IList props, IConstructor inner, IEvaluatorContext ctx) {
		super(fpa, inheritedProps, props, ctx);
		if(inner != null){
			this.inner = FigureFactory.make(fpa, inner, this.properties, ctx);
		}
		if(debug)System.err.printf("container.init: width=%f, height=%f, hanchor=%f, vanchor=%f\n", width, height, properties.hanchor, properties.vanchor);
	}

	@Override 
	void bbox(){
		float lw = getLineWidthProperty();
		width = getWidthProperty();
		height = getHeightProperty();
		if(inner != null){
			hgap = getHGapProperty();
			vgap = getVGapProperty();
			inner.bbox();
			if(width == 0 && height == 0){
				width = inner.width + 2 * hgap;
				height = inner.height + 2 * vgap;
			}
		} 
		width += 2*lw;
		height += 2*lw;
		if(debug)System.err.printf("container.bbox: width=%f, height=%f, hanchor=%f, vanchor=%f\n", width, height, properties.hanchor, properties.vanchor);

	}

	@Override
	void draw(float left, float top) {
		if(!isVisible())
			return;
		this.left = left;
		this.top = top;
	
		applyProperties();
		if(debug)System.err.printf("container.draw: left=%f, top=%f, width=%f, height=%f, hanchor=%f, vanchor=%f\n", left, top, width, height, properties.hanchor, properties.vanchor);

		if(height > 0 && width > 0){
			drawContainer();
			if(inner != null && isNextVisible()){
				if(debug)System.err.printf("container.draw2: hgap=%f, vgap=%f, inside.width=%f\n", hgap, vgap, inner.width);
				if(insideFits()){
					fpa.incDepth();
					insideDraw();
					fpa.decDepth();
				}
			}
		}
	}
	
	/**
	 * @return true if the inside element fits in the current container.
	 */
	boolean insideFits(){
		return inner.width + 2*hgap <= width && inner.height + 2*vgap <= height;
	}
	
	/**
	 * If the inside  element fits, draw it.
	 */
	void insideDraw(){
		inner.draw(left + hgap + properties.hanchor*(width  - inner.width  - 2 * hgap),
			    	top + vgap + properties.vanchor*(height - inner.height - 2 * vgap));
	}
	
	
	/**
	 * drawContainer: draws the graphics associated with the container (if any). It is overridden by subclasses.
	 */
	void drawContainer(){
	}
	
	@Override
	public boolean mouseOver(int mousex, int mousey){
		if(!isVisible())
			return false;
		if(inner != null && isNextVisible() &&  inner.mouseOver(mousex, mousey))
				return true;
		if(mouseInside(mousex, mousey)){
			fpa.registerFocus(this);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean mousePressed(int mousex, int mousey){
		if(!isVisible())
			return false;
		if(debug)System.err.println("Container.mousePressed: " + mousex + ", " + mousey);
		if(inner != null && isNextVisible() && inner.mousePressed(mousex, mousey))
				return true;
		if(mouseInside(mousex, mousey)){
			fpa.registerFocus(this);
			return true;
		}
		return false;
	}
}
