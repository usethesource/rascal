package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PApplet;

public class Overlay extends Compose {

	Overlay(HashMap<String,IValue> inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(inheritedProps, props, elems, ctx);
	}
	
	@Override
	BoundingBox draw(PApplet pa, int valueIndex, int left, int bottom) {
		int nValues = getNumberOfValues();
		int bbh = 0;
		int bbw = 0;
		for(int i = 0; i < velems.size(); i++){
			int l = left + getOffset(valueIndex);
			int b = bottom;
			for(int vi = 0; vi < nValues; vi++){
				System.err.println("i =" + i + ", vi = " + vi + ", l = " + l + ", b =" + b);
				VELEM ve = velems.get(i);
				if(vi < ve.getNumberOfValues()){
					BoundingBox bb = ve.draw(pa, vi, l, b);
					bbh = max(bbh, bb.getHeight());
					bbw = max(bbw, bb.getWidth());
					l = l + bb.getWidth();
				} else {
					l += ve.getWidth(ve.getNumberOfValues() - 1);
				}	
			}
		}
		return new BoundingBox(bbw, bbh);
}
}
