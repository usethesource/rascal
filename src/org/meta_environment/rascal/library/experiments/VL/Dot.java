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
	BoundingBox draw(PApplet pa, int left, int bottom) {
		applyProperties(pa);
		int h = getHeight();
		int w = getWidth();
		float w2 = w / 2.0f;
		//System.err.println("line: h =" + h + ", w = " + w);
		//System.err.println("line: " + left + ", " + (bottom-h) + ", " + (left+w) + ", " + bottom);
		pa.ellipse(left, bottom - h, w2, w2);
		return new BoundingBox(w, PApplet.round(h + w2));
	}

}
