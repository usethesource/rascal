package org.rascalmpl.library.vis.properties;

public class Measure {
	public String axisName;
	public float value;
	
	public Measure(float value){
		this.value = value;
		axisName = "";
	}
	
	public Measure(float value, String axisName){
		this.value = value;
		this.axisName = axisName;
	}

}
