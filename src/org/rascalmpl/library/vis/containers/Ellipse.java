package org.rascalmpl.library.vis.containers;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.PropertyManager;

import processing.core.PConstants;


/**
 * 
 * Ellipse that can act as container
 *
 * @author paulk
 *
 */
public class Ellipse extends Container {

	public Ellipse(FigurePApplet fpa, PropertyManager properties, IConstructor inside, IEvaluatorContext ctx) {
		super(fpa, properties, inside, ctx);
	}
	
	@Override
	void drawContainer(){
		fpa.ellipseMode(PConstants.CORNERS);
		fpa.ellipse(left, top, left + width, top + height);
	}
}
