/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Atze van der Ploeg - Atze.van.der.Ploeg@cwi.nl - CWI
*******************************************************************************/

package org.rascalmpl.library.vis.properties;

import static org.rascalmpl.library.vis.properties.CombinedProperty.Combine.MUL;
import static org.rascalmpl.library.vis.util.FigureColorUtils.BLACK;
import static org.rascalmpl.library.vis.util.FigureColorUtils.LIGHTGRAY;
import static org.rascalmpl.library.vis.util.FigureColorUtils.WHITE;
import static org.rascalmpl.library.vis.util.FigureColorUtils.dropShadowColor;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.library.vis.properties.CombinedProperty.Combine;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.RascalToJavaValueConverters.Convert;
import org.rascalmpl.library.vis.util.RascalToJavaValueConverters.ConvertStr;
import org.rascalmpl.library.vis.util.RascalToJavaValueConverters.DoNotConvert;

	  	  

public enum Properties {
	
// 	Name				Type			rascalName			stdDefault			determines layout?	combination
	SHAPE_CLOSED		(Types.BOOL,	"shapeClosed",		false,				false	),	// scheduled for removal
	SHAPE_CONNECTED		(Types.BOOL,	"shapeConnected",	false,				false	),	// scheduled for removal
	SHAPE_CURVED		(Types.BOOL,	"shapeCurved",		false,				false	),	// scheduled for removal
	HSTART_GAP			(Types.BOOL,	"hstartGap",		false,				true	),
	HEND_GAP			(Types.BOOL,	"hendGap",			false,				true	),
	VSTART_GAP			(Types.BOOL,	"vstartGap",		false,				true	),
	VEND_GAP			(Types.BOOL,	"vendGap",			false,				true	),
	HRESIZABLE			(Types.BOOL,	"hresizable",		true,				true	),
	VRESIZABLE			(Types.BOOL,	"vresizable",		true,				true	),
	HZOOMABLE			(Types.BOOL,    "hzoomable",		true,				true	),
	VZOOMABLE			(Types.BOOL,	"vzoomable",		true,				true	),
	ALLOW_ROTATE_FULL	(Types.BOOL,	"allAngles",		true,				true	),
	SHADOW				(Types.BOOL,	"shadow",			false,				false	),
	SPREAD				(Types.BOOL,    "spread",			true,				false	),
	MANHATTAN_LINES		(Types.BOOL,	"manhattan",		true,				false	),
	MAJOR_X				(Types.BOOL,    "majorx",			false,				false	),
	
	FILL_COLOR			(Types.COLOR,	"fillColor",		WHITE,				false	),  
	FONT_COLOR			(Types.COLOR,	"fontColor",		BLACK,				false	),   
	LINE_COLOR			(Types.COLOR,	"lineColor",		BLACK,				false	),  
	SHADOW_COLOR		(Types.COLOR,	"shadowColor",		dropShadowColor(),	false	),  
	
	ASPECT_RATIO		(Types.REAL,	"aspectRatio",		1.0,				true	),
	INNER_ALIGN			(Types.REAL,	"ialign",			0.5,				false	),
	HSIZE				(Types.REAL,	"hsize",			0.0,				true	),
	VSIZE				(Types.REAL,	"vsize",			0.0,				true	),
	HGAP				(Types.REAL,	"hgap",				0.0,				false	),	// scheduled for removal
	VGAP				(Types.REAL,	"vgap",				0.0,				false	),	// scheduled for removal
	HSHADOWPOS			(Types.REAL,	"hshadowPos",		10.0,				false	),
	VSHADOWPOS			(Types.REAL,	"vshadowPos",		10.0,				false	),
	HCONNECT			(Types.REAL,	"hConnect",			0.5,				true	),
	VCONNECT			(Types.REAL,	"vConnect",			0.5,				true	),
	HSHRINK				(Types.REAL,	"hshrink",			1.0,				true,	 MUL),
	VSHRINK				(Types.REAL, 	"vshrink",			1.0,				true,	 MUL),
	HALIGN				(Types.REAL,	"halign",			0.5,				true	),
	VALIGN				(Types.REAL,	"valign",			0.5,				true	),
	HPOS				(Types.REAL,	"hpos",				0.0,				true	),
	VPOS				(Types.REAL,	"vpos",				0.0,				true	),
	HGROW				(Types.REAL, 	"hgrow",			1.0,				true,	 MUL),
	VGROW				(Types.REAL, 	"vgrow",			1.0,				true,	 MUL),
	LINE_WIDTH			(Types.REAL,	"lineWidth",		1.0,				true	),
	
