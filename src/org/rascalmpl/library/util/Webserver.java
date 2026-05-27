package org.rascalmpl.library.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.library.lang.json.internal.JsonValueWriter;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.functions.IFunction;

import com.google.gson.stream.JsonWriter;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IWithKeywordParameters;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class Webserver {
    private final IRascalValueFactory vf;
    private final TypeStore store;
    private final PrintWriter out;
    private final IRascalMonitor monitor;
    private final ExecutorService executorService;

    private final Type requestType;
    private final  Type post;
    private final  Type get;
    private final  Type head;
    private final  Type delete;
    private final  Type put;
    private final  Type receive;
    private final  Type bodyType;
	
    private final Map<ISourceLocation, NanoHTTPD> servers;
    private final Map<IConstructor,Status> statusValues = new HashMap<>();
    private final WebBody body;
    
    public Webserver(IRascalValueFactory vf, TypeFactory tf, TypeStore store, PrintWriter out, IRascalMonitor monitor, PrintWriter err) {
        this.vf = vf;
        this.store = store;
        this.out = out;
        this.monitor = monitor;
        this.servers = new HashMap<>();
        this.body = new WebBody(store, TypeFactory.getInstance(), vf, monitor, out, err);
        this.executorService = Executors.newCachedThreadPool();

        Type statusType = store.lookupAbstractDataType("Status");

        statusValues.put(vf.constructor(store.lookupConstructor(statusType, "ok", tf.voidType())), Status.OK);
        statusValues.put(vf.constructor(store.lookupConstructor(statusType, "created", tf.voidType())), Status.CREATED);
        statusValues.put(vf.constructor(store.lookupConstructor(statusType, "accepted", tf.voidType())), Status.ACCEPTED);
        statusValues.put(vf.constructor(store.lookupConstructor(statusType, "noContent", tf.voidType())), Status.NO_CONTENT);
        statusValues.put(vf.constructor(store.lookupConstructor(statusType, "partialContent", tf.voidType())), Status.PARTIAL_CONTENT);
        statusValues.put(vf.constructor(store.lookupConstructor(statusType, "redirect", tf.voidType())), Status.REDIRECT);
        statusValues.put(vf.constructor(store.lookupConstructor(statusType, "notModified", tf.voidType())), Status.NOT_MODIFIED);
        statusValues.put(vf.constructor(store.lookupConstructor(statusType, "badRequest", tf.voidType())), Status.BAD_REQUEST);
        statusValues.put(vf.constructor(store.lookupConstructor(statusType, "unauthorized", tf.voidType())), Status.UNAUTHORIZED);
        statusValues.put(vf.constructor(store.lookupConstructor(statusType, "forbidden", tf.voidType())), Status.FORBIDDEN);
        statusValues.put(vf.constructor(store.lookupConstructor(statusType, "notFound", tf.voidType())), Status.NOT_FOUND);
        statusValues.put(vf.constructor(store.lookupConstructor(statusType, "rangeNotSatisfiable", tf.voidType())), Status.RANGE_NOT_SATISFIABLE);
        statusValues.put(vf.constructor(store.lookupConstructor(statusType, "internalError", tf.voidType())), Status.INTERNAL_ERROR);

        this.requestType = store.lookupAbstractDataType("Request");
        this.bodyType = store.lookupAbstractDataType("Body");
        this.get = store.lookupConstructor(requestType, "get", tf.tupleType(tf.stringType()));
        this.put = store.lookupConstructor(requestType, "put",  tf.tupleType(tf.stringType(), bodyType));
        this.post = store.lookupConstructor(requestType, "post",  tf.tupleType(tf.stringType(), bodyType));
        this.delete = store.lookupConstructor(requestType, "delete",  tf.tupleType(tf.stringType()));
        this.head = store.lookupConstructor(requestType, "head",  tf.tupleType(tf.stringType()));
        this.receive = store.lookupConstructor(bodyType, "receive").iterator().next();
    }


    public void serve(ISourceLocation url, final IFunction callback, IBool asDeamon) {
        URI uri = url.getURI();
        initMethodAndStatusValues(store);

        int port = uri.getPort() != -1 ? uri.getPort() : 80;
        String host = uri.getHost() != null ? uri.getHost() : "localhost";
        host = host.equals("localhost") ? "127.0.0.1" : host; // NanoHttp tries to resolve localhost, which isn't what we want!

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
                    IConstructor request = makeRequest(vf.sourceLocation(uri), path, method, headers, parms, session.getInputStream());
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
                IConstructor body = (IConstructor) cons.get("body");
                IConstructor kind = (IConstructor) body.get("kind");
                IMap header = (IMap) cons.get("header");
                IString contentType = (IString) cons.get("mimeType");
                Status status = translateStatus((IConstructor) cons.get("status"));

                initMethodAndStatusValues(store);

                switch (kind.getName()) {
                    case "file":
                        return translateFileResponse(
                            status,
                            kind.asWithKeywordParameters().getParameter("storage"),
                            kind.asWithKeywordParameters().getParameter("header")
                        );
                    case "json":
                        return translateJsonResponse(
                            status,
                            contentType,
                            header,
                            kind.asWithKeywordParameters().getParameter("options"), 
                            body.get("source"));
                    case "xml": // TODO
                    case "html": // TODO
                    case "text":
                    default:
                        return translateTextResponse(
                            method,
                            status, 
                            contentType,
                            header,
                            (IString) body.get("source"));
                }
            }

            private Response translateJsonResponse(Status status, IString contentType, IMap header, IConstructor options, IValue data) {
                IWithKeywordParameters<? extends IConstructor> kws = options.asWithKeywordParameters();
                IValue dtf = kws.getParameter("dateTimeFormat");
                IValue dai = kws.getParameter("dateTimeAsInt");
                IValue ras = kws.getParameter("rationalsAsString");
                IValue formatters = kws.getParameter("formatter");
                IValue ecn = kws.getParameter("explicitConstructorNames");
                IValue edt = kws.getParameter("explicitDataTypes");

                JsonValueWriter writer = new JsonValueWriter()
                    .setCalendarFormat(dtf != null ? ((IString) dtf).getValue() : "yyyy-MM-dd\'T\'HH:mm:ss\'Z\'")
                    .setFormatters((IFunction) formatters)
                    .setDatesAsInt(dai != null ? ((IBool) dai).getValue() : true)
                    .setRationalsAsString(ras != null ? ((IBool) ras).getValue() : false)
                    .setExplicitConstructorNames(ecn != null ? ((IBool) ecn).getValue() : false)
                    .setExplicitDataTypes(edt != null ? ((IBool) edt).getValue() : false)
                    ;

                try {
                    PipedOutputStream w = new PipedOutputStream();
                    PipedInputStream r = new PipedInputStream(w);
                    Deque<IOException> exceptions = new ConcurrentLinkedDeque<>();

                    var jsonStreamer = executorService.submit(() -> {
                        try {
                            JsonWriter out = new JsonWriter(new OutputStreamWriter(w, Charset.forName("UTF-8")));  
                            writer.write(out, data);
                            out.flush();
                            out.close();
                            return true;
                        }
						catch (IOException e) {
                            exceptions.add(e);
                            return false;
						}
                    });

                    // TODO: check if this is asynchronous
                    Response response = newChunkedResponse(status, contentType.getValue() + ";charset=utf-8", r);
                    addHeaders(response, header);
                    
                    try {
                        // TODO: check if this does not block
                        if (!jsonStreamer.get() || exceptions.size() > 1) {
                            throw exceptions.peek();
                        }
                    }
					catch (InterruptedException e) {
                        throw new IOException("JSON serialization was not finished");
                    }
                    catch (ExecutionException e) {
                        throw new IOException(e.getCause());
					}

                    return response;
                }
                catch (IOException e) {
                    return newFixedLengthResponse(Status.NOT_FOUND, "text/plain", "JSON serialization failed: " + e + "\n");
                }
            }

            private Response translateFileResponse(Status status, IMap header, ISourceLocation storage) {
                try {
                    Response response = newChunkedResponse(status, "application/octet-stream", URIResolverRegistry.getInstance().getInputStream(storage));
                    addHeaders(response, header);
                    return response;
                } 
                catch (IOException e) {
                    return newFixedLengthResponse(Status.INTERNAL_ERROR, "text/plain", "IO failed " + e + "\n");
                } 
            }

            private Response translateTextResponse(Method method, Status status, IString contentType, IMap header, IString data) {
                if (method != Method.HEAD) {
                    switch (status) {
                        case BAD_REQUEST:
                        case UNAUTHORIZED:
                        case NOT_FOUND:
                        case FORBIDDEN:
                        case RANGE_NOT_SATISFIABLE:
                        case INTERNAL_ERROR:
                            if (data.length() == 0) {
                                data = vf.string(status.getDescription());
                            }
                        default:
                            break;
                    }
                }

                try {
                    var fixedContentType = new ContentType(contentType.getValue()).tryUTF8();
                    Response response = newChunkedResponse(status, fixedContentType.getContentTypeHeader(), toInputStream(data, Charset.forName(fixedContentType.getEncoding())));
                    addHeaders(response, header);
                    return response;
                }
                catch (IOException e) {
                    return newFixedLengthResponse(Status.INTERNAL_ERROR, MIME_HTML, e.getMessage());
                }
            }

            private InputStream toInputStream(IString data, Charset encoding) throws IOException {
                return new ByteArrayInputStream(data.getValue().getBytes(encoding));
            }

            private void addHeaders(Response response, IMap header) {
                // TODO add first class support for cache control on the Rascal side. For
                // now we prevent any form of client-side caching with this.. hopefully.
                response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.addHeader("Pragma", "no-cache");
                response.addHeader("Expires", "0");

                for (IValue key : header) {
                    response.addHeader(((IString) key).getValue(), ((IString) header.get(key)).getValue());
                }
            }

            private Status translateStatus(IConstructor cons) {
                initMethodAndStatusValues(store);
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
            servers.put(url, server);
           
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
                servers.remove(url);
            }
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(e);
        }
    }
    
    public void shutdown(ISourceLocation server) {
        NanoHTTPD nano = servers.get(server);
        if (nano != null) {
            //if (nano.isAlive()) {
            nano.stop();
            servers.remove(server);
            //}
        }
        else {
            throw RuntimeExceptionFactory.illegalArgument(server, "could not shutdown");
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

    private void initMethodAndStatusValues(TypeStore store) {
        
    }
}
