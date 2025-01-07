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
import java.util.Map;
import java.util.stream.Collectors;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.library.lang.json.internal.JsonValueReader;
import org.rascalmpl.library.lang.json.internal.JsonValueWriter;
import org.rascalmpl.types.ReifiedType;
import org.rascalmpl.types.TypeReifier;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.functions.IFunction;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeStore;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class IO {
	private final IRascalValueFactory values;
	private final IRascalMonitor monitor;

	public IO(IRascalValueFactory values, IRascalMonitor monitor) {
		super();
		this.values = values;
		this.monitor = monitor;
	}

	public IValue readJSON(IValue type, ISourceLocation loc, IString dateTimeFormat, IBool lenient, IBool trackOrigins, IFunction parsers, IMap nulls, IBool explicitConstructorNames, IBool explicitDataTypes) {
      TypeStore store = new TypeStore();
      Type start = new TypeReifier(values).valueToType((IConstructor) type, store);
      
	  if (parsers.getType() instanceof ReifiedType && parsers.getType().getTypeParameters().getFieldType(0).isTop()) {
		// ignore the default parser
		parsers = null;
	  }
	  
      try (JsonReader in = new JsonReader(URIResolverRegistry.getInstance().getCharacterReader(loc))) {
		in.setLenient(lenient.getValue());
        return new JsonValueReader(values, store, monitor, trackOrigins.getValue() ? loc : null)
            .setCalendarFormat(dateTimeFormat.getValue())
			.setParsers(parsers)
			.setNulls(unreify(nulls))
			.setExplicitConstructorNames(explicitConstructorNames.getValue())
			.setExplicitDataTypes(explicitDataTypes.getValue())
            .read(in, start);
      }
      catch (IOException e) {
        throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
      }
      catch (NullPointerException e) {
        throw RuntimeExceptionFactory.io(values.string("NPE in error handling code"), null, null);
      }
    }
	
	private Map<Type, IValue> unreify(IMap nulls) {
		var tr = new TypeReifier(values);
		return nulls.stream()
			.map(t -> (ITuple) t)
			.collect(Collectors.toMap(t -> tr.valueToType((IConstructor) t.get(0)), t -> t.get(1)));
	}

	public IValue parseJSON(IValue type, IString src, IString dateTimeFormat, IBool lenient, IBool trackOrigins, IFunction parsers, IMap nulls, IBool explicitConstructorNames, IBool explicitDataTypes) {
	      TypeStore store = new TypeStore();
	      Type start = new TypeReifier(values).valueToType((IConstructor) type, store);

	      try (JsonReader in = new JsonReader(new StringReader(src.getValue()))) {
			in.setLenient(lenient.getValue());
	        return new JsonValueReader(values, store, monitor, trackOrigins.getValue() ? URIUtil.rootLocation("unknown") : null)
	            .setCalendarFormat(dateTimeFormat.getValue())
				.setParsers(parsers)
				.setNulls(unreify(nulls))
				.setExplicitConstructorNames(explicitConstructorNames.getValue())
				.setExplicitDataTypes(explicitDataTypes.getValue())
	            .read(in, start);
	      }
	      catch (IOException e) {
	        throw RuntimeExceptionFactory.io(values.string(e.getMessage()));
	      }
	      catch (NullPointerException e) {
	        throw RuntimeExceptionFactory.io(values.string("NPE"));
	      }
	    }
	
	public void writeJSON(ISourceLocation loc, IValue value, IBool unpackedLocations, IString dateTimeFormat, IBool dateTimeAsInt, IInteger indent, IBool dropOrigins, IFunction formatter, IBool explicitConstructorNames, IBool explicitDataTypes) {
	    try (JsonWriter out = new JsonWriter(new OutputStreamWriter(URIResolverRegistry.getInstance().getOutputStream(loc, false), Charset.forName("UTF8")))) {
	        if (indent.intValue() > 0) {
	            out.setIndent("        ".substring(0, indent.intValue() % 9));
	        }
	        
	        new JsonValueWriter()
	        .setCalendarFormat(dateTimeFormat.getValue())
	        .setDatesAsInt(dateTimeAsInt.getValue())
	        .setUnpackedLocations(unpackedLocations.getValue())
			.setDropOrigins(dropOrigins.getValue())
			.setFormatters(formatter)
			.setExplicitConstructorNames(explicitConstructorNames.getValue())
			.setExplicitDataTypes(explicitDataTypes.getValue())
	        .write(out, value);
	    } catch (IOException e) {
	        throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
	    } 
	}
	
	public IString asJSON(IValue value, IBool unpackedLocations, IString dateTimeFormat, IBool dateTimeAsInt, IInteger indent, IBool dropOrigins, IFunction formatter, IBool explicitConstructorNames, IBool explicitDataTypes) {
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
				.setFormatters(formatter)
				.setExplicitConstructorNames(explicitConstructorNames.getValue())
				.setExplicitDataTypes(explicitDataTypes.getValue())
	        	.write(out, value);

	        return values.string(string.toString());
	    } catch (IOException e) {
	        throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
	    } 
	}
}
