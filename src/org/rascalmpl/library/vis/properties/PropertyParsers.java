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
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureColorUtils;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.descriptions.BoolProp;
import org.rascalmpl.library.vis.properties.descriptions.ColorProp;
import org.rascalmpl.library.vis.properties.descriptions.FigureProp;
import org.rascalmpl.library.vis.properties.descriptions.HandlerProp;
import org.rascalmpl.library.vis.properties.descriptions.IntProp;
import org.rascalmpl.library.vis.properties.descriptions.RealProp;
import org.rascalmpl.library.vis.properties.descriptions.StrProp;

/**
 * Utilities for fetching arguments from property values. Arguments come in three flavours:
 * - a value of an expected type, e.g., an integer argument. A Constant<TYPE>Property is returned
 * - a "like" argument that refers to a property of another figure, e.g. like("fig1"). A Like<TYPE>Property is returned.
 * - a callback, a Computed<TYPE>Property is returned.
 * 
 * @author paulklint
 *
 */
public class PropertyParsers {
	interface PropertyParser<Prop,PropValue>{
		IPropertyValue<PropValue> parseProperty(Prop prop,IConstructor c, PropertyManager pm, int propIndex,
				IFigureApplet fpa, IEvaluatorContext ctx);
	}
	
	static abstract class AbstractPropertyParser<Prop,PropValue> implements PropertyParser<Prop,PropValue>{
		
		abstract boolean isLiteralType(Type type);
		
		abstract IPropertyValue<PropValue> makeConstantProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx);
		
		abstract IPropertyValue<PropValue> makeLikeProperty(Prop prop,String id,IFigureApplet fpa,IEvaluatorContext ctx);
		
		abstract IPropertyValue<PropValue> makeComputedProperty(IValue arg,PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx);
		
