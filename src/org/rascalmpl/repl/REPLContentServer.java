package org.rascalmpl.repl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CancellationException;
import java.util.function.Function;

import org.rascalmpl.library.lang.json.internal.JsonValueWriter;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.ValueFactoryFactory;
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
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.IWithKeywordParameters;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class REPLContentServer extends NanoHTTPD {
    private static final IValueFactory vf = ValueFactoryFactory.getValueFactory();
    private Function<IValue, IValue> callback;
    private long lastServedAt = 0L;
    
    public REPLContentServer(int port, Function<IValue, IValue> callback) {
        super(port);
        this.callback = callback;
        this.lastServedAt = System.currentTimeMillis();
    }

    public void updateCallback(Function<IValue, IValue> callback) {
        this.callback = callback;
        lastServedAt = System.currentTimeMillis();
    }
    
    /**
     * @return the system time at which the last page was served by this server,
     * for GC purposes.
     */
    public long getLastServedAt() {
        return lastServedAt;
    }
    
    public Response serve(String uri, Method method, java.util.Map<String,String> headers, java.util.Map<String,String> parms, java.util.Map<String,String> files) {
        try {
            this.lastServedAt = System.currentTimeMillis();
            IConstructor request = makeRequest(uri, method, headers, parms, files);
            IValue rascalResponse = callback.apply(request);
            return translateResponse(method, rascalResponse);
        }
        catch (CancellationException e) {
            stop();
            return newFixedLengthResponse(Status.INTERNAL_ERROR, MIME_HTML, "Shutting down!");
        }
        catch (Throwable t) {
            return handleGeneralThrowable(t);
        }
    }

    private Response handleGeneralThrowable(Throwable e) {
        StringWriter str = new StringWriter(); 
        PrintWriter print = new PrintWriter(str);
        
        print.append("Exception while serving content:</br>");
        print.append("<pre>");
        e.printStackTrace(print);
        print.append("</pre>");
        print.flush();
        return newFixedLengthResponse(Status.NOT_FOUND, MIME_HTML, str.toString());
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
                // TODO: PUT 
                //                return vf.constructor(put, new IValue[]{vf.string(path), getContent(files, "content")}, kws);
            case POST:
                // TODO POST
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

    private static Response translateJsonResponse(Method method, IConstructor cons) {
        IMap header = (IMap) cons.get("header");
        IValue data = cons.get("val");
        Status status = translateStatus((IConstructor) cons.get("status"));
        IWithKeywordParameters<? extends IConstructor> kws = cons.asWithKeywordParameters();

        IValue dtf = kws.getParameter("dateTimeFormat");
        IValue dai = kws.getParameter("dateTimeAsInt");
        IValue formatters = kws.getParameter("formatter");

        JsonValueWriter writer = new JsonValueWriter()
            .setCalendarFormat(dtf != null ? ((IString) dtf).getValue() : "yyyy-MM-dd\'T\'HH:mm:ss\'Z\'")
            .setFormatters((IFunction) formatters)
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

    private static Response translateFileResponse(Method method, IConstructor cons) {
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

    private static Response translateTextResponse(Method method, IConstructor cons) {
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

    private static void addHeaders(Response response, IMap header) {
        // TODO add first class support for cache control on the Rascal side. For
        // now we prevent any form of client-side caching with this.. hopefully.
        response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Expires", "0");

        for (IValue key : header) {
            response.addHeader(((IString) key).getValue(), ((IString) header.get(key)).getValue());
        }
    }

    private static Status translateStatus(IConstructor cons) {
        return statusValues.get(cons);
    }

    private IMap makeMap(Map<String, String> headers) {
        IMapWriter writer = vf.mapWriter();
        for (Entry<String, String> entry : headers.entrySet()) {
            writer.put(vf.string(entry.getKey()), vf.string(entry.getValue()));
        }
        return writer.done();
    }
    
    // these are statics for quick access of and creation of typed Rascal values:
    private final static Map<IConstructor,Status> statusValues = new HashMap<>();
    public  final static Type requestType;
    private final static Type get;
    private final static Type head;
    private final static Type delete;
    
    static {
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
