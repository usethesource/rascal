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

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.swt.ICallbackEnv;

public class ConstantProperties {


	private static class ConstantProperty<PropType> extends PropertyValue<PropType>{
		PropType value;
		
		public ConstantProperty(Properties property,PropType value){
			super(property);
			this.value = value;
		}
		
		public PropType getValue(){
			return value;
		}
		
		public void compute(ICallbackEnv env) {
		}

	}
	
	public static class ConstantRealProperty extends ConstantProperty<Double>{
		public ConstantRealProperty(Properties property,Double value) {
			super(property,value);
		}
	}
	
	public static class ConstantStringProperty extends ConstantProperty<String>{
		public ConstantStringProperty(Properties property,String value) {
			super(property,value);
		}
	}
	
	public static class ConstantBooleanProperty extends ConstantProperty<Boolean>{
		public ConstantBooleanProperty(Properties property,Boolean value) {
			super(property,value);
		}
	}
	
	public static class ConstantIntegerProperty extends ConstantProperty<Integer>{
		public ConstantIntegerProperty(Properties property,Integer value) {
			super(property,value);
		}	
	}
	
	public static class ConstantColorProperty extends ConstantIntegerProperty{
		public ConstantColorProperty(Properties property,Integer value) {
			super(property,value);
		}
	}
	
	public static class ConstantFigureProperty extends ConstantProperty<Figure>{
		public ConstantFigureProperty(Properties property,Figure value) {
			super(property,value);
		}
	}
}
