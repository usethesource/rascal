package org.rascalmpl.library.vis.properties;

import org.rascalmpl.library.vis.FigurePApplet;

public class TriggeredStringProperty implements IStringPropertyValue {
	Property property;
	String tname;
	private FigurePApplet fpa;

	public TriggeredStringProperty(Property prop, String name, FigurePApplet fpa){
		this.property = prop;
		this.tname = name;
		this.fpa = fpa;
	}
	
	public Property getProperty(){
		return property;
	}
	
	public String getValue() {
		return fpa.getStrTrigger(tname);
	}

}
