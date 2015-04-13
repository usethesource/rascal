/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Bert Lisser    - Bert.Lisser@cwi.nl
 *******************************************************************************/
package org.rascalmpl.library.lang.json;

import java.io.IOException;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.lang.json.io.JSONReadingTypeVisitor;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.icu.text.DateFormat;

/*
 * This class overrides the method fromJSON from lang/json/IO that need to be handled differenty in compiled code
 * since we have to reify the type differently.
 */

public class IOCompiled extends IO {
	private final IValueFactory values;
	private TypeReifier tr;

	public IOCompiled(IValueFactory values) {
		super(values);
		this.values = values;
		this.tr = new TypeReifier(values);
	}

	public IValue fromJSON(IValue type, IString src, RascalExecutionContext rex) {
		TypeStore store = new TypeStore();
		
		IConstructor type_cons = ((IConstructor) type);
		IMap definitions = rex.getSymbolDefinitions();

		tr.declareAbstractDataTypes(definitions, store);
		Type start = tr.valueToType(type_cons, store);
		
		//TypeStore store = ctx.getCurrentEnvt().getStore();
		//Type start = new TypeReifier(ctx.getValueFactory()).valueToType((IConstructor) type, store);
		
		System.err.println("fromJSON0:"+start);
		Gson gson = new GsonBuilder()
		.enableComplexMapKeySerialization()
		.setDateFormat(DateFormat.LONG)
		.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
		.setVersion(1.0)
		.create();
		System.err.println("fromJSON1:"+src.getValue());
		Object obj = gson.fromJson(src.getValue(), Object.class);
		System.err.println("fromJSON2:"+start);
		try {
			return JSONReadingTypeVisitor.read(obj, values, store, start);
		}
		catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
	}
	
}
