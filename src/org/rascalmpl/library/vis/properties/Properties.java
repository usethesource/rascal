package org.rascalmpl.library.vis.properties;

import java.util.HashMap;

import org.rascalmpl.library.vis.FigureColorUtils;
import org.rascalmpl.library.vis.properties.Types;
import org.rascalmpl.library.vis.util.Dimension;

public enum Properties {
	DRAW_SCREEN_X(Types.BOOL,true),
	DRAW_SCREEN_Y(Types.BOOL,true),
	SHAPE_CLOSED(Types.BOOL,false), 	
	SHAPE_CONNECTED(Types.BOOL,false),
	SHAPE_CURVED(Types.BOOL,false),
	START_GAP(Types.BOOL,false),
	END_GAP(Types.BOOL,false),
	SCALE_ALL(Types.BOOL,true),
	
	FILL_COLOR(Types.COLOR,FigureColorUtils.colorNames.get("white").intValue()),     
	FONT_COLOR(Types.COLOR,FigureColorUtils.colorNames.get("black").intValue()),    
	LINE_COLOR(Types.COLOR,FigureColorUtils.colorNames.get("black").intValue()),
	
	HEIGHT(Types.DIMENSIONAL,new Measure(40.0),Dimension.X),
	HGAP(Types.DIMENSIONAL,new Measure(0.0),Dimension.X), 
	VGAP(Types.DIMENSIONAL,new Measure(0.0),Dimension.Y), 	
	WIDTH(Types.DIMENSIONAL,new Measure(40.0),Dimension.Y),
	
	MOUSE_OVER(Types.FIGURE,null),
	TO_ARROW(Types.FIGURE,null),
	FROM_ARROW(Types.FIGURE,null),
	LABEL(Types.FIGURE,null),
	
	MOUSE_CLICK(Types.HANDLER,null),
	ON_MOUSEOVER(Types.HANDLER,null),
	ON_MOUSEOFF(Types.HANDLER,null),
	
	DOI(Types.INT,1000000),            // degree of interest
	FONT_SIZE(Types.INT,12),
	
	HALIGN(Types.REAL,0.5,Dimension.X),
	MOUSEOVER_HALIGN(Types.REAL,0.5,Dimension.X),
	HGAP_FACTOR(Types.REAL,0.0,Dimension.X),
	INNERRADIUS(Types.REAL,0.0), // TODO: innerradisu is not axis aligned
	LINE_WIDTH(Types.REAL,1.0),
	TEXT_ANGLE(Types.REAL,0.0), 	
	FROM_ANGLE(Types.REAL,0.0),
	TO_ANGLE(Types.REAL,0.0),			
	VALIGN(Types.REAL,0.5,Dimension.Y),	
	MOUSEOVER_VALIGN(Types.REAL,0.5,Dimension.Y),
	VGAP_FACTOR(Types.REAL,0.0,Dimension.Y),
	
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
		put("startGap", new PropertySetters.SingleBooleanPropertySetter(SHAPE_CURVED));
		put("endGap", new PropertySetters.SingleBooleanPropertySetter(SHAPE_CURVED));
		put("scaleAll", new PropertySetters.SingleBooleanPropertySetter(SCALE_ALL));
		// aliasses
		put("drawScreen", new PropertySetters.DualOrRepeatSingleBooleanPropertySetter(DRAW_SCREEN_X, DRAW_SCREEN_Y));
		put("capGaps", new PropertySetters.DualOrRepeatSingleBooleanPropertySetter(START_GAP, END_GAP));
		
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
		put("mouseOverHalign", new PropertySetters.SingleIntOrRealPropertySetter(MOUSEOVER_HALIGN));
		put("hgapFactor", new PropertySetters.SingleIntOrRealPropertySetter(HGAP_FACTOR));
		put("innerRadius", new PropertySetters.SingleIntOrRealPropertySetter(INNERRADIUS));
		put("lineWidth", new PropertySetters.SingleIntOrRealPropertySetter(LINE_WIDTH));
		put("textAngle", new PropertySetters.SingleIntOrRealPropertySetter(TEXT_ANGLE));
		put("fromAngle", new PropertySetters.SingleIntOrRealPropertySetter(FROM_ANGLE));
		put("toAngle", new PropertySetters.SingleIntOrRealPropertySetter(TO_ANGLE));
		put("valign", new PropertySetters.SingleRealPropertySetter(VALIGN));
		put("mouseOverValign", new PropertySetters.SingleRealPropertySetter(MOUSEOVER_VALIGN));
		put("vgapFactor", new PropertySetters.SingleIntOrRealPropertySetter(VGAP_FACTOR));
		// below: aliases
		put("align", new PropertySetters.DualOrRepeatSingleRealPropertySetter(HALIGN, VALIGN));
		put("mouseOverAlign", new PropertySetters.DualOrRepeatSingleIntOrRealPropertySetter(MOUSEOVER_HALIGN, MOUSEOVER_VALIGN));
		put("gapFactor", new PropertySetters.DualOrRepeatSingleIntOrRealPropertySetter(HGAP_FACTOR, VGAP_FACTOR));
		
		put("direction", new PropertySetters.SingleStrPropertySetter(DIRECTION));
		put("layer", new PropertySetters.SingleStrPropertySetter(LAYER));
		put("hint", new PropertySetters.SingleStrPropertySetter(HINT));
		put("id", new PropertySetters.SingleStrPropertySetter(ID));
		put("font", new PropertySetters.SingleStrPropertySetter(FONT));
		put("text", new PropertySetters.SingleStrPropertySetter(TEXT));
	}};
}
