package org.rascalmpl.library.vis.properties;

public class ConstantBooleanProperty implements IBooleanPropertyValue {
	Property property;
	boolean value;

	public ConstantBooleanProperty(Property prop, boolean val){
		this.property = prop;
		this.value = val;
	}
	
	public Property getProperty(){
		return property;
	}
	
	public boolean getValue() {
		return value;
	}

	public boolean isCallBack() {
		return false;
	}

}
