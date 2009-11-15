package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

public class VELEMFactory {
	
	public static VELEM make(IConstructor c, HashMap<String,IValue> inheritedProps, IEvaluatorContext ctx){
		String ename = c.getName();
		System.err.println("ename = " + ename);
		if(c.arity() == 1){ // basic forms
			IList props = (IList) c.get(0);
			if(ename.equals("rect"))
				return new Rect(inheritedProps, props, ctx);
			if(ename.equals("line"))
				return new Line(inheritedProps, props, ctx);
			if(ename.equals("dot"))
				return new Dot(inheritedProps, props, ctx);
		} else if(c.arity() == 2){ // composition operators
			IList props = (IList) c.get(0);
			IList elems = (IList) c.get(1);
			if(ename.equals("merge"))
				return new Merge(inheritedProps, props, elems, ctx);
			if(ename.equals("concat"))
				return new Concat(inheritedProps, props, elems, ctx);
			if(ename.equals("overlay"))
				return new Overlay(inheritedProps, props, elems, ctx);
		}
		throw RuntimeExceptionFactory.illegalArgument(c, ctx.getCurrentAST(), ctx.getStackTrace());
	}
}
