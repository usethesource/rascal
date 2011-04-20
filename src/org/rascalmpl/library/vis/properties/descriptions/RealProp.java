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

public enum RealProp {
	HALIGN(0.5f),
	HEIGHT(40.0f),
	HGAP(0.0f), 
	HGAP_FACTOR(0.0f),
	INNERRADIUS(0.0f),
	LINE_WIDTH(1.0f),
	PROJECTX_GAP(10.0f),
	PROJECTY_GAP(10.0f),
	TEXT_ANGLE(0.0f), 	
	FROM_ANGLE(0.0f),
	TO_ANGLE(0.0f),			
	VALIGN(0.5f),		
	VGAP(0.0f), 		
	VGAP_FACTOR(0.0f),
	WIDTH(40.0f);
	
	float stdDefault;
	
	RealProp(float stdDefault){
		this.stdDefault = stdDefault;
	}

	@SuppressWarnings("serial")
	public static final HashMap<String, PropertySetters.PropertySetter<RealProp,Float>> propertySetters = new HashMap<String, PropertySetters.PropertySetter<RealProp,Float>>() {{
	put("halign", new PropertySetters.SingleRealPropertySetter(HALIGN));
	put("height", new PropertySetters.SingleIntOrRealPropertySetter(HEIGHT));
	put("hgap", new PropertySetters.SingleIntOrRealPropertySetter(HGAP));
	put("hgapFactor", new PropertySetters.SingleIntOrRealPropertySetter(HGAP_FACTOR));
	put("innerRadius", new PropertySetters.SingleIntOrRealPropertySetter(INNERRADIUS));
	put("lineWidth", new PropertySetters.SingleIntOrRealPropertySetter(LINE_WIDTH));
	put("projectXGap", new PropertySetters.SingleIntOrRealPropertySetter(PROJECTX_GAP));
	put("projectYGap", new PropertySetters.SingleIntOrRealPropertySetter(PROJECTY_GAP));
	put("textAngle", new PropertySetters.SingleIntOrRealPropertySetter(TEXT_ANGLE));
	put("fromAngle", new PropertySetters.SingleIntOrRealPropertySetter(FROM_ANGLE));
	put("toAngle", new PropertySetters.SingleIntOrRealPropertySetter(TO_ANGLE));
	put("valign", new PropertySetters.SingleRealPropertySetter(VALIGN));
	put("vgap", new PropertySetters.SingleIntOrRealPropertySetter(VGAP));
	put("vgapFactor", new PropertySetters.SingleIntOrRealPropertySetter(VGAP_FACTOR));
	put("width", new PropertySetters.SingleIntOrRealPropertySetter(WIDTH));
	// below: aliases
	put("align", new PropertySetters.DualOrRepeatSingleRealPropertySetter(HALIGN, VALIGN));
	put("gap", new PropertySetters.DualOrRepeatSingleIntOrRealPropertySetter(HGAP, VGAP));
	put("gapFactor", new PropertySetters.DualOrRepeatSingleIntOrRealPropertySetter(HGAP_FACTOR, VGAP_FACTOR));
	put("size", new PropertySetters.DualOrRepeatSingleIntOrRealPropertySetter(WIDTH, HEIGHT));
	put("projectGap", new PropertySetters.DualOrRepeatSingleIntOrRealPropertySetter(PROJECTX_GAP, PROJECTY_GAP));
	}};

	public Float getStdDefault() {
		return stdDefault;
	}	
}