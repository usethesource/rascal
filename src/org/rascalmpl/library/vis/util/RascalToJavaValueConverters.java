/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.util;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.FigureFactory;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IReal;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;

public class RascalToJavaValueConverters {
	
	public interface Convert<T>{
		T convert(IValue val, PropertyManager pm, IFigureConstructionEnv env);
	}
	
	public static class ConvertBool implements Convert<Boolean>{
		public static final ConvertBool instance = new ConvertBool();
		@Override
		public Boolean convert(IValue val, PropertyManager pm, IFigureConstructionEnv env){
			return ((IBool) val).getValue();
		}
	}
	
	public static class ConvertInt implements Convert<Integer>{
		public static final ConvertInt instance = new ConvertInt();
		@Override
		public Integer convert(IValue val, PropertyManager pm, IFigureConstructionEnv env){
			return ((IInteger) val).intValue();
		}
	}
	

	public static class ConvertColor implements Convert<Integer>{
		public static final ConvertColor instance = new ConvertColor();
		@Override
		public Integer convert(IValue val, PropertyManager pm, IFigureConstructionEnv env){
			if(val instanceof IString){
				String name = ((IString)val).getValue().toLowerCase();
				if(FigureColorUtils.colorNames.containsKey(name)){
					val =  FigureColorUtils.colorNames.get(name);
				} else {
					throw new Error("No such color "+ name + "!");
				}

			} 
			return ((IInteger) val).intValue();
		}
	}
	
	public static class ConvertReal implements Convert<Double>{
		public static final ConvertReal instance = new ConvertReal();
		@Override
		public Double convert(IValue val, PropertyManager pm, IFigureConstructionEnv env){
			return (val instanceof IInteger) ? ((IInteger) val).intValue() : ((IReal) val).doubleValue();
		}
	}
	

	public static class ConvertStr implements Convert<String>{
		public static final ConvertStr instance = new ConvertStr();
		@Override
		public String convert(IValue val, PropertyManager pm, IFigureConstructionEnv env){
			return ((IString) val).getValue();
		}
	}
	

	public static class ConvertFig implements Convert<Figure>{
		public static final ConvertFig instance = new ConvertFig();
		@Override
		public Figure convert(IValue val, PropertyManager pm, IFigureConstructionEnv env){
			return FigureFactory.make(env, (IConstructor)val, null, null);
		}
	}
	
	public static class DoNotConvert implements Convert<IValue>{
		public static final DoNotConvert instance = new DoNotConvert();
		@Override
		public IValue convert(IValue val, PropertyManager pm, IFigureConstructionEnv env){
			return val;
		}
	}
	
	public static class ConvertNum implements Convert<Double>{
		public static final ConvertNum instance = new ConvertNum();
		@Override
		public Double convert(IValue val, PropertyManager pm,IFigureConstructionEnv env) {
			if(val instanceof IReal){
				return ConvertReal.instance.convert(val, pm, env);
			} else if (val instanceof IInteger){
				return (double)ConvertInt.instance.convert(val, pm, env);
			} else {
				throw new Error("Unkown number case in convertnum!");
			}
		}
	}
	
	

}
