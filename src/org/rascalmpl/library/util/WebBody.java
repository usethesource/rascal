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
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.io.InputStreamReader;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.library.lang.json.internal.JsonValueReader;
import org.rascalmpl.library.lang.json.internal.JsonValueWriter;
import org.rascalmpl.types.RascalTypeFactory;
import org.rascalmpl.types.TypeReifier;
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
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IWithKeywordParameters;
import io.usethesource.vallang.io.StandardTextWriter;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

/**
 * Share utilities between Webserver and Webclient to send and receive HTTP body content
 */
public class WebBody {
    private final ExecutorService executor;
    private final IRascalValueFactory vf;
    private final TypeFactory tf;
    private final IRascalMonitor monitor;
    private org.rascalmpl.library.lang.html.IO html;
    private final org.rascalmpl.library.lang.xml.IO xml;

    public WebBody(TypeFactory tf, IRascalValueFactory vf, IRascalMonitor monitor) {
        this.executor = Executors.newCachedThreadPool();
        this.tf = tf;
        this.vf = vf;
        this.monitor = monitor;
        this.html = null; // initialize later from reified type
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
        TypeStore store = new TypeStore();
        var BodyKind = tf.abstractDataType(store, "BodyKind");
        var rt = RascalTypeFactory.getInstance().reifiedType(tf.parameterType("T"));
        var ft = tf.functionType(tf.parameterType("T"), tf.tupleType(BodyKind, rt), tf.tupleEmpty());

        return vf.function(ft, (args, kwargs) -> {
            IConstructor kind = (IConstructor) args[0];
            IConstructor reified = (IConstructor) args[1];
            Type expect = new TypeReifier(vf).valueToType((IConstructor) reified, store);

            switch (kind.getName()) {
                case "json":
                    return receiveJsonBody(input, url, kind, expect, store, contentType, charset);
                case "html":
                    return receiveHTMLBody(input, url, kind, expect, store, contentType, charset);
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

    private IValue receiveJsonBody(InputStream input, ISourceLocation url, IConstructor kind, Type expect, TypeStore store, String contentType, String charset) {
        if (!contentType.equals("application/json")) {
            monitor.warning("Expected content-type 'application/json', got: " + contentType, url);
        }

        IConstructor options = kind.asWithKeywordParameters().getParameter("jOptions");

        try {  
            JsonReader jsonReader = new JsonReader(new InputStreamReader(input, charset));
            JsonValueReader parser = new JsonValueReader(vf, store, monitor, url).setOptions(options);

            return parser.read(jsonReader, expect);
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(e);
        }
    }

    private IValue receiveHTMLBody(InputStream reader, ISourceLocation url, IConstructor kind, Type expect, TypeStore store, String contentType, String charset) {
        // store contains the defintions of HTMLElement via the reified type in the receiver bodykind
        if (html == null) {
            html = new org.rascalmpl.library.lang.html.IO(vf, monitor, store /* now we have a full definition of HTMLElement to work with */);
        }

        if (!contentType.equals("text/html")) {
            monitor.warning("Expected content-type 'text/html', got: " + contentType, url);
        }

        var optCons = kind.asWithKeywordParameters().getParameter("hOptions");
        IBool trackOrigins = vf.bool(false);
        IBool includeEndTags = vf.bool(true);

        if (optCons != null) {
            var options = optCons.asWithKeywordParameters();
            trackOrigins = options.hasParameter("trackOrigins") ? options.getParameter("trackOrigins") : vf.bool(false);
            includeEndTags = options.hasParameter("includeEndTags") ? options.getParameter("includeEndTags") : vf.bool(true);
        }

        return html.readHTMLStream(reader, url, trackOrigins, includeEndTags);  
    }

    private IValue receiveXMLBody(InputStream reader, ISourceLocation url, IConstructor kind, Type expect, String contentType, String charset) {
        if (!contentType.equals("application/xml")) {
            monitor.warning("Expected content-type 'application/xml', got: " + contentType, url);
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

    private WriterFunction writeJsonBody(IConstructor input, String charset) {    
        IConstructor options = input.asWithKeywordParameters().getParameter("options");
        JsonValueWriter valueWriter = new JsonValueWriter().setOptions(options);

        return (w) -> { 
            JsonWriter jsonW = new JsonWriter(w);
            valueWriter.write(jsonW, input.get("source"));
        };
    }

    private WriterFunction writeHTMLBody(IConstructor input, String charset) {   
        IConstructor kind = (IConstructor) input.get("kind");
        IConstructor options = kind.asWithKeywordParameters().getParameter("hOptions");
        Map<String, IValue> kws = options != null ? options.asWithKeywordParameters().getParameters() : Collections.emptyMap();

        IConstructor escapeMode = (IConstructor) kws.getOrDefault("escapeMode", html.baseMode());
        IBool outline = (IBool) kws.getOrDefault("outline", vf.bool(false));
        IBool prettyPrint = (IBool) kws.getOrDefault("prettyPrint", vf.bool(false));
        IInteger indentAmount = (IInteger) kws.getOrDefault("indentAmount", vf.integer(4));
        IInteger maxPaddingWidth = (IInteger) kws.getOrDefault("maxPaddingWidth", vf.integer(10));
        IConstructor syntax = (IConstructor) kws.getOrDefault("syntax", html.htmlSyntax());
        IBool dropOrigins = (IBool) kws.getOrDefault("dropOrigins", vf.bool(true));
        IBool normalise = (IBool) kws.getOrDefault("normalise", vf.bool(false));

        return (w) -> {            
            html.writeHTML(w, (IConstructor) input.get("source"), 
                vf.string(charset), 
                escapeMode, 
                outline, 
                prettyPrint,
                indentAmount,
                maxPaddingWidth,
                syntax,
                dropOrigins,
                normalise);
        };
    }

    private WriterFunction writeXMLBody(IConstructor input, String charset) {    
        return (w) -> {            
            xml.writeXML(w, input.get("source"), 
                vf.string(charset), 
                vf.bool(false), 
                vf.bool(false),
                vf.integer(4),
                vf.integer(10),
                vf.bool(true));
        };
    }

    public void sendFileBody(OutputStream out, IConstructor input, String charset) throws IOException {
        try (InputStream in = sendFileBody(input, charset)) {
            in.transferTo(out);
        }
    }

    public InputStream sendFileBody(IConstructor input, String charset) {
        // TODO: interesting what to do with charset
        final var loc = (ISourceLocation) input.get("source");

        try {
            return URIResolverRegistry.getInstance().getInputStream(loc);
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(e);
        }
    }

    public InputStream sendTextBody(IConstructor input, String charset) {
        return writeToInputStream(writeTextBody(input, charset), charset);
    }

    public InputStream sendJsonBody(IConstructor input, String charset) {
        return writeToInputStream(writeJsonBody(input, charset), charset);
    }

    public InputStream sendXMLBody(IConstructor input, String charset) {
        return writeToInputStream(writeXMLBody(input, charset), charset);
    }

    public InputStream sendHTMLBody(IConstructor input, String charset) {
        return writeToInputStream(writeHTMLBody(input, charset), charset);
    }

    public void sendTextBody(OutputStream out, IConstructor input, String charset) throws IOException {
        writeToOutputStream(out, writeTextBody(input, charset), charset);
    }

    public void sendJsonBody(OutputStream out, IConstructor input, String charset) throws IOException {
        writeToOutputStream(out, writeJsonBody(input, charset), charset);
    }

    public void sendXMLBody(OutputStream out, IConstructor input, String charset) throws IOException {
        writeToOutputStream(out, writeXMLBody(input, charset), charset);
    }

    public void sendHTMLBody(OutputStream out, IConstructor input, String charset) throws IOException {
        writeToOutputStream(out, writeHTMLBody(input, charset), charset);
    }

    private WriterFunction writeTextBody(IConstructor input, String charset) {
        final var value = input.get("source");

        // Note this intentionally mimicks the semantics of IO::writeFile
        if (value.getType().isString()) {
            return (WriterFunction) (Writer w) -> {
                ((IString) value).write(w);
            };
        }
        else if (value.getType().isSubtypeOf(RascalValueFactory.Tree)) {
            return (WriterFunction) (Writer w) -> {
                TreeAdapter.yield((ITree) value, w);
            };
        } 
        else {
            return (WriterFunction) (Writer w) -> {
                new StandardTextWriter().write(value, w);
            };
        }			
    }

    private void writeToOutputStream(OutputStream out, WriterFunction write, String charset) throws UnsupportedEncodingException, IOException {
        try (Writer w = new OutputStreamWriter(out, charset)) {
            write.accept(w);
        }
    }

    /** 
     * This turns Writers into InputStreams, taking care of proper exception
     * handling, encodings, liveness and cancellation. This is used only
     * by the Webclient, because the Webserver can use an Outputstream directly.
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
