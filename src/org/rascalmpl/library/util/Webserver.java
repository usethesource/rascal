/*******************************************************************************
 * Copyright (c) 2014-2026 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.rascalmpl.library.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.types.TypeReifier;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.functions.IFunction;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class Webserver {
    private final IRascalValueFactory vf;
    private final PrintWriter out;
    private final IRascalMonitor monitor;

    private final Type requestType;
    private final Type responseType;
    private final Type responseCons;
    private final Type sendCons;
    private final Type kindType;
    private final Type textCons;
    private final Type jsonCons;
    private final Type xmlCons;
    private final Type htmlCons;
    private final Type htmlElementType;
    private final  Type post;
    private final  Type get;
    private final  Type head;
    private final  Type delete;
    private final  Type put;
    private final  Type receive;
    private final  Type bodyType;
	
    private final IConstructor statusOK;
    private final IConstructor statusNotFound;

    private final Map<IInteger, NanoHTTPD> servers;
    private final Map<IConstructor,Status> statusValues = new HashMap<>();
    private final WebBody body;
    
    
    public Webserver(IRascalValueFactory vf, TypeFactory tf, TypeStore store, PrintWriter out, IRascalMonitor monitor, PrintWriter err) {
        this.vf = vf;
        this.out = out;
        this.monitor = monitor;
        this.servers = new HashMap<>();
        this.body = new WebBody(store, tf, vf, monitor, out, err);

        Type statusType = store.lookupAbstractDataType("Status");

        this.statusOK = vf.constructor(store.lookupConstructor(statusType, "ok", tf.voidType()));
        this.statusNotFound = vf.constructor(store.lookupConstructor(statusType, "notFound", tf.voidType()));

        statusValues.put(statusOK, Status.OK);
        statusValues.put(vf.constructor(store.lookupConstructor(statusType, "created", tf.voidType())), Status.CREATED);
        statusValues.put(vf.constructor(store.lookupConstructor(statusType, "accepted", tf.voidType())), Status.ACCEPTED);
        statusValues.put(vf.constructor(store.lookupConstructor(statusType, "noContent", tf.voidType())), Status.NO_CONTENT);
        statusValues.put(vf.constructor(store.lookupConstructor(statusType, "partialContent", tf.voidType())), Status.PARTIAL_CONTENT);
        statusValues.put(vf.constructor(store.lookupConstructor(statusType, "redirect", tf.voidType())), Status.REDIRECT);
        statusValues.put(vf.constructor(store.lookupConstructor(statusType, "notModified", tf.voidType())), Status.NOT_MODIFIED);
        statusValues.put(vf.constructor(store.lookupConstructor(statusType, "badRequest", tf.voidType())), Status.BAD_REQUEST);
        statusValues.put(vf.constructor(store.lookupConstructor(statusType, "unauthorized", tf.voidType())), Status.UNAUTHORIZED);
        statusValues.put(vf.constructor(store.lookupConstructor(statusType, "forbidden", tf.voidType())), Status.FORBIDDEN);
        statusValues.put(statusNotFound, Status.NOT_FOUND);
        statusValues.put(vf.constructor(store.lookupConstructor(statusType, "rangeNotSatisfiable", tf.voidType())), Status.RANGE_NOT_SATISFIABLE);
        statusValues.put(vf.constructor(store.lookupConstructor(statusType, "internalError", tf.voidType())), Status.INTERNAL_ERROR);

        this.requestType = store.lookupAbstractDataType("Request");
        this.responseType = store.lookupAbstractDataType("Response");
        this.responseCons = store.lookupConstructor(responseType, "response").iterator().next();
        this.bodyType = store.lookupAbstractDataType("Body");
        this.kindType = store.lookupAbstractDataType("BodyKind");
        this.htmlElementType = store.lookupAbstractDataType("HTMLElement");
        this.sendCons = store.lookupConstructor(bodyType, "send").iterator().next();
        this.textCons = store.lookupConstructor(kindType, "text").iterator().next();
        this.jsonCons = store.lookupConstructor(kindType, "json").iterator().next();
        this.xmlCons = store.lookupConstructor(kindType, "xml").iterator().next();
        this.htmlCons = store.lookupConstructor(kindType, "html").iterator().next();
        this.get = store.lookupConstructor(requestType, "get", tf.tupleType(tf.stringType()));
        this.put = store.lookupConstructor(requestType, "put",  tf.tupleType(tf.stringType(), bodyType));
        this.post = store.lookupConstructor(requestType, "post",  tf.tupleType(tf.stringType(), bodyType));
        this.delete = store.lookupConstructor(requestType, "delete",  tf.tupleType(tf.stringType()));
        this.head = store.lookupConstructor(requestType, "head",  tf.tupleType(tf.stringType()));
        this.receive = store.lookupConstructor(bodyType, "receive").iterator().next();
    }

    public void serve(IInteger pPort, final IFunction callback, IBool asDeamon) {
        int port = pPort.intValue();
        String host = "127.0.0.1"; // NanoHttp tries to resolve localhost, which isn't what we want!
        
        BlockingQueue<Runnable> mainThreadExecutor;
        final Function<IValue, CompletableFuture<IValue>> executor;
        if (asDeamon.getValue()) {
            mainThreadExecutor = null;
            executor = buildRegularExecutor(callback);
        }
        else {
            mainThreadExecutor = new ArrayBlockingQueue<>(1024, true);
            executor = asyncExecutor(callback, mainThreadExecutor);
        }

        NanoHTTPD server = new NanoHTTPD(host, port) {

            @Override
            public Response serve(IHTTPSession session) {
                var method = session.getMethod();
                var parms = session.getParms();
                var headers = session.getHeaders();
                var path = session.getUri(); // method misnomer
                
                try {
                    IConstructor request = makeRequest(vf.sourceLocation("http", "localhost" + port, path), path, method, headers, parms, session.getInputStream());
                    CompletableFuture<IValue> rascalResponse = executor.apply(request);
                    return translateResponse(method, rascalResponse.get());
                }
                catch (CancellationException e) {
                    stop();
                    return newFixedLengthResponse(Status.INTERNAL_ERROR, MIME_PLAINTEXT, "Shutting down!");
                }
                catch (ExecutionException e) {
                    Throwable actualException = e.getCause();
                    if (actualException instanceof Throw) {
                        Throw rascalException = (Throw) actualException;

                        if (isCallFailed(rascalException)) {
                            return newFixedLengthResponse(Status.NOT_FOUND, MIME_PLAINTEXT, "404: NOT FOUND\n\n" + rascalException.getMessage());
                        }
                        else {
                            return newFixedLengthResponse(Status.INTERNAL_ERROR, MIME_PLAINTEXT, "500: INTERNAL ERROR\n\n" + rascalException.getMessage());
                        }
                    }
                    else {
                        return handleGeneralThrowable(actualException);
                    }
                }
                catch (Throwable t) {
                    return handleGeneralThrowable(t);
                }
            }

            private boolean isCallFailed(Throw rascalException) {
                IValue exc = rascalException.getException();
                
                return exc.getType().isAbstractData() 
                    && ((IConstructor) exc).getConstructorType() == RuntimeExceptionFactory.CallFailed;
            }
            
            private Response handleGeneralThrowable(Throwable actualException) {
                return newFixedLengthResponse(Status.INTERNAL_ERROR, MIME_PLAINTEXT, "500 INTERNAL SERVER ERROR:\n\n" + actualException.getMessage() + "\n");
            }

            private String getMimeType(Map<String, String> headers) {
                String contenType = headers.getOrDefault("Content-Type", "text/plain");
                String[] parts = contenType.split(";");
                return parts[0].trim();
            }

             private String getCharset(Map<String, String> headers) {
                String contentType = headers.getOrDefault("Content-Type", "text/plain");
                String result = StandardCharsets.UTF_8.name();

                String[] parts = contentType.split(";");
                if (parts.length > 1) {
                    String[] assign = parts[1].split("=");

                    if (assign[0].equals("charset")) {
                        result = assign[1].trim();
                    }
                }
                
                return result;
            }

            private IConstructor makeRequest(ISourceLocation host, String path, Method method, Map<String, String> headers,
                Map<String, String> parms, InputStream inputStream) throws FactTypeUseException, IOException {
                Map<String,IValue> kws = new HashMap<>();
                kws.put("parameters", makeMap(parms));
                kws.put("headers", makeMap(headers));
                var mimeType = getMimeType(headers);
                var charset = getCharset(headers);

                switch (method) {
                    case HEAD:
                        return vf.constructor(head, new IValue[]{vf.string(path)}, kws);
                    case DELETE:
                        return vf.constructor(delete, new IValue[]{vf.string(path)}, kws);
                    case GET:
                        return vf.constructor(get, new IValue[]{vf.string(path)}, kws);
                    case PUT:
                        return vf.constructor(put, new IValue[]{vf.string(path), vf.constructor(receive, body.createBodyReceiver(inputStream, host, mimeType, charset))}, kws);
                    case POST:
                        return vf.constructor(post, new IValue[]{vf.string(path), vf.constructor(receive, body.createBodyReceiver(inputStream, host, mimeType, charset))}, kws);
                    default:
                        throw new IOException("Unhandled request " + method);
                }
            }

            private Response translateResponse(Method method, IValue value) throws IOException {
                IConstructor cons = (IConstructor) value;
                IConstructor b = (IConstructor) cons.get("body");
                IConstructor kind = (IConstructor) b.get("kind");
                IMap header = (IMap) cons.asWithKeywordParameters().getParameter("headers");
                if (header == null) {
                    header = vf.map();
                }
                IString mimeType = b.asWithKeywordParameters().getParameter("mimeType");
                if (mimeType == null) {
                    mimeType = vf.string("text/plain");
                }
                Status status = translateStatus((IConstructor) cons.get("status"));
                IString charset = b.asWithKeywordParameters().getParameter("charset");
                if (charset == null) {
                    charset = vf.string("utf-8");
                }
                String contentType = mimeType.getValue() + ";charset=" + charset.getValue();

                if (method != Method.HEAD) {
                    switch (status) {
                        case BAD_REQUEST:
                        case UNAUTHORIZED:
                        case NOT_FOUND:
                        case FORBIDDEN:
                        case RANGE_NOT_SATISFIABLE:
                        case INTERNAL_ERROR:
                            if (value.getType().isString() && ((IString) value).length() == 0) {
                                value = vf.string(status.getDescription());
                            }
                        default:
                            break;
                    }
                }

                Response r;
                switch (kind.getName()) {
                    case "file":
                        r = newChunkedResponse(status, contentType, body.sendFileBody(b));
                        break;
                    case "json":
                        r = newChunkedResponse(status, contentType, body.sendJsonBody(b, charset.getValue()));
                        break;
                    case "xml": 
                        r = newChunkedResponse(status, contentType, body.sendXMLBody(b, charset.getValue()));
                        break;
                    case "html": 
                        r = newChunkedResponse(status, contentType, body.sendHTMLBody(b, charset.getValue()));
                        break;
                    case "text":
                    default:
                        r = newChunkedResponse(status, contentType, body.sendTextBody(b, charset.getValue()));
                }

                addHeaders(r, header, contentType);
                return r;
            }

            private void addHeaders(Response response, IMap header, String contentType) {
                response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.addHeader("Pragma", "no-cache");
                response.addHeader("Expires", "0");                
                response.addHeader("Content-Type", contentType);

                for (IValue key : header) {
                    response.addHeader(((IString) key).getValue(), ((IString) header.get(key)).getValue());
                }
            }

            private Status translateStatus(IConstructor cons) {
                return statusValues.get(cons);
            }

            private IMap makeMap(Map<String, String> headers) {
                IMapWriter writer = vf.mapWriter();
                for (Entry<String, String> entry : headers.entrySet()) {
                    writer.put(vf.string(entry.getKey()), vf.string(entry.getValue()));
                }
                return writer.done();
            }
        };

        try {
            server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, asDeamon.getValue());
            servers.put(pPort, server);
           
            if (!asDeamon.getValue()) {
                out.println("Starting http server in non-daemon mode, hit ctrl-c to stop it");
                out.flush();
                while (!monitor.jobIsCanceled("Server: " + server)) {
                    try {
                        Runnable job;
                        if ((job = mainThreadExecutor.poll(10, TimeUnit.MILLISECONDS)) != null) {
                            job.run();
                        }
                    }
                    catch (InterruptedException e) {
                        break;
                    }
                }
                server.stop();
                servers.remove(pPort);
            }
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(e);
        }
    }
    
    public void shutdown(IInteger port) {
        NanoHTTPD nano = servers.get(port);
        if (nano != null) {
            nano.stop();
            servers.remove(port);
        }
        else {
            monitor.warning("Server at port " + port + " was not running anymore", URIUtil.correctLocation("http", "localhost:" + port, ""));
        }
    }

    @Override
    protected void finalize() throws Throwable {
        for (NanoHTTPD server : servers.values()) {
            if (server != null && server.wasStarted()) {
                server.stop();
            }
        }
    }

    private Function<IValue, CompletableFuture<IValue>> buildRegularExecutor(IFunction target) {
        return (request) -> {
            CompletableFuture<IValue> result = new CompletableFuture<>();
            executeCallback(target, result, request, true);
            return result;
        };
    }

    private Function<IValue, CompletableFuture<IValue>> asyncExecutor(IFunction callback, BlockingQueue<Runnable> mainThreadExecutor) {
        return (request) -> {
            CompletableFuture<IValue> result = new CompletableFuture<>();
            try {
                mainThreadExecutor.put(() -> executeCallback(callback, result, request, false));
            }
            catch (InterruptedException e) {
                result.cancel(true);
            }
            return result;
        };
    }

    private void executeCallback(IFunction callback, CompletableFuture<IValue> target, IValue request, boolean asDaemon) {
        try {
            target.complete(callback.call(request));
        }
        catch (Throwable t) {
            target.completeExceptionally(t);
        }
    }

    /**
     * This exercises the entire server infrastructure without locking the interpreter
     */
    public void startEchoServerJava(IInteger port) {
        TypeFactory tf = TypeFactory.getInstance();
        Type serverFunction = tf.functionType(responseType, tf.tupleType(requestType), tf.tupleEmpty());
        TypeReifier tr = new TypeReifier(vf);

        serve(
            port,
            vf.function(serverFunction, (args, kwArgs) -> {
                IConstructor request = (IConstructor) args[0];
                IMap headers = request.asWithKeywordParameters().getParameter("headers");
                String path = ((IString) request.get(0)).getValue();
                IValue nodeT = tr.typeToValue(tf.nodeType(), new TypeStore(), vf.map());
                IValue strT = tr.typeToValue(tf.stringType(), new TypeStore(), vf.map());
                IValue htmlT = tr.typeToValue(htmlElementType, new TypeStore(), vf.map());
                
                switch (path) {
                    case "/get":
                        var getBody = vf.constructor(sendCons, vf.constructor(textCons), vf.string("ok"));
                        return vf.constructor(responseCons, statusOK, getBody);
                    case "/post/json":
                    case "/put/json":
                        var receiveJsonBody = ((IFunction) ((IConstructor) request.get(1)).get(0)).call(vf.constructor(jsonCons), nodeT);
                        assert receiveJsonBody.getType() == tf.nodeType();
                        var postJsonBody = vf.constructor(sendCons, vf.constructor(jsonCons), receiveJsonBody);
                        return vf.constructor(responseCons, statusOK, postJsonBody);
                    case "/post/html":
                    case "/put/html":
                        var receiveHTMLBody = ((IFunction) ((IConstructor) request.get(1)).get(0)).call(vf.constructor(htmlCons), htmlT);
                        assert receiveHTMLBody.getType() == htmlElementType;
                        var postHTMLBody = vf.constructor(sendCons, vf.constructor(htmlCons), receiveHTMLBody);
                        return vf.constructor(responseCons, statusOK, postHTMLBody);
                    case "/post/xml":
                    case "/put/xml":
                        var receiveXMLBody = ((IFunction) ((IConstructor) request.get(1)).get(0)).call(vf.constructor(xmlCons), nodeT);
                        assert receiveXMLBody.getType() == tf.nodeType();
                        var postXMLBody = vf.constructor(sendCons, vf.constructor(xmlCons), receiveXMLBody);
                        return vf.constructor(responseCons, statusOK, postXMLBody);
                    case "/post/text":
                    case "/put/text":
                        var receiveTextBody = ((IFunction) ((IConstructor) request.get(1)).get(0)).call(vf.constructor(textCons), strT);
                        assert receiveTextBody.getType() == tf.stringType();
                        var postTextBody = vf.constructor(sendCons, vf.constructor(xmlCons), receiveTextBody);
                        return vf.constructor(responseCons, statusOK, postTextBody);
                    case "/head":
                        var headBody = vf.constructor(sendCons, vf.constructor(textCons), vf.string(headers.toString()));
                        return vf.constructor(responseCons, statusOK, headBody);
                    default: // 404
                        var notFoundBody = vf.constructor(sendCons, vf.constructor(textCons), vf.string(path + " was not found."));
                        return vf.constructor(responseCons, statusNotFound, notFoundBody);
                }
            })
        , vf.bool(true));
    }
}
