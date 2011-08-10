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
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.FigureFactory;
import org.rascalmpl.library.vis.swt.ICallbackEnv;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.FigureColorUtils;

public class ComputedProperties {

	public static abstract class ComputedProperty<PropType> extends PropertyValue<PropType> {
		
		IValue fun;
		PropType value;

		public ComputedProperty(Properties property,IValue fun){
			super(property);
			this.fun = fun;
		}
		
		abstract PropType convertValue(IValue res);
		
		public void compute(ICallbackEnv env) {
			Result<IValue> res = env.executeRascalCallBackWithoutArguments(fun);
			value = convertValue(res.getValue());
		}
		
		public PropType getValue() {
			return value;
		}	
	}
	

	public static class ComputedRealProperty extends ComputedProperty<Double>{

		public ComputedRealProperty(Properties property,IValue fun) {
			super(property,fun);
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

		public ComputedStringProperty(Properties property,IValue fun) {
			super(property,fun);
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

		public ComputedBooleanProperty(Properties property,IValue fun) {
			super(property,fun);
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

		public ComputedIntegerProperty(Properties property,IValue fun) {
			super(property,fun);
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
		public ComputedColorProperty(Properties property,IValue fun) {
			super(property,fun);
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
		IFigureConstructionEnv fpa;
		
		public ComputedFigureProperty(Properties property,IValue fun, PropertyManager parentPm) {
			super(property,fun);
			this.parentPm = parentPm;
		}
		
		static Figure convertValueS(IFigureConstructionEnv fpa,IValue res,PropertyManager parentPm) {
			Figure fig = FigureFactory.make(fpa, ((IConstructor) res), parentPm, null);
			fig.bbox();
			return fig;
		}

		@Override
		Figure convertValue(IValue res) {
			return convertValueS(fpa,res,parentPm);
		}
		
		public synchronized void compute(ICallbackEnv env) {
			if(value!=null){
				value.destroy();
			}
			super.compute(env);
		}
		
		
	}
	
	public static class HandlerProperty extends ComputedProperty<Void>{

		public HandlerProperty(Properties property,IValue fun) {
			super(property,fun);
		}

		@Override
		Void convertValue(IValue res) {
			return null;
		}
		@Override
		public IValue executeWithSingleArg(ICallbackEnv env,Type type,IValue arg){
			return env.executeRascalCallBackSingleArgument(fun, type, arg).getValue();
		}
		
		public void  executeVoid(ICallbackEnv env,Type[] types,IValue[] args){
			env.executeRascalCallBack(fun, types, args);
		}
		
		@Override
		public IValue execute(ICallbackEnv env,Type[] types,IValue[] args){
			return env.executeRascalCallBack(fun, types, args).getValue();
		}
	}
}

