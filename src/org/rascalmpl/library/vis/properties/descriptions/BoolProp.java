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

public enum BoolProp{	
	DRAW_SCREEN_X(true),
	DRAW_SCREEN_Y(true),
	SHAPE_CLOSED(false), 	
	SHAPE_CONNECTED(false),
	SHAPE_CURVED(false),
	START_GAP(false),
	END_GAP(false),
	SCALE_ALL(true);

	Boolean stdDefault;
	
	BoolProp(Boolean stdDefault ){
		this.stdDefault = stdDefault;
	}
	
	
	@SuppressWarnings("serial")
	public static final HashMap<String, PropertySetters.PropertySetter<BoolProp,Boolean>> propertySetters = new HashMap<String, PropertySetters.PropertySetter<BoolProp,Boolean>>() {{
	put("drawScreenX", new PropertySetters.SingleBooleanPropertySetter(DRAW_SCREEN_X));
	put("drawScreenY", new PropertySetters.SingleBooleanPropertySetter(DRAW_SCREEN_Y));
	put("shapeClosed", new PropertySetters.SingleBooleanPropertySetter(SHAPE_CLOSED));
	put("shapeConnected", new PropertySetters.SingleBooleanPropertySetter(SHAPE_CONNECTED));
	put("shapeCurved", new PropertySetters.SingleBooleanPropertySetter(SHAPE_CURVED));
	put("startGap", new PropertySetters.SingleBooleanPropertySetter(SHAPE_CURVED));
	put("endGap", new PropertySetters.SingleBooleanPropertySetter(SHAPE_CURVED));
	put("scaleAll", new PropertySetters.SingleBooleanPropertySetter(SCALE_ALL));
	// aliasses
	put("drawScreen", new PropertySetters.DualOrRepeatSingleBooleanPropertySetter(DRAW_SCREEN_X, DRAW_SCREEN_Y));
	put("capGaps", new PropertySetters.DualOrRepeatSingleBooleanPropertySetter(START_GAP, END_GAP));
	}};

	public Boolean getStdDefault() {
		return stdDefault;
	}
}