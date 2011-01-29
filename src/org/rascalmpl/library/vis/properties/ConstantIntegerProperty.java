package org.rascalmpl.library.vis.properties;

public class ConstantIntegerProperty implements IIntegerPropertyValue {
	Property property;
	Integer value;

	public ConstantIntegerProperty(Property prop, int val){
		this.property = prop;
		this.value = val;
	}
	
	public Property getProperty(){
		return property;
	}
	
	public int getValue() {
		return value;
	}

	public boolean usesTrigger() {
		return false;
	}

}
