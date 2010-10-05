package org.rascalmpl.library.vis;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;

/**
 * Rectangular box that can act as container
 * 
 * @author paulk
 *
 */
public class Box extends Container {

	public Box(FigurePApplet fpa, PropertyManager inheritedProps, IList props, IConstructor inside,IEvaluatorContext ctx) {
		super(fpa, inheritedProps, props, inside, ctx);
	}

	@Override
	void drawContainer(){
		fpa.rect(left + leftDragged, top + topDragged, width, height);
	}
}
