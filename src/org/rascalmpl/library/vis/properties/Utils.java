package org.rascalmpl.library.vis.properties;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.FigureLibrary;
import org.rascalmpl.library.vis.FigurePApplet;

public class Utils {
	
	public static IIntegerPropertyValue getIntArg(Property prop, IValue v, FigurePApplet fpa, IEvaluatorContext ctx){
		if(v.getType().isIntegerType())
			return new ConstantIntegerProperty(prop, ((IInteger) v).intValue());
		
		if(v.getType().isExternalType() && ((v instanceof RascalFunction) || (v instanceof OverloadedFunctionResult))){
			return new ComputedIntegerProperty(prop, v, fpa);
		}
		throw RuntimeExceptionFactory.illegalArgument(v, ctx.getCurrentAST(), ctx.getStackTrace());
	}
	
	public static IStringPropertyValue getStrArg(Property prop, IValue v, FigurePApplet fpa, IEvaluatorContext ctx){
		
		if(v.getType().isStringType())
			return new ConstantStringProperty(prop, ((IString) v).getValue());

		
		if(v.getType().isExternalType() && ((v instanceof RascalFunction) || (v instanceof OverloadedFunctionResult))){
			return new ComputedStringProperty(prop, v, fpa);
		}
		
		throw RuntimeExceptionFactory.illegalArgument(v, ctx.getCurrentAST(),
				ctx.getStackTrace());
	}
	
	public static IRealPropertyValue getRealArg(Property prop, IConstructor c, int i, FigurePApplet fpa, IEvaluatorContext ctx){
		IValue arg = c.get(i);
		
		if(arg.getType().isExternalType() && ((arg instanceof RascalFunction) || (arg instanceof OverloadedFunctionResult))){
			return new ComputedRealProperty(prop, arg, fpa);
		}
		return new ConstantRealProperty(prop, ((IReal) c.get(0)).floatValue());
	}
	
	public static IRealPropertyValue getIntOrRealArg(Property prop, IConstructor c, int i, FigurePApplet fpa, IEvaluatorContext ctx){
		IValue arg = c.get(i);
		
		if(arg.getType().isExternalType() && ((arg instanceof RascalFunction) || (arg instanceof OverloadedFunctionResult))){
			return new ComputedRealProperty(prop, arg, fpa);
		}
		if(c.get(i).getType().isIntegerType())
			return new ConstantRealProperty(prop, ((IInteger) c.get(i)).intValue());
		
		return new ConstantRealProperty(prop, ((IReal) c.get(i)).floatValue());
	}

	public static IColorPropertyValue getColorArg(Property prop, IConstructor c, FigurePApplet fpa, IEvaluatorContext ctx) {
		
		IValue arg = c.get(0);
		
		if(arg.getType().isExternalType() && ((arg instanceof RascalFunction) || (arg instanceof OverloadedFunctionResult))){
			return new ComputedColorProperty(prop, arg, fpa);
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
