package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IInteger;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;


public class Space extends VELEM {
	
	public Space(VLPApplet vlp, IInteger amount, IEvaluatorContext ctx) {
		super(vlp, ctx);
		width = height = amount.intValue();
	}
	
	@Override
	BoundingBox bbox(){
		return new BoundingBox(width, height);
	}

	@Override
	void draw(int left, int bottom) {
	}

}
