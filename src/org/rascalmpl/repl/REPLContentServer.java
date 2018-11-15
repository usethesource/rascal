package org.rascalmpl.repl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.rascalmpl.library.lang.json.io.JsonValueWriter;
import org.rascalmpl.uri.URIResolverRegistry;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
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
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.IWithKeywordParameters;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class REPLContentServer extends NanoHTTPD {
    private final IValueFactory vf;
    private final Cache<String, Function<IValue, IValue>> contentProviders 
      = Caffeine.newBuilder()
        .maximumSize(128)
        .expireAfterAccess(30, TimeUnit.MINUTES).build();
    
    private final Map<IConstructor,Status> statusValues = new HashMap<>();
    private Type requestType;
    private Type get;
    private Type head;
    private Type delete;

    private REPLContentServer(IValueFactory vf, int port) {
        super(port);
        this.vf = vf;
        initMethodAndStatusValues();
    }

    public static REPLContentServer startContentServer(IValueFactory vf) throws IOException {
        REPLContentServer server = null;

        for(int port = 9050; port < 9050+125; port++){
            try {
                server = new REPLContentServer(vf, port);
                server.start();
                // success
                break;
            } catch (IOException e) {
                // failure is expected if the port is taken
                continue;
            }
        }

        if (server == null) {
            throw new IOException("Could not find port to run single page server on");
        }

        return server;
    }

    public Response serve(String uri, Method method, java.util.Map<String,String> headers, java.util.Map<String,String> parms, java.util.Map<String,String> files) {
        try {
            String providerSource = uri;
            if (providerSource.startsWith("/")) {
                providerSource = providerSource.substring(1);
            }

            String[] providerParts = providerSource.split("/");

            if (providerParts.length >= 1) {
                String providerId = providerParts[0];
                Function<IValue, IValue> executor = contentProviders.getIfPresent(providerId);

                if (executor != null) {
                    IConstructor request = makeRequest(uri, method, headers, parms, files);
                    IValue rascalResponse = executor.apply(request);
                    return translateResponse(method, rascalResponse);
                }
            }

            return newFixedLengthResponse(Status.INTERNAL_ERROR, MIME_PLAINTEXT, "no content provider found for " + uri);
        }
        catch (CancellationException e) {
            stop();
            return newFixedLengthResponse(Status.INTERNAL_ERROR, MIME_PLAINTEXT, "Shutting down!");
        }
        catch (Throwable t) {
            return handleGeneralThrowable(t);
        }
    }

    private Response handleGeneralThrowable(Throwable actualException) {
        return newFixedLengthResponse(Status.INTERNAL_ERROR, MIME_PLAINTEXT, actualException.getMessage());
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
                //                return vf.constructor(put, new IValue[]{vf.string(path), getContent(files, "content")}, kws);
            case POST:
                //                return vf.constructor(post, new IValue[]{vf.string(path), getContent(files, "postData")}, kws);
            default:
                throw new IOException("Unhandled request " + method);
        }
    }

    public static Response translateResponse(Method method, IValue value) throws IOException {
        IConstructor cons = (IConstructor) value;

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
        return statusValues.get(cons);
    }

    private IMap makeMap(Map<String, String> headers) {
        IMapWriter writer = vf.mapWriter();
        for (Entry<String, String> entry : headers.entrySet()) {
            writer.put(vf.string(entry.getKey()), vf.string(entry.getValue()));
        }
        return writer.done();
    }
    
    public void registerContentProvider(String id, Function<IValue, IValue> target) {
        contentProviders.put(id, target);
    }

    private void initMethodAndStatusValues() {
        if (statusValues.isEmpty() || requestType == null) {
            TypeFactory tf = TypeFactory.getInstance();
            TypeStore store = new TypeStore();
            Type statusType = tf.abstractDataType(store, "Status");

            statusValues.put(vf.constructor(tf.constructor(store, statusType, "ok")), Status.OK);
            statusValues.put(vf.constructor(tf.constructor(store, statusType, "created")), Status.CREATED);
            statusValues.put(vf.constructor(tf.constructor(store, statusType, "accepted")), Status.ACCEPTED);
            statusValues.put(vf.constructor(tf.constructor(store, statusType, "noContent")), Status.NO_CONTENT);
            statusValues.put(vf.constructor(tf.constructor(store, statusType, "partialContent")), Status.PARTIAL_CONTENT);
            statusValues.put(vf.constructor(tf.constructor(store, statusType, "redirect")), Status.REDIRECT);
            statusValues.put(vf.constructor(tf.constructor(store, statusType, "notModified")), Status.NOT_MODIFIED);
            statusValues.put(vf.constructor(tf.constructor(store, statusType, "badRequest")), Status.BAD_REQUEST);
            statusValues.put(vf.constructor(tf.constructor(store, statusType, "unauthorized")), Status.UNAUTHORIZED);
            statusValues.put(vf.constructor(tf.constructor(store, statusType, "forbidden")), Status.FORBIDDEN);
            statusValues.put(vf.constructor(tf.constructor(store, statusType, "notFound")), Status.NOT_FOUND);
            statusValues.put(vf.constructor(tf.constructor(store, statusType, "rangeNotSatisfiable")), Status.RANGE_NOT_SATISFIABLE);
            statusValues.put(vf.constructor(tf.constructor(store, statusType, "internalError")), Status.INTERNAL_ERROR);

            requestType = tf.abstractDataType(store, "Request");

            get = tf.constructor(store, requestType, "get", tf.stringType(), "path");
            delete = tf.constructor(store, requestType, "delete",  tf.stringType(), "path");
            head = tf.constructor(store, requestType, "head",  tf.stringType(), "path");
            
            // TODO: add GET and POST!
        }
    }
}
