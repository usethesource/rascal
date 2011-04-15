package org.rascalmpl.library.vis.properties.descriptions;

import java.util.EnumMap;
import java.util.HashMap;

import org.rascalmpl.library.vis.properties.PropertySetters;

public enum HandlerProp {
	MOUSE_CLICK;
	
	@SuppressWarnings("serial")
	public static final EnumMap<HandlerProp,Void> stdDefaults = new EnumMap<HandlerProp, Void>(HandlerProp.class){{
		put(MOUSE_CLICK,null);
	}};
	
	@SuppressWarnings("serial")
	public static final HashMap<String, PropertySetters.PropertySetter<HandlerProp,Void>> propertySetters = new HashMap<String, PropertySetters.PropertySetter<HandlerProp,Void>>() {{
	put("onClick", new PropertySetters.SingleHandlerPropertySetter(MOUSE_CLICK));
	// Aliases
	}};

}
