package org.rascalmpl.library.vis.properties.descriptions;

import java.util.HashMap;

import org.rascalmpl.library.vis.properties.Measure;
import org.rascalmpl.library.vis.properties.PropertySetters;

public enum MeasureProp {
	HEIGHT(40.0f,false),
	HGAP(0.0f,true), 
	INNERRADIUS(0.0f,true), // TODO: innerradisu is not axis aligned
	VGAP(0.0f,false), 	
	WIDTH(40.0f,true);
	
	
	
	float stdDefault;
	boolean horizontal;
	
	MeasureProp(float stdDefault, boolean horizontal){
		this.stdDefault = stdDefault;
		this.horizontal = horizontal;
	}
	

	public Measure getStdDefault() {
		return new Measure(stdDefault);
	}	
	
	public boolean isHorizontal(){
		return this.horizontal;
	}
	
	@SuppressWarnings("serial")
	public static final HashMap<String, PropertySetters.PropertySetter<MeasureProp,Measure>> propertySetters = new HashMap<String, PropertySetters.PropertySetter<MeasureProp,Measure>>() {{
		put("height", new PropertySetters.SingleMeasurePropertySetter(HEIGHT));
		put("hgap", new PropertySetters.SingleMeasurePropertySetter(HGAP));
		put("innerRadius", new PropertySetters.SingleMeasurePropertySetter(INNERRADIUS));
		put("vgap", new PropertySetters.SingleMeasurePropertySetter(VGAP));
		put("width", new PropertySetters.SingleMeasurePropertySetter(WIDTH));
		// below: aliasses
		put("gap", new PropertySetters.DualOrRepeatMeasurePropertySetter(HGAP, VGAP));
		put("size", new PropertySetters.DualOrRepeatMeasurePropertySetter(WIDTH, HEIGHT));
	}};

}
