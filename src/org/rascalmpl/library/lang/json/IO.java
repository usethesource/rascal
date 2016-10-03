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
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.lang.json.io.IValueAdapter;
import org.rascalmpl.library.lang.json.io.JSONReadingTypeVisitor;
import org.rascalmpl.library.lang.json.io.JsonValueReader;
import org.rascalmpl.library.lang.json.io.JsonValueWriter;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeStore;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.ibm.icu.text.DateFormat;

public class IO {
	private final IValueFactory values;

	public IO(IValueFactory values) {
		super();
		this.values = values;
	}
	
	public IString toJSON(IValue value) {
		  return toJSON(value, this.values.bool(false));
	}


	public IString toJSON(IValue value, IBool compact) {
		// System.err.println("hallo");
		IValueAdapter adap = new IValueAdapter(compact.getValue());
		// System.err.println(adap);
		Gson gson = new GsonBuilder()
		.registerTypeAdapter(IValue.class, adap)
		.enableComplexMapKeySerialization()
		.setDateFormat(DateFormat.LONG)
		.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
		.setVersion(1.0)
		.disableHtmlEscaping()  // Bert Lisser
		.create();
		try {
			String json = gson.toJson(value, new TypeToken<IValue>() {}.getType());
			return values.string(json);
		} catch (Exception e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
	}
	
	public IValue readJSON(IValue type, ISourceLocation loc, IBool implicitConstructors, IBool implicitNodes, IString dateTimeFormat) {
      TypeStore store = new TypeStore();
      Type start = new TypeReifier(values).valueToType((IConstructor) type, store);
      
      try (JsonReader in = new JsonReader(URIResolverRegistry.getInstance().getCharacterReader(loc))) {
        return new JsonValueReader(values, store)
            .setConstructorsAsObjects(implicitConstructors.getValue())
            .setNodesAsObjects(implicitNodes.getValue())
            .setCalendarFormat(dateTimeFormat.getValue())
            .read(in, start);
      }
      catch (IOException e) {
        throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
      }
      catch (NullPointerException e) {
        e.printStackTrace();
        throw RuntimeExceptionFactory.io(values.string("NPE"), null, null);
      }
    }
	
	public void writeJSON(ISourceLocation loc, IValue value, IBool implicitConstructors, IBool implicitNodes, IString dateTimeFormat, IBool dateTimeAsInt) {
	  try (JsonWriter out = new JsonWriter(new OutputStreamWriter(URIResolverRegistry.getInstance().getOutputStream(loc, false), Charset.forName("UTF8")))) {
        new JsonValueWriter()
        .setConstructorsAsObjects(implicitConstructors.getValue())
        .setNodesAsObjects(implicitNodes.getValue())
        .setCalendarFormat(dateTimeFormat.getValue())
        .setDatesAsInt(dateTimeAsInt.getValue())
        .write(out, value);
      } catch (IOException e) {
        throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
      } 
	}
	
	
	public IValue fromJSON(IValue type, IString src, IEvaluatorContext ctx) {
		TypeStore store = ctx.getCurrentEnvt().getStore();
		Type start = new TypeReifier(ctx.getValueFactory()).valueToType((IConstructor) type, store);
		Gson gson = new GsonBuilder()
		.enableComplexMapKeySerialization()
		.setDateFormat(DateFormat.LONG)
		.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
		.setVersion(1.0)
		.create();
		Object obj = gson.fromJson(src.getValue(), Object.class);
		try {
			return JSONReadingTypeVisitor.read(obj, values, store, start);
		}
		catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
	}
	
}
