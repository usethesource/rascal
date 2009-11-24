package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;


public class Grid extends Compose {
	
	int leftElem[];
	int toTopElem[];
	int rowHeight[];
	int inRow[];
	int nrow;

	Grid(VLPApplet vlp, HashMap<String,IValue> inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, elems, ctx);
		leftElem = new int[elems.length()];
		toTopElem = new int[elems.length()];
		rowHeight = new int[elems.length()];
		inRow = new int[elems.length()];
		nrow = 0;
	}
	
	@Override
	BoundingBox bbox(){
		width = getWidth();
		height = 0;
		int w = 0;
		int hrow = 0;
		int toprow = 0;
		nrow = 0;
		int gap = getGap();
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
		height += hrow + gap;
		nrow++;
		return new BoundingBox(width, height);
	}
	
	@Override
	void draw(int left, int bottom){
		applyProperties();
		int top = bottom - height;
		int b;
		for(int i = 0; i < velems.size(); i++){
			
			VELEM ve = velems.get(i);
			int hrow = rowHeight[inRow[i]];
			
			if(isTop())
				b = top + toTopElem[i] + ve.getHeight();
			else if(isCenter())
				b = top + toTopElem[i] + hrow - (hrow - ve.getHeight())/2;
			else
				b = top + toTopElem[i] + hrow;
			
			//System.err.printf("elem %d: row=%d, rowHeight=%d, leftElem=%d, toTopElem=%d, top=%d, b = %d\n", 
			//		                 i, inRow[i], hrow,       leftElem[i],  toTopElem[i], top, b);
			ve.draw(leftElem[i], b);
		}
	}
}
