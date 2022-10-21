package org.rascalmpl.library.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
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
import org.rascalmpl.library.Prelude;
import org.rascalmpl.library.lang.json.internal.JsonValueReader;
import org.rascalmpl.library.lang.json.internal.JsonValueWriter;
import org.rascalmpl.types.RascalTypeFactory;
import org.rascalmpl.types.TypeReifier;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.functions.IFunction;

import com.google.gson.stream.JsonReader;
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

    private final Map<ISourceLocation, NanoHTTPD> servers;
    private final Map<IConstructor,Status> statusValues = new HashMap<>();
    private final IRascalMonitor monitor;
    private Type requestType;
    private Type post;
    private Type get;
    private Type head;
    private Type delete;
    private Type put;
    private Type functionType;

    public Webserver(IRascalValueFactory vf, TypeStore store, PrintWriter out, IRascalMonitor monitor) {
        this.vf = vf;
        this.store = store;
        this.out = out;
        this.monitor = monitor;
        this.servers = new HashMap<>();
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
            public Response serve(String uri, Method method, Map<String, String> headers, Map<String, String> parms,
                Map<String, String> files) {
                try {
                    IConstructor request = makeRequest(uri, method, headers, parms, files);
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
                return newFixedLengthResponse(Status.INTERNAL_ERROR, MIME_PLAINTEXT, "500 INTERNAL SERVER ERROR:\n\n" + actualException.getMessage());
            }

            private IConstructor makeRequest(String path, Method method, Map<String, String> headers,
                Map<String, String> parms, Map<String, String> files) throws FactTypeUseException, IOException {
                Map<String,IValue> kws = new HashMap<>();
                kws.put("parameters", makeMap(parms));
                kws.put("uploads", makeMap(files));
                kws.put("headers", makeMap(headers));

                switch (method) {
                    case HEAD:
                        return vf.constructor(head, new IValue[]{vf.string(path)}, kws);
                    case DELETE:
                        return vf.constructor(delete, new IValue[]{vf.string(path)}, kws);
                    case GET:
                        return vf.constructor(get, new IValue[]{vf.string(path)}, kws);
                    case PUT:
                        return vf.constructor(put, new IValue[]{vf.string(path), getContent(files, "content", "UTF-8")}, kws);
                    case POST:
                        return vf.constructor(post, new IValue[]{vf.string(path), getContent(files, "postData", "UTF-8")}, kws);
                    default:
                        throw new IOException("Unhandled request " + method);
                }
            }

            protected IValue getContent(Map<String, String> parms, String contentParamName, String charset) throws IOException {
                return vf.function(functionType, (argValues, keyArgValues) -> {
                    try {
                        TypeStore store = new TypeStore();
                        Type topType = new TypeReifier(vf).valueToType((IConstructor) argValues[0], store);

                        if (topType.isString()) {
                            // if #str is requested we literally provide the content
                            return getRawContent(parms, contentParamName, charset);
                        }
                        else {
                            // otherwise the content is parsed as JSON and validated against the given type
                            IValue dtf = keyArgValues.get("dateTimeFormat");
                            IValue ics = keyArgValues.get("implicitConstructors");
                            IValue icn = keyArgValues.get("implicitNodes");

                            return new JsonValueReader(vf, store)
                                .setCalendarFormat((dtf != null) ? ((IString) dtf).getValue() : "yyyy-MM-dd\'T\'HH:mm:ss\'Z\'")
                                .setConstructorsAsObjects((ics != null) ? ((IBool) ics).getValue() : true)
                                .setNodesAsObjects((icn != null) ? ((IBool) icn).getValue() : true)
                                .read(new JsonReader(getRawContentReader(parms, contentParamName)), topType);
                        }
                    } catch (IOException | URISyntaxException e) {
                        throw RuntimeExceptionFactory.io(vf.string(e.getMessage()));
                    }
                });
            }

            private Reader getRawContentReader(Map<String, String> parms, String contentParamName) throws FileNotFoundException {
                String path = parms.get(contentParamName);
                if (path != null && !path.isEmpty()) {
                    return new FileReader(path);
                }
                else {
                    // empty content is a valid response (data could be in the parameters map)
                    return new StringReader("");
                }
            }

            private IString getRawContent(Map<String, String> parms, String contentParamName, String charset) throws URISyntaxException {
                String path = parms.get(contentParamName);
                if (path != null && !path.isEmpty()) {
                    return Prelude.readFile(vf, false, URIUtil.createFileLocation(path), charset);
                }
                else {
                    // empty content is a valid response.
                    return vf.string("");
                }
            }

            private Response translateResponse(Method method, IValue value) throws IOException {
                IConstructor cons = (IConstructor) value;
                initMethodAndStatusValues(store);

                switch (cons.getName()) {
                    case "fileResponse":
                        return translateFileResponse(method, cons);
                    case "jsonResponse":
                        return translateJsonResponse(method, cons);
                    case "response":
                        return translateTextResponse(method, cons);
                    default:
                        throw new IOException("Unknown response kind: " + value);
                }
            }

            private Response translateJsonResponse(Method method, IConstructor cons) {
                IMap header = (IMap) cons.get("header");
                IValue data = cons.get("val");
                Status status = translateStatus((IConstructor) cons.get("status"));
                IWithKeywordParameters<? extends IConstructor> kws = cons.asWithKeywordParameters();

                IValue dtf = kws.getParameter("dateTimeFormat");
                IValue ics = kws.getParameter("implicitConstructors");
                IValue ipn = kws.getParameter("implicitNodes");
                IValue dai = kws.getParameter("dateTimeAsInt");

                JsonValueWriter writer = new JsonValueWriter()
                    .setCalendarFormat(dtf != null ? ((IString) dtf).getValue() : "yyyy-MM-dd\'T\'HH:mm:ss\'Z\'")
                    .setConstructorsAsObjects(ics != null ? ((IBool) ics).getValue() : true)
                    .setNodesAsObjects(ipn != null ? ((IBool) ipn).getValue() : true)
                    .setDatesAsInt(dai != null ? ((IBool) dai).getValue() : true);

                try {
                    final ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    JsonWriter out = new JsonWriter(new OutputStreamWriter(baos, Charset.forName("UTF8")));

                    writer.write(out, data);
                    out.flush();
                    out.close();

                    Response response = newFixedLengthResponse(status, "application/json", new ByteArrayInputStream(baos.toByteArray()), baos.size());
                    addHeaders(response, header);
                    return response;
                }
                catch (IOException e) {
                    // this should not happen in theory
                    throw new RuntimeException("Could not create piped inputstream");
                }
            }

            private Response translateFileResponse(Method method, IConstructor cons) {
                ISourceLocation l = (ISourceLocation) cons.get("file");
                IString mimeType = (IString) cons.get("mimeType");
                IMap header = (IMap) cons.get("header");

                Response response;
                try {
                    response = newChunkedResponse(Status.OK, mimeType.getValue(), URIResolverRegistry.getInstance().getInputStream(l));
                    addHeaders(response, header);
                    return response;
                } catch (IOException e) {
                    return newFixedLengthResponse(Status.NOT_FOUND, "text/plain", l + " not found.\n" + e);
                } 
            }

            private Response translateTextResponse(Method method, IConstructor cons) {
                IString mimeType = (IString) cons.get("mimeType");
                IMap header = (IMap) cons.get("header");
                IString data = (IString) cons.get("content");
                Status status = translateStatus((IConstructor) cons.get("status"));

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
                Response response = newFixedLengthResponse(status, mimeType.getValue(), data.getValue());
                addHeaders(response, header);
                return response;
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
            monitor.jobStart("Server: " + server);
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
                monitor.jobEnd("Server: " + server, true);
            }
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(vf.string(e.getMessage()), null, null);
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
        if (statusValues.isEmpty() || requestType == null) {
            TypeFactory tf = TypeFactory.getInstance();
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

            requestType = store.lookupAbstractDataType("Request");

            RascalTypeFactory rtf = RascalTypeFactory.getInstance();
            functionType = tf.functionType(tf.valueType(), tf.tupleType(rtf.reifiedType(tf.valueType())), tf.voidType());

            get = store.lookupConstructor(requestType, "get", tf.tupleType(tf.stringType()));
            put = store.lookupConstructor(requestType, "put",  tf.tupleType(tf.stringType(), functionType));
            post = store.lookupConstructor(requestType, "post",  tf.tupleType(tf.stringType(), functionType));
            delete = store.lookupConstructor(requestType, "delete",  tf.tupleType(tf.stringType()));
            head = store.lookupConstructor(requestType, "head",  tf.tupleType(tf.stringType()));
        }
    }
}
