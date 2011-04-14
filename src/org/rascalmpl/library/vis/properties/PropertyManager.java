/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Bert Lisser - Bert.Lisser@cwi.nl (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.vis.properties;

import java.util.EnumMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.descriptions.BoolProp;
import org.rascalmpl.library.vis.properties.descriptions.ColorProp;
import org.rascalmpl.library.vis.properties.descriptions.IntProp;
import org.rascalmpl.library.vis.properties.descriptions.RealProp;
import org.rascalmpl.library.vis.properties.descriptions.StrProp;
import org.rascalmpl.values.ValueFactoryFactory;

/**
 * Manage the properties of a figure.
 * 
 * @author paulk
 *
 */

public class PropertyManager implements IPropertyManager {

	static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	static IList emptyList = vf.list();
	
	protected Figure mouseOverFigure = null; // Interaction and mouse handling
	protected IValue onClickHandler = null;
	private boolean draggable;
	
	class Values{
		
		EnumMap<BoolProp, IPropertyValue<Boolean>> boolValues;
		EnumMap<IntProp, IPropertyValue<Integer>> intValues;
		EnumMap<RealProp, IPropertyValue<Float>> realValues;
		EnumMap<StrProp, IPropertyValue<String>> strValues;
		EnumMap<ColorProp, IPropertyValue<Integer>> colorValues;
		
	}
	
	Values explicitValues, stdValues;
	
	public static PropertyManager extendProperties(IFigureApplet fpa, IConstructor c, PropertyManager pm, IEvaluatorContext ctx){		
		IList props = (IList) c.get(c.arity()-1);
		if(pm != null && !pm.anyExplicitPropertiesSet() && pm.getMouseOver() == null && pm.getOnClick() == null 
				&& (props == null || props.length()==0)){
			return pm; // reuse old property manager
		} else {
			return new PropertyManager(fpa, pm, props, ctx);
		}
								                         
	}
	
	public PropertyManager(IFigureApplet fpa, PropertyManager inherited, IList props, IEvaluatorContext ctx) {
		draggable = false;
		setProperties(fpa, props, ctx);
		inheritStdProperties(inherited);
	}

	private void setProperties(IFigureApplet fpa, IList props,
			IEvaluatorContext ctx) {
		for (IValue v : props) {
			
			IConstructor c = (IConstructor) v;
			String pname = c.getName();
			if(pname.equals("mouseOver")){
				mouseOverFigure = FigureFactory.make(fpa, 
													 (IConstructor)c.get(0), 
													 new PropertyManager(fpa,this,emptyList,ctx),
													 ctx);
				continue;	
			} 
			if(pname.equals("onClick")){
				onClickHandler = c.get(0);
				continue;	
			} 
			Values values;
			if(pname.startsWith("std")){
				if(stdValues == null){
					stdValues = new Values();
				}
				values = stdValues;
				int stdLength = "std".length();
				// convert stdSize to size
				pname = pname.substring(stdLength,stdLength+1).toLowerCase() + pname.substring(stdLength+1);
			} else {
				if(explicitValues == null){
					explicitValues = new Values();
				}
				values = explicitValues;
			}
			if(BoolProp.propertySetters.containsKey(pname)){
				if(values.boolValues == null){
					values.boolValues = new EnumMap<BoolProp, IPropertyValue<Boolean>>(BoolProp.class);
				}
				BoolProp.propertySetters.get(pname).execute(values.boolValues, c, fpa, ctx);
			} else if(IntProp.propertySetters.containsKey(pname)){
				if(values.intValues == null){
					values.intValues = new EnumMap<IntProp, IPropertyValue<Integer>>(IntProp.class);
				}
				IntProp.propertySetters.get(pname).execute(values.intValues, c, fpa, ctx);
			}  else if(RealProp.propertySetters.containsKey(pname)){
				if(values.realValues == null){
					 values.realValues = new EnumMap<RealProp, IPropertyValue<Float>>(RealProp.class);
				}
				RealProp.propertySetters.get(pname).execute(values.realValues, c, fpa, ctx);
			} else if(StrProp.settersStr.containsKey(pname)){
				if(values.strValues == null){
					values.strValues = new EnumMap<StrProp, IPropertyValue<String>>(StrProp.class);
				}
				StrProp.settersStr.get(pname).execute(values.strValues, c, fpa, ctx);
			} else if(ColorProp.propertySetters.containsKey(pname)){
				if(values.colorValues == null){
					values.colorValues = new EnumMap<ColorProp, IPropertyValue<Integer>>(ColorProp.class);
				}
				ColorProp.propertySetters.get(pname).execute(values.colorValues, c, fpa, ctx);
			} else {
				throw RuntimeExceptionFactory.illegalArgument(c, ctx
						.getCurrentAST(), ctx.getStackTrace());
			}
		}
	}
	
	private void inheritStdProperties(PropertyManager inherited) {
		if(inherited == null){
			return;
		}
		if(stdValues == null){
			stdValues = inherited.stdValues;
		} else if(inherited.stdValues != null){
			
			if(stdValues.boolValues == null){
				stdValues.boolValues = inherited.stdValues.boolValues;
			} else if(inherited.stdValues.boolValues != null) {
				for(BoolProp p : BoolProp.values()){
					if(!stdValues.boolValues.containsKey(p) && inherited.stdValues.boolValues.containsKey(p)){
						stdValues.boolValues.put(p, inherited.stdValues.boolValues.get(p));
					}
				}
			}
			if(stdValues.intValues == null){
				stdValues.intValues = inherited.stdValues.intValues;
			} else if(inherited.stdValues.intValues != null){
				for(IntProp p : IntProp.values()){
					if(!stdValues.intValues.containsKey(p) && inherited.stdValues.intValues.containsKey(p)){
						stdValues.intValues.put(p, inherited.stdValues.intValues.get(p));
					}
				}
			}
			if(stdValues.realValues == null){
				stdValues.realValues = inherited.stdValues.realValues;
			} else if(inherited.stdValues.realValues != null){
				for(RealProp p : RealProp.values()){
					if(!stdValues.realValues.containsKey(p) && inherited.stdValues.realValues.containsKey(p)){
						stdValues.realValues.put(p, inherited.stdValues.realValues.get(p));
					}
				}
			}
			if(stdValues.strValues == null){
				stdValues.strValues = inherited.stdValues.strValues;
			} else if(inherited.stdValues.strValues != null){
				for(StrProp p : StrProp.values()){
					if(!stdValues.strValues.containsKey(p) && inherited.stdValues.strValues.containsKey(p)){
						stdValues.strValues.put(p, inherited.stdValues.strValues.get(p));
					}
				}
			}
			if(stdValues.colorValues == null){
				stdValues.colorValues = inherited.stdValues.colorValues;
			} else if(inherited.stdValues.colorValues != null){
				for(ColorProp p : ColorProp.values()){
					if(!stdValues.colorValues.containsKey(p) && inherited.stdValues.colorValues.containsKey(p)){
						stdValues.colorValues.put(p, inherited.stdValues.colorValues.get(p));
					}
				}
			}
		}
	}
	
	
	public boolean anyExplicitPropertiesSet() {
		return explicitValues != null;
	}
	

	public boolean isBooleanPropertySet(BoolProp property){
		return explicitValues != null && 
		       explicitValues.boolValues != null &&
		       explicitValues.boolValues.containsKey(property);
	}
	public boolean getBooleanProperty(BoolProp property) {
		if(isBooleanPropertySet(property)){
			return explicitValues.boolValues.get(property).getValue();
		} else if(stdValues!= null && stdValues.boolValues != null && 
				stdValues.boolValues.containsKey(property)){
			return stdValues.boolValues.get(property).getValue();
		} else {
			return BoolProp.stdDefaults.get(property);
		}
	}
	
	public boolean isIntegerPropertySet(IntProp property){
		return explicitValues != null && 
	       explicitValues.intValues != null &&
	       explicitValues.intValues.containsKey(property);
	}
	public int getIntegerProperty(IntProp property) {
		if(isIntegerPropertySet(property)){
			return explicitValues.intValues.get(property).getValue();
		} else if(stdValues!= null && stdValues.intValues != null && 
				stdValues.intValues.containsKey(property)){
			return stdValues.intValues.get(property).getValue();
		} else {
			return IntProp.stdDefaults.get(property);
		}
	}
	
	public boolean isRealPropertySet(RealProp property){
		return explicitValues != null && 
	       explicitValues.realValues != null &&
	       explicitValues.realValues.containsKey(property);

	}
	public float getRealProperty(RealProp property) {
		if(isRealPropertySet(property)){
			return explicitValues.realValues.get(property).getValue();
		} else if(stdValues!= null && stdValues.realValues != null && 
				stdValues.realValues.containsKey(property)){
			return stdValues.realValues.get(property).getValue();
		} else {
			return RealProp.stdDefaults.get(property);
		}
	}
	
	public boolean isStringPropertySet(StrProp property){
		return explicitValues != null && 
	       explicitValues.strValues != null &&
	       explicitValues.strValues.containsKey(property);
	}
	public String getStringProperty(StrProp property) {
		if(isStringPropertySet(property)){
			return explicitValues.strValues.get(property).getValue();
		} else if(stdValues!= null && stdValues.strValues != null && 
				stdValues.strValues.containsKey(property)){
			return stdValues.strValues.get(property).getValue();
		} else {
			return StrProp.stdDefaults.get(property);
		}
	}
	
	public boolean isColorPropertySet(ColorProp property){
		return explicitValues != null && 
	       explicitValues.colorValues != null &&
	       explicitValues.colorValues.containsKey(property);
	}
	public int getColorProperty(ColorProp property) {
		if(isColorPropertySet(property)){
			return explicitValues.colorValues.get(property).getValue();
		} else if(stdValues!= null && stdValues.colorValues != null && 
				stdValues.colorValues.containsKey(property)){
			return stdValues.colorValues.get(property).getValue();
		} else {
			return ColorProp.stdDefaults.get(property);
		}
	}
	
	public Figure getMouseOver() {
			return mouseOverFigure;
	}
	
	public IValue getOnClick(){
			return onClickHandler;
	}
	
	public boolean isDraggable(){
		return draggable;
	}
}
