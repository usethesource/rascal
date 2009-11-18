package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PApplet;
import processing.core.PConstants;

public class Rect extends VELEM {

	public Rect(HashMap<String,IValue> inheritedProps, IList props, IEvaluatorContext ctx) {
		super(inheritedProps, props, ctx);
	}

	@Override
	BoundingBox draw(PApplet pa, int left, int bottom) {
		applyProperties(pa);
		int h = getHeight();
		int w = getWidth();
		pa.rectMode(PConstants.CORNERS);
		System.err.println("rect: h =" + h + ", w = " + w);
		System.err.println("rect: " + left + ", " + (bottom-h) + ", " + (left+w) + ", " + bottom);
		pa.rect(left, bottom-h, left + w, bottom);
		return new BoundingBox(w, h);
	}

}
