package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;


public class Grid extends Compose {
	
	float leftElem[];
	float toTopElem[];
	float rowHeight[];
	int inRow[];
	int nrow;

	Grid(VLPApplet vlp, PropertyManager inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, elems, ctx);
		leftElem = new float[elems.length()];
		toTopElem = new float[elems.length()];
		rowHeight = new float[elems.length()];
		inRow = new int[elems.length()];
		nrow = 0;
	}
	
	@Override
	void bbox(float left, float top){
		this.left = left;
		this.top = top;
		width = getWidthProperty();
		height = 0;
		float w = 0;
		float hrow = 0;
		float toprow = 0;
		nrow = 0;
		int gap = getGapProperty();
		for(int i = 0; i < velems.length; i++){
			VELEM ve = velems[i];
			if(w + gap + ve.width > width){
				if(w == 0){
					width = ve.width;
				} else {
					rowHeight[nrow] = hrow;
					nrow++;
					height += hrow + gap;
					toprow = height;
					w = hrow = 0;
				}
			}
			leftElem[i] = w;
			toTopElem[i] = toprow;
			inRow[i] = nrow;
			w += ve.width + gap;
			hrow = max(hrow, ve.height);
	
		}
		rowHeight[nrow] = hrow;
		height += hrow;
		nrow++;
	}
	
	@Override
	void draw(){
		
		applyProperties();

		for(int i = 0; i < velems.length; i++){
			
			VELEM ve = velems[i];
			float hrow = rowHeight[inRow[i]];
			float veTop;
			if(isTop())
				veTop = top + toTopElem[i];
			else if(isBottom())
				veTop = top + toTopElem[i] + hrow;
			else
				veTop = top + toTopElem[i];
			ve.draw(leftElem[i], veTop);
		}
	}
}
