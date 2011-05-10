package org.rascalmpl.library.vis.properties;

public class Measure {
	public String axisName;
	public double value;
	
	public Measure(double value){
		this.value = value;
		axisName = "";
	}
	
	public Measure(double value, String axisName){
		this.value = value;
		this.axisName = axisName;
	}

}
