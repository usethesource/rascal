package org.rascalmpl.library.vis.properties;

import org.rascalmpl.library.vis.FigurePApplet;

public class ControlStringProperty implements IStringPropertyValue {
	Property property;
	String name;
	private FigurePApplet fpa;

	public ControlStringProperty(Property prop, String name, FigurePApplet fpa){
		this.property = prop;
		this.name = name;
		this.fpa = fpa;
	}
	
	public Property getProperty(){
		return property;
	}
	
	public String getValue() {
		// TODO Auto-generated method stub
		return fpa.getStrControl(name);
	}

}
