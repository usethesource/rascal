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

import org.rascalmpl.library.vis.Figure;

public class ConstantProperties {


	private static class ConstantProperty<PropType> implements IPropertyValue<PropType>{
		PropType value;
		
		public ConstantProperty(PropType value){
			this.value = value;
		}
		
		public PropType getValue(){
			return value;
		}

	}
	
	public static class ConstantRealProperty extends ConstantProperty<Double>{
		public ConstantRealProperty(Double value) {
			super(value);
		}
	}
	
	public static class ConstantStringProperty extends ConstantProperty<String>{
		public ConstantStringProperty(String value) {
			super(value);
		}
	}
	
	public static class ConstantBooleanProperty extends ConstantProperty<Boolean>{
		public ConstantBooleanProperty(Boolean value) {
			super(value);
		}
	}
	
	public static class ConstantIntegerProperty extends ConstantProperty<Integer>{
		public ConstantIntegerProperty(Integer value) {
			super(value);
		}	
	}
	
	public static class ConstantColorProperty extends ConstantIntegerProperty{
		public ConstantColorProperty(Integer value) {
			super(value);
		}
	}
	
	public static class ConstantFigureProperty extends ConstantProperty<Figure>{
		public ConstantFigureProperty(Figure value) {
			super(value);
		}
	}
	
	public static class ConstantMeasureProperty extends ConstantProperty<Measure>{
		public ConstantMeasureProperty(Measure value) {
			super(value);
		}
		
		
	}
}
