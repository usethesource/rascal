package org.rascalmpl.library.vis.properties;

import org.rascalmpl.library.vis.FigurePApplet;

public class TriggeredIntegerProperty implements IIntegerPropertyValue {
	Property property;
	String tname;
	protected FigurePApplet fpa;

	public TriggeredIntegerProperty(Property prop, String name, FigurePApplet fpa){
		this.property = prop;
		this.tname = name;
		this.fpa = fpa;
	}
	
	public Property getProperty(){
		return property;
	}
	
	public int getValue() {
		String s = fpa.getStrTrigger(tname);
		
		try {
			int n = Integer.valueOf(s);
			return n;
		} catch (NumberFormatException e){

		}
		return 0;
	}

}
