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
import org.eclipse.imp.pdb.facts.INumber;
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
	interface PropertyParser<PropValue>{
		PropertyValue<PropValue> parseProperty(IConstructor c, PropertyManager pm, int propIndex,
				IFigureApplet fpa, IEvaluatorContext ctx);
	}
	
	static abstract class AbstractPropertyParser<PropValue> implements PropertyParser<PropValue>{
		
		Properties property;
		
		public AbstractPropertyParser(Properties property) {
			this.property = property;
		}
		
		abstract boolean isLiteralType(Type type);
		
		abstract PropertyValue<PropValue> makeConstantProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx);
		
		abstract PropertyValue<PropValue> makeLikeProperty(String id,IFigureApplet fpa,IEvaluatorContext ctx);
		
		abstract PropertyValue<PropValue> makeComputedProperty(IValue arg,PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx);
		
		public PropertyValue<PropValue> parseProperty(IConstructor c, PropertyManager pm, int propIndex,
				IFigureApplet fpa, IEvaluatorContext ctx) {
			IValue arg = c.get(propIndex);
			
			if(isLiteralType(arg.getType()))
				return makeConstantProperty(arg, pm, fpa, ctx);
			
			if(arg.getType().isAbstractDataType()){
				IConstructor cs = (IConstructor) arg;
				if(cs.getName().equals("like")){
					return makeLikeProperty(((IString) cs.get(0)).getValue(), fpa, ctx);
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
	static class BooleanArgParser extends AbstractPropertyParser<Boolean>{
		
		public BooleanArgParser(Properties property) {
			super(property);
		}

		@Override
		boolean isLiteralType(Type type) {
			return type.isBoolType();
		}

		@Override
		PropertyValue<Boolean> makeConstantProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			return new ConstantProperties.ConstantBooleanProperty(property,((IBool) arg).getValue());
		}

		@Override
		PropertyValue<Boolean> makeLikeProperty( String id,
				IFigureApplet fpa, IEvaluatorContext ctx) {
			return  new LikeProperties.LikeBooleanProperty(property, id, fpa, ctx);
		}

		@Override
		PropertyValue<Boolean> makeComputedProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			return new ComputedProperties.ComputedBooleanProperty(property,arg, fpa);
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
	static class IntegerArgParser extends AbstractPropertyParser<Integer>{
		public IntegerArgParser(Properties property) {
			super(property);
		}

		@Override
		boolean isLiteralType(Type type) {
			return type.isIntegerType();
		}

		@Override
		PropertyValue<Integer> makeConstantProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			return new ConstantProperties.ConstantIntegerProperty(property,((IInteger) arg).intValue());
		}

		@Override
		PropertyValue<Integer> makeLikeProperty(String id,
				IFigureApplet fpa, IEvaluatorContext ctx) {
			return new LikeProperties.LikeIntegerProperty(property, id, fpa, ctx);
		}

		@Override
		PropertyValue<Integer> makeComputedProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			return new ComputedProperties.ComputedIntegerProperty(property,arg, fpa);
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
	public static class StringArgParser extends AbstractPropertyParser<String>{
		public StringArgParser(Properties property) {
			super(property);
		}

		@Override
		boolean isLiteralType(Type type) {
			return type.isStringType();
		}

		@Override
		PropertyValue<String> makeConstantProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			return new ConstantProperties.ConstantStringProperty(property,((IString) arg).getValue());
		}

		@Override
		PropertyValue<String> makeLikeProperty(String id,
				IFigureApplet fpa, IEvaluatorContext ctx) {
			return new LikeProperties.LikeStringProperty(property, id, fpa, ctx);
		}

		@Override
		PropertyValue<String> makeComputedProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			return new ComputedProperties.ComputedStringProperty(property,arg, fpa);
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
	static class RealArgParser extends AbstractPropertyParser<Double>{
		public RealArgParser(Properties property) {
			super(property);
		}

		@Override
		boolean isLiteralType(Type type) {
			return type.isRealType();
		}

		@Override
		PropertyValue<Double> makeConstantProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			return new ConstantProperties.ConstantRealProperty(property,(double) ((IReal) arg).floatValue());
		}

		@Override
		PropertyValue<Double> makeLikeProperty(String id,
				IFigureApplet fpa, IEvaluatorContext ctx) {
			return new LikeProperties.LikeRealProperty(property, id, fpa, ctx);
		}

		@Override
		PropertyValue<Double> makeComputedProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			return new ComputedProperties.ComputedRealProperty(property,arg, fpa);
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
		
		public IntOrRealArgParser(Properties property) {
			super(property);
		}

		@Override
		boolean isLiteralType(Type type) {
			return type.isIntegerType() || type.isRealType() || type.isNumberType();
		}
		
		@Override
		PropertyValue<Double> makeConstantProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			double value;
			value = parseNum(arg);
			return new ConstantProperties.ConstantRealProperty(property,value);
		}
	}
	
	public static double parseNum(IValue arg) {
		double value;
		if(arg.getType().isIntegerType()){
			value = ((IInteger) arg).intValue();
		} else if(arg.getType().isRealType()){
			value = ((IReal) arg).doubleValue();
		} else { //  if(arg.getType().isNumberType()){
			value = ((INumber)arg).toReal().doubleValue();
		}
		return value;
	}

	public static Measure parseMeasure(IValue arg) {
		Measure val;
		if(arg.getType().isIntegerType()){
			val = new Measure(((IInteger) arg).intValue());
		} else if(arg.getType().isRealType()) {
			val = new Measure(((IReal) arg).floatValue());
		} else { // measure
			IConstructor cons = (IConstructor) arg;
			float quantity;
			if(cons.get(0).getType().isIntegerType()){
				quantity = ((IInteger) cons.get(0)).intValue();
			} else {
				quantity = ((IReal) cons.get(0)).floatValue();
			}
			String id = ((IString)cons.get(1)).getValue();
			val = new Measure(quantity,id);
		}
		return val;
	}

	/**
	 * Get an color argument
	 * @param prop	The desired property.
	 * @param c		The constructor of the property, e.g. fillColor("blue" or fillColor(color(0,0,255))
	 * @param fpa	The IFigureApplet
	 * @param ctx	The evaulator context (to generate exceptions)
	 * @return
	 */
	static class ColorArgParser extends AbstractPropertyParser<Integer>{
		
		public ColorArgParser(Properties property) {
			super(property);
		}

		@Override
		boolean isLiteralType(Type type) {
			return type.isIntegerType() || type.isStringType();
		}
		
		@Override
		PropertyValue<Integer> makeConstantProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
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
				return new ConstantProperties.ConstantColorProperty(property,value);
			}
			
			if (arg.getType().isIntegerType())
				return new ConstantProperties.ConstantColorProperty(property,((IInteger) arg).intValue());
			return null; // cannot happen
		}

		@Override
		PropertyValue<Integer> makeLikeProperty(String id,
				IFigureApplet fpa, IEvaluatorContext ctx) {
			return new LikeProperties.LikeColorProperty(property, id, fpa, ctx);
		}

		@Override
		PropertyValue<Integer> makeComputedProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			return new ComputedProperties.ComputedColorProperty(property,arg, fpa);
		}
	}
	
	
	static class MeasureArgParser extends AbstractPropertyParser<Measure>{
		public MeasureArgParser(Properties property) {
			super(property);
		}

		@Override
		boolean isLiteralType(Type type) {
			return  type.isIntegerType() || type.isRealType() || (type.isAbstractDataType() && type.getName().equals("Measure"));
		}

		@Override
		PropertyValue<Measure> makeConstantProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			Measure val = parseMeasure(arg);
			return new ConstantProperties.ConstantMeasureProperty(property,val);
		}

		@Override
		PropertyValue<Measure> makeLikeProperty(String id,
				IFigureApplet fpa, IEvaluatorContext ctx) {
			return new LikeProperties.LikeMeasureProperty(property, id, fpa, ctx);
		}

		@Override
		PropertyValue<Measure> makeComputedProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			return new ComputedProperties.ComputedMeasureProperty(property,arg, fpa);
		}
	}
	
	static class FigureArgParser extends AbstractPropertyParser<Figure>{
		
		public FigureArgParser(Properties property) {
			super(property);
			// TODO Auto-generated constructor stub
		}

		@Override
		boolean isLiteralType(Type type) {
			return type.isAbstractDataType() &&  type.getName().equals("Figure"); 
		}
		
		@Override
		PropertyValue<Figure> makeConstantProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			
			Figure fig =  FigureFactory.make(fpa, ((IConstructor) arg), pm, null, ctx);
			return new ConstantProperties.ConstantFigureProperty(property,fig); 
		}

		@Override
		PropertyValue<Figure> makeLikeProperty(String id,
				IFigureApplet fpa, IEvaluatorContext ctx) {
			return new LikeProperties.LikeFigureProperty(property,id, fpa, ctx);
		}

		@Override
		PropertyValue<Figure> makeComputedProperty(IValue arg, PropertyManager pm, IFigureApplet fpa, IEvaluatorContext ctx) {
			return new ComputedProperties.ComputedFigureProperty(property,arg, fpa, pm, ctx);
		}
	}
	
	static class HandlerArgParser implements PropertyParser<Void>{

		Properties property;
		
		public HandlerArgParser(Properties property) {
			this.property = property;
		}
		
		@Override
		public PropertyValue<Void> parseProperty(
				IConstructor c, PropertyManager pm, int propIndex,
				IFigureApplet fpa, IEvaluatorContext ctx) {
			 return new ComputedProperties.HandlerProperty(property,c.get(0),fpa);
		}
		
	}
	
}
