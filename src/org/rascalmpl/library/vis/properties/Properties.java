package org.rascalmpl.library.vis.properties;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import org.rascalmpl.library.vis.FigureColorUtils;
import org.rascalmpl.library.vis.properties.Types;
import org.rascalmpl.library.vis.properties.PropertySetters.SingleIntOrRealPropertySetter;
import org.rascalmpl.library.vis.util.Dimension;

public enum Properties {
	MOUSE_STICK(Types.BOOL,true),
	SHAPE_CLOSED(Types.BOOL,false), 	
	SHAPE_CONNECTED(Types.BOOL,false),
	SHAPE_CURVED(Types.BOOL,false),
	HSTART_GAP(Types.BOOL,false),
	HEND_GAP(Types.BOOL,false),
	VSTART_GAP(Types.BOOL,false),
	VEND_GAP(Types.BOOL,false),
	HRESIZABLE(Types.BOOL,true),
	VRESIZABLE(Types.BOOL,true),
	SHADOW(Types.BOOL,false),
	
	FILL_COLOR(Types.COLOR,FigureColorUtils.colorNames.get("white").intValue()),  
	FONT_COLOR(Types.COLOR,FigureColorUtils.colorNames.get("black").intValue()),    
	LINE_COLOR(Types.COLOR,FigureColorUtils.colorNames.get("black").intValue()),
	GUIDE_COLOR(Types.COLOR,FigureColorUtils.colorNames.get("lightgray").intValue()),
	SHADOW_COLOR(Types.COLOR,FigureColorUtils.dropShadowColor()),
	
	HEIGHT(Types.REAL,0.0,Dimension.X),
	HGAP(Types.REAL,0.0,Dimension.X), 
	VGAP(Types.REAL,0.0,Dimension.Y), 	
	WIDTH(Types.REAL,0.0,Dimension.Y),
	HLOC(Types.REAL,0.0,Dimension.X),
	VLOC(Types.REAL,0.0,Dimension.Y),
	SHADOWLEFT(Types.REAL,10.0,Dimension.X),
	SHADOWTOP(Types.REAL,10.0,Dimension.Y),
	
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
	HCONNECT(Types.REAL,0.5,Dimension.X),
	VCONNECT(Types.REAL, 0.5, Dimension.Y),
	MOUSEOVER_HALIGN(Types.REAL,0.5,Dimension.X), // TODO: remove this, replace with overlay rewrite
	INNERRADIUS(Types.REAL,0.0), // TODO: innerradisu is not axis aligned
	LINE_WIDTH(Types.REAL,1.0),
	LINE_STYLE(Types.STR,"solid"),
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
		put("mouseStick" , new PropertySetters.SingleBooleanPropertySetter(MOUSE_STICK));
		put("shapeClosed", new PropertySetters.SingleBooleanPropertySetter(SHAPE_CLOSED));
		put("shapeConnected", new PropertySetters.SingleBooleanPropertySetter(SHAPE_CONNECTED));
		put("shapeCurved", new PropertySetters.SingleBooleanPropertySetter(SHAPE_CURVED));
		put("hstartGap", new PropertySetters.SingleBooleanPropertySetter(HSTART_GAP));
		put("hendGap", new PropertySetters.SingleBooleanPropertySetter(HEND_GAP));
		put("vstartGap", new PropertySetters.SingleBooleanPropertySetter(VSTART_GAP));
		put("vendGap", new PropertySetters.SingleBooleanPropertySetter(VEND_GAP));
		put("hresizable", new PropertySetters.SingleBooleanPropertySetter(HRESIZABLE));
		put("vresizable", new PropertySetters.SingleBooleanPropertySetter(VRESIZABLE));
		put("shadow", new PropertySetters.SingleBooleanPropertySetter(SHADOW));
		// aliasses
		put("hcapGaps", new PropertySetters.DualOrRepeatSingleBooleanPropertySetter(HSTART_GAP, HEND_GAP));
		put("vcapGaps", new PropertySetters.DualOrRepeatSingleBooleanPropertySetter(VSTART_GAP, VEND_GAP));
		put("resizable", new PropertySetters.DualOrRepeatSingleBooleanPropertySetter(HRESIZABLE, VRESIZABLE));
		
		put("fillColor", new PropertySetters.SingleColorPropertySetter(FILL_COLOR));
		put("fontColor", new PropertySetters.SingleColorPropertySetter(FONT_COLOR));
		put("lineColor", new PropertySetters.SingleColorPropertySetter(LINE_COLOR));
		put("lineStyle", new PropertySetters.SingleStrPropertySetter(LINE_STYLE));
		put("guideColor",new PropertySetters.SingleColorPropertySetter(GUIDE_COLOR));	
		put("shadowColor",new PropertySetters.SingleColorPropertySetter(SHADOW_COLOR));
		
		put("height", new PropertySetters.SingleIntOrRealPropertySetter(HEIGHT));
		put("hgap", new PropertySetters.SingleIntOrRealPropertySetter(HGAP));
		put("vgap", new PropertySetters.SingleIntOrRealPropertySetter(VGAP));
		put("width", new PropertySetters.SingleIntOrRealPropertySetter(WIDTH));
		put("hpos", new PropertySetters.SingleIntOrRealPropertySetter(HLOC));
		put("vpos", new PropertySetters.SingleIntOrRealPropertySetter(VLOC));
		put("shadowLeft", new PropertySetters.SingleIntOrRealPropertySetter(SHADOWLEFT));
		put("shadowTop", new PropertySetters.SingleIntOrRealPropertySetter(SHADOWTOP));
		
