/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.vis.properties;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.library.vis.swt.ICallbackEnv;
import org.rascalmpl.library.vis.util.Key;
import org.rascalmpl.library.vis.util.NameResolver;

@SuppressWarnings("rawtypes")
public abstract class PropertyValue<PropType> implements Comparable{
	public Properties property;
	
	public PropertyValue(Properties property) {
		this.property = property;
	}
	
	public Properties getProperty(){
		return property;
	}
	
	public abstract PropType getValue();
	public void compute(ICallbackEnv env){}
	public void registerMeasures(NameResolver resolver){}
	public void getLikes(NameResolver resolver){}
	public void executeVoid(ICallbackEnv env,Type[] types,IValue[] args){}
	public IValue executeWithSingleArg(ICallbackEnv env,Type type,IValue arg){return null;}
	public IValue execute(ICallbackEnv env,Type[] types,IValue[] args){return null;}
	
	public boolean isConverted(){ return false;}
	public String getKeyId() { return null; }
	public Key getKey() { return null; }
	public IValue getUnconverted(){ return null; }
	
	public int compareTo(Object rhs){
		if(rhs instanceof PropertyValue){
			return property.ordinal() - ((PropertyValue)rhs).property.ordinal();
		} else if(rhs instanceof Properties){
			return property.ordinal() - ((Properties)rhs).ordinal();
		} else {
			return -1;
		}
		
	}
}
