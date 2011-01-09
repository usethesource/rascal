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
			if(c.getName().equals("intTrigger")){
				String name = ((IString) cons.get(0)).getValue();
				new TriggeredIntegerProperty(prop, name, fpa);
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
			if(c.getName().equals("strTigger")){
				String name = ((IString) c.get(0)).getValue();
				return new TriggeredStringProperty(prop, name, fpa);
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
			if(cons.getName().equals("realTrigger")){
				String name = ((IString) cons.get(0)).getValue();
				return new TriggeredRealProperty(prop, name, fpa);
			}
			throw RuntimeExceptionFactory.illegalArgument(c, ctx.getCurrentAST(),
					ctx.getStackTrace());
		}
		return new ConstantRealProperty(prop, ((IReal) c.get(0)).floatValue());
	}
	
	public static IRealPropertyValue getIntOrRealArg(Property prop, IConstructor c, int i, FigurePApplet fpa, IEvaluatorContext ctx){
		IValue arg = c.get(i);
		
		if(arg.getType().isAbstractDataType()){
			IConstructor cons = (IConstructor) arg;
			if(cons.getName().equals("numTrigger") || cons.getName().equals("intTrigger") || cons.getName().equals("realTrigger")){
				String name = ((IString) cons.get(0)).getValue();
				return new TriggeredRealProperty(prop, name, fpa);
			}
			throw RuntimeExceptionFactory.illegalArgument(c, ctx.getCurrentAST(),
					ctx.getStackTrace());
		}
		if(c.get(i).getType().isIntegerType())
			return new ConstantRealProperty(prop, ((IInteger) c.get(i)).intValue());
		
		return new ConstantRealProperty(prop, ((IReal) c.get(i)).floatValue());
	}

	public static IColorPropertyValue getColorArg(Property prop, IConstructor c, FigurePApplet fpa, IEvaluatorContext ctx) {
		
		IValue arg = c.get(0);
		
		if(arg.getType().isAbstractDataType()){
			IConstructor cons = (IConstructor) arg;
			if(cons.getName().equals("colorTrigger")){
				String name = ((IString) cons.get(0)).getValue();
				return new TriggeredColorProperty(prop, name, fpa);
			}
			throw RuntimeExceptionFactory.illegalArgument(c, ctx.getCurrentAST(),
					ctx.getStackTrace());
		}
		
		if (arg.getType().isStringType()) {
			String s = ((IString) arg).getValue().toLowerCase();
			if(s.length() == 0)
				s = "black";
			IInteger cl = FigureLibrary.colorNames.get(s);
			if (cl != null)
				return new ConstantColorProperty(prop, cl.intValue());
			
			throw RuntimeExceptionFactory.illegalArgument(c, ctx.getCurrentAST(),
					ctx.getStackTrace());
		}
		
		if (arg.getType().isIntegerType())
			return new ConstantColorProperty(prop, ((IInteger) arg).intValue());
		
		
		throw RuntimeExceptionFactory.illegalArgument(c, ctx.getCurrentAST(),
				ctx.getStackTrace());
	}
}
