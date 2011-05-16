package org.rascalmpl.library.vis.properties.descriptions;

import java.util.HashMap;

import org.rascalmpl.library.vis.properties.PropertySetters;

public enum HandlerProp {
	MOUSE_CLICK(),
	ON_MOUSEOVER(),
	ON_MOUSEOFF();

	@SuppressWarnings("serial")
	public static final HashMap<String, PropertySetters.PropertySetter<HandlerProp,Void>> propertySetters = new HashMap<String, PropertySetters.PropertySetter<HandlerProp,Void>>() {{
	put("onClick", new PropertySetters.SingleHandlerPropertySetter(MOUSE_CLICK));
	put("onMouseOver",  new PropertySetters.SingleHandlerPropertySetter(ON_MOUSEOVER));
	put("onMouseOff",  new PropertySetters.SingleHandlerPropertySetter(ON_MOUSEOFF));
	// Aliases
	}};
	
	public Void getStdDefault() {
		return null;
	}

}
