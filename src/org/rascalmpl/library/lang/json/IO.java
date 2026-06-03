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
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.library.lang.json.internal.JsonValueReader;
import org.rascalmpl.library.lang.json.internal.JsonValueWriter;
import org.rascalmpl.types.TypeReifier;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.IRascalValueFactory;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
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
        
        try {
            return new JsonValueReader(values, store, monitor, loc)
                    .setOptions(options)
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
        try (JsonWriter out =
            new JsonWriter(new OutputStreamWriter(URIResolverRegistry.getInstance().getOutputStream(loc, false),
                Charset.forName("UTF8")))) {
            
            var w = new JsonValueWriter();

            if (options != null) {
                w.setOptions(options);
            }

            w.write(out, value);
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(e);
        }
    }

    public IString asJSON(IValue value, IConstructor options) {
        StringWriter string = new StringWriter();

        try (JsonWriter out = new JsonWriter(string)) {
            var w = new JsonValueWriter();
            if (options != null) {
                w = w.setOptions(options);    
            }

            w.write(out, value);
            
            return values.string(string.toString());
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(e);
        }
    }    
}
