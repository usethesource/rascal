package org.rascalmpl.library.vis.properties.descriptions;

import java.util.HashMap;

import org.rascalmpl.library.vis.properties.PropertySetters;

public enum HandlerProp implements IProperties<HandlerProp, Void>{
	MOUSE_CLICK();
	
	@SuppressWarnings("serial")
	public static final HashMap<String, PropertySetters.PropertySetter<HandlerProp,Void>> propertySetters = new HashMap<String, PropertySetters.PropertySetter<HandlerProp,Void>>() {{
	put("onClick", new PropertySetters.SingleHandlerPropertySetter(MOUSE_CLICK));
	// Aliases
	}};
	
	@Override
	public Void getStdDefault() {
		return null;
	}

}
