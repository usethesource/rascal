/*******************************************************************************
 * Copyright (c) 2009-2013 CWI All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 
 * * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI 
 * * Mark Hills - Mark.Hills@cwi.nl (CWI) 
 * * Arnold - Lankamp - Arnold.Lankamp@cwi.nl 
 * * Bert Lisser - Bert.Lisser@cwi.nl
 *******************************************************************************/
package org.rascalmpl.library.lang.json;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
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

import com.google.gson.stream.JsonWriter;

public class IO {
    private final IRascalValueFactory values;
    private final IRascalMonitor monitor;

    public IO(IRascalValueFactory values, IRascalMonitor monitor) {
        super();
        this.values = values;
        this.monitor = monitor;
    }

    private IValue doReadJSON(Reader in, 
        IValue type, ISourceLocation loc, IConstructor options) throws IOException {
        TypeStore store = new TypeStore();
        Type start = new TypeReifier(values).valueToType((IConstructor) type, store);
        var kws = options.asWithKeywordParameters();
        IString dtf = (IString) kws.getParameter("dateTimeFormat");
        IBool lenient = (IBool) kws.getParameter("lenient");
        IFunction parsers = (IFunction) kws.getParameter("parser");
        IBool ecn = (IBool) kws.getParameter("explicitConstructorNames");
        IBool edt = (IBool) kws.getParameter("explicitDataTypes");
        IBool tor = (IBool) kws.getParameter("trackOrigins");
        IMap nulls = (IMap) kws.getParameter("nulls");

        if (nulls == null) {
            throw RuntimeExceptionFactory.io("JSON option `nulls` must have been set.");
        }
       
        
        if (parsers.getType() instanceof ReifiedType && parsers.getType().getTypeParameters().getFieldType(0).isTop()) {
            // ignore the default parser
            parsers = null;
        }

        try {
            return new JsonValueReader(values, store, monitor, loc)
                    .setCalendarFormat(dtf != null ? dtf.getValue() : "yyyy-MM-dd\'T\'HH:mm:ss\'Z\'")
                    .setLenient(lenient != null ? lenient.getValue() : false)
                    .setParsers(parsers) 
                    .setNulls(unreify(nulls)) // nulls must be explicit in the options constructor
                    .setExplicitConstructorNames(ecn != null ? ((IBool) ecn).getValue() : false)
                    .setExplicitDataTypes(edt != null ? ((IBool) edt).getValue() : false)
                    .setTrackOrigins(tor != null ? ((IBool) tor).getValue() : false)
                    .read(in, start);
        }
        catch (NullPointerException e) {
            throw RuntimeExceptionFactory.io("NPE in error handling code");
        }
    }
        
    public IValue readJSON(IValue type, ISourceLocation loc, IConstructor options) {
        try (Reader in = URIResolverRegistry.getInstance().getCharacterReader(loc)) {
            return doReadJSON(in, type, loc, options);
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(e);
        }
    }

    public IValue parseJSON(IValue type, IString src, IConstructor options) {
       
        try (Reader in = new StringReader(src.getValue())) {
            return doReadJSON(in, type, null, options);
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(e);
        }
    }

    public void writeJSON(ISourceLocation loc, IValue value, IConstructor options) {
        var kws = options.asWithKeywordParameters();
        int indent = ((IInteger) kws.getParameter("indent")).intValue();
        IString dtf = (IString) kws.getParameter("dateTimeFormat");
        IBool dai = (IBool) kws.getParameter("dateTimeAsInt");
        IBool ras = (IBool) kws.getParameter("rationalsAsString");
        IFunction formatters = (IFunction) kws.getParameter("formatter");
        IBool ecn = (IBool) kws.getParameter("explicitConstructorNames");
        IBool edt = (IBool) kws.getParameter("explicitDataTypes");
        IBool upl = (IBool) kws.getParameter("unpackLocations");
        IBool dor = (IBool) kws.getParameter("dropOrigins");
        
        try (JsonWriter out =
            new JsonWriter(new OutputStreamWriter(URIResolverRegistry.getInstance().getOutputStream(loc, false),
                Charset.forName("UTF8")))) {
            if (indent > 0) {
                out.setIndent("        ".substring(0, indent % 9));
            }

            new JsonValueWriter()
                .setCalendarFormat(dtf != null ? ((IString) dtf).getValue() : "yyyy-MM-dd\'T\'HH:mm:ss\'Z\'")
                .setDatesAsInt(dai != null ? ((IBool) dai).getValue() : true)
                .setRationalsAsString(ras != null ? ((IBool) ras).getValue() : false)
                .setUnpackedLocations(upl != null ? ((IBool) upl).getValue() : false)
                .setDropOrigins(dor != null ? ((IBool) dor).getValue() : true)
                .setFormatters(formatters)
                .setExplicitConstructorNames(ecn != null ? ((IBool) ecn).getValue() : false)
                .setExplicitDataTypes(edt != null ? ((IBool) edt).getValue() : false)
                .write(out, value);
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(e);
        }
    }

    public IString asJSON(IValue value, IConstructor options) {
        var kws = options.asWithKeywordParameters();
        int indent = ((IInteger) kws.getParameter("indent")).intValue();
        IString dtf = (IString) kws.getParameter("dateTimeFormat");
        IBool dai = (IBool) kws.getParameter("dateTimeAsInt");
        IBool ras = (IBool) kws.getParameter("rationalsAsString");
        IFunction formatters = (IFunction) kws.getParameter("formatter");
        IBool ecn = (IBool) kws.getParameter("explicitConstructorNames");
        IBool edt = (IBool) kws.getParameter("explicitDataTypes");
        IBool upl = (IBool) kws.getParameter("unpackLocations");
        IBool dor = (IBool) kws.getParameter("dropOrigins");
       
        StringWriter string = new StringWriter();

        try (JsonWriter out = new JsonWriter(string)) {
            if (indent > 0) {
                out.setIndent("        ".substring(0, indent % 9));
            }
            new JsonValueWriter()
                .setCalendarFormat(dtf != null ? ((IString) dtf).getValue() : "yyyy-MM-dd\'T\'HH:mm:ss\'Z\'")
                .setDatesAsInt(dai != null ? ((IBool) dai).getValue() : true)
                .setRationalsAsString(ras != null ? ((IBool) ras).getValue() : false)
                .setUnpackedLocations(upl != null ? ((IBool) upl).getValue() : false)
                .setDropOrigins(dor != null ? ((IBool) dor).getValue() : true)
                .setFormatters(formatters)
                .setExplicitConstructorNames(ecn != null ? ((IBool) ecn).getValue() : false)
                .setExplicitDataTypes(edt != null ? ((IBool) edt).getValue() : false)
                .write(out, value);

            return values.string(string.toString());
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(e);
        }
    }

    private Map<Type, IValue> unreify(IMap nulls) {
        var tr = new TypeReifier(values);
        return nulls.stream().map(t -> (ITuple) t)
            .collect(Collectors.toMap(t -> tr.valueToType((IConstructor) t.get(0)), t -> t.get(1)));
    }    
}
