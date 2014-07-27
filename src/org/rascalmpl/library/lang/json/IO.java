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

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.lang.json.io.IValueAdapter;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ibm.icu.text.DateFormat;

public class IO {
	private final IValueFactory values;

	public IO(IValueFactory values) {
		super();

		this.values = values;
	}


	private Gson gsonFor(Type t, TypeStore ts) {
		return new GsonBuilder()
		.registerTypeAdapter(IValue.class, new IValueAdapter(t, values, ts))
		.enableComplexMapKeySerialization()
		.setDateFormat(DateFormat.LONG)
		.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
		.setVersion(1.0)
		.create();
	}

	public IString toJSON(IValue value) {
		Gson gson = gsonFor(value.getType(), new TypeStore() /* ignored when writing */);
		try {
			String json = gson.toJson(value, new TypeToken<IValue>() {}.getType());
			return values.string(json);
		} catch (Exception e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
	}
	public IValue fromJSON(IValue type, IString src, IEvaluatorContext ctx) {
		TypeStore store = ctx.getCurrentEnvt().getStore();
		Type start = new TypeReifier(ctx.getValueFactory()).valueToType((IConstructor) type, store);
		Gson gson = gsonFor(start, store);
		try {
			return gson.fromJson(src.getValue(), IValue.class);
		} catch (Exception e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		} 
	}
	
}
