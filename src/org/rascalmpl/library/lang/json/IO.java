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
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.library.lang.json.internal.IValueAdapter;
import org.rascalmpl.library.lang.json.internal.JSONReadingTypeVisitor;
import org.rascalmpl.library.lang.json.internal.JsonValueReader;
import org.rascalmpl.library.lang.json.internal.JsonValueWriter;
import org.rascalmpl.types.TypeReifier;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeStore;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.ibm.icu.text.DateFormat;

public class IO {
	private final IValueFactory values;
	private final IRascalMonitor monitor;

	public IO(IValueFactory values, IRascalMonitor monitor) {
		super();
		this.values = values;
		this.monitor = monitor;
	}
	
	public IString toJSON(IValue value) {
		  return toJSON(value, this.values.bool(false));
	}


	public IString toJSON(IValue value, IBool compact) {
		IValueAdapter adap = new IValueAdapter(compact.getValue());
		Gson gson = new GsonBuilder()
		.registerTypeAdapter(IValue.class, adap)
		.enableComplexMapKeySerialization()
		.setDateFormat(DateFormat.LONG)
		.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
		.setVersion(1.0)
		.disableHtmlEscaping() 
		.create();
		try {
			String json = gson.toJson(value, new TypeToken<IValue>() {}.getType());
			return values.string(json);
		} catch (Exception e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
	}
	
	public IValue fromJSON(IValue type, IString src) {
        TypeStore store = new TypeStore();
        Type start = new TypeReifier(values).valueToType((IConstructor) type, store);
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
    
	
	public IValue readJSON(IValue type, ISourceLocation loc, IString dateTimeFormat, IBool lenient, IBool trackOrigins) {
      TypeStore store = new TypeStore();
      Type start = new TypeReifier(values).valueToType((IConstructor) type, store);
      
      try (JsonReader in = new JsonReader(URIResolverRegistry.getInstance().getCharacterReader(loc))) {
		in.setLenient(lenient.getValue());
        return new JsonValueReader(values, store, monitor, trackOrigins.getValue() ? loc : null)
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
	
	public IValue parseJSON(IValue type, IString src, IString dateTimeFormat, IBool lenient, IBool trackOrigins) {
	      TypeStore store = new TypeStore();
	      Type start = new TypeReifier(values).valueToType((IConstructor) type, store);
	      
	      try (JsonReader in = new JsonReader(new StringReader(src.getValue()))) {
			in.setLenient(lenient.getValue());
	        return new JsonValueReader(values, store, monitor, trackOrigins.getValue() ? URIUtil.rootLocation("unknown") : null)
	            .setCalendarFormat(dateTimeFormat.getValue())
	            .read(in, start);
	      }
	      catch (IOException e) {
	        throw RuntimeExceptionFactory.io(values.string(e.getMessage()));
	      }
	      catch (NullPointerException e) {
	        throw RuntimeExceptionFactory.io(values.string("NPE"));
	      }
	    }
	
	public void writeJSON(ISourceLocation loc, IValue value, IBool unpackedLocations, IString dateTimeFormat, IBool dateTimeAsInt, IInteger indent, IBool dropOrigins) {
	    try (JsonWriter out = new JsonWriter(new OutputStreamWriter(URIResolverRegistry.getInstance().getOutputStream(loc, false), Charset.forName("UTF8")))) {
	        if (indent.intValue() > 0) {
	            out.setIndent("        ".substring(0, indent.intValue() % 9));
	        }
	        
	        new JsonValueWriter()
	        .setCalendarFormat(dateTimeFormat.getValue())
	        .setDatesAsInt(dateTimeAsInt.getValue())
	        .setUnpackedLocations(unpackedLocations.getValue())
			.setDropOrigins(dropOrigins.getValue())
	        .write(out, value);
	    } catch (IOException e) {
	        throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
	    } 
	}
	
	public IString asJSON(IValue value, IBool unpackedLocations, IString dateTimeFormat, IBool dateTimeAsInt, IInteger indent, IBool dropOrigins) {
	    StringWriter string = new StringWriter();

	    try (JsonWriter out = new JsonWriter(string)) {
	        if (indent.intValue() > 0) {
	            out.setIndent("        ".substring(0, indent.intValue() % 9));
	        }
	        new JsonValueWriter()
	        	.setCalendarFormat(dateTimeFormat.getValue())
	        	.setDatesAsInt(dateTimeAsInt.getValue())
	        	.setUnpackedLocations(unpackedLocations.getValue())
				.setDropOrigins(dropOrigins.getValue())
	        	.write(out, value);

	        return values.string(string.toString());
	    } catch (IOException e) {
	        throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
	    } 
	}
	
	
	
}
