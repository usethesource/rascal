package org.rascalmpl.library.vis.containers;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;

/**
 * Rectangular box that can act as container
 * 
 * @author paulk
 *
 */
public class Box extends Container {

	public Box(FigurePApplet fpa, IPropertyManager properties, IConstructor inside, IEvaluatorContext ctx) {
		super(fpa, properties, inside, ctx);
	}

	@Override
	void drawContainer(){
		fpa.rect(getLeft(), getTop(), width, height);
	}
	
	String containerName(){
		return "box";
	}
}
