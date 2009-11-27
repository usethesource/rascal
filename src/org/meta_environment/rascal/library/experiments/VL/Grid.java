package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;


public class Grid extends Compose {
	
	float leftElem[];
	float toTopElem[];
	float rowHeight[];
	int inRow[];
	int nrow;

	Grid(VLPApplet vlp, HashMap<String,IValue> inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, elems, ctx);
		leftElem = new float[elems.length()];
		toTopElem = new float[elems.length()];
		rowHeight = new float[elems.length()];
		inRow = new int[elems.length()];
		nrow = 0;
	}
	
	@Override
	BoundingBox bbox(){
		width = getWidthProperty();
		height = 0;
		float w = 0;
		float hrow = 0;
		float toprow = 0;
		nrow = 0;
		int gap = getGapProperty();
		for(int i = 0; i < velems.size(); i++){
			VELEM ve = velems.get(i);
			BoundingBox bb = ve.bbox();
			if(w + gap + bb.getWidth() > width){
				if(w == 0){
					width = bb.getWidth();
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
			w += bb.getWidth() + gap;
			hrow = max(hrow, bb.getHeight());
	
		}
		rowHeight[nrow] = hrow;
		height += hrow;
		nrow++;
		return new BoundingBox(width, height);
	}
	
	@Override
	void draw(float x, float y){
		applyProperties();
		float top = y - height/2;

		for(int i = 0; i < velems.size(); i++){
			
			VELEM ve = velems.get(i);
			float hrow = rowHeight[inRow[i]];
			float vey;
			if(isTop())
				vey = top + toTopElem[i] + ve.height/2;
			else if(isBottom())
				vey = top + toTopElem[i] + hrow - ve.height/2;
			else
				vey = top + toTopElem[i] + hrow/2;
			ve.draw(leftElem[i] + ve.width/2, vey);
		}
	}
}
