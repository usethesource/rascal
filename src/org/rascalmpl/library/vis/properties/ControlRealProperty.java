package org.rascalmpl.library.vis.properties;

import org.rascalmpl.library.vis.FigurePApplet;

public class ControlRealProperty implements IRealPropertyValue {
	Property property;
	String name;
	private FigurePApplet fpa;

	public ControlRealProperty(Property prop, String name, FigurePApplet fpa){
		this.property = prop;
		this.name = name;
		this.fpa = fpa;
	}
	
	public Property getProperty(){
		return property;
	}
	
	public float getValue() {
		return fpa.getRealControl(name);
	}

}
