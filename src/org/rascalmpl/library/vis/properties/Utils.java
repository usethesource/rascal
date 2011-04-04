/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.vis.properties;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.FigureColorUtils;
import org.rascalmpl.library.vis.FigurePApplet;

/**
 * Utilities for fetching arguments from property values. Arguments come in three flavours:
 * - a value of an expected type, e.g., an integer argument. A Constant<TYPE>Property is returned
 * - a "like" argument that refers to a property of another figure, e.g. like("fig1"). A Like<TYPE>Property is returned.
 * - a callback, a Computed<TYPE>Property is returned.
 * 
 * @author paulklint
 *
 */
public class Utils {
	
	/**
	 * Get a boolean argument
	 * @param prop	The desired property.
	 * @param c		The constructor of the property, e.g. shapeConnected(true)
	 * @param fpa	The FigurePApplet
	 * @param ctx	The evaulator context (to generate exceptions)
	 * @return
	 */
	public static IBooleanPropertyValue getBooleanArg(Property prop, IConstructor c, FigurePApplet fpa, IEvaluatorContext ctx){
		IValue arg = c.get(0);

		if(arg.getType().isBoolType())
			return new ConstantBooleanProperty(prop, ((IBool) arg).getValue());
		
		if(arg.getType().isAbstractDataType()){
			IConstructor cs = (IConstructor) arg;
			if(cs.getName().equals("like")){
				return new LikeBooleanProperty(prop, ((IString) cs.get(0)).getValue(), fpa, ctx);
			}
		}
		
		if(arg.getType().isExternalType() && ((arg instanceof RascalFunction) || (arg instanceof OverloadedFunctionResult))){
			return new ComputedBooleanProperty(prop, arg, fpa);
		}
		throw RuntimeExceptionFactory.illegalArgument(arg, ctx.getCurrentAST(), ctx.getStackTrace());
	}
	
	/**
	 * Get an integer argument
	 * @param prop	The desired property.
	 * @param c		The constructor of the property, e.g. lineWidth(10)
	 * @param fpa	The FigurePApplet
	 * @param ctx	The evaulator context (to generate exceptions)
	 * @return
	 */
	public static IIntegerPropertyValue getIntArg(Property prop, IConstructor c, FigurePApplet fpa, IEvaluatorContext ctx){
		IValue arg = c.get(0);

		if(arg.getType().isIntegerType())
			return new ConstantIntegerProperty(prop, ((IInteger) arg).intValue());
		
		if(arg.getType().isAbstractDataType()){
			IConstructor cs = (IConstructor) arg;
			if(cs.getName().equals("like")){
				return new LikeIntegerProperty(prop, ((IString) cs.get(0)).getValue(), fpa, ctx);
			}
		}
		
		if(arg.getType().isExternalType() && ((arg instanceof RascalFunction) || (arg instanceof OverloadedFunctionResult))){
			return new ComputedIntegerProperty(prop, arg, fpa);
		}
		throw RuntimeExceptionFactory.illegalArgument(arg, ctx.getCurrentAST(), ctx.getStackTrace());
	}
	/**
	 * Get a string argument
	 * @param prop	The desired property.
	 * @param c		The constructor of the property, e.g. fontName("Helvetica")
	 * @param fpa	The FigurePApplet
	 * @param ctx	The evaulator context (to generate exceptions)
	 * @return
	 */
	public static IStringPropertyValue getStrArg(Property prop, IConstructor c, FigurePApplet fpa, IEvaluatorContext ctx){
		IValue arg = c.get(0);
		
		if(arg.getType().isStringType())
			return new ConstantStringProperty(prop, ((IString) arg).getValue());
		
		if(arg.getType().isAbstractDataType()){
			IConstructor cs = (IConstructor) arg;
			if(cs.getName().equals("like")){
				return new LikeStringProperty(prop, ((IString) cs.get(0)).getValue(), fpa, ctx);
			}
		}
		
		if(arg.getType().isExternalType() && ((arg instanceof RascalFunction) || (arg instanceof OverloadedFunctionResult))){
			return new ComputedStringProperty(prop, arg, fpa);
		}
		
		throw RuntimeExceptionFactory.illegalArgument(arg, ctx.getCurrentAST(),
				ctx.getStackTrace());
	}
	
