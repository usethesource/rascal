package org.rascalmpl.library.viz.Figure;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;

import processing.core.PConstants;


/**
 * 
 * Ellipse that can act as container
 *
 * @author paulk
 *
 */
public class Ellipse extends Container {

	public Ellipse(FigurePApplet vlp, PropertyManager inheritedProps, IList props, IConstructor inside, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, inside, ctx);
	}
	
	@Override
	void drawContainer(){
		vlp.ellipseMode(PConstants.CORNERS);
		float l = left + leftDragged;
		float t = top + topDragged;
		vlp.ellipse(l, t, l + width, t + height);
	}
}
