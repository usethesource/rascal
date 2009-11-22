package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;
import org.meta_environment.values.ValueFactoryFactory;

public class VELEMFactory {
	static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	static IList emptyList = vf.list();
	
	public static VELEM make(VLPApplet vlp, IConstructor c, HashMap<String,IValue> inheritedProps, IEvaluatorContext ctx){
		String ename = c.getName();
		System.err.println("ename = " + ename);
		IList props;
		IList elems;
		if(ename.equals("combine") || ename.equals("overlay") || ename.equals("grid")){
			if(c.arity() == 2){
				props = (IList) c.get(0);
				elems = (IList) c.get(1);
			} else {
				props = emptyList;
				elems = (IList) c.get(0);
			}
			return ename.equals("combine") ? new Combine(vlp, inheritedProps, props, elems, ctx)
			                                : (ename.equals("overlay") ? new Overlay(vlp, inheritedProps, props, elems, ctx)
			                                                           : new Grid(vlp, inheritedProps, props, elems, ctx));
		}
		if(ename.equals("graph") && c.arity() == 3){
			props = (IList) c.get(0);
			return new Graph(vlp,inheritedProps, props, (IList) c.get(1), (IList)c.get(2), ctx);
		}
		if(c.arity() == 1){ // basic forms
			if(ename.equals("space"))
				return new Space(vlp, (IInteger) c.get(0), ctx);
			props = (IList) c.get(0);
			if(ename.equals("rect"))
				return new Rect(vlp, inheritedProps, props, ctx);
			if(ename.equals("line"))
				return new Line(vlp, inheritedProps, props, ctx);
			if(ename.equals("dot"))
				return new Dot(vlp, inheritedProps, props, ctx);
			if(ename.equals("area"))
				return new Area(vlp, inheritedProps, props, ctx);
			if(ename.equals("label"))
				return new Label(vlp, inheritedProps, props, ctx);
		}
		throw RuntimeExceptionFactory.illegalArgument(c, ctx.getCurrentAST(), ctx.getStackTrace());
	}
	
	public static Edge makeEdge(VLPApplet vlp, IConstructor c, HashMap<String,IValue> inheritedProps, IEvaluatorContext ctx){
		String ename = c.getName();
		if(ename.equals("edge") && c.arity() == 3){
			IList props = (IList) c.get(0);
			return new Edge(vlp,inheritedProps, props, (IString)c.get(1), (IString)c.get(2), ctx);
		}
		throw RuntimeExceptionFactory.illegalArgument(c, ctx.getCurrentAST(), ctx.getStackTrace());
	}
	
//	public static Part makePart(VLPApplet vlp, IConstructor c, HashMap<String,IValue> inheritedProps, IEvaluatorContext ctx){
//		String ename = c.getName();
//		if(ename.equals("part") && c.arity() == 2){
//			String name = ((IString) c.get(0)).getValue();
//			VELEM ve = make(vlp, (IConstructor)c.get(1), inheritedProps, ctx);
//			Part nd = new Part(name, ve);
//			vlp.register(name, nd);
//			return nd;
//		}
//		throw RuntimeExceptionFactory.illegalArgument(c, ctx.getCurrentAST(), ctx.getStackTrace());
//	}

}
