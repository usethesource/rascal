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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.types.TypeReifier;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
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
	
	public IValue getConstructor(INode v) {
		assert v.getType().isAbstractData();
		io.usethesource.vallang.type.Type constructor = ((IConstructor) v).getUninstantiatedConstructorType();

		TypeStore store = new TypeStore();
		store.declareAbstractDataType(v.getType());
		store.declareConstructor(constructor);

		ISetWriter grammar = vf.setWriter();
		
		new TypeReifier(vf).typeToValue(constructor, store, vf.map());

		constructor.asProductions(vf, store, grammar, new HashSet<>());

		return grammar.done().stream().map(t -> ((ITuple) t).get(1)).findFirst().get();
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

	public IValue make(IConstructor cons, IList args, IMap keywordParameters) {
		io.usethesource.vallang.type.Type constructor = new TypeReifier(vf).productionToConstructorType(cons);

		assert constructor != null;
		
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
			if (constructor == null) {
				// TODO: improve error messaging, using specialized exception
				throw RuntimeExceptionFactory.illegalArgument(cons, null, null);
			}
			return vf.constructor(constructor, children, kwmap);

		}
		catch (FactTypeUseException e) {
			// TODO: improve error messaging, using specialized exception
			throw RuntimeExceptionFactory.illegalArgument(cons, null, null);
		}
	}
	
}
