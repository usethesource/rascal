package org.rascalmpl.library.vis.properties.descriptions;

import java.util.HashMap;

import org.rascalmpl.library.vis.properties.Measure;
import org.rascalmpl.library.vis.properties.PropertySetters;
import org.rascalmpl.library.vis.util.Dimension;

public enum DimensionalProp {
	
	HEIGHT(40.0f,Dimension.X),
	HGAP(0.0f, Dimension.X), 
	VGAP(0.0f, Dimension.Y ), 	
	WIDTH(40.0f,Dimension.Y);
	

	public static enum TranslateDimensionToProp {
		
		DIMENSION(HEIGHT,WIDTH),
		GAP(HGAP,VGAP);
		
		private DimensionalProp horizontal, vertical;
		
		public DimensionalProp getDimensionalProp(Dimension d){
			switch(d){
			case X: return horizontal;
			case Y: return vertical;
			default: throw new Error("Unkown dimension");
			}
		}
		
		TranslateDimensionToProp(DimensionalProp horizontal, DimensionalProp vertical){
			this.horizontal = horizontal;
			this.vertical = vertical;
		}
	}
	
	float stdDefault;
	Dimension dimension;
	
	DimensionalProp(float stdDefault, Dimension dimension){
		this.stdDefault = stdDefault;
		this.dimension = dimension;
	}
	

	public Measure getStdDefault() {
		return new Measure(stdDefault);
	}	
	
	public Dimension getDimension(){
		return dimension;
	}
	
	
	@SuppressWarnings("serial")
	public static final HashMap<String, PropertySetters.PropertySetter<DimensionalProp,Measure>> propertySetters = new HashMap<String, PropertySetters.PropertySetter<DimensionalProp,Measure>>() {{
		put("height", new PropertySetters.SingleMeasurePropertySetter(HEIGHT));
		put("hgap", new PropertySetters.SingleMeasurePropertySetter(HGAP));
		put("vgap", new PropertySetters.SingleMeasurePropertySetter(VGAP));
		put("width", new PropertySetters.SingleMeasurePropertySetter(WIDTH));
		// below: aliasses
		put("gap", new PropertySetters.DualOrRepeatMeasurePropertySetter(HGAP, VGAP));
		put("size", new PropertySetters.DualOrRepeatMeasurePropertySetter(WIDTH, HEIGHT));
	}};
}
