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

import java.util.Arrays;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.values.ValueFactoryFactory;


/**
 * Manage the properties of a figure.
 * 
 * @author paulk
 *
 */
@SuppressWarnings("rawtypes")
public class PropertyManager {

	static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	static IList emptyList = vf.list();
	
	PropertyValue[] explicitValues, stdValues;
	PropertyManager parent;

	
	public static PropertyManager extendProperties(IFigureApplet fpa, IConstructor c, PropertyManager pm, IList childProps, IEvaluatorContext ctx){
		IList props = (IList) c.get(c.arity()-1);
		
			
		/*if(childProps != null){
			PropertyManager result = new PropertyManager(fpa, pm, childProps, ctx);
			// explicitly set props override childprops..
			result.setProperties(fpa, props, ctx);
			return result;
		} else { */
			 return new PropertyManager(fpa, pm, props, ctx);
		//}
				                         
	}
	
	public static IList getChildProperties(IList props){
		IList result = null;
		for (IValue v : props) {
			if(v instanceof IConstructor && ((IConstructor)v).getName().equals("_child")){
				IList childList = (IList)((IConstructor)v).get(0);
				if(result == null){
					result = childList;
				} else {
					result.concat(childList);
				}
			}
		}
		return result;
	}
	
	public PropertyManager(IFigureApplet fpa, PropertyManager inherited, IList props, IEvaluatorContext ctx) {
		parent = inherited;
		allocateArrays(props);
		setProperties(fpa,props,ctx);
	}
	
	public PropertyManager() {
		explicitValues = new PropertyValue[0];
		stdValues = new PropertyValue[0];
		parent = null;
	}
	

	private void allocateArrays(IList props) {
		int nrExplicitProperties = 0;
		int nrStdProperties = 0;
		for(IValue v : props){
			String pname = ((IConstructor) v).getName();
			
			if(pname.startsWith("_child")){
			} 
			else if(pname.startsWith("std")){
				pname = stripStd(pname);
				nrStdProperties+=Properties.propertySetters.get(pname).nrOfPropertiesProduced();;
			} else {
				nrExplicitProperties+=Properties.propertySetters.get(pname).nrOfPropertiesProduced();;
			}
		}
		explicitValues = new PropertyValue[nrExplicitProperties];
		stdValues = new PropertyValue[nrStdProperties];
	}
	
	
	private String stripStd(String s){
		int stdLength = "std".length();
		return s.substring(stdLength,stdLength+1).toLowerCase() + s.substring(stdLength+1);
	}

	private void setProperties(IFigureApplet fpa, IList props,
			IEvaluatorContext ctx) {
		int stdPropsIndex = 0;
		int explicitPropsIndex = 0;
		for (IValue v : props) {
			
			IConstructor c = (IConstructor) v;
			String pname = c.getName();
			if(pname.startsWith("_child")){
				continue;
			}
			if(pname.startsWith("std")){
				// convert stdSize to size
				pname = stripStd(pname);
				stdPropsIndex = Properties.propertySetters.get(pname).execute(stdValues, stdPropsIndex, c, fpa, ctx, this);
			} else {
				explicitPropsIndex = Properties.propertySetters.get(pname).execute(explicitValues, explicitPropsIndex, c, fpa, ctx, this);
			}
		}
		Arrays.sort(explicitValues);
		Arrays.sort(stdValues);
	}
	

	public void computeProperties(){
		for(PropertyValue v : explicitValues){
			if(v.property.type != Types.HANDLER)
				v.compute();
		}
		for(PropertyValue v : stdValues){
			if(v.property.type != Types.HANDLER)
				v.compute();
		}
	}
	
	public boolean isPropertySet(Properties property){
		return safeBinarySearch(explicitValues, property) >= 0;
	}
	
	int safeBinarySearch(PropertyValue[] values, Properties property){
		if(values.length == 0) return -1;
		else return Arrays.binarySearch(explicitValues, property);
	}
	
	public PropertyValue getPropertyValue(Properties property){
		int i = safeBinarySearch(explicitValues, property);
		if(i>=0){
			return explicitValues[i];
		}
		i = safeBinarySearch(stdValues, property);
		if(i>=0){
			return stdValues[i];
		}
		if(parent != null){
			return parent.getPropertyValue(property);
		} 
		return null;
	}
	
	public Object getProperty(Properties property){
		PropertyValue pv = getPropertyValue(property);
		if(pv == null){
			return property.stdDefault;
		} else {
			return pv.getValue();
		}
	}
	
	public void checkCorrectType(Properties property, Types type){
		if(property.type != type ){
			throw new Error(String.format("Property %s is not of type %s\n",property,Types.BOOL));
		}
	}
	
	public boolean anyExplicitPropertiesSet() {
		return explicitValues.length > 0;
	}

	public boolean isBooleanPropertySet(Properties property){
		checkCorrectType(property, Types.BOOL);
		return isPropertySet(property);
	}

	public boolean getBooleanProperty(Properties property) {
		checkCorrectType(property, Types.BOOL);
		return (Boolean)getProperty(property);
	}
	
	public boolean isIntegerPropertySet(Properties property){
		checkCorrectType(property, Types.INT);
		return isPropertySet(property);
	}
	public int getIntegerProperty(Properties property) {
		checkCorrectType(property, Types.INT);
		return (Integer)getProperty(property);
	}
	
	public boolean isRealPropertySet(Properties property){
		checkCorrectType(property, Types.REAL);
		return isPropertySet(property);

	}
	public double getRealProperty(Properties property) {
		checkCorrectType(property, Types.REAL);
		return (Double)getProperty(property);
	}
	
	public boolean isMeasurePropertySet(Properties property){
		checkCorrectType(property, Types.DIMENSIONAL);
		return isPropertySet(property);
	}
	
	public Measure getMeasureProperty(Properties property){
		checkCorrectType(property, Types.DIMENSIONAL);
		return (Measure)getProperty(property);
	}
	
	public boolean isStringPropertySet(Properties property){
		checkCorrectType(property, Types.STR);
		return isPropertySet(property);
	}
	public String getStringProperty(Properties property) {
		checkCorrectType(property, Types.STR);
		return (String)getProperty(property);
	}
	
	public boolean isColorPropertySet(Properties property){
		checkCorrectType(property, Types.COLOR);
		return isPropertySet(property);
	}
	public int getColorProperty(Properties property) {
		checkCorrectType(property, Types.COLOR);
		return (Integer)getProperty(property);
	}
	
	public boolean isFigurePropertySet(Properties property){
		checkCorrectType(property, Types.FIGURE);
		return isPropertySet(property);
	}
	public Figure getFigureProperty(Properties property) {
		checkCorrectType(property, Types.FIGURE);
		return (Figure)getProperty(property);
	}
	
	
	
	public boolean handlerCanBeExecuted(Properties property){
		return isHandlerPropertySet(property) || isStandardHandlerPropertySet(property) || isStandardDefaultHandlerPropertySet(property);
	}
	
	public boolean isHandlerPropertySet(Properties property){
		checkCorrectType(property, Types.HANDLER);
		return isPropertySet(property);
	}
	public boolean isStandardHandlerPropertySet(Properties property){
		checkCorrectType(property, Types.HANDLER);
		 return Arrays.binarySearch(stdValues, property) >= 0;
	}
	
	public boolean isStandardDefaultHandlerPropertySet(Properties property){
		return false; // std default handler are not supported
	}
	
	public void executeHandlerProperty(Properties property) {
		checkCorrectType(property, Types.HANDLER);
		getPropertyValue(property).compute();
	}
}
