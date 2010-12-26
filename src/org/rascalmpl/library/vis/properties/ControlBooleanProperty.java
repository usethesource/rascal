package org.rascalmpl.library.vis.properties;

public class ControlBooleanProperty implements IBooleanPropertyValue {
	Property property;
	String name;

	public ControlBooleanProperty(Property prop, String name){
		this.property = prop;
		this.name = name;
	}
	
	public Property getProperty(){
		return property;
	}
	
	public boolean getValue() {
		// TODO Auto-generated method stub
		return false;
	}

}
