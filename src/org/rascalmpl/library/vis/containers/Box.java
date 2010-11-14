package org.rascalmpl.library.vis.containers;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.PropertyManager;

/**
 * Rectangular box that can act as container
 * 
 * @author paulk
 *
 */
public class Box extends Container {

	public Box(FigurePApplet fpa, PropertyManager properties, IConstructor inside, IEvaluatorContext ctx) {
		super(fpa, properties, inside, ctx);
	}

	@Override
	void drawContainer(){
		fpa.rect(left, top, width, height);
	}
}
