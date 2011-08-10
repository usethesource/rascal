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
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.swt.ICallbackEnv;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.Key;
import org.rascalmpl.library.vis.util.NameResolver;
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
	IRunTimePropertyChanges runTimeChanges;
	
	public static PropertyManager extendProperties(IFigureConstructionEnv fpa, IConstructor c, PropertyManager pm, IList childProps){
		IList props = (IList) c.get(c.arity()-1);
			 return new PropertyManager(fpa, pm, props);                         
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
	
	public PropertyManager(IFigureConstructionEnv fpa, PropertyManager inherited, IList props) {
		parent = inherited;
		allocateArrays(props);
		setProperties(fpa,props);
		this.runTimeChanges = fpa.getRunTimePropertyChanges();
	}
	
	public PropertyManager() {
		explicitValues = new PropertyValue[0];
		stdValues = new PropertyValue[0];
		parent = null;
		this.runTimeChanges = null;
	}
	

	private void allocateArrays(IList props) {
		int nrExplicitProperties = 0;
		int nrStdProperties = 0;
		for(IValue v : props){
			IConstructor c = (IConstructor) v;
			String pname = c.getName();
			if(pname.startsWith("_child")){
			} 
			else if(pname.equals("std")){
				pname = ((IConstructor)c.get(0)).getName();
				nrStdProperties+=Properties.propertySetters.get(pname).nrOfPropertiesProduced();;
			} else {
				nrExplicitProperties+=Properties.propertySetters.get(pname).nrOfPropertiesProduced();;
			}
		}
		explicitValues = new PropertyValue[nrExplicitProperties];
		stdValues = new PropertyValue[nrStdProperties];
	}
	
	private void setProperties(IFigureConstructionEnv fpa, IList props) {
		int stdPropsIndex = 0;
		int explicitPropsIndex = 0;
		for (IValue v : props) {
			
			IConstructor c = (IConstructor) v;
			String pname = c.getName();
			if(pname.startsWith("_child")){
				continue;
			}
			if(pname.equals("std")){
				// convert stdSize to size
				c = (IConstructor)c.get(0);
				pname = c.getName();
				stdPropsIndex = Properties.propertySetters.get(pname).execute(stdValues, stdPropsIndex, c, fpa, this);
			} else {
				explicitPropsIndex = Properties.propertySetters.get(pname).execute(explicitValues, explicitPropsIndex, c, fpa, this);
			}
		}
		Arrays.sort(explicitValues);
		Arrays.sort(stdValues);
	}
	

	public void computeProperties(ICallbackEnv env){
		for(PropertyValue v : explicitValues){
			if(v.property.type != Types.HANDLER)
				v.compute(env);
		}
		for(PropertyValue v : stdValues){
			if(v.property.type != Types.HANDLER)
				v.compute(env);
		}
	}
	
	public void registerMeasures(NameResolver resolver){
		for(PropertyValue v : explicitValues){
				v.registerMeasures(resolver);
		}
		for(PropertyValue v : stdValues){
				v.registerMeasures(resolver);
		}
	}
	
	public void getLikes(NameResolver resolver){
		for(PropertyValue v : explicitValues){
				v.getLikes(resolver);
		}
		for(PropertyValue v : stdValues){
				v.getLikes(resolver);
		}
	}
	
	public boolean isPropertySet(Properties property){
		return safeBinarySearch(explicitValues, property) >= 0;
	}
	
	int safeBinarySearch(PropertyValue[] values, Properties property){
		if(values.length == 0) return -1;
		else return Arrays.binarySearch(values, property);
	}
	
	public PropertyValue getPropertyValue(Properties property){
		int i = safeBinarySearch(explicitValues, property);
		if(i>=0){
			return explicitValues[i];
		}
		return getStdPropertyValue(property);
	}

	PropertyValue getStdPropertyValue(Properties property) {
		int i;
		i = safeBinarySearch(stdValues, property);
		if(i>=0){
			return stdValues[i];
		} 
		if(parent != null){
			return parent.getStdPropertyValue(property);
		} 
		return null;
	}
	
	public boolean isConverted(Properties property){
		PropertyValue v = getPropertyValue(property);
		if(v == null) return false;
		return v.isConverted();
	}
	
	public IValue getUnconverted(Properties property){
		PropertyValue v = getPropertyValue(property);
		return v.getUnconverted();
	}
	
	public Key getKey(Properties property){
		PropertyValue v = getPropertyValue(property);
		if(v == null) return null;
		return v.getKey();
	}
	
	public String getKeyId(Properties property){
		PropertyValue v = getPropertyValue(property);
		if(v == null) return null;
		return v.getKeyId();
	}
	
	private Object adoptProperty(Properties property, Object val){
		if(runTimeChanges == null){
			return val;
		} else {
			return runTimeChanges.adoptPropertyVal(property, val);
		}
	}
	
	public Object getProperty(Properties property){
		PropertyValue pv = getPropertyValue(property);
		if(pv == null){
			return adoptProperty(property,property.stdDefault);
		} else {
			return adoptProperty(property,pv.getValue());
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
	
	public void executeHandlerProperty(ICallbackEnv env,Properties property) {
		checkCorrectType(property, Types.HANDLER);
		getPropertyValue(property).compute(env);
	}
	

	public void executeVoidHandlerProperty(ICallbackEnv env,Properties property,Type[] types,IValue[] args ){
		getPropertyValue(property).executeVoid(env,types, args);
	}
	
	public IValue executeHandlerPropertyWithSingleArgument(ICallbackEnv env,Properties property,Type type,IValue arg ){
		return getPropertyValue(property).executeWithSingleArg(env,type, arg);
	}
	
	public IValue executeHandlerProperty(ICallbackEnv env,Properties property,Type[] types,IValue[] args ){
		return getPropertyValue(property).execute(env,types, args);
	}
	
}
