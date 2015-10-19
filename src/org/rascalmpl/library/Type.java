/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;

public class Type {
	private final IValueFactory vf;
	private final TypeFactory tf;
	private final IMap emptyMap;

	public Type(IValueFactory vf) {
		this.vf = vf;
		this.tf = TypeFactory.getInstance();
		emptyMap = vf.map(tf.stringType(), tf.valueType());
	}
	
	public IValue typeOf(IValue v, IEvaluatorContext ctx) {
		return ((IConstructor) new TypeReifier(vf).typeToValue(v.getType(), ctx).getValue()).get("symbol");
	}
	
	public IBool eq(IValue x, IValue y) {
	  return vf.bool(x.isEqual(y));
	}
	
	public IValue make(IValue type, IString name, IList args) {
		return make(type, name, args, emptyMap);
	}
	
	public IValue make(IValue type, IString name, IList args, IMap keywordParameters) {
		TypeStore store = new TypeStore();
		org.rascalmpl.value.type.Type t = new TypeReifier(vf).valueToType((IConstructor) type, store);
		
		IValue[] children = new IValue[args.length()];
		org.rascalmpl.value.type.Type[] argsTypes = new org.rascalmpl.value.type.Type[args.length()];

		for (int i = 0; i < args.length(); i++) {
			children[i] = args.get(i);
			argsTypes[i] = children[i].getType();
		}
		
		Map<String, IValue> kwmap;
		
		if(keywordParameters.size() == 0){
			kwmap = Collections.emptyMap();
		} else {

			Iterator<Entry<IValue, IValue>> iter = keywordParameters.entryIterator();
			kwmap = new HashMap<String, IValue>();
			while(iter.hasNext()){
				Entry<IValue, IValue> entry = iter.next();
				kwmap.put(((IString) entry.getKey()).getValue(), entry.getValue());
			}
		}
		
		try {
			
			org.rascalmpl.value.type.Type constructor 
			= store.lookupConstructor(t, name.getValue(), TypeFactory.getInstance().tupleType(argsTypes));
			
			if (constructor == null) {
				// TODO: improve error messaging, using specialized exception
				throw RuntimeExceptionFactory.illegalArgument(type, null, null);
			}
			return vf.constructor(constructor, children, kwmap);

		}
		catch (FactTypeUseException e) {
			// TODO: improve error messaging, using specialized exception
			throw RuntimeExceptionFactory.illegalArgument(type, null, null);
		}
	}
	
}
