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
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.IFigureApplet;

public class ComputedProperties {

	private static abstract class ComputedProperty<PropType> implements IPropertyValue<PropType> {

		IFigureApplet fpa;
		
		IValue fun;
		PropType value;

		public ComputedProperty(IValue fun, IFigureApplet fpa){
			this.fun = fun;
			this.fpa = fpa;
		}
		
		abstract PropType convertValue(Result<IValue> res);
		
		public synchronized PropType getValue() {
			PropType old = value;
			Result<IValue> res = fpa.executeRascalCallBackWithoutArguments(fun);
			
			value = convertValue(res);
			fpa.setComputedValueChanged();
			return value;
		}	
	}
	

	static class ComputedRealProperty extends ComputedProperty<Double>{

		public ComputedRealProperty(IValue fun, IFigureApplet fpa) {
			super(fun, fpa);
		}

		Double convertValue(Result<IValue> res){
			if(res.getType().isIntegerType())
				return (double)((IInteger) res.getValue()).intValue();
			else if(res.getType().isRealType())
				return ((IReal) res.getValue()).doubleValue();
			else
				return ((INumber) res.getValue()).toReal().doubleValue();
		}
	}
	
	static class ComputedStringProperty extends ComputedProperty<String>{

		public ComputedStringProperty(IValue fun, IFigureApplet fpa) {
			super(fun, fpa);
		}

		@Override
		String convertValue(Result<IValue> res) {
			return ((IString) res.getValue()).getValue();
		}

	}
	
	static class ComputedBooleanProperty extends ComputedProperty<Boolean>{

		public ComputedBooleanProperty(IValue fun, IFigureApplet fpa) {
			super(fun, fpa);
		}

		@Override
		Boolean convertValue(Result<IValue> res) {
			return ((IBool) res.getValue()).getValue();
		}

	}
	
	static class ComputedIntegerProperty extends ComputedProperty<Integer>{

		public ComputedIntegerProperty(IValue fun, IFigureApplet fpa) {
			super(fun, fpa);
		}

		@Override
		Integer convertValue(Result<IValue> res) {
			return ((IInteger) res.getValue()).intValue();
		}

	}
	
	static class ComputedColorProperty extends ComputedIntegerProperty{
		public ComputedColorProperty(IValue fun, IFigureApplet fpa) {
			super(fun, fpa);
		}
	}
	
	static class ComputedMeasureProperty extends ComputedProperty<Measure>{

		public ComputedMeasureProperty(IValue fun, IFigureApplet fpa) {
			super(fun, fpa);
		}

		@Override
		Measure convertValue(Result<IValue> res) {
			if(res.getType().isIntegerType()){
				return new Measure((double)((IInteger)res.getValue()).intValue());
			}
			else if(res.getType().isRealType()){
				return new Measure((((IReal)res.getValue()).doubleValue()));
			}
			else if(res.getType().isNumberType()){
				return new Measure((((INumber)res.getValue()).toReal().doubleValue()));
			} else { // if(res.getType().isAbstractDataType() && res.getType().getName().equals("Measure")){
				Measure result;
				IConstructor c = (IConstructor)res.getValue();
				double val = ((INumber)c.get(0)).toReal().doubleValue();
				String name = ((IString)c.get(1)).getValue();
				return new Measure(val,name);
			} 
		}

	}
	
	static class ComputedFigureProperty extends ComputedProperty<Figure>{
		PropertyManager parentPm;
		IFigureApplet fpa;
		IEvaluatorContext ctx;
		
		public ComputedFigureProperty(IValue fun, IFigureApplet fpa,PropertyManager parentPm, IEvaluatorContext ctx) {
			super(fun, fpa);
			this.fpa = fpa;
			this.parentPm = parentPm;
			this.ctx = ctx;
		}

		@Override
		Figure convertValue(Result<IValue> res) {
			Figure fig = FigureFactory.make(fpa, ((IConstructor) res.getValue()), parentPm, null, ctx);
			fig.bbox(Figure.AUTO_SIZE, Figure.AUTO_SIZE);
			fig.setVisibleInMouseOver(true);
			return fig;
		}
		
	}
	
	static class HandlerProperty extends ComputedProperty<Void>{

		public HandlerProperty(IValue fun, IFigureApplet fpa) {
			super(fun, fpa);
		}

		@Override
		Void convertValue(Result<IValue> res) {
			return null;
		}
	}
}

