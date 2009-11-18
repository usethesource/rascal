package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PApplet;

public class Merge extends Compose {

	Merge(HashMap<String,IValue> inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(inheritedProps, props, elems, ctx);
	}
	
	@Override
	BoundingBox draw(PApplet pa, int left, int bottom) {
		return new BoundingBox(0, 0);
	}
	
	
//	@Override
//	BoundingBox draw(PApplet pa, int left, int bottom) {
//		int nValues = getNumberOfValues();
//		if(isHorizontal()){
//			int bbh = 0;
//			int l = left;
//			for(int vi = 0; vi < nValues; vi++){
//				System.err.println("vi = " + vi + ", l = " + l + ", b =" + bottom);
//				for(int i = 0; i < velems.size(); i++){
//					VELEM ve = velems.get(i);
//					if(vi < ve.getNumberOfValues()){
//						BoundingBox bb = ve.draw(pa, l, bottom);
//						bbh = max(bbh, bb.getHeight());
//						l = l + bb.getWidth();
//					} else {
//						l += ve.getWidth(ve.getNumberOfValues() - 1);
//					}	
//				}
//				l = l + getGap(valueIndex);
//			}
//			return new BoundingBox(l - left, bbh);
//		} else {
//			int l = left;
//			int b = bottom;
//			int bbh = 0;
//			for(int vi = 0; vi < nValues; vi++){
//				System.err.println("vi = " + vi + ", l = " + l + ", b =" + bottom);
//				int bbw = 0;
//				for(int i = 0; i < velems.size(); i++){
//					VELEM ve = velems.get(i);
//					if(vi < ve.getNumberOfValues()){
//						BoundingBox bb = ve.draw(pa, l, b);
//						bbw = max(bbw, bb.getWidth());
//						b = b - bb.getHeight();
//					}
//				}
//				l = l + bbw + getGap(valueIndex);
//				bbh = max(bbh, b - bottom);
//				b = bottom;
//			}
//			return new BoundingBox(l - left, bbh);
//		}
//	}
}
