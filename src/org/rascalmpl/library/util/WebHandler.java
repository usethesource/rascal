package org.rascalmpl.library.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.types.RascalTypeFactory;
import org.rascalmpl.types.TypeReifier;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.functions.IFunction;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import io.undertow.server.HttpHandler;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.io.StandardTextWriter;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

/** 
 * This handler plugs into Undertow webservers. It should be reused
 * for different contexts that Rascal offers web services (the REPL, util::Webserver,
 * the Tutor, the IDEServices, etc)
 * 
 * This different context differ mainly in the selection of ports on localhost, and the
 * keeping alive or garbage collection scheme for running servers (manual, automated).
 * 
 * The class implements a mapping from HTTP requests to the Rascal representation as a constructor
 * and back from the Rascal representation of a response to the HTTP level. Undertow takes
 * care of all the lower level HTTP protocol decoding/encoding and the streaming of input and output.
 */
public class WebHandler implements HttpHandler {
    private final IRascalValueFactory vf;
    private final TypeFactory tf = TypeFactory.getInstance();
    private final RascalTypeFactory rtf = RascalTypeFactory.getInstance();

    private final TypeStore store = new TypeStore();
    private final Type requestType = tf.abstractDataType(store, "Request");
    private final Type responseType = tf.abstractDataType(store, "Response");
    private final Type bodyKindType = tf.abstractDataType(store, "BodyKind");
    private final Type statusType = tf.abstractDataType(store, "Status");
    private final Type bodyType = tf.abstractDataType(store, "Body");
    private final Type responseCons = tf.constructor(store, responseType, "response", statusType, "status", bodyType, "body");
    private final Type sendCons = tf.constructor(store, bodyType, "send", bodyKindType, "kind", tf.valueType(), "source");
    private final Type parT = tf.parameterType("T");
    private final Type typeT = rtf.reifiedType(parT);
    private final Type receiveCons = tf.constructor(store, bodyType, "receiver", tf.functionType(parT, tf.tupleType(bodyKindType, typeT), tf.tupleEmpty()));

    private final Type textCons = tf.constructor(store, bodyKindType, "text");
    private final Type jsonCons = tf.constructor(store, bodyKindType, "json");
    private final Type xmlCons =  tf.constructor(store, bodyKindType, "xml");
    private final Type htmlCons = tf.constructor(store, bodyKindType, "html");
    private final Type htmlElementType =  tf.abstractDataType(store, "HTMLElement");
    
    private final  Type post = tf.constructor(store, requestType, "post", tf.stringType(), "path", bodyType, "content");
    private final  Type get = tf.constructor(store, requestType, "get", tf.stringType(), "path");
    private final  Type head = tf.constructor(store, requestType, "head", tf.stringType(), "path");
    private final  Type delete = tf.constructor(store, requestType, "delete", tf.stringType(), "path");
    private final  Type put = tf.constructor(store, requestType, "put", tf.stringType(), "path", bodyType, "content");
    
	
    private final IConstructor statusOK;
    private final IConstructor statusNotFound;

    private final Map<IConstructor, HttpStatus> statusValues;
    private final WebBody body;
    
    private Function<IValue,IValue> callback;
    private int port;
    private long lastServedAt = System.currentTimeMillis();
    private final IRascalMonitor monitor;

    /**
     * This is only for testing purposes. It creates a echoing servert that does not require a interpreter lock
     */
    public WebHandler(int port, IRascalValueFactory vf, IRascalMonitor monitor) {
        this((Function<IValue, IValue>) null, port, vf, monitor);
        this.callback = testEchoFunction();
    }

    public WebHandler(IFunction server, int port, IRascalValueFactory vf, IRascalMonitor monitor) {
        this(r -> server.call(r), port, vf, monitor);
    }

    public WebHandler(Function<IValue, IValue> server, int port, IRascalValueFactory vf, IRascalMonitor monitor) {
        this.callback = server;
        this.monitor = monitor;
        this.port = port;
        this.vf = vf;
        this.body = new WebBody(tf, vf, monitor);

        this.statusValues = new HashMap<>();
        for (HttpStatus code : HttpStatus.allCodes()) {
            statusValues.put(vf.constructor(tf.constructor(store, statusType, code.toConstructor())), code);
        }

        this.statusNotFound = vf.constructor(tf.constructor(store, statusType, "notFound"));
        this.statusOK = vf.constructor(tf.constructor(store, statusType, "ok"));
    }

