package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

public abstract class Compose extends VELEM {

	protected VELEM[] velems;

	Compose(VLPApplet vlp,PropertyManager inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);	
		int n = elems.length();
		velems = new VELEM[n];
		for(int i = 0; i < n; i++){
			IValue v = elems.get(i);
			IConstructor c = (IConstructor) v;
			System.err.println("Compose, elem = " + c.getName());
			velems[i] = VELEMFactory.make(vlp, c, properties, ctx);
		}
	}
	
	@Override
	public void mouseOver(int mousex, int mousey){
		for(VELEM ve : velems)
			ve.mouseOver(mousex, mousey);
	}
}
