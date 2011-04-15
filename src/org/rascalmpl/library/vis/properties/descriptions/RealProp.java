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

import java.util.EnumMap;
import java.util.HashMap;

import org.rascalmpl.library.vis.properties.PropertySetters;

public enum RealProp{
	HALIGN,	
	HANCHOR,
	HEIGHT,
	HGAP, 
	HGAP_FACTOR,
	INNERRADIUS,
	LINE_WIDTH,
	TEXT_ANGLE, 	
	FROM_ANGLE,
	TO_ANGLE,		
	VALIGN,			
	VANCHOR,		
	VGAP, 		
	VGAP_FACTOR,
	WIDTH;

	@SuppressWarnings("serial")
	public static final EnumMap<RealProp,Float> stdDefaults = new EnumMap<RealProp, Float>(RealProp.class){{
		put(HALIGN,0.5f);
		put(HANCHOR,0.5f);
		put(HEIGHT,10.0f);
		put(HGAP,0.0f);
		put(HGAP_FACTOR,0.0f);
		put(INNERRADIUS,0.0f);
		put(LINE_WIDTH,1.0f);
		put(TEXT_ANGLE,0.0f);
		put(FROM_ANGLE,0.0f);
		put(TO_ANGLE,0.0f);
		put(VALIGN,0.5f);
		put(VANCHOR,0.5f);
		put(VGAP,0.0f);
		put(VGAP_FACTOR,0.0f);
		put(WIDTH,10.0f);
	}};
	@SuppressWarnings("serial")
	public static final HashMap<String, PropertySetters.PropertySetter<RealProp,Float>> propertySetters = new HashMap<String, PropertySetters.PropertySetter<RealProp,Float>>() {{
	put("halign", new PropertySetters.SingleRealPropertySetter(HALIGN));
	put("hanchor", new PropertySetters.SingleRealPropertySetter(HANCHOR));
	put("height", new PropertySetters.SingleIntOrRealPropertySetter(HEIGHT));
	put("hgap", new PropertySetters.SingleIntOrRealPropertySetter(HGAP));
	put("hgapFactor", new PropertySetters.SingleIntOrRealPropertySetter(HGAP_FACTOR));
	put("innerRadius", new PropertySetters.SingleIntOrRealPropertySetter(INNERRADIUS));
	put("lineWidth", new PropertySetters.SingleIntOrRealPropertySetter(LINE_WIDTH));
	put("textAngle", new PropertySetters.SingleIntOrRealPropertySetter(TEXT_ANGLE));
	put("fromAngle", new PropertySetters.SingleIntOrRealPropertySetter(FROM_ANGLE));
	put("toAngle", new PropertySetters.SingleIntOrRealPropertySetter(TO_ANGLE));
	put("valign", new PropertySetters.SingleRealPropertySetter(VALIGN));
	put("vanchor", new PropertySetters.SingleRealPropertySetter(VANCHOR));
	put("vgap", new PropertySetters.SingleIntOrRealPropertySetter(VGAP));
	put("vgapFactor", new PropertySetters.SingleIntOrRealPropertySetter(VGAP_FACTOR));
	put("width", new PropertySetters.SingleIntOrRealPropertySetter(WIDTH));
	// below: aliases
	put("align", new PropertySetters.DualOrRepeatSingleRealPropertySetter(HALIGN, VALIGN));
	put("anchor", new PropertySetters.DualOrRepeatSingleRealPropertySetter(HANCHOR, VANCHOR));
	put("gap", new PropertySetters.DualOrRepeatSingleIntOrRealPropertySetter(HGAP, VGAP));
	put("gapFactor", new PropertySetters.DualOrRepeatSingleIntOrRealPropertySetter(HGAP_FACTOR, VGAP_FACTOR));
	put("size", new PropertySetters.DualOrRepeatSingleIntOrRealPropertySetter(WIDTH, HEIGHT));
	}};	
}