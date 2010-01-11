package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;


public class Align extends Compose {
	
	float leftElem[];
	float topRowElem[];
	float rowHeight[];
	float rowWidth[];
	int inRow[];

	Align(VLPApplet vlp, PropertyManager inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, elems, ctx);
		leftElem = new float[elems.length()];
		topRowElem = new float[elems.length()];
		rowHeight = new float[elems.length()];
		rowWidth = new float[elems.length()];
		inRow = new int[elems.length()];
	}
	
	@Override
	void bbox(int left, int top){
		this.left = left;
		this.top = top;
		width = getWidthProperty();
		height = 0;
		float w = 0;
		float hrow = 0;
		float toprow = 0;
		int nrow = 0;
		int hgap = getHGapProperty();
		int vgap = getVGapProperty();
		for(int i = 0; i < velems.length; i++){
			VELEM ve = velems[i];
			ve.bbox();
			if(w + hgap + ve.width > width){
				if(w == 0){
					width = ve.width;
				} else {
					rowHeight[nrow] = hrow;
					rowWidth[nrow] = w;
					nrow++;
					height += hrow + vgap;
					toprow = height;
					w = hrow = 0;
				}
			}
			leftElem[i] = w;
			topRowElem[i] = toprow;
			inRow[i] = nrow;
			w += ve.width + hgap;
			hrow = max(hrow, ve.height);
	
		}
		rowHeight[nrow] = hrow;
		rowWidth[nrow] = w;
		height += hrow;
		if(nrow == 0)
			width = w - hgap;
		System.err.printf("Align.bbox: width=%f, height=%f\n", width, height);
	}
	
	@Override
	void draw(){
		
		applyProperties();

		for(int i = 0; i < velems.length; i++){
			
			VELEM ve = velems[i];
			float hrow = rowHeight[inRow[i]];
			float rfiller = width - rowWidth[inRow[i]];
			
			float veTop;
			if(ve.isTopAligned())
				veTop = top + topRowElem[i];
			else if(ve.isBottomAligned())
				veTop = top + topRowElem[i] + hrow - ve.height;
			else
				veTop = top + topRowElem[i] + (hrow - ve.height)/2;
			
			float veLeft;
			if(ve.isLeftAligned())
				veLeft = left + leftElem[i];
			else if(ve.isRightAligned())
				veLeft = left + rfiller + leftElem[i];
			else
				veLeft = left + rfiller/2 + leftElem[i];
			
			ve.draw(veLeft, veTop);
		}
	}
}
