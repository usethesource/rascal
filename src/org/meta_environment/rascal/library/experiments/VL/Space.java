package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

/**
 * Spacing that can act as container.
 * 
 * @author paulk
 *
 */
public class Space extends Container {

	public Space(VLPApplet vlp, PropertyManager inheritedProps, IList props, IConstructor inside,IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, inside, ctx);
	}
}