	/**
	 * Get a real argument
	 * @param prop	The desired property.
	 * @param c		The constructor of the property, e.g. width(25.7)
	 * @param fpa	The FigurePApplet
	 * @param ctx	The evaulator context (to generate exceptions)
	 * @return
	 */
	
	public static IRealPropertyValue getRealArg(Property prop, IConstructor c, int i, FigurePApplet fpa, IEvaluatorContext ctx){
		IValue arg = c.get(i);
		
		if(arg.getType().isRealType())
			return new ConstantRealProperty(prop, ((IReal) arg).floatValue());
		
		if(arg.getType().isAbstractDataType()){
			IConstructor cs = (IConstructor) arg;
			if(cs.getName().equals("like")){
				return new LikeRealProperty(prop, ((IString) cs.get(0)).getValue(), fpa, ctx);
			}
		}
		
		if(arg.getType().isExternalType() && ((arg instanceof RascalFunction) || (arg instanceof OverloadedFunctionResult))){
			return new ComputedRealProperty(prop, arg, fpa);
		}
		throw RuntimeExceptionFactory.illegalArgument(arg, ctx.getCurrentAST(),
				ctx.getStackTrace());
	}
	
	/**
	 * Get an integer or real argument
	 * @param prop	The desired property.
	 * @param c		The constructor of the property, e.g. lineWidth(10)
	 * @param i		The argument position of the argument in the constructor
	 * @param fpa	The FigurePApplet
	 * @param ctx	The evaulator context (to generate exceptions)
	 * @return
	 */
	
	public static IRealPropertyValue getIntOrRealArg(Property prop, IConstructor c, int i, FigurePApplet fpa, IEvaluatorContext ctx){
		IValue arg = c.get(i);
		
		if(arg.getType().isIntegerType())
			return new ConstantRealProperty(prop, ((IInteger) arg).intValue());
		
		if(arg.getType().isRealType())
			return new ConstantRealProperty(prop, ((IReal) arg).floatValue());
		
		if(arg.getType().isAbstractDataType()){
			IConstructor cs = (IConstructor) arg;
			if(cs.getName().equals("like")){
				return new LikeRealProperty(prop, ((IString) cs.get(0)).getValue(), fpa, ctx);
			}
		}
		
		if(arg.getType().isExternalType() && ((arg instanceof RascalFunction) || (arg instanceof OverloadedFunctionResult))){
			return new ComputedRealProperty(prop, arg, fpa);
		}
		
		throw RuntimeExceptionFactory.illegalArgument(arg, ctx.getCurrentAST(),
				ctx.getStackTrace());
	}

	/**
	 * Get an color argument
	 * @param prop	The desired property.
	 * @param c		The constructor of the property, e.g. fillColor("blue" or fillColor(color(0,0,255))
	 * @param fpa	The FigurePApplet
	 * @param ctx	The evaulator context (to generate exceptions)
	 * @return
	 */
	public static IColorPropertyValue getColorArg(Property prop, IConstructor c, FigurePApplet fpa, IEvaluatorContext ctx) {
		
		IValue arg = c.get(0);
		
		if(arg.getType().isExternalType() && ((arg instanceof RascalFunction) || (arg instanceof OverloadedFunctionResult))){
			return new ComputedColorProperty(prop, arg, fpa);
		}
		
		if (arg.getType().isStringType()) {
			String s = ((IString) arg).getValue().toLowerCase();
			if(s.length() == 0)
				s = "black";
			IInteger cl = FigureColorUtils.colorNames.get(s);
			if (cl != null)
				return new ConstantColorProperty(prop, cl.intValue());
			
			throw RuntimeExceptionFactory.illegalArgument(c, ctx.getCurrentAST(),
					ctx.getStackTrace());
		}
		
		if (arg.getType().isIntegerType())
			return new ConstantColorProperty(prop, ((IInteger) arg).intValue());
		
		if(arg.getType().isAbstractDataType()){
			IConstructor cs = (IConstructor) arg;
			if(cs.getName().equals("like")){
				return new LikeColorProperty(prop, ((IString) cs.get(0)).getValue(), fpa, ctx);
			}
		}
		
		
		throw RuntimeExceptionFactory.illegalArgument(c, ctx.getCurrentAST(),
				ctx.getStackTrace());
	}
}
