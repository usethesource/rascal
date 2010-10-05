package org.rascalmpl.library.vis;

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

	public Ellipse(FigurePApplet fpa, PropertyManager inheritedProps, IList props, IConstructor inside, IEvaluatorContext ctx) {
		super(fpa, inheritedProps, props, inside, ctx);
	}
	
	@Override
	void drawContainer(){
		fpa.ellipseMode(PConstants.CORNERS);
		float l = left + leftDragged;
		float t = top + topDragged;
		fpa.ellipse(l, t, l + width, t + height);
	}
}