		// below: aliasses
		put("pos", new PropertySetters.DualOrRepeatSingleIntOrRealPropertySetter(HLOC, VLOC));
		put("gap", new PropertySetters.DualOrRepeatSingleIntOrRealPropertySetter(HGAP, VGAP));
		put("size", new PropertySetters.DualOrRepeatSingleIntOrRealPropertySetter(WIDTH, HEIGHT));
		
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
		put("textAngle", new PropertySetters.SingleIntOrRealPropertySetter(TEXT_ANGLE));
		put("fromAngle", new PropertySetters.SingleIntOrRealPropertySetter(FROM_ANGLE));
		put("toAngle", new PropertySetters.SingleIntOrRealPropertySetter(TO_ANGLE));
		put("valign", new PropertySetters.SingleRealPropertySetter(VALIGN));
		put("vgrow", new PropertySetters.SingleIntOrRealPropertySetter(VGROW));
		put("vshrink", new PropertySetters.SingleIntOrRealPropertySetter(VSHRINK));
		put("mouseOverValign", new PropertySetters.SingleRealPropertySetter(MOUSEOVER_VALIGN));
		put("hconnect", new PropertySetters.SingleIntOrRealPropertySetter(HCONNECT));
		put("vconnect", new PropertySetters.SingleIntOrRealPropertySetter(VCONNECT));
		// below: aliases
		put("align", new PropertySetters.DualOrRepeatSingleRealPropertySetter(HALIGN, VALIGN));
		put("mouseOverAlign", new PropertySetters.DualOrRepeatSingleIntOrRealPropertySetter(MOUSEOVER_HALIGN, MOUSEOVER_VALIGN));
		put("grow", new PropertySetters.DualOrRepeatSingleIntOrRealPropertySetter(HGROW, VGROW));
		put("shrink", new PropertySetters.DualOrRepeatSingleIntOrRealPropertySetter(HSHRINK, VSHRINK));
		put("connect", new PropertySetters.DualOrRepeatSingleIntOrRealPropertySetter(HCONNECT, VCONNECT));
		
		put("direction", new PropertySetters.SingleStrPropertySetter(DIRECTION));
		put("layer", new PropertySetters.SingleStrPropertySetter(LAYER));
		put("hint", new PropertySetters.SingleStrPropertySetter(HINT));
		put("id", new PropertySetters.SingleStrPropertySetter(ID));
		put("font", new PropertySetters.SingleStrPropertySetter(FONT));
		put("text", new PropertySetters.SingleStrPropertySetter(TEXT));
	}};
	
	
	// Below:code to generate rascal code for properties
	static enum Notations{
		CONSTANT,
		CONSTANT_SUGAR,
		COMPUTED,
		LIKE,
		MEASURE;
	}
	
	static String capitalize(String s){
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}
	
	static String argumentTypeInNotation(Types type,Notations n){
		switch(n){
		case CONSTANT:  return type.rascalName;
		case CONSTANT_SUGAR:  return type.syntaxSugar;
		case COMPUTED: return "computed" + capitalize(type.rascalName);
		case LIKE: return "Like";
		case MEASURE: return String.format("Convert", type.rascalName);
		}
		throw new Error("Unkown notation");
	}
	
	static String argumentInNotation(Types type,Notations n){
		switch(n){
		case CONSTANT:  return String.format("%2s", type.shortName);
		case CONSTANT_SUGAR:  return String.format("s%s", type.shortName);
		case COMPUTED: return String.format("c%s", type.shortName);
		case LIKE: return String.format("l%s", type.shortName);
		case MEASURE: return String.format("m%s", type.shortName);
		}
		throw new Error("Unkown notation");
	}
	

	
	static Vector<String> genArgumentCode(HashMap<String, Integer> nameOccurances, Types type, int nrTimes){
		Vector<String> result = new Vector<String>();
		if(nrTimes == 0 ){
			result.add("");
			return result;
		}
		for(Notations n : Notations.values()){
			if(type == Types.HANDLER && n == Notations.COMPUTED) continue;
			if(n == Notations.CONSTANT_SUGAR && type.syntaxSugar == null) continue;
			String typeName = argumentTypeInNotation(type, n);
			String argName = argumentInNotation(type, n);
			Vector<String> deeper = genArgumentCode(nameOccurances,type,nrTimes-1);
			for(String rest : deeper){
				int occur = 0;
				if(nameOccurances.containsKey(argName)){
					occur = nameOccurances.get(argName);
				} 
				String argNameN = argName+String.format("%d",occur);
				nameOccurances.put(argName, occur+1);
				String myArg = String.format("%-17s %s",typeName,argNameN);
				if(!rest.equals("")){
					myArg = myArg + ", " + rest;
				}
				result.add(myArg);
			}
		}
		return result;
	}
	
	static final boolean[] stds = {false,true};
	
	static String genPropertyCode(String propertyName,PropertySetters.PropertySetter setter,boolean std){
		Types type = setter.getProperty(0).type;
		HashMap<String, Integer> nameOccurances = new HashMap<String, Integer>();
		String result = "";
		for(int nrTimes = setter.minNrOfArguments(); nrTimes <= setter.maxNrOfArguments(); nrTimes++){
			Vector<String> argStrings = genArgumentCode(nameOccurances, type, nrTimes);
			for(String s : argStrings){
				String propertyDesc ;
				if(std){
					propertyDesc = "std" + capitalize(propertyName);
				} else {
					propertyDesc = propertyName;
				}
				result= result + String.format("\t| %-20s (%s)\n",propertyDesc,s);
			}
		}
		return result;
	}
	
	// generates rascal code for properties!
	public static void main(String[] argv){
		String[] propertyNames = propertySetters.keySet().toArray(new String[0]);
		Arrays.sort(propertyNames);
		for(boolean std :stds){
			for(String propertyName : propertyNames){
				System.out.print(genPropertyCode(propertyName,propertySetters.get(propertyName),std));
			}
		}
		
	}
}
