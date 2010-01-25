package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PConstants;


public class Ellipse extends Container {

	public Ellipse(VLPApplet vlp, PropertyManager inheritedProps, IList props, IConstructor inside, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, inside, ctx);
	}
	
	@Override
	void drawContainer(){
		vlp.ellipseMode(PConstants.CORNERS);
		vlp.ellipse(left, top, left + width, top + height);
	}
}
