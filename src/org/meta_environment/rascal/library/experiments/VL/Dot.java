package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PApplet;

public class Dot extends VELEM {

	public Dot(HashMap<String,IValue> inheritedProps, IList props, IEvaluatorContext ctx) {
		super(inheritedProps, props, ctx);
	}

	@Override
	BoundingBox bbox(){
		int s = getSize();
		return new BoundingBox(max(getWidth(), s), getHeight() + s);
	}
	
	@Override
	BoundingBox draw(PApplet pa, int left, int bottom) {
		applyProperties(pa);
		int h = getHeight();
		int s = getSize();
		//System.err.println("line: h =" + h + ", w = " + w);
		//System.err.println("line: " + left + ", " + (bottom-h) + ", " + (left+w) + ", " + bottom);
		pa.ellipse(left - s, bottom - h, 2*s, 2*s);
		return bbox();
	}

}
