/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Atze van der Ploeg - Atze.van.der.Ploeg@cwi.nl - CWI
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
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureColorUtils;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.IFigureApplet;

public class ComputedProperties {

	public static abstract class ComputedProperty<PropType> extends PropertyValue<PropType> {

		IFigureApplet fpa;
		
		IValue fun;
		PropType value;

		public ComputedProperty(Properties property,IValue fun, IFigureApplet fpa){
			super(property);
			this.fun = fun;
			this.fpa = fpa;
		}
		
		abstract PropType convertValue(IValue res);
		
		public void compute() {
			Result<IValue> res = fpa.executeRascalCallBackWithoutArguments(fun);
			value = convertValue(res.getValue());
			fpa.setComputedValueChanged();
		}
		
		public PropType getValue() {
			return value;
		}	
	}
	

	public static class ComputedRealProperty extends ComputedProperty<Double>{

		public ComputedRealProperty(Properties property,IValue fun, IFigureApplet fpa) {
			super(property,fun, fpa);
		}

		static Double convertValueS(IValue res){
			if(res.getType().isIntegerType())
				return (double)((IInteger) res).intValue();
			else if(res.getType().isRealType())
				return ((IReal) res).doubleValue();
			else
				return ((INumber) res).toReal().doubleValue();
		}
		
		Double convertValue(IValue res){
			return convertValueS(res);
		}
	}
	
	public static class ComputedStringProperty extends ComputedProperty<String>{

		public ComputedStringProperty(Properties property,IValue fun, IFigureApplet fpa) {
			super(property,fun, fpa);
		}

		static String convertValueS(IValue res){
			return ((IString) res).getValue();
		}
		
		@Override
		String convertValue(IValue res) {
			return convertValueS(res);
		}

	}
	
	public static class ComputedBooleanProperty extends ComputedProperty<Boolean>{

		public ComputedBooleanProperty(Properties property,IValue fun, IFigureApplet fpa) {
			super(property,fun, fpa);
		}

		static Boolean convertValueS(IValue res){
			return ((IBool) res).getValue();
		}
		
		@Override
		Boolean convertValue(IValue res) {
			return convertValueS(res);
		}

	}
	
	public static class ComputedIntegerProperty extends ComputedProperty<Integer>{

		public ComputedIntegerProperty(Properties property,IValue fun, IFigureApplet fpa) {
			super(property,fun, fpa);
		}

		static Integer convertValueS(IValue res){
			return ((IInteger) res).intValue();
		}
		
		@Override
		Integer convertValue(IValue res) {
			return convertValueS(res);
		}

	}
	
	public static class ComputedColorProperty extends ComputedIntegerProperty{
		public ComputedColorProperty(Properties property,IValue fun, IFigureApplet fpa) {
			super(property,fun, fpa);
		}
		
		static Integer convertValueS(IValue res){
			if (res.getType().isStringType()) {
				String s = ((IString) res).getValue().toLowerCase();
				if(s.length() == 0)
					s = "black";
				IInteger cl = FigureColorUtils.colorNames.get(s);
				if (cl != null){
					return cl.intValue();
				} else {
					return 0;
				}
			} else {
				return ComputedIntegerProperty.convertValueS(res);
			}
		}
		
		@Override
		Integer convertValue(IValue res) {
			return convertValueS(res);
		}

	}
	
	public static class ComputedFigureProperty extends ComputedProperty<Figure>{
		PropertyManager parentPm;
		IFigureApplet fpa;
		IEvaluatorContext ctx;
		
		public ComputedFigureProperty(Properties property,IValue fun, IFigureApplet fpa,PropertyManager parentPm, IEvaluatorContext ctx) {
			super(property,fun, fpa);
			this.fpa = fpa;
			this.parentPm = parentPm;
			this.ctx = ctx;
		}
		
		static Figure convertValueS(IFigureApplet fpa,IValue res,PropertyManager parentPm, IEvaluatorContext ctx) {
			Figure fig = FigureFactory.make(fpa, ((IConstructor) res), parentPm, null, ctx);
			fig.bbox();
			return fig;
		}

		@Override
		Figure convertValue(IValue res) {
			return convertValueS(fpa,res,parentPm,ctx);
		}
		
		public synchronized void compute() {
			if(value!=null){
				value.destroy();
			}
			super.compute();
		}
		
		
	}
	
	public static class HandlerProperty extends ComputedProperty<Void>{

		public HandlerProperty(Properties property,IValue fun, IFigureApplet fpa) {
			super(property,fun, fpa);
		}

		@Override
		Void convertValue(IValue res) {
			return null;
		}
		@Override
		public IValue executeWithSingleArg(Type type,IValue arg){
			return fpa.executeRascalCallBackSingleArgument(fun, type, arg).getValue();
		}
		
		public void  executeVoid(Type[] types,IValue[] args){
			fpa.executeRascalCallBack(fun, types, args);
		}
		
		@Override
		public IValue execute(Type[] types,IValue[] args){
			return fpa.executeRascalCallBack(fun, types, args).getValue();
		}
	}
}

