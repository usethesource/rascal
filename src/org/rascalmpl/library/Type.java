/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
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

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.types.TypeReifier;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class Type {
	private final IValueFactory vf;
	private final IMap emptyMap;

	public Type(IValueFactory vf) {
		this.vf = vf;
		emptyMap = vf.mapWriter().done();
	}
	  
	public IValue typeOf(IValue v) {
		return new TypeReifier(vf).typeToValue(
		        v.getType(), 
		        new TypeStore(),
		        vf.mapWriter().done()
		        ).get("symbol");
	}
	
	public IBool eq(IValue x, IValue y) {
	  return vf.bool(x.equals(y));
	}
	
	public IValue make(IValue type, IString name, IList args) {
		return make(type, name, args, emptyMap);
	}
	
	public IValue make(IValue type, IString name, IList args, IMap keywordParameters) {
		TypeStore store = new TypeStore();
		io.usethesource.vallang.type.Type t = new TypeReifier(vf).valueToType((IConstructor) type, store);
		
		IValue[] children = new IValue[args.length()];
		io.usethesource.vallang.type.Type[] argsTypes = new io.usethesource.vallang.type.Type[args.length()];

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
			
			io.usethesource.vallang.type.Type constructor
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

	public IBool intersects(IConstructor sym1, IConstructor sym2) {
		var tr = new TypeReifier(vf);
		io.usethesource.vallang.type.Type t1 = tr.symbolToType(sym1);
		io.usethesource.vallang.type.Type t2 = tr.symbolToType(sym2);
		return vf.bool(t1.intersects(t2));
	}

	public IBool subtype(IConstructor sym1, IConstructor sym2) {
		var tr = new TypeReifier(vf);
		io.usethesource.vallang.type.Type t1 = tr.symbolToType(sym1);
		io.usethesource.vallang.type.Type t2 = tr.symbolToType(sym2);
		return vf.bool(t1.isSubtypeOf(t2));
	}

	public IConstructor lub(IConstructor sym1, IConstructor sym2) {
		var tr = new TypeReifier(vf);
		var ts = new TypeStore();
		io.usethesource.vallang.type.Type t1 = tr.symbolToType(sym1);
		io.usethesource.vallang.type.Type t2 = tr.symbolToType(sym2);
		return (IConstructor) tr.typeToValue(t1.lub(t2), ts, vf.map()).get("symbol");
	}

	public IConstructor glb(IConstructor sym1, IConstructor sym2) {
		var tr = new TypeReifier(vf);
		var ts = new TypeStore();
		io.usethesource.vallang.type.Type t1 = tr.symbolToType(sym1);
		io.usethesource.vallang.type.Type t2 = tr.symbolToType(sym2);
		return (IConstructor) tr.typeToValue(t1.glb(t2), ts, vf.map()).get("symbol");
	}
	
}