	TO_ARROW			(Types.FIGURE,	"toArrow",			null,				true	),
	FROM_ARROW			(Types.FIGURE,	"fromArrow",		null,				true	),
	LABEL				(Types.FIGURE,	"label",			null,				true	),
	
	FONT_SIZE			(Types.INT,		"fontSize",			12,					true	),

	LINE_STYLE			(Types.STR,		"lineStyle",		"solid",			false	),
	HINT				(Types.STR,		"hint",				"",					false	),	// scheduled for removal
	ID					(Types.STR,		"id",				"",					false	),
	LAYER				(Types.STR,		"layer",			"",					false	),
	FONT				(Types.STR,		"font",				"Helvetica",		true	),
	DIR					(Types.STR,		"dir",				"",					false	),
	
	MOUSE_CLICK			(Types.HANDLER,	"onClick",			null,		"bool ()"		),
	ON_MOUSEMOVE		(Types.HANDLER,	"onMouseMove",		null,		"void (bool)"		),
	ON_KEY				(Types.HANDLER,	"onKey",			null,		"bool (KeySym, bool, map[KeyModifier,bool])");
	

	public String name;
	public Types type;
	public Object stdDefault;
	public String callBackType;
	public Combine combine;
	public boolean determinesLayout;
	

	Properties(Types type,String name,Object stdDefault,boolean determinesLayout){
		this(type,name,stdDefault,null,type.defaultCombine,determinesLayout);
	}
	
	Properties(Types type,String name,Object stdDefault,boolean determinesLayout, Combine combine){
		this(type,name,stdDefault,null,combine,determinesLayout);
	}
	

	Properties(Types type,String name,Object stdDefault, String callBackType){
		this(type,name,stdDefault,callBackType,type.defaultCombine,false);
	}
	
	Properties(Types type,String name,Object stdDefault, String callBackType, Combine combine,boolean determinesLayout){
		this.name = name;
		this.type = type;
		this.stdDefault = stdDefault;
		this.callBackType = callBackType;
		this.combine = combine;
		this.determinesLayout = determinesLayout;
	}
	

	@SuppressWarnings("unchecked")
	public <PropValue> PropertyValue<PropValue> producePropertyValue(IValue arg,
			PropertyManager pm, IFigureConstructionEnv env) {
		if(type == Types.HANDLER){
			return (PropertyValue<PropValue>) new HandlerValue(arg);
		}
		return producePropertyValue(arg, pm, env,type.getConverter());
	}
	

	@SuppressWarnings("unchecked")
	public static <PropValue> PropertyValue<PropValue> produceMaybeComputedValue(Types type,IValue arg,
			PropertyManager pm, IFigureConstructionEnv env){
		return produceMaybeComputedValue(arg, pm, env, type.getConverter());
	}
	
	private static <PropValue> PropertyValue<PropValue> produceMaybeComputedValue(IValue arg,
			PropertyManager pm, IFigureConstructionEnv env,Convert<PropValue> convert){

		if(arg.getType().isExternalType() && ((arg instanceof RascalFunction) || (arg instanceof OverloadedFunctionResult))){
			return new ComputedValue<PropValue>(arg, env, pm, convert);
		}
		return new ConstantValue<PropValue>( convert.convert(arg, pm, env));
	}
	
	private static <PropValue> PropertyValue<PropValue> producePropertyValue(IValue arg,
			PropertyManager pm, IFigureConstructionEnv env, Convert<PropValue> convert) {
		if(arg.getType().isAbstractDataType()){
			IConstructor cs = (IConstructor) arg;
			if(cs.getName().equals("convert")){
				return new MeasureValue<PropValue>(
						producePropertyValue(cs.get(0),pm,env, ConvertStr.instance), 
						producePropertyValue(cs.get(1),pm,env, DoNotConvert.instance));
			}
		} 
		return produceMaybeComputedValue(arg,pm,env,convert);
	}
	
	static final HashMap<String, Properties> propertyLookup = new HashMap<String, Properties>();
	
	static {
		for(Properties property : values()){
			propertyLookup.put(property.name, property);
		}
	}
}
