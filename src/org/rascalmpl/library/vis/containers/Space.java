package org.rascalmpl.library.vis.containers;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;

/**
 * Spacing that can act as container.
 * 
 * @author paulk
 *
 */
public class Space extends Container {

	public Space(FigurePApplet fpa, IPropertyManager properties, IConstructor inside, IEvaluatorContext ctx) {
		super(fpa, properties, inside, ctx);
	}

	@Override
	void drawContainer() {
		return;
	}
	
	@Override
	String containerName(){
		return "space";
	}
}
