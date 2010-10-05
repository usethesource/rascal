package org.rascalmpl.library.vis;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;

/**
 * Spacing that can act as container.
 * 
 * @author paulk
 *
 */
public class Space extends Container {

	public Space(FigurePApplet fpa, PropertyManager inheritedProps, IList props, IConstructor inside,IEvaluatorContext ctx) {
		super(fpa, inheritedProps, props, inside, ctx);
	}
}
