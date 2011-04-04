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

public class ConstantBooleanProperty implements IBooleanPropertyValue {
	Property property;
	boolean value;

	public ConstantBooleanProperty(Property prop, boolean val){
		this.property = prop;
		this.value = val;
	}
	
	public Property getProperty(){
		return property;
	}
	
	public boolean getValue() {
		return value;
	}

	public boolean isCallBack() {
		return false;
	}

}