    /**
     * For cheaply updating a reloaded version of the callback without restarting the server
     */
    public void updateCallback(Function<IValue, IValue> handler) {
        this.callback = handler;
    }

    public Function<IValue,IValue> testEchoFunction() {
        TypeReifier tr = new TypeReifier(vf);
        IValue nodeT = tr.typeToValue(tf.nodeType(), new TypeStore(), vf.map());
        IValue strT = tr.typeToValue(tf.stringType(), new TypeStore(), vf.map());
        IValue htmlT = tr.typeToValue(htmlElementType, new TypeStore(), vf.map());
        
        return (r) -> {
            IConstructor request = (IConstructor) r;
            IMap headers = request.asWithKeywordParameters().getParameter("headers");
            String path = ((IString) request.get(0)).getValue();
            
            switch (path) {
                case "/get":
                    var getBody = vf.constructor(sendCons, vf.constructor(textCons), vf.string("ok"));
                    return vf.constructor(responseCons, statusOK, getBody);
                case "/post/json":
                case "/put/json":
                    var body = (IConstructor) request.get("content");
                    var receiver = (IFunction) body.get("receiver");
                    var receiveJsonBody = receiver.call(vf.constructor(jsonCons), nodeT);
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
        };
    }

    /**
     * This is used for automatic server management (stopping unused servers)
     */
    public long getLastServedAt() {
        return lastServedAt;
    }

    /**
     * Translates an HTTP request to a Rascal constructor version and calls the callback
     * to get a Rascal Response which is translated back to a HTTP response.
     * 
     * Does error handling and serves Rascal stack traces for transparancy sake
     * when internal errors happen.
     */
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        lastServedAt = System.currentTimeMillis();
        exchange.startBlocking();

        var method = exchange.getRequestMethod();
        var parms = exchange.getQueryParameters();
        var headers = exchange.getRequestHeaders();
        var path = exchange.getRequestPath();
        var url = vf.sourceLocation("http", "localhost" + port, path);

        exchange.dispatch(() -> {
            try {
                IConstructor request = makeRequest(url, path, method, headers, parms, exchange);
                translateResponse(method, exchange, callback.apply(request));
            }
            catch (Throwable e) {
                if (e instanceof Throw) {
                    handleRascalThrow(HttpStatus.INTERNAL_ERROR, (Throw) e, exchange);
                }
                else {
                    handleGeneralThrowable(HttpStatus.INTERNAL_ERROR, e, exchange);
                }
            }
        });
    };

    
    private void handleGeneralThrowable(HttpStatus status, Throwable actualException, HttpServerExchange exchange)  {
        exchange.setStatusCode(HttpStatus.INTERNAL_ERROR.toCode());
        exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "text/plain");

        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(exchange.getOutputStream(), "utf-8"))) {
            out.println(status + ": " + actualException.getMessage());
            actualException.printStackTrace(out);
        }
        catch (IOException ex) {
            monitor.warning("handler could not report an error to the user: " + ex.getMessage(), URIUtil.rootLocation("contentServer"));
        }
    }

    private void handleRascalThrow(HttpStatus status, Throw e, HttpServerExchange exchange) {
        exchange.setStatusCode(HttpStatus.INTERNAL_ERROR.toCode());
        exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "text/plain");

        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(exchange.getOutputStream(), "utf-8"))) {
            out.println(status + ": " + e.getMessage());
            e.getTrace().prettyPrintedString(out, new StandardTextWriter(true));
        }
        catch (IOException ex) {
            monitor.warning("handler could not report an error to the user: " + ex.getMessage(), URIUtil.rootLocation("contentServer"));
        }
    }

    private String getMimeType(HeaderMap headers) {
        String contenType = headers.get(Headers.CONTENT_TYPE).getFirst();
        String[] parts = contenType.toString().split(";");
        return parts[0].trim();
    }

    private String getCharset(HeaderMap headers) {
        String contentType = headers.get(Headers.CONTENT_TYPE).getFirst();
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

    private IConstructor makeRequest(ISourceLocation host, String path, HttpString method, HeaderMap headers,
        Map<String, Deque<String>> parms, HttpServerExchange exchange) throws FactTypeUseException, IOException {

        Map<String,IValue> kws = new HashMap<>();
        kws.put("parameters", makeMap(parms));
        kws.put("headers", makeMap(headers));
        var mimeType = getMimeType(headers);
        var charset = getCharset(headers);
                
        switch (method.toString()) {
            case "HEAD":
                return vf.constructor(head, new IValue[]{vf.string(path)}, kws);
            case "DELETE":
                return vf.constructor(delete, new IValue[]{vf.string(path)}, kws);
            case "GET":
                return vf.constructor(get, new IValue[]{vf.string(path)}, kws);
            case "PUT":  
                return vf.constructor(put, new IValue[]{vf.string(path), vf.constructor(receiveCons, body.createBodyReceiver(exchange.getInputStream(), host, mimeType, charset))}, kws);
            case "POST":
                return vf.constructor(post, new IValue[]{vf.string(path), vf.constructor(receiveCons, body.createBodyReceiver(exchange.getInputStream(), host, mimeType, charset))}, kws);
            default:
                throw new IOException("Unhandled request method: " + method);
        }
    }

    private void translateResponse(HttpString method, HttpServerExchange exchange, IValue response) throws IOException {
        IConstructor cons = (IConstructor) response;
        IConstructor b = (IConstructor) cons.get("body");
        IValue value = b.get("source");
        IConstructor kind = (IConstructor) b.get("kind");
        IMap header = (IMap) cons.asWithKeywordParameters().getParameter("headers");
        if (header == null) {
            header = vf.map();
        }
        IString mimeType = b.asWithKeywordParameters().getParameter("mimeType");
        if (mimeType == null) {
            mimeType = vf.string("text/plain");
        }
        HttpStatus status = translateStatus((IConstructor) cons.get("status"));
        IString charset = b.asWithKeywordParameters().getParameter("charset");
        if (charset == null) {
            charset = vf.string(StandardCharsets.UTF_8.name());
        }
        String contentType = mimeType.getValue() + ";charset=" + charset.getValue();

        addHeaders(exchange.getResponseHeaders(), header, contentType);

        if (!method.equals(Methods.HEAD)) {
            switch (status) {
                case BAD_REQUEST:
                case UNAUTHORIZED:
                case NOT_FOUND:
                case FORBIDDEN:
                case RANGE_NOT_SATISFIABLE:
                case INTERNAL_ERROR:
                    if (value.getType().isString() && ((IString) value).length() == 0) {
                        value = vf.string(status.toCode() + ": " + status.toString());
                    }
                default:
                    break;
            }
        }

        try (OutputStream out = exchange.getOutputStream()) {
            switch (kind.getName()) {
                case "file":
                    try (InputStream in = body.sendFileBody(kind)) {
                        in.transferTo(out);
                    }
                    break;
                case "json":
                    body.writeToOutputStream(out, body.sendJsonBody(b, charset.getValue()), charset.getValue());
                    break;
                case "xml": 
                    body.writeToOutputStream(out, body.sendXMLBody(b, charset.getValue()), charset.getValue());
                    break;
                case "html": 
                    body.writeToOutputStream(out, body.sendHTMLBody(b, charset.getValue()), charset.getValue());
                    break;
                case "text":
                default:
                    body.writeTextBody(b, charset.getValue());
            }
        }

        return;
    }

    private void addHeaders(HeaderMap response, IMap header, String contentType) {
        response.add(Headers.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        response.add(Headers.PRAGMA, "no-cache");
        response.add(Headers.EXPIRES, "0");                
        response.add(Headers.CONTENT_TYPE, contentType);

        for (IValue key : header) {
            response.add(new HttpString(((IString) key).getValue()), ((IString) header.get(key)).getValue());
        }
    }

    private HttpStatus translateStatus(IConstructor cons) {
        return statusValues.get(cons);
    }

    private IMap makeMap(Map<String, Deque<String>> headers) {
        IMapWriter writer = vf.mapWriter();
        for (Entry<String, Deque<String>> entry : headers.entrySet()) {
            writer.put(vf.string(entry.getKey()), vf.string(entry.getValue().getFirst()));
        }
        return writer.done();
    }

    private IMap makeMap(HeaderMap headers) {
        IMapWriter writer = vf.mapWriter();
        for (HttpString key : headers.getHeaderNames()) {
            writer.put(vf.string(key.toString()), vf.string(headers.get(key).toString()));
        }
        return writer.done();
    }
}
