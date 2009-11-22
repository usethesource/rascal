package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;


public class Dot extends VELEM {

	public Dot(VLPApplet vlp, HashMap<String,IValue> inheritedProps, IList props, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);
	}

	@Override
	BoundingBox bbox(){
		int s = getSize();
		width = getWidth();
		height = getHeight();
		return new BoundingBox(max(width,2*s), height + s);
	}
	
	@Override
	void draw(int l, int b) {
		applyProperties();
		left = l;
		bottom = b;
		int s = getSize();
		//System.err.println("line: h =" + h + ", w = " + w);
		//System.err.println("line: " + left + ", " + (bottom-h) + ", " + (left+w) + ", " + bottom);
		vlp.ellipse(left, bottom - height, 2*s, 2*s);
	}

}
