package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PApplet;

public class Concat extends Compose {

	Concat(HashMap<String,IValue> inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(inheritedProps, props, elems, ctx);
	}
	
	@Override
	BoundingBox draw(PApplet pa, int valueIndex, int left, int bottom) {
		int nValues = getNumberOfValues();
		if(isHorizontal()){
			int bbh = 0;
			int l = left;
			int b = bottom;
			for(int i = 0; i < velems.size(); i++){
				for(int vi = 0; vi < nValues; vi++){
				System.err.println("vi = " + vi + ", l = " + l + ", b =" + b);
					VELEM ve = velems.get(i);
					if(vi < ve.getNumberOfValues()){
						System.err.println("** offset = " + ve.getOffset(vi));
						if(ve.isHorizontal()){
							BoundingBox bb = ve.draw(pa, vi, l, b - ve.getOffset(vi));
							bbh = max(bbh, bb.getHeight());
							l = l + bb.getWidth();
						} else {
							BoundingBox bb = ve.draw(pa, vi, l + ve.getOffset(vi), b);
							bbh = max(bbh, bb.getHeight());
							b = b - bb.getHeight();
						}
					}	
				}
				l = l + getGap(valueIndex);
				b = bottom;
			}
			return new BoundingBox(l - left, bbh);
		} else {
			int l = left;
			int b = bottom;
			int bbh = 0;
			int bbw = 0;
			for(int i = 0; i < velems.size(); i++){
				bbh = 0;
				for(int vi = 0; vi < nValues; vi++){
					System.err.println("vi = " + vi + ", l = " + l + ", b =" + bottom);
					VELEM ve = velems.get(i);
					if(vi < ve.getNumberOfValues()){
						if(ve.isHorizontal()){
							BoundingBox bb = ve.draw(pa, vi, l, b  - ve.getOffset(vi));
							bbw = max(bbw, bb.getWidth());
							bbh = max(bbh, bb.getHeight());
							l = l + bb.getWidth();
						} else {
							BoundingBox bb = ve.draw(pa, vi, l + ve.getOffset(vi), b);
							bbh = max(bbh, bb.getHeight());
							b = b - bb.getHeight();
						}
					}
				}
				l = left;
				b = bottom - bbh - getGap(valueIndex);
			}
			return new BoundingBox(bbw, bottom - b);
		}
	}
}
