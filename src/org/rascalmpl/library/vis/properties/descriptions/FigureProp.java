package org.rascalmpl.library.vis.properties.descriptions;

import java.util.HashMap;

import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.properties.PropertySetters;

public enum FigureProp{
	MOUSE_OVER(null);
	
	Figure stdDefault;
	
	FigureProp(Figure stdDefault){
		this.stdDefault = stdDefault;
	}
	
	@SuppressWarnings("serial")
	public static final HashMap<String, PropertySetters.PropertySetter<FigureProp,Figure>> propertySetters = new HashMap<String, PropertySetters.PropertySetter<FigureProp,Figure>>() {{
	put("mouseOver", new PropertySetters.SingleFigurePropertySetter(MOUSE_OVER));
	// aliasses
	}};

	public Figure getStdDefault() {
		return stdDefault;
	}
}
