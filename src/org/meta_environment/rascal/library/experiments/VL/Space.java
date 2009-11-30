package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IInteger;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;


public class Space extends VELEM {
	
	public Space(VLPApplet vlp, IInteger amount, IEvaluatorContext ctx) {
		super(vlp, ctx);
		width = height = amount.intValue();
	}
	
	@Override
	void bbox(){
	}

	@Override
	void draw(float x, float y) {
	}

}
