package org.rascalmpl.library.vis;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.interpreter.IEvaluatorContext;

/**
 * Spacing that can act as container.
 * 
 * @author paulk
 *
 */
public class Space extends Container {

	public Space(FigurePApplet fpa, PropertyManager properties, IConstructor inside, IEvaluatorContext ctx) {
		super(fpa, properties, inside, ctx);
	}
}
