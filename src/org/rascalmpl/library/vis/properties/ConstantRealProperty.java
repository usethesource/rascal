package org.rascalmpl.library.vis.properties;

public class ConstantRealProperty implements IRealPropertyValue {
	Property property;
	float value;

	public ConstantRealProperty(Property prop, float val){
		this.property = prop;
		this.value = val;
	}
	
	public Property getProperty(){
		return property;
	}
	
	public float getValue() {
		return value;
	}

	public boolean usesTrigger() {
		return false;
	}

}
