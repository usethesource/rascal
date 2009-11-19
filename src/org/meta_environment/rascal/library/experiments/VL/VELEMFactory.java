package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;
import org.meta_environment.values.ValueFactoryFactory;

public class VELEMFactory {
	static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	static IList emptyList = vf.list();
	
	public static VELEM make(IConstructor c, HashMap<String,IValue> inheritedProps, IEvaluatorContext ctx){
		String ename = c.getName();
		System.err.println("ename = " + ename);
		IList props;
		IList elems;
		if(ename.equals("concat") || ename.equals("overlay")){
			if(c.arity() == 2){
				props = (IList) c.get(0);
				elems = (IList) c.get(1);
			} else {
				props = emptyList;
				elems = (IList) c.get(0);
			}
			return ename.equals("concat") ? new Concat(inheritedProps, props, elems, ctx)
			                              : new Overlay(inheritedProps, props, elems, ctx);
		}
		if(c.arity() == 1){ // basic forms
			props = (IList) c.get(0);
			if(ename.equals("rect"))
				return new Rect(inheritedProps, props, ctx);
			if(ename.equals("line"))
				return new Line(inheritedProps, props, ctx);
			if(ename.equals("dot"))
				return new Dot(inheritedProps, props, ctx);
			if(ename.equals("area"))
				return new Area(inheritedProps, props, ctx);
			if(ename.equals("label"))
				return new Label(inheritedProps, props, ctx);
		}
		throw RuntimeExceptionFactory.illegalArgument(c, ctx.getCurrentAST(), ctx.getStackTrace());
	}
}
