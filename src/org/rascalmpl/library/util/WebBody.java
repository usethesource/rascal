/*******************************************************************************
 * Copyright (c) 2014-2026 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.rascalmpl.library.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.io.InputStreamReader;

import org.apache.commons.io.input.ReaderInputStream;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.library.lang.json.internal.JsonValueReader;
import org.rascalmpl.library.lang.json.internal.JsonValueWriter;
import org.rascalmpl.types.RascalTypeFactory;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.functions.IFunction;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.io.StandardTextWriter;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

/**
 * Share utilities between Webserver and Webclient to send and receive HTTP body content
 */
public class WebBody {
    private final ExecutorService executor;
    private final TypeStore store;
    private final IRascalValueFactory vf;
    private final TypeFactory tf;
    private final IRascalMonitor monitor;
    private final org.rascalmpl.library.lang.html.IO html;
    private final org.rascalmpl.library.lang.xml.IO xml;

    public WebBody(TypeStore store, TypeFactory tf, IRascalValueFactory vf, IRascalMonitor monitor, PrintWriter out, PrintWriter err) {
        this.executor = Executors.newCachedThreadPool();
        this.store = store;
        this.tf = tf;
        this.vf = vf;
        this.monitor = monitor;
        this.html = new org.rascalmpl.library.lang.html.IO(vf, store, out, err);
        this.xml = new org.rascalmpl.library.lang.xml.IO(vf);
    }

    public void stopAllStreams() {
        executor.shutdownNow();
    }

    /*
     * This creates a Body::receive() constructor that wraps a lambda to call and receive the data
     * from the HTTP response as a Rascal value. The intermediate step is required such that the 
     * caller of `fetch` can express their expectations on the data and call appropriate parsing,
     * validation and binding mechanisms. The options are encoded as `BodyKind`.
     * 
     * Also this generated function asks for a reified type parameter for (dynamic) type safety
     * and validation of abstract grammars during reception of the body.
     */
    public IFunction createBodyReceiver(InputStream input, ISourceLocation url, String contentType, String charset) {        
        var BodyKind = store.lookupAbstractDataType("BodyKind");
        var rt = RascalTypeFactory.getInstance().reifiedType(tf.parameterType("T"));
        var ft = tf.functionType(tf.parameterType("T"), tf.tupleType(BodyKind, rt), tf.tupleEmpty());

        return vf.function(ft, (args, kwargs) -> {
            IConstructor kind = (IConstructor) args[0];
            IConstructor reified = (IConstructor) args[1];
            Type expect = reified.getType().getTypeParameters().getFieldType(0);

            switch (kind.getName()) {
                case "json":
                    return receiveJsonBody(input, url, kind, expect, contentType, charset);
                case "html":
                    return receiveHTMLBody(input, url, kind, expect, contentType, charset);
                case "xml":
                    return receiveXMLBody(input, url, kind, expect, contentType, charset);
                case "file":
                    return receiveFileBody(input, kind, expect);
                case "text":
                default:
                    return receiveTextBody(input, url, kind, expect, contentType, charset);
            }
        });
    }

    private IValue receiveTextBody(InputStream input, ISourceLocation url, IConstructor kind, Type expect, String contentType, String charset) {
        if (!expect.isSubtypeOf(tf.stringType())) {
            monitor.warning("a text response expects a `str` type, but we have " + expect, url);
        }

        if (!contentType.contains("text/")) {
            monitor.warning("a text response was expected but the mimetype is " + contentType, url);
        }

        try {
            return vf.string(Prelude.consumeInputStream(new InputStreamReader(input, charset)));
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(e);
        }
    }

    private IValue receiveFileBody(InputStream input, IConstructor kind, Type expect) {
        ISourceLocation loc = (ISourceLocation) kind.get("storage");

        // note that mimetype and charset are kept as-is during the download directly to disk
        try (OutputStream out = URIResolverRegistry.getInstance().getOutputStream(loc, false)) {
            input.transferTo(out);
            return loc;
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(e);
        }
    }

    private IValue receiveJsonBody(InputStream input, ISourceLocation url, IConstructor kind, Type expect, String contentType, String charset) {
        if (!contentType.equals("application/json")) {
            monitor.warning("Expected content-type 'application/json', got: " + contentType, url);
        }

        try {
            // this reader must not be closed! The framework will do this. Otherwise the socket will
            // be closed prematurely.
            JsonReader jsonReader = new JsonReader(new InputStreamReader(input, charset));
            JsonValueReader parser = new JsonValueReader(vf, store, monitor, url);
            return parser.read(jsonReader, expect);
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(e);
        }
    }

    private IValue receiveHTMLBody(InputStream reader, ISourceLocation url, IConstructor kind, Type expect, String contentType, String charset) {
        if (!contentType.equals("text/html")) {
            monitor.warning("Expected content-type 'text/html', got: " + contentType, url);
        }

        // TODO: expose options to API level
        return html.readHTMLStream(reader, url, vf.bool(false), vf.bool(false));  
    }

