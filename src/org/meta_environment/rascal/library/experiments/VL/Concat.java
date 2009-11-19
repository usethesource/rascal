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
	BoundingBox bbox(){
		int bbw = 0;
		int bbh = 0;
		for(VELEM ve : velems){
			BoundingBox bb = ve.bbox();
			if(isHorizontal()){
				bbw += bb.getWidth();
				bbh = max(bbh, bb.getHeight());
			} else {
				bbw = max(bbw, bb.getWidth());
				bbh = bbh + bb.getHeight();
			}
		} 
		return new BoundingBox(bbw, bbh);
	}
	
	@Override
	BoundingBox draw(PApplet pa, int left, int bottom){
		applyProperties(pa);
		printProperties();
		BoundingBox maxBB = bbox();
		if(isHorizontal()){
			int l = left;
			int b = bottom;
			for(VELEM ve : velems){
				BoundingBox bb = ve.bbox();
				if(isTop())
					b = bottom - (maxBB.getHeight() - bb.getHeight());
				else if(isCenter())
					b = bottom - (maxBB.getHeight() - bb.getHeight())/2;
				else
					b = bottom;
				ve.draw(pa, l, b);
				l = l + bb.getWidth();
			}
		} else {
			int l = left;
			int b = bottom;
			for(VELEM ve : velems){
				BoundingBox bb = ve.bbox();
				if(isRight())
					l = left + (maxBB.getWidth() - bb.getWidth());
				else if(isCenter())
					l = left + (maxBB.getWidth() - bb.getWidth())/2;
				else
					l = left;
				ve.draw(pa, l, b);
				b = b - bb.getHeight();
			}
		}
		return maxBB;
	}
}
