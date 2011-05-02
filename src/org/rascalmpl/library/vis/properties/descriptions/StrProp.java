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

package org.rascalmpl.library.vis.properties.descriptions;

import java.util.HashMap;

import org.rascalmpl.library.vis.properties.PropertySetters;

public enum StrProp {
	DIRECTION("TD"),	
	LAYER(""),		
	HINT(""),			
	ID(""), 		
	FONT("Helvetica"),
	TEXT("");

	String stdDefault;
	
	StrProp(String stdDefault){
		this.stdDefault = stdDefault;
	}
	
	@SuppressWarnings("serial")
	public static final HashMap<String, PropertySetters.PropertySetter<StrProp,String>> settersStr = new HashMap<String, PropertySetters.PropertySetter<StrProp,String>>() {{
	put("direction", new PropertySetters.SingleStrPropertySetter(DIRECTION));
	put("layer", new PropertySetters.SingleStrPropertySetter(LAYER));
	put("hint", new PropertySetters.SingleStrPropertySetter(HINT));
	put("id", new PropertySetters.SingleStrPropertySetter(ID));
	put("font", new PropertySetters.SingleStrPropertySetter(FONT));
	put("text", new PropertySetters.SingleStrPropertySetter(TEXT));
	}};

	public String getStdDefault() {
		return stdDefault;
	}
}