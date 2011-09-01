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
import static org.rascalmpl.library.vis.properties.PropertySemantics.*;

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
	
// 	Name				Type			rascalName			stdDefault			semantics	combination
	SHAPE_CLOSED		(Types.BOOL,	"shapeClosed",		false,				INTERNAL	),	// scheduled for removal
	SHAPE_CONNECTED		(Types.BOOL,	"shapeConnected",	false,				INTERNAL	),	// scheduled for removal
	SHAPE_CURVED		(Types.BOOL,	"shapeCurved",		false,				INTERNAL	),	// scheduled for removal
	HSTART_GAP			(Types.BOOL,	"hstartGap",		false,				INTERNAL	),
	HEND_GAP			(Types.BOOL,	"hendGap",			false,				INTERNAL	),
	VSTART_GAP			(Types.BOOL,	"vstartGap",		false,				INTERNAL	),
	VEND_GAP			(Types.BOOL,	"vendGap",			false,				INTERNAL	),
	HRESIZABLE			(Types.BOOL,	"hresizable",		true,				BOTH		),
	VRESIZABLE			(Types.BOOL,	"vresizable",		true,				BOTH		),
	HZOOMABLE			(Types.BOOL,    "hzoomable",		true,				BOTH		),
	VZOOMABLE			(Types.BOOL,	"vzoomable",		true,				BOTH		),
	ALLOW_ROTATE_FULL	(Types.BOOL,	"allAngles",		true,				INTERNAL	),
	SHADOW				(Types.BOOL,	"shadow",			false,				INTERNAL	),
	SPREAD				(Types.BOOL,    "spread",			false,				INTERNAL	),
	MANHATTAN_LINES		(Types.BOOL,	"manhattan",		true,				INTERNAL	),
	MAJOR_X				(Types.BOOL,    "majorx",			false,				INTERNAL	),
	
	FILL_COLOR			(Types.COLOR,	"fillColor",		WHITE,				INTERNAL	),  
	FONT_COLOR			(Types.COLOR,	"fontColor",		BLACK,				INTERNAL	),   
	LINE_COLOR			(Types.COLOR,	"lineColor",		BLACK,				INTERNAL	),  
	SHADOW_COLOR		(Types.COLOR,	"shadowColor",		dropShadowColor(),	INTERNAL	),  
	
	ASPECT_RATIO		(Types.REAL,	"aspectRatio",		1.0,				INTERNAL	),
	INNER_ALIGN			(Types.REAL,	"ialign",			0.0,				INTERNAL	),
	HSIZE				(Types.REAL,	"hsize",			0.0,				BOTH		),
	VSIZE				(Types.REAL,	"vsize",			0.0,				BOTH		),
	HGAP				(Types.REAL,	"hgap",				0.0,				INTERNAL	),	
	VGAP				(Types.REAL,	"vgap",				0.0,				INTERNAL	),	
	HSHADOWPOS			(Types.REAL,	"hshadowPos",		10.0,				INTERNAL	),
	VSHADOWPOS			(Types.REAL,	"vshadowPos",		10.0,				INTERNAL	),
	HCONNECT			(Types.REAL,	"hConnect",			0.5,				EXTERNAL	),
	VCONNECT			(Types.REAL,	"vConnect",			0.5,				EXTERNAL	),
	HSHRINK				(Types.REAL,	"hshrink",			1.0,				EXTERNAL,	 MUL),
	VSHRINK				(Types.REAL, 	"vshrink",			1.0,				EXTERNAL,	 MUL),
	HALIGN				(Types.REAL,	"halign",			0.5,				EXTERNAL	),
	VALIGN				(Types.REAL,	"valign",			0.5,				EXTERNAL	),
	HPOS				(Types.REAL,	"hpos",				0.0,				EXTERNAL	),
	VPOS				(Types.REAL,	"vpos",				0.0,				EXTERNAL	),
	HGROW				(Types.REAL, 	"hgrow",			1.0,				INTERNAL,	 MUL),
	VGROW				(Types.REAL, 	"vgrow",			1.0,				INTERNAL,	 MUL),
	LINE_WIDTH			(Types.REAL,	"lineWidth",		1.0,				INTERNAL	),
	TEXT_ANGLE			(Types.REAL,	"textAngle",		0.0,				INTERNAL	),
	
	TO_ARROW			(Types.FIGURE,	"toArrow",			null,				INTERNAL	),
	FROM_ARROW			(Types.FIGURE,	"fromArrow",		null,				INTERNAL	),
	LABEL				(Types.FIGURE,	"label",			null,				INTERNAL	),
	
	FONT_SIZE			(Types.INT,		"fontSize",			12,					INTERNAL	),

	LINE_STYLE			(Types.STR,		"lineStyle",		"solid",			INTERNAL	),
	HINT				(Types.STR,		"hint",				"",					INTERNAL	),	
	ID					(Types.STR,		"id",				"",					EXTERNAL	),
	LAYER				(Types.STR,		"layer",			"",					INTERNAL	),
	FONT				(Types.STR,		"font",				"Helvetica",		INTERNAL	),
	DIR					(Types.STR,		"dir",				"",					INTERNAL	),
	
	MOUSE_CLICK			(Types.HANDLER,	"onClick",			null,		"bool ()"		),
	ON_MOUSEMOVE		(Types.HANDLER,	"onMouseMove",		null,		"void (bool)"		),
	ON_KEY				(Types.HANDLER,	"onKey",			null,		"bool (KeySym, bool, map[KeyModifier,bool])");
	

	public String name;
	public Types type;
	public Object stdDefault;
	public String callBackType;
	public Combine combine;
	public PropertySemantics semantics;
	

	Properties(Types type,String name,Object stdDefault,PropertySemantics semantics){
		this(type,name,stdDefault,null,type.defaultCombine,semantics);
	}
	
	Properties(Types type,String name,Object stdDefault,PropertySemantics semantics, Combine combine){
		this(type,name,stdDefault,null,combine,semantics);
	}
	

	Properties(Types type,String name,Object stdDefault, String callBackType){
		this(type,name,stdDefault,callBackType,type.defaultCombine,PropertySemantics.INTERNAL);
	}
	
	Properties(Types type,String name,Object stdDefault, String callBackType, Combine combine,PropertySemantics semantics){
		this.name = name;
		this.type = type;
		this.stdDefault = stdDefault;
		this.callBackType = callBackType;
		this.combine = combine;
		this.semantics = semantics;
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

