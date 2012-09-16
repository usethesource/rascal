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

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.swt.ICallbackEnv;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.NameResolver;
import org.rascalmpl.library.vis.util.vector.Dimension;
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
	
	HashMap<Properties, PropertyValue> explicitValues, stdValues;
	
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
	
	public PropertyManager(IFigureConstructionEnv env, PropertyManager inherited, IList props) {
		explicitValues = new HashMap<Properties, PropertyValue>();
		stdValues =  new HashMap<Properties, PropertyValue>();
		parent = inherited;
		setProperties(env,props);
		this.runTimeChanges = env.getRunTimePropertyChanges();
	}
	
	public PropertyManager(IFigureConstructionEnv env) {
		explicitValues = new HashMap<Properties, PropertyValue>(0);
		stdValues =  new HashMap<Properties, PropertyValue>(0);
		parent = null;
		if(env!=null){
			this.runTimeChanges = env.getRunTimePropertyChanges();
		}
	}
	


	public PropertyManager() {
		this(null);
	}
	
	@SuppressWarnings("unchecked")
	private void setProperties(IFigureConstructionEnv env, IList props) {
		for (IValue v : props) {
			HashMap<Properties,PropertyValue> addIn;
			IConstructor c = (IConstructor) v;
			String pname = c.getName();
			if(pname.startsWith("_child")){
				continue;
			}
			if(pname.equals("std")){
				c = (IConstructor)c.get(0);
				pname = c.getName();
				addIn = stdValues;
			} else {
				addIn = explicitValues;
			}
			Properties prop = Properties.propertyLookup.get(pname);
			if(prop == null){
				System.out.printf("Cannot find %s !\n", pname);
			}
			else {
				PropertyValue val = prop.producePropertyValue(c.get(0), this, env);
				if(addIn.containsKey(prop)){
					System.out.printf("Combining!\n");
					addIn.put(prop, new CombinedProperty(addIn.get(prop),val,prop.combine));
				} else {
					addIn.put(prop, val);
				}
			}
		}
	}
	
	public boolean hasHandlerProperties(){
		for(Properties p : explicitValues.keySet()){
			if(p.type == Types.HANDLER) return true;
		}
		return false;
	}
	
	public void stealExternalPropertiesFrom(PropertyManager other){
		stealExternalProperties(other.explicitValues, explicitValues);
		stealExternalProperties(other.stdValues, stdValues);
	}
	
	private void stealExternalProperties(HashMap<Properties, PropertyValue> from, HashMap<Properties, PropertyValue> to){
		for(Properties p : Properties.values()){
			if(from.containsKey(p)){
				if(p.semantics == PropertySemantics.EXTERNAL)  {
					to.put(p, from.get(p));
					from.remove(p);
				}
				if(p.semantics == PropertySemantics.BOTH){
					to.put(p, from.get(p));
					
				}
			}	
		}
	}
	
	public void registerMeasures(NameResolver resolver){
		for(PropertyValue v : explicitValues.values()){
				v.registerMeasures(resolver);
		}
		for(PropertyValue v : stdValues.values()){
				v.registerMeasures(resolver);
		}
	}
	
	public boolean isSet(Properties property){
		return explicitValues.containsKey(property);
	}
	
	public PropertyValue getPropertyValue(Properties property){
		if(explicitValues.containsKey(property)){
			return explicitValues.get(property);
		} else {
			return getStdPropertyValue(property);
		}
	}

	PropertyValue getStdPropertyValue(Properties property) {
		if(stdValues.containsKey(property)){
			return stdValues.get(property);
		} else if (parent != null){
			return parent.getStdPropertyValue(property);
		} else {
			return null;
		}
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
		return explicitValues.size() > 0;
	}


	public boolean getBool(Properties property) {
		checkCorrectType(property, Types.BOOL);
		return (Boolean)getProperty(property);
	}
	
	public int getInt(Properties property) {
		checkCorrectType(property, Types.INT);
		return (Integer)getProperty(property);
	}
	
	public double getReal(Properties property) {
		checkCorrectType(property, Types.REAL);
		return (Double)getProperty(property);
	}

	public String getStr(Properties property) {
		checkCorrectType(property, Types.STR);
		return (String)getProperty(property);
	}
	
	public int getColor(Properties property) {
		checkCorrectType(property, Types.COLOR);
		return (Integer)getProperty(property);
	}
	
	public Figure getFig(Properties property) {
		checkCorrectType(property, Types.FIGURE);
		return (Figure)getProperty(property);
	}
	
	public boolean handlerCanBeExecuted(Properties property){
		return isSet(property) || isStandardHandlerSet(property) || isStandardDefaultHandlerSet(property);
	}
	
	public boolean isStandardHandlerSet(Properties property){
		checkCorrectType(property, Types.HANDLER);
		return stdValues.containsKey(property);
	}
	
	public boolean isStandardDefaultHandlerSet(Properties property){
		return false; // std default handler are not supported
	}
	
	public IValue executeHandler(ICallbackEnv env,Properties property,Type[] types,IValue[] args ){
		return getPropertyValue(property).execute(env,types, args);
	}
	
	public Object get2DProperty(Dimension d,TwoDProperties prop){
		switch(d){
		case X: return getProperty(prop.hor);
		case Y: return getProperty(prop.ver);
		}
		return null;
	}
	
	public boolean is2DPropertySet(Dimension d,TwoDProperties prop){
		switch(d){
		case X: return isSet(prop.hor);
		case Y: return isSet(prop.ver);
		}
		return false;
	}
	
	
	public double get2DReal(Dimension d, TwoDProperties prop){
		checkCorrectType(prop.hor, Types.REAL);
		checkCorrectType(prop.ver, Types.REAL);
		return (Double)get2DProperty(d, prop);
	}
	
	public boolean get2DBool(Dimension d, TwoDProperties prop){
		checkCorrectType(prop.hor, Types.BOOL);
		checkCorrectType(prop.ver, Types.BOOL);
		return (Boolean)get2DProperty(d, prop);
	}

	public void stealProperty(Properties p, PropertyManager prop) {
		if(prop.explicitValues.containsKey(p)){
			explicitValues.put(p, prop.explicitValues.get(p));
			prop.explicitValues.remove(p);
		}
	}
	
}
