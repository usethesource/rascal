package org.rascalmpl.library.vis.properties;

import java.util.HashMap;

import org.rascalmpl.library.vis.FigureColorUtils;
import org.rascalmpl.library.vis.properties.Types;
import org.rascalmpl.library.vis.properties.PropertySetters.SingleIntOrRealPropertySetter;
import org.rascalmpl.library.vis.util.Dimension;

public enum Properties {
	DRAW_SCREEN_X(Types.BOOL,true),
	DRAW_SCREEN_Y(Types.BOOL,true),
	SHAPE_CLOSED(Types.BOOL,false), 	
	SHAPE_CONNECTED(Types.BOOL,false),
	SHAPE_CURVED(Types.BOOL,false),
	HSTART_GAP(Types.BOOL,false),
	HEND_GAP(Types.BOOL,false),
	VSTART_GAP(Types.BOOL,false),
	VEND_GAP(Types.BOOL,false),
	HRESIZABLE(Types.BOOL,true),
	VRESIZABLE(Types.BOOL,true),
	
	FILL_COLOR(Types.COLOR,FigureColorUtils.colorNames.get("white").intValue()),     
	FONT_COLOR(Types.COLOR,FigureColorUtils.colorNames.get("black").intValue()),    
	LINE_COLOR(Types.COLOR,FigureColorUtils.colorNames.get("black").intValue()),
	
	HEIGHT(Types.DIMENSIONAL,new Measure(0.0),Dimension.X),
	HGAP(Types.DIMENSIONAL,new Measure(0.0),Dimension.X), 
	VGAP(Types.DIMENSIONAL,new Measure(0.0),Dimension.Y), 	
	WIDTH(Types.DIMENSIONAL,new Measure(0.0),Dimension.Y),
	
	MOUSE_OVER(Types.FIGURE,null),
	TO_ARROW(Types.FIGURE,null),
	FROM_ARROW(Types.FIGURE,null),
	LABEL(Types.FIGURE,null),
	
	MOUSE_CLICK(Types.HANDLER,null),
	ON_MOUSEOVER(Types.HANDLER,null),
	ON_MOUSEOFF(Types.HANDLER,null),
	
	DOI(Types.INT,1000000),            // degree of interest
	FONT_SIZE(Types.INT,12),
	
	HGROW(Types.REAL, 1.0, Dimension.X),
	HSHRINK(Types.REAL, 1.0, Dimension.X),
	HALIGN(Types.REAL,0.5,Dimension.X),
	MOUSEOVER_HALIGN(Types.REAL,0.5,Dimension.X),
	INNERRADIUS(Types.REAL,0.0), // TODO: innerradisu is not axis aligned
	LINE_WIDTH(Types.REAL,1.0),
	LINE_STYLE(Types.STR,"solid"),
	LINE_JOIN(Types.STR,"miter"),
	LINE_CAP(Types.STR,"flat"),
	TEXT_ANGLE(Types.REAL,0.0), 	
	FROM_ANGLE(Types.REAL,0.0),
	TO_ANGLE(Types.REAL,0.0),			
	VALIGN(Types.REAL,0.5,Dimension.Y),	
	VGROW(Types.REAL, 1.0, Dimension.X),
	VSHRINK(Types.REAL, 1.0, Dimension.X),
	MOUSEOVER_VALIGN(Types.REAL,0.5,Dimension.Y),
	
	DIRECTION(Types.STR,"TD"),	
	LAYER(Types.STR,""),		
	HINT(Types.STR,""),			
	ID(Types.STR,""), 		
	FONT(Types.STR,"Helvetica"),
	TEXT(Types.STR,"");
	
	
	public Object stdDefault;
	public Types type;
	public Dimension dimension;
	
	
	Properties(Types type,Object stdDefault){
		this(type,stdDefault,null);
	}
	
	Properties(Types type,Object stdDefault, Dimension dimension){
		this.type = type;
		this.stdDefault = stdDefault;
		this.dimension =dimension;
	}
	
