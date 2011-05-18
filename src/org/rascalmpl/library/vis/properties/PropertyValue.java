/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.vis.properties;

@SuppressWarnings("rawtypes")
public abstract class PropertyValue<PropType> implements Comparable{
	public Properties property;
	
	public PropertyValue(Properties property) {
		this.property = property;
	}
	
	public Properties getProperty(){
		return property;
	}
	
	public abstract PropType getValue();
	public abstract void compute();
	
	public int compareTo(Object rhs){
		if(rhs instanceof PropertyValue){
			return property.ordinal() - ((PropertyValue)rhs).property.ordinal();
		} else if(rhs instanceof Properties){
			return property.ordinal() - ((Properties)rhs).ordinal();
		} else {
			return -1;
		}
		
	}
}
