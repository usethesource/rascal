package org.rascalmpl.library.vis.properties;

public class ConstantStringProperty implements IStringPropertyValue {
	Property property;
	String value;

	public ConstantStringProperty(Property prop, String val){
		this.property = prop;
		this.value = val;
	}
	
	public Property getProperty(){
		return property;
	}
	
	public String getValue() {
		return value;
	}

	public boolean isCallBack() {
		return false;
	}

}
