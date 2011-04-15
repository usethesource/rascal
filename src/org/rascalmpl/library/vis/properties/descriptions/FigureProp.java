package org.rascalmpl.library.vis.properties.descriptions;

import java.util.EnumMap;
import java.util.HashMap;

import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.properties.PropertySetters;

public enum FigureProp {
	MOUSE_OVER;
	
	@SuppressWarnings("serial")
	public static final EnumMap<FigureProp,Figure> stdDefaults = new EnumMap<FigureProp, Figure>(FigureProp.class){{
		put(MOUSE_OVER,null);
	}};
	
	@SuppressWarnings("serial")
	public static final HashMap<String, PropertySetters.PropertySetter<FigureProp,Figure>> propertySetters = new HashMap<String, PropertySetters.PropertySetter<FigureProp,Figure>>() {{
	put("mouseOver", new PropertySetters.SingleFigurePropertySetter(MOUSE_OVER));
	// aliasses
	}};
}
