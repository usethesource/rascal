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

import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.types.TypeReifier;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.RascalValueFactory;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class Type {
	private final IRascalValueFactory vf;
	// private final IMap emptyMap;
	private final TypeFactory tf;

	public Type(IRascalValueFactory vf, TypeFactory tf) {
		this.vf = vf;
		this.tf = tf;
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
		return make(type, name, args, vf.map());
	}
	
	public IValue make(IValue type, IString name, IList args, IMap keywordParameters) {
		Map<String, IValue> kwMap = keywordParameters
			.stream()
			.map(v -> (ITuple) v)
			.collect(Collectors.toMap(t -> ((IString) t.get(0)).getValue(), t -> t.get(1)));

		IValue[] children = args.stream().toArray(IValue[]::new);
		
		io.usethesource.vallang.type.Type[] argsTypes 
			= args.stream().map(v -> v.getType()).toArray(io.usethesource.vallang.type.Type[]::new);

		TypeStore store = new TypeStore();
		io.usethesource.vallang.type.Type t = new TypeReifier(vf).valueToType((IConstructor) type, store);
		
		try {
			
			io.usethesource.vallang.type.Type constructor
			= store.lookupConstructor(t, name.getValue(), TypeFactory.getInstance().tupleType(argsTypes));
			
			if (constructor == null) {
				throw RuntimeExceptionFactory.illegalArgument(type, vf.string("one of the parameters of the constructor did not fit its declaration."));
			}
			
			return vf.constructor(constructor, children, kwMap);

		}
		catch (FactTypeUseException e) {
			throw RuntimeExceptionFactory.illegalArgument(type, vf.string("one of the (keyword) parameters of the constructor did not fit its declaration."));
		}
	}

	public IValue make(IConstructor cons, IList args, IMap keywordParameters) {
		Map<String, IValue> kwMap = keywordParameters
			.stream()
			.map(v -> (ITuple) v)
			.collect(Collectors.toMap(t -> ((IString) t.get(0)).getValue(), t -> t.get(1)));

		if (cons.getConstructorType() == RascalValueFactory.Production_Default) {
			// this is a parse tree production. We can just return the tree, and add the keyword parameters
			return vf.appl(cons, args).asWithKeywordParameters().setParameters(kwMap);
		}

		if (cons.getConstructorType() != RascalValueFactory.Production_Cons) {
			throw RuntimeExceptionFactory.illegalArgument(cons, "must be a cons or a prod rule.");
		}

		io.usethesource.vallang.type.Type constructor = new TypeReifier(vf).productionToConstructorType(cons);
	
		try {
			IValue[] children = args.stream().toArray(IValue[]::new);
			return vf.constructor(constructor, children, kwMap);
		}
		catch (FactTypeUseException e) {
			throw RuntimeExceptionFactory.illegalArgument(cons, vf.string("one of the parameters of the constructor did not fit its declaration."));
		}
	}
	
}
