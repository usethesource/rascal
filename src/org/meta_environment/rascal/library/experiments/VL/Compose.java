package org.meta_environment.rascal.library.experiments.VL;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

public abstract class Compose extends VELEM {

	protected ArrayList<VELEM> velems;

	Compose(HashMap<String,IValue> inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(inheritedProps, props, ctx);		
		velems = new ArrayList<VELEM>();
		for(IValue v : elems){
			IConstructor c = (IConstructor) v;
			System.err.println("Compose, elem = " + c.getName());
			velems.add(VELEMFactory.make(c, properties, ctx));
		}
	}
}