    private IValue receiveXMLBody(InputStream reader, ISourceLocation url, IConstructor kind, Type expect, String contentType, String charset) {
        if (!contentType.equals("text/html")) {
            monitor.warning("Expected content-type 'text/html', got: " + contentType, url);
        }

        // TODO: expose options to API level
        return xml.readXMLInputStream(reader, url, 
            vf.bool(false), 
            vf.bool(false), 
            vf.bool(false),
            vf.bool(false), 
            vf.bool(true), 
            vf.string(charset), 
            vf.bool(false)
        );
    }

    /** 
     * for injecting writer consumers into WriterBodyPublisher
     * and not having to handle IOException in the lambda body.
     */
    @FunctionalInterface 
    private interface WriterFunction {
        void accept(Writer writer) throws IOException;
    }

    public InputStream sendJsonBody(IConstructor input, String charset) {    
        IConstructor options = input.asWithKeywordParameters().getParameter("options");
        Map<String, IValue> kws = options != null 
            ? options.asWithKeywordParameters().getParameters() 
            : Collections.emptyMap(); 
        IString dtf = (IString) kws.get("dateTimeFormat");
        IBool dai = (IBool) kws.get("dateTimeAsInt");
        IBool ras = (IBool) kws.get("rationalsAsString");
        IFunction formatters = (IFunction) kws.get("formatter");
        IBool ecn = (IBool) kws.get("explicitConstructorNames");
        IBool edt = (IBool) kws.get("explicitDataTypes");
        JsonValueWriter valueWriter = new JsonValueWriter()
            .setCalendarFormat(dtf != null ? ((IString) dtf).getValue() : "yyyy-MM-dd\'T\'HH:mm:ss\'Z\'")
            .setFormatters(formatters)
            .setDatesAsInt(dai != null ? ((IBool) dai).getValue() : true)
            .setRationalsAsString(ras != null ? ((IBool) ras).getValue() : false)
            .setExplicitConstructorNames(ecn != null ? ((IBool) ecn).getValue() : false)
            .setExplicitDataTypes(edt != null ? ((IBool) edt).getValue() : false)
            ;
        
        return writeToInputStream((w) -> {
            JsonWriter jsonW = new JsonWriter(w);
            valueWriter.write(jsonW, input.get("source"));
        }, charset);
    }

    public InputStream sendHTMLBody(IConstructor input, String charset) {    
        return writeToInputStream((w) -> {            
            html.writeHTML(w, (IConstructor) input.get("source"), 
                vf.string(charset), 
               (IConstructor) null, 
                vf.bool(false), 
                vf.bool(false),
                vf.integer(4),
                vf.integer(10),
                (IConstructor) null,
                vf.bool(true),
                vf.bool(false));
        }, charset);
    }

    public InputStream sendXMLBody(IConstructor input, String charset) {    
        return writeToInputStream((w) -> {            
            xml.writeXML(w, input.get("source"), 
                vf.string(charset), 
                vf.bool(false), 
                vf.bool(false),
                vf.integer(4),
                vf.integer(10),
                vf.bool(true));
        }, charset);
    }

    public InputStream sendFileBody(IConstructor input) {
        final var loc = (ISourceLocation) input.get("source");

        try {
            return URIResolverRegistry.getInstance().getInputStream(loc);
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(e);
        }
    }

    public InputStream sendTextBody(IConstructor input, String charset) {
        final var value = input.get("source");

        // Note this intentionally mimicks the semantics of IO::writeFile
        if (value.getType().isString()) {
            var text = (IString) value;
            return new ReaderInputStream(text.asReader(), charset);
        }
        else if (value.getType().isSubtypeOf(RascalValueFactory.Tree)) {
            return writeToInputStream((w) -> TreeAdapter.yield((ITree) value, w), charset);  
        } 
        else {
            return writeToInputStream((w) -> new StandardTextWriter().write(value, w), charset);
        }			
    }

    /** 
     * This turns Writers into InputStreams, taking care of proper exception
     * handling, encodings, liveness and cancellation.
     */
    private InputStream writeToInputStream(WriterFunction write, String charset) {
        try {
            final PipedInputStream result = new PipedInputStream(8192);
            final PipedOutputStream out = new PipedOutputStream(result);
            final AtomicReference<IOException> caught = new AtomicReference<>();

            /**
             * We write asynchronously on another thread to guarantee liveness.
             */
            var future = executor.submit(() -> {
                try (Writer w = new OutputStreamWriter(out, charset)) {
                    write.accept(w);
                    w.flush();
                }
                catch (IOException e) {
                    caught.set(e);
                }
            });

            /**
             * Forward the outputstream IO error to read calls here, so they are
             * propagated to the user. Also forward close events back
             * to the running future, to avoid hanging outputstream 
             * when someone cancels a download.
             */
            return new FilterInputStream(result) {
                @Override
                public void close() throws IOException {
                    super.close();
                    future.cancel(true);
                }

                void checkCaught() {
                    IOException e = caught.get();
                    if (e != null) {
                        throw RuntimeExceptionFactory.io(e);
                    }
                }

                @Override
                public int read() throws IOException {
                    checkCaught();
                    return super.read();
                }

                @Override
                public int read(byte[] b, int off, int len) throws IOException {
                    checkCaught();
                    return super.read(b, off, len);
                }
            };
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(e);
        }
    }
}