		public IPropertyValue<PropValue> parseProperty(Prop prop,IConstructor c, PropertyManager pm, int propIndex,
				IFigureApplet fpa, IEvaluatorContext ctx) {
			IValue arg = c.get(propIndex);
			
			if(isLiteralType(arg.getType()))
				return makeConstantProperty(arg, pm, fpa, ctx);
			
			if(arg.getType().isAbstractDataType()){
				IConstructor cs = (IConstructor) arg;
				if(cs.getName().equals("like")){
					return makeLikeProperty(prop, ((IString) cs.get(0)).getValue(), fpa, ctx);
				}
			}
			
			if(arg.getType().isExternalType() && ((arg instanceof RascalFunction) || (arg instanceof OverloadedFunctionResult))){
				return makeComputedProperty(arg, pm, fpa, ctx);
			}
			throw RuntimeExceptionFactory.illegalArgument(arg, ctx.getCurrentAST(), ctx.getStackTrace());
		}
	}
	
	/**
	 * Get a boolean argument
	 * @param prop	The desired property.
	 * @param c		The constructor of the property, e.g. shapeConnected(true)
	 * @param fpa	The IFigureApplet
	 * @param ctx	The evaulator context (to generate exceptions)
	 * @return
	 */
	static class BooleanArgParser extends AbstractPropertyParser<BoolProp,Boolean>{
		@Override
		boolean isLiteralType(Type type) {
			return type.isBoolType();
		}

		@Override
		IPropertyValue<Boolean> makeConstantProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			return new ConstantProperties.ConstantBooleanProperty(((IBool) arg).getValue());
		}

		@Override
		IPropertyValue<Boolean> makeLikeProperty(BoolProp prop, String id,
				IFigureApplet fpa, IEvaluatorContext ctx) {
			return  new LikeProperties.LikeBooleanProperty(prop, id, fpa, ctx);
		}

		@Override
		IPropertyValue<Boolean> makeComputedProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			return new ComputedProperties.ComputedBooleanProperty(arg, fpa);
		}
	} 
	/**
	 * Get an integer argument
	 * @param prop	The desired property.
	 * @param c		The constructor of the property, e.g. lineWidth(10)
	 * @param fpa	The IFigureApplet
	 * @param ctx	The evaulator context (to generate exceptions)
	 * @return
	 */
	static class IntegerArgParser extends AbstractPropertyParser<IntProp,Integer>{
		@Override
		boolean isLiteralType(Type type) {
			return type.isIntegerType();
		}

		@Override
		IPropertyValue<Integer> makeConstantProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			return new ConstantProperties.ConstantIntegerProperty(((IInteger) arg).intValue());
		}

		@Override
		IPropertyValue<Integer> makeLikeProperty(IntProp prop, String id,
				IFigureApplet fpa, IEvaluatorContext ctx) {
			return new LikeProperties.LikeIntegerProperty(prop, id, fpa, ctx);
		}

		@Override
		IPropertyValue<Integer> makeComputedProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			return new ComputedProperties.ComputedIntegerProperty(arg, fpa);
		}
	}
	/**
	 * Get a string argument
	 * @param prop	The desired property.
	 * @param c		The constructor of the property, e.g. fontName("Helvetica")
	 * @param fpa	The IFigureApplet
	 * @param ctx	The evaulator context (to generate exceptions)
	 * @return
	 */
	public static class StringArgParser extends AbstractPropertyParser<StrProp,String>{
		@Override
		boolean isLiteralType(Type type) {
			return type.isStringType();
		}

		@Override
		IPropertyValue<String> makeConstantProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			return new ConstantProperties.ConstantStringProperty(((IString) arg).getValue());
		}

		@Override
		IPropertyValue<String> makeLikeProperty(StrProp prop, String id,
				IFigureApplet fpa, IEvaluatorContext ctx) {
			return new LikeProperties.LikeStringProperty(prop, id, fpa, ctx);
		}

		@Override
		IPropertyValue<String> makeComputedProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			return new ComputedProperties.ComputedStringProperty(arg, fpa);
		}
	}
	
	/**
	 * Get a real argument
	 * @param prop	The desired property.
	 * @param c		The constructor of the property, e.g. width(25.7)
	 * @param fpa	The IFigureApplet
	 * @param ctx	The evaulator context (to generate exceptions)
	 * @return
	 */
	static class RealArgParser extends AbstractPropertyParser<RealProp,Float>{
		@Override
		boolean isLiteralType(Type type) {
			return type.isRealType();
		}

		@Override
		IPropertyValue<Float> makeConstantProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			return new ConstantProperties.ConstantRealProperty(((IReal) arg).floatValue());
		}

		@Override
		IPropertyValue<Float> makeLikeProperty(RealProp prop, String id,
				IFigureApplet fpa, IEvaluatorContext ctx) {
			return new LikeProperties.LikeRealProperty(prop, id, fpa, ctx);
		}

		@Override
		IPropertyValue<Float> makeComputedProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			return new ComputedProperties.ComputedRealProperty(arg, fpa);
		}
	}
	
	/**
	 * Get an integer or real argument
	 * @param prop	The desired property.
	 * @param c		The constructor of the property, e.g. lineWidth(10)
	 * @param i		The argument position of the argument in the constructor
	 * @param fpa	The IFigureApplet
	 * @param ctx	The evaulator context (to generate exceptions)
	 * @return
	 */
	static class IntOrRealArgParser extends RealArgParser{
		
		@Override
		boolean isLiteralType(Type type) {
			return type.isIntegerType() || type.isRealType();
		}
		
		@Override
		IPropertyValue<Float> makeConstantProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			float value;
			if(arg.getType().isIntegerType()){
				value = ((IInteger) arg).intValue();
			} else {
				value = ((IReal) arg).floatValue();
			}
			return new ConstantProperties.ConstantRealProperty(value);
		}
	}

	/**
	 * Get an color argument
	 * @param prop	The desired property.
	 * @param c		The constructor of the property, e.g. fillColor("blue" or fillColor(color(0,0,255))
	 * @param fpa	The IFigureApplet
	 * @param ctx	The evaulator context (to generate exceptions)
	 * @return
	 */
	static class ColorArgParser extends AbstractPropertyParser<ColorProp,Integer>{
		
		@Override
		boolean isLiteralType(Type type) {
			return type.isIntegerType() || type.isStringType();
		}
		
		@Override
		IPropertyValue<Integer> makeConstantProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			if (arg.getType().isStringType()) {
				String s = ((IString) arg).getValue().toLowerCase();
				if(s.length() == 0)
					s = "black";
				IInteger cl = FigureColorUtils.colorNames.get(s);
				int value;
				if (cl != null){
					value = cl.intValue();
				} else {
					value = 0;
				}
				return new ConstantProperties.ConstantColorProperty(value);
			}
			
			if (arg.getType().isIntegerType())
				return new ConstantProperties.ConstantColorProperty(((IInteger) arg).intValue());
			return null; // cannot happen
		}

		@Override
		IPropertyValue<Integer> makeLikeProperty(ColorProp prop, String id,
				IFigureApplet fpa, IEvaluatorContext ctx) {
			return new LikeProperties.LikeColorProperty(prop, id, fpa, ctx);
		}

		@Override
		IPropertyValue<Integer> makeComputedProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			return new ComputedProperties.ComputedColorProperty(arg, fpa);
		}
	}
	
	static class FigureArgParser extends AbstractPropertyParser<FigureProp,Figure>{
		
		@Override
		boolean isLiteralType(Type type) {
			return type.isAbstractDataType() &&  type.getName().equals("Figure"); 
		}
		
		@Override
		IPropertyValue<Figure> makeConstantProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			
			Figure fig =  FigureFactory.make(fpa, ((IConstructor) arg), pm, null, ctx);
			return new ConstantProperties.ConstantFigureProperty(fig); 
		}

		@Override
		IPropertyValue<Figure> makeLikeProperty(FigureProp prop, String id,
				IFigureApplet fpa, IEvaluatorContext ctx) {
			return new LikeProperties.LikeFigureProperty(id, fpa, ctx);
		}

		@Override
		IPropertyValue<Figure> makeComputedProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			return new ComputedProperties.ComputedFigureProperty(arg, fpa, pm, ctx);
		}
	}
	
	static class HandlerArgParser implements PropertyParser<HandlerProp,Void>{

		@Override
		public IPropertyValue<Void> parseProperty(HandlerProp prop,
				IConstructor c, PropertyManager pm, int propIndex,
				IFigureApplet fpa, IEvaluatorContext ctx) {
			 return new ComputedProperties.HandlerProperty(c.get(0),fpa);
		}
		
	}
	
}
