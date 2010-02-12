package org.rascalmpl.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;

/**
 * Abstract class for the composition of a list of visual elements.
 * 
 * @author paulk
 *
 */
public abstract class Compose extends VELEM {

	protected VELEM[] velems;
	private static boolean debug = false;

	Compose(VLPApplet vlp,PropertyManager inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);	
		int n = elems.length();
		velems = new VELEM[n];
		for(int i = 0; i < n; i++){
			IValue v = elems.get(i);
			IConstructor c = (IConstructor) v;
			if(debug)System.err.println("Compose, elem = " + c.getName());
			velems[i] = VELEMFactory.make(vlp, c, properties, ctx);
		}
	}
	
	@Override
	public boolean mouseOver(int mousex, int mousey){
		for(VELEM ve : velems)
			if(ve.mouseOver(mousex, mousey))
				return true;
		return false;
	}
}
