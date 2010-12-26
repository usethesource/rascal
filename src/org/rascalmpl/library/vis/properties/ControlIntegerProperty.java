package org.rascalmpl.library.vis.properties;

import org.rascalmpl.library.vis.FigurePApplet;

public class ControlIntegerProperty implements IIntegerPropertyValue {
	Property property;
	String name;
	private FigurePApplet fpa;

	public ControlIntegerProperty(Property prop, String name, FigurePApplet fpa){
		this.property = prop;
		this.name = name;
		this.fpa = fpa;
	}
	
	public Property getProperty(){
		return property;
	}
	
	public int getValue() {
		return fpa.getIntControl(name);
	}

}