	@SuppressWarnings("rawtypes")
	public static final HashMap<String, PropertySetters.PropertySetter> propertySetters = new HashMap<String, PropertySetters.PropertySetter>() {{
		put("drawScreenX", new PropertySetters.SingleBooleanPropertySetter(DRAW_SCREEN_X));
		put("drawScreenY", new PropertySetters.SingleBooleanPropertySetter(DRAW_SCREEN_Y));
		put("shapeClosed", new PropertySetters.SingleBooleanPropertySetter(SHAPE_CLOSED));
		put("shapeConnected", new PropertySetters.SingleBooleanPropertySetter(SHAPE_CONNECTED));
		put("shapeCurved", new PropertySetters.SingleBooleanPropertySetter(SHAPE_CURVED));
		put("hstartGap", new PropertySetters.SingleBooleanPropertySetter(HSTART_GAP));
		put("hendGap", new PropertySetters.SingleBooleanPropertySetter(HEND_GAP));
		put("vstartGap", new PropertySetters.SingleBooleanPropertySetter(VSTART_GAP));
		put("vendGap", new PropertySetters.SingleBooleanPropertySetter(VEND_GAP));
		put("hresizable", new PropertySetters.SingleBooleanPropertySetter(HRESIZABLE));
		put("vresizable", new PropertySetters.SingleBooleanPropertySetter(VRESIZABLE));
		// aliasses
		put("drawScreen", new PropertySetters.DualOrRepeatSingleBooleanPropertySetter(DRAW_SCREEN_X, DRAW_SCREEN_Y));
		put("hcapGaps", new PropertySetters.DualOrRepeatSingleBooleanPropertySetter(HSTART_GAP, HEND_GAP));
		put("vcapGaps", new PropertySetters.DualOrRepeatSingleBooleanPropertySetter(VSTART_GAP, VEND_GAP));
		put("resizable", new PropertySetters.DualOrRepeatSingleBooleanPropertySetter(HRESIZABLE, VRESIZABLE));
		
		put("fillColor", new PropertySetters.SingleColorPropertySetter(FILL_COLOR));
		put("fontColor", new PropertySetters.SingleColorPropertySetter(FONT_COLOR));
		put("lineColor", new PropertySetters.SingleColorPropertySetter(LINE_COLOR));
		
		put("height", new PropertySetters.SingleMeasurePropertySetter(HEIGHT));
		put("hgap", new PropertySetters.SingleMeasurePropertySetter(HGAP));
		put("vgap", new PropertySetters.SingleMeasurePropertySetter(VGAP));
		put("width", new PropertySetters.SingleMeasurePropertySetter(WIDTH));
		// below: aliasses
		put("gap", new PropertySetters.DualOrRepeatMeasurePropertySetter(HGAP, VGAP));
		put("size", new PropertySetters.DualOrRepeatMeasurePropertySetter(WIDTH, HEIGHT));
		
		put("mouseOver", new PropertySetters.SingleFigurePropertySetter(MOUSE_OVER));
		put("toArrow", new PropertySetters.SingleFigurePropertySetter(TO_ARROW));
		put("fromArrow", new PropertySetters.SingleFigurePropertySetter(FROM_ARROW));
		put("label", new PropertySetters.SingleFigurePropertySetter(LABEL));
		
		put("onClick", new PropertySetters.SingleHandlerPropertySetter(MOUSE_CLICK));
		put("onMouseOver",  new PropertySetters.SingleHandlerPropertySetter(ON_MOUSEOVER));
		put("onMouseOff",  new PropertySetters.SingleHandlerPropertySetter(ON_MOUSEOFF));
		
		put("doi", new PropertySetters.SingleIntPropertySetter(DOI));
		put("fontSize", new PropertySetters.SingleIntPropertySetter(FONT_SIZE));
		
		put("halign", new PropertySetters.SingleIntOrRealPropertySetter(HALIGN));
		put("hgrow", new PropertySetters.SingleIntOrRealPropertySetter(HGROW));
		put("hshrink", new PropertySetters.SingleIntOrRealPropertySetter(HSHRINK));
		put("mouseOverHalign", new PropertySetters.SingleIntOrRealPropertySetter(MOUSEOVER_HALIGN));
		put("innerRadius", new PropertySetters.SingleIntOrRealPropertySetter(INNERRADIUS));
		put("lineWidth", new PropertySetters.SingleIntOrRealPropertySetter(LINE_WIDTH));
		put("lineStyle", new PropertySetters.SingleStrPropertySetter(LINE_STYLE));
		put("lineCap", new PropertySetters.SingleStrPropertySetter(LINE_CAP));
		put("lineJoin", new PropertySetters.SingleStrPropertySetter(LINE_JOIN));
		put("textAngle", new PropertySetters.SingleIntOrRealPropertySetter(TEXT_ANGLE));
		put("fromAngle", new PropertySetters.SingleIntOrRealPropertySetter(FROM_ANGLE));
		put("toAngle", new PropertySetters.SingleIntOrRealPropertySetter(TO_ANGLE));
		put("valign", new PropertySetters.SingleRealPropertySetter(VALIGN));
		put("vgrow", new PropertySetters.SingleIntOrRealPropertySetter(VGROW));
		put("vshrink", new PropertySetters.SingleIntOrRealPropertySetter(VSHRINK));
		put("mouseOverValign", new PropertySetters.SingleRealPropertySetter(MOUSEOVER_VALIGN));
		// below: aliases
		put("align", new PropertySetters.DualOrRepeatSingleRealPropertySetter(HALIGN, VALIGN));
		put("mouseOverAlign", new PropertySetters.DualOrRepeatSingleIntOrRealPropertySetter(MOUSEOVER_HALIGN, MOUSEOVER_VALIGN));
		put("grow", new PropertySetters.DualOrRepeatSingleIntOrRealPropertySetter(HGROW, VGROW));
		put("shrink", new PropertySetters.DualOrRepeatSingleIntOrRealPropertySetter(HSHRINK, VSHRINK));
		
		put("direction", new PropertySetters.SingleStrPropertySetter(DIRECTION));
		put("layer", new PropertySetters.SingleStrPropertySetter(LAYER));
		put("hint", new PropertySetters.SingleStrPropertySetter(HINT));
		put("id", new PropertySetters.SingleStrPropertySetter(ID));
		put("font", new PropertySetters.SingleStrPropertySetter(FONT));
		put("text", new PropertySetters.SingleStrPropertySetter(TEXT));
	}};
	
	
	// generates rascal code for properties!
	public static void main(String[] argv){
		
	}
}
