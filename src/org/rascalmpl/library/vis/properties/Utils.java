package org.rascalmpl.library.vis.properties;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.FigureLibrary;
import org.rascalmpl.library.vis.FigurePApplet;

public class Utils {
	
	public static IIntegerPropertyValue getIntArg(Property prop, IConstructor c, FigurePApplet fpa, IEvaluatorContext ctx){
		IValue arg = c.get(0);
		if(arg.getType().isAbstractDataType()){
			IConstructor cons = (IConstructor) arg;
			if(c.getName().equals("intVar")){
				String name = ((IString) cons.get(0)).getValue();
				new ControlIntegerProperty(prop, name, fpa);
			}
			throw RuntimeExceptionFactory.illegalArgument(c, ctx.getCurrentAST(),
					ctx.getStackTrace());
		}
		return new ConstantIntegerProperty(prop, ((IInteger) c.get(0)).intValue());
	}
	
	public static IStringPropertyValue getStrArg(Property prop, IValue v, FigurePApplet fpa, IEvaluatorContext ctx){
		if(v.getType().isStringType())
			return new ConstantStringProperty(prop, ((IString) v).getValue());
		IConstructor c = (IConstructor) v;
		if(c.getType().isAbstractDataType()){
			System.err.println("cname = " + c.getName());
			if(c.getName().equals("strVar")){
				String name = ((IString) c.get(0)).getValue();
				return new ControlStringProperty(prop, name, fpa);
			}
			throw RuntimeExceptionFactory.illegalArgument(c, ctx.getCurrentAST(),
					ctx.getStackTrace());
		}
		throw RuntimeExceptionFactory.illegalArgument(v, ctx.getCurrentAST(),
				ctx.getStackTrace());
	}
	
	public static IRealPropertyValue getRealArg(Property prop, IConstructor c, int i, FigurePApplet fpa, IEvaluatorContext ctx){
		IValue arg = c.get(i);
		if(arg.getType().isAbstractDataType()){
			IConstructor cons = (IConstructor) arg;
			if(c.getName().equals("realVar")){
				String name = ((IString) cons.get(0)).getValue();
				return new ControlRealProperty(prop, name, fpa);
			}
			throw RuntimeExceptionFactory.illegalArgument(c, ctx.getCurrentAST(),
					ctx.getStackTrace());
		}
		return new ConstantRealProperty(prop, ((IReal) c.get(0)).floatValue());
	}
	//TODO: realVar ok?
	public static IRealPropertyValue getIntOrRealArg(Property prop, IConstructor c, int i, FigurePApplet fpa, IEvaluatorContext ctx){
		IValue arg = c.get(i);
		if(arg.getType().isAbstractDataType()){
			IConstructor cons = (IConstructor) arg;
			if(c.getName().equals("realVar")){
				String name = ((IString) cons.get(0)).getValue();
				return new ControlRealProperty(prop, name, fpa);
			}
			throw RuntimeExceptionFactory.illegalArgument(c, ctx.getCurrentAST(),
					ctx.getStackTrace());
		}
		if(c.get(i).getType().isIntegerType())
			return new ConstantRealProperty(prop, ((IInteger) c.get(i)).intValue());
		
		return new ConstantRealProperty(prop, ((IReal) c.get(i)).floatValue());
	}

	//TODO Make colors also variable-dependent
	public static IIntegerPropertyValue getColorArg(Property prop, IConstructor c, IEvaluatorContext ctx) {
		IValue arg = c.get(0);
		if (arg.getType().isStringType()) {
			IInteger cl = FigureLibrary.colorNames.get(((IString) arg).getValue().toLowerCase());
			if (cl != null)
				return new ConstantIntegerProperty(prop, cl.intValue());
			
			throw RuntimeExceptionFactory.illegalArgument(c, ctx.getCurrentAST(),
					ctx.getStackTrace());
		}
		return new ConstantIntegerProperty(prop, ((IInteger) arg).intValue());
	}
	
	
}
