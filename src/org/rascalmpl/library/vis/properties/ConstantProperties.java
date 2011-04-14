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
	
	static class ConstantRealProperty extends ConstantProperty<Float>{
		public ConstantRealProperty(Float value) {
			super(value);
		}
	}
	
	static class ConstantStringProperty extends ConstantProperty<String>{
		public ConstantStringProperty(String value) {
			super(value);
		}
	}
	
	static class ConstantBooleanProperty extends ConstantProperty<Boolean>{
		public ConstantBooleanProperty(Boolean value) {
			super(value);
		}
	}
	
	static class ConstantIntegerProperty extends ConstantProperty<Integer>{
		public ConstantIntegerProperty(Integer value) {
			super(value);
		}	
	}
	
	static class ConstantColorProperty extends ConstantIntegerProperty{
		public ConstantColorProperty(Integer value) {
			super(value);
		}
	}
}
