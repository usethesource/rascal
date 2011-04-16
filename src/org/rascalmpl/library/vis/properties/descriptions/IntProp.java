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

public enum IntProp {
	DOI(1000000),            // degree of interest
	FONT_SIZE(12);

	int stdDefault;
	
	IntProp(int stdDefault){
		this.stdDefault = stdDefault;
	}
	
	@SuppressWarnings("serial")
	public static final HashMap<String, PropertySetters.PropertySetter<IntProp,Integer>> propertySetters = new HashMap<String, PropertySetters.PropertySetter<IntProp,Integer>>() {{
	put("doi", new PropertySetters.SingleIntPropertySetter(DOI));
	put("fontSize", new PropertySetters.SingleIntPropertySetter(FONT_SIZE));
	}};

	public Integer getStdDefault() {
		return stdDefault;
	}
}