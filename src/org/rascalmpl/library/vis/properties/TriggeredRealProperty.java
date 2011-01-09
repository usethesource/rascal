package org.rascalmpl.library.vis.properties;

import org.rascalmpl.library.vis.FigurePApplet;

public class TriggeredRealProperty implements IRealPropertyValue {
	Property property;
	String tname;
	private FigurePApplet fpa;

	public TriggeredRealProperty(Property prop, String tname, FigurePApplet fpa){
		this.property = prop;
		this.tname = tname;
		this.fpa = fpa;
	}
	
	public Property getProperty(){
		return property;
	}
	
	public float getValue() {
		String s = fpa.getStrTrigger(tname);
		
		try {
			float f = Float.valueOf(s);
			return f;
		} catch (NumberFormatException e){

		}
		return 0;
	}

}
