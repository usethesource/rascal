package org.rascalmpl.library.vis;

import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;


/**
 * Place elements on fixed grid positions. The width is determined by the width property, height is
 * determined by number of elements. Each element is positioned with its anchor on the grid point.
 * 
 * @author paulk
 *
 */
public class Grid extends Compose {
	
	float xElem[];
	float yElem[];
	
	float extTop = 0;
	float extBot = 0;
	float extLeft = 0;
	float extRight = 0;
	private static boolean debug = false;

	Grid(FigurePApplet fpa, PropertyManager inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(fpa, inheritedProps, props, elems, ctx);
		xElem = new float[elems.length()];
		yElem = new float[elems.length()];
	}
	
	@Override
	void bbox(){
		
		width = getWidthProperty();
		height = 0;
		float w = 0;
		int nrow = 0;
		
		float hgap = getHGapProperty();
		float vgap = getVGapProperty();
		
		int lastRow = (hgap == 0) ? 0 : figures.length / (1 + (int) (width / hgap));
		if(debug)System.err.printf("lastRow = %d\n", lastRow);
		
		extTop = 0;
		extBot = 0;
		extLeft = 0;
		extRight = 0;
		
		for(int i = 0; i < figures.length; i++){
			
			if(w > width){
				nrow++;
				height += vgap;
				w = 0;
			}
			
			Figure ve = figures[i];
			ve.bbox();
			
			if(w == 0)
				extLeft = max(extLeft, ve.leftAnchor());
			if(w + hgap >= width)
				extRight = max(extRight, ve.rightAnchor());
			if(nrow == 0)
				extTop = max(extTop, ve.topAnchor());
			if(nrow == lastRow){
				extBot = max(extBot, ve.bottomAnchor());
			}
			
			if(debug)System.err.printf("i=%d, row=%d, w=%f, extLeft=%f, extRight=%f, extTop=%f, extBot=%f\n", i, nrow, w, extLeft, extRight, extTop, extBot);
			
			xElem[i] = w;
			yElem[i] = height;
			w += hgap;
		}
		width += extLeft + extRight;
		height += extTop + extBot;
		if(debug)System.err.printf("grid.bbox: %f, %f\n", width, height);
	}
	
	@Override
	void draw(float left, float top){
		this.left = left;
		this.top = top;
		left += leftDragged;
		top  += topDragged;
		applyProperties();

		for(int i = 0; i < figures.length; i++){
			
			Figure ve = figures[i];
			
			if(debug)System.err.printf("i=%d: %f, %f, left=%f, top=%f\n", i, xElem[i], yElem[i], left, top);
			
			ve.draw(left + extLeft + xElem[i] - ve.leftAnchor(), top + extTop + yElem[i] - ve.topAnchor());
		}
	}
}
