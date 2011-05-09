package org.rascalmpl.library.vis.properties.descriptions;

import java.util.HashMap;

import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.properties.PropertySetters;

public enum FigureProp{
	MOUSE_OVER(null),
	TO_ARROW(null),
	FROM_ARROW(null),
	LABEL(null);
	
	Figure stdDefault;
	
	FigureProp(Figure stdDefault){
		this.stdDefault = stdDefault;
	}
	
	@SuppressWarnings("serial")
	public static final HashMap<String, PropertySetters.PropertySetter<FigureProp,Figure>> propertySetters = new HashMap<String, PropertySetters.PropertySetter<FigureProp,Figure>>() {{
	put("mouseOver", new PropertySetters.SingleFigurePropertySetter(MOUSE_OVER));
	put("toArrow", new PropertySetters.SingleFigurePropertySetter(TO_ARROW));
	put("fromArrow", new PropertySetters.SingleFigurePropertySetter(FROM_ARROW));
	put("label", new PropertySetters.SingleFigurePropertySetter(LABEL));
	// aliasses
	}};

	public Figure getStdDefault() {
		return stdDefault;
	}
}
