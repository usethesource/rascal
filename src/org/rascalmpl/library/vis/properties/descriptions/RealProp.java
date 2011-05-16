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
	HALIGN(0.5),
	MOUSEOVER_HALIGN(0.5),
	HGAP_FACTOR(0.0),
	INNERRADIUS(0.0f), // TODO: innerradisu is not axis aligned
	LINE_WIDTH(1.0),
	TEXT_ANGLE(0.0), 	
	FROM_ANGLE(0.0),
	TO_ANGLE(0.0),			
	VALIGN(0.5),	
	MOUSEOVER_VALIGN(0.5),
	VGAP_FACTOR(0.0);
	
	double stdDefault;
	
	RealProp(double stdDefault){
		this.stdDefault = stdDefault;
	}

	@SuppressWarnings("serial")
	public static final HashMap<String, PropertySetters.PropertySetter<RealProp, Double>> propertySetters = new HashMap<String, PropertySetters.PropertySetter<RealProp, Double>>() {{
	put("halign", new PropertySetters.SingleIntOrRealPropertySetter(HALIGN));
	put("mouseOverHalign", new PropertySetters.SingleIntOrRealPropertySetter(MOUSEOVER_HALIGN));
	put("hgapFactor", new PropertySetters.SingleIntOrRealPropertySetter(HGAP_FACTOR));
	put("innerRadius", new PropertySetters.SingleIntOrRealPropertySetter(INNERRADIUS));
	put("lineWidth", new PropertySetters.SingleIntOrRealPropertySetter(LINE_WIDTH));
	put("textAngle", new PropertySetters.SingleIntOrRealPropertySetter(TEXT_ANGLE));
	put("fromAngle", new PropertySetters.SingleIntOrRealPropertySetter(FROM_ANGLE));
	put("toAngle", new PropertySetters.SingleIntOrRealPropertySetter(TO_ANGLE));
	put("valign", new PropertySetters.SingleRealPropertySetter(VALIGN));
	put("mouseOverValign", new PropertySetters.SingleRealPropertySetter(MOUSEOVER_VALIGN));
	put("vgapFactor", new PropertySetters.SingleIntOrRealPropertySetter(VGAP_FACTOR));
	// below: aliases
	put("align", new PropertySetters.DualOrRepeatSingleRealPropertySetter(HALIGN, VALIGN));
	put("mouseOverAlign", new PropertySetters.DualOrRepeatSingleIntOrRealPropertySetter(MOUSEOVER_HALIGN, MOUSEOVER_VALIGN));
	put("gapFactor", new PropertySetters.DualOrRepeatSingleIntOrRealPropertySetter(HGAP_FACTOR, VGAP_FACTOR));
	}};

	public Double getStdDefault() {
		return stdDefault;
	}	
}