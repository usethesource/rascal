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
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.IFigureApplet;

public class ComputedProperties {

	private static abstract class ComputedProperty<PropType> implements IPropertyValue<PropType> {

		private IFigureApplet fpa;
		
		IValue fun;
		Type[] argTypes = new Type[0];			// Argument types of callback: list[str]
		IValue[] argVals = new IValue[0];		// Argument values of callback: argList
		PropType value;

		public ComputedProperty(IValue fun, IFigureApplet fpa){
			this.fun = fun;
			this.fpa = fpa;
		}
		
		abstract PropType convertValue(Result<IValue> res);
		
		public synchronized PropType getValue() {
			
			Result<IValue> res;
			PropType old = value;
			synchronized(fpa){
				if(fun instanceof RascalFunction)
					res = ((RascalFunction) fun).call(argTypes, argVals);
				else
					res = ((OverloadedFunctionResult) fun).call(argTypes, argVals);
			}
			
			value = convertValue(res);
			if(value != old)
				fpa.setComputedValueChanged();
			return value;
		}	
	}
	

	static class ComputedRealProperty extends ComputedProperty<Float>{

		public ComputedRealProperty(IValue fun, IFigureApplet fpa) {
			super(fun, fpa);
		}

		Float convertValue(Result<IValue> res){
			if(res.getType().isIntegerType())
				return (float)((IInteger) res.getValue()).intValue();
			else if(res.getType().isRealType())
				return ((IReal) res.getValue()).floatValue();
			else
				return ((INumber) res.getValue()).toReal().floatValue();
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
			return ((Measure) res.getValue());
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
			System.err.print("Computing figure..");
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
