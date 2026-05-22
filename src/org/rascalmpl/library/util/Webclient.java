package org.rascalmpl.library.util;

import java.io.BufferedOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.input.ReaderInputStream;
import org.checkerframework.checker.units.qual.s;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.library.lang.json.internal.JsonValueReader;
import org.rascalmpl.library.lang.json.internal.JsonValueWriter;
import org.rascalmpl.types.RascalTypeFactory;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.functions.IFunction;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import fi.iki.elonen.NanoHTTPD.Response.Status;
import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class Webclient {
    private final IRascalValueFactory vf;
    private final IRascalMonitor monitor;
    private final TypeStore store;
    private final TypeFactory tf;
    private final ExecutorService executor;
    private final HttpClient.Builder client;

    public Webclient(IRascalValueFactory vf, IRascalMonitor monitor, TypeStore store, TypeFactory tf) {
        this.vf = vf;
        this.monitor = monitor;
        this.store = store;
        this.tf = tf;
        this.executor = Executors.newCachedThreadPool();
        this.client = HttpClient.newBuilder();
    }

    private String[] makeHeaders(IMap headers) {
        return headers
            .stream()
            .map(ITuple.class::cast)
            .flatMap((ITuple t) -> Stream.of(
                ((IString) t.get(0)).getValue(),
                ((IString) t.get(1)).getValue()
            ))
            .toArray(String[]::new)
            ;
    }

    private HttpRequest makeGetRequest(IConstructor input, URI uri, String[] headers) {
        return HttpRequest.newBuilder(uri)
            .headers(headers)
            .GET()
            .build();
    }

    private HttpRequest makePutRequest(IConstructor input, URI uri, String[] headers, String charset) {        
        return HttpRequest.newBuilder(uri)
            .headers(headers)
            .PUT(publishBody(input, charset))
            .build();
    }

    private HttpRequest makeDeleteRequest(IConstructor input, URI uri, String[] headers) {
        return HttpRequest.newBuilder(uri)
            .headers(headers)
            .DELETE()
            .build();
    }

    private URI requireHost(IConstructor input) {
        ISourceLocation host = (ISourceLocation) input.asWithKeywordParameters().getParameter("host");
        IString path = (IString) input.get("path");

        if (host == null) {
            throw RuntimeExceptionFactory.illegalArgument(input, "missing `host` field");
        }

        if (!host.getPath().equals("/") && !host.getPath().equals("")) {
            throw RuntimeExceptionFactory.illegalArgument(host, "path after hostname should be given with the \'path\' parameter of a request");
        }

        // TODO: query, fragment
        return URIUtil.getChildLocation(host, path.getValue()).getURI();
    }

    private HttpRequest makeHeadRequest(IConstructor input, URI uri, String[] headers) {
        return HttpRequest.newBuilder(uri)
            .headers(headers)
            .method("HEAD", BodyPublishers.noBody())
            .build();
    }

    private BodyPublisher publishJsonBody(IConstructor input, String charset) {    
        return new BodyPublisher() {  
            @Override
            public void subscribe(Subscriber<? super ByteBuffer> subscriber) {
                final var publisher = new OutputStreamPublisher(subscriber);

                executor.submit(() -> { 
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

                    JsonValueWriter writer = new JsonValueWriter()
                        .setCalendarFormat(dtf != null ? ((IString) dtf).getValue() : "yyyy-MM-dd\'T\'HH:mm:ss\'Z\'")
                        .setFormatters(formatters)
                        .setDatesAsInt(dai != null ? ((IBool) dai).getValue() : true)
                        .setRationalsAsString(ras != null ? ((IBool) ras).getValue() : false)
                        .setExplicitConstructorNames(ecn != null ? ((IBool) ecn).getValue() : false)
                        .setExplicitDataTypes(edt != null ? ((IBool) edt).getValue() : false)
                        ;
                    
                    try (JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(publisher, charset))) {
                        writer.write(jsonWriter, input);
                    }
                    catch (IOException e) {
                        throw RuntimeExceptionFactory.io(e);
                    }
                }, true);
            }

            @Override
            public long contentLength() {
                return -1;
            }     
        };
    }

    private BodyPublisher publishFileBody(IConstructor kind, IConstructor input) {
        final var loc = (ISourceLocation) input.get("source");

        // have to hope that the encoding matches the actual contents of the stream

        return BodyPublishers.ofInputStream(() -> {
            try {
                return URIResolverRegistry.getInstance().getInputStream(loc);
            }
            catch (IOException e) {
                throw RuntimeExceptionFactory.io(e);
            }
        });
    }

    private BodyPublisher publishTextBody(IConstructor input, String charset) {
        final var text = (IString) input.get("source");
        return BodyPublishers.ofInputStream(() -> new ReaderInputStream(text.asReader(), charset));
    }

    /**
     * This is for PUT and POST requests that need to send data along with the request
     */
    private BodyPublisher publishBody(IConstructor input, String charset) {
        var postBody = (IConstructor) input.get("content");
       
        if (!postBody.getName().equals("send")) {
            throw RuntimeExceptionFactory.illegalArgument(postBody, "Client-side POST should send a Body, not receive one");
        }
      
        final var kind = (IConstructor) postBody.get("kind");

        switch (kind.getName()) {
            case "json":
                return publishJsonBody(postBody, charset);
            case "file":
                return publishFileBody(kind, postBody);
            case "text":
                return publishTextBody(postBody, charset);
            default:
                return null;
        }
    }
    
    /**
     * On demand streamer for the HttpClient API (for sending bodies for PUT and POST)
     */
    private static class OutputStreamPublisher extends BufferedOutputStream {  
        
        public OutputStreamPublisher(Subscriber<? super ByteBuffer> subscriber) {  
            super(new PublishingStream(subscriber));  
        }  
        /**  
         * The buffed outputstream will take care to collect the bytes untill there's a decent chunk to forward to the consumers  
         */  
        private static class PublishingStream extends OutputStream implements Subscription {  
            private final Subscriber<? super ByteBuffer> subscriber;  

            public PublishingStream(Subscriber<? super ByteBuffer> subscriber) {
                this.subscriber = subscriber;
                subscriber.onSubscribe(this);
            }

            @Override  
            public void write(int b) throws IOException {      
                subscriber.onNext(ByteBuffer.wrap(new byte[] { (byte)(b & 0xFF) }));         
            }  

            @Override  
            public void write(byte[] b, int off, int len) throws IOException {  
                subscriber.onNext(ByteBuffer.wrap(b, off, len).asReadOnlyBuffer());   
            }  

            @Override  
            public void close() throws IOException {  
                subscriber.onComplete();  
            }

            @Override
            public void request(long n) {
                // @DavyLandman we can not implement this pull model if we are a writer
            }

            @Override
            public void cancel() {
                try {
                    close();
                }
                catch (IOException e) {
                    // ignore
                }
            }  
        }  
    }

    private HttpRequest makePostRequest(IConstructor input, URI uri, String[] headers, String charset) {
        return HttpRequest.newBuilder()
            .uri(uri)
            .headers(headers)
            .POST(publishBody(input, charset))
            .build();
    }

    private String defaultContentType(IConstructor input) {
        if (input.getName().equals("post") || input.getName().equals("put")) {
            var body = (IConstructor) input.get("content");
            var kind = (IConstructor) body.get("kind");

            switch (kind.getName()) {
                case "json":
                    return "application/json";
                case "file":
                    return "application/octet-stream";
                case "text":
                default:
                    return "text/plain";
            }
        }

        // only post and put have content to send
        return "";
    }

    private HttpRequest makeRequest(IConstructor input) {
        var host = requireHost(input);
        var headers = (IMap) input.asWithKeywordParameters().getParameter("header");

        if (headers == null) {
            headers = vf.map();
        }

        var charset = ((IString) input.asWithKeywordParameters().getParameter("charset"));

        if (charset == null) {
            charset = vf.string(StandardCharsets.UTF_8.name());
        }

        var contentType = ((IString) input.asWithKeywordParameters().getParameter("content-type"));
        
        if (contentType == null) {
            contentType = vf.string(defaultContentType(input));
        }

        if (headers.get(vf.string("Content-Type")) != null) {
            throw RuntimeExceptionFactory.illegalArgument(input, "For POST and PUT, use the keyword fields 'content-type' and 'charset' instead of the 'Content-Type' header field.");
        }

        if (contentType.length() != 0) {
            headers = headers.put(vf.string("Content-Type"), vf.string(contentType + ";charset=" + charset));
        }        

        // need at least one header to avoid IllegalArgumentExceptions  by the HTTP builder
        headers = headers.put(vf.string("User-Agent"), vf.string("rascal-stdlib"));

        var httpHeaders = makeHeaders(headers);

        switch (input.getName()) {
            case "get":
                return makeGetRequest(input, host, httpHeaders);
            case "post":
                return makePostRequest(input, host, httpHeaders, charset.getValue());
            case "put":
                return makePutRequest(input, host, httpHeaders, charset.getValue());
            case "delete":
                return makeDeleteRequest(input, host, httpHeaders);
            case "head":
                return makeHeadRequest(input, host, httpHeaders);

            default:
                throw RuntimeExceptionFactory.illegalArgument(input);
        }
    }

    /**
     * This is the main API method for the Rascal side
     */
    public IConstructor fetch(IConstructor input) {
        try {
            var request = makeRequest(input);
            var response = client
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build()
                .send(request, HttpResponse.BodyHandlers.ofInputStream());
            var body = (IConstructor) input.asWithKeywordParameters().getParameter("body");

            return translateResponse(request.uri().toString(), body, response);
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(e);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
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
    private IFunction createBodyReceiver(InputStream input, String url, String contentType, String charset) {        
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
                case "file":
                    return receiveFileBody(input, kind, expect);
                case "text":
                default:
                    return receiveTextBody(input, url, kind, expect, contentType, charset);
            }
        });
    }

    private IValue receiveTextBody(InputStream input, String url, IConstructor kind, Type expect, String contentType, String charset) {
        if (!expect.isSubtypeOf(tf.stringType())) {
            monitor.warning("a text response expects a `str` type, but we have " + expect, URIUtil.assumeCorrectLocation(url));
        }

        if (!contentType.contains("text/")) {
            monitor.warning("a text response was expected but the mimetype is " + contentType, URIUtil.assumeCorrectLocation(url));
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

    private IValue receiveJsonBody(InputStream input, String url, IConstructor kind, Type expect, String contentType, String charset) {
        if (!contentType.equals("application/json")) {
            throw RuntimeExceptionFactory.illegalArgument(kind, "expected content-type 'application/json', got: " + contentType);
        }

        try {
            JsonReader jsonReader = new JsonReader(new InputStreamReader(input, charset));
            JsonValueReader parser = new JsonValueReader(vf, store, monitor, URIUtil.assumeCorrectLocation(url));
            return parser.read(jsonReader, expect);
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(e);
        }
    }

    private IConstructor translateResponse(String url, IConstructor expect, HttpResponse<InputStream> response) throws IOException {
        var headers = response
            .headers()
            .map()
            .entrySet()
            .stream()
            .map(e -> vf.tuple(
                vf.string(e.getKey()),
                vf.string(e.getValue().stream().collect(Collectors.joining(","))
            )))
            .collect(vf.mapWriter());

        long totalBytes = response.headers()
            .firstValueAsLong("Content-Length")
            .orElse(-1);

        var input = totalBytes > 0 
            ? new MonitoredInputStream(response.body(), monitor, "Fetching " + url, totalBytes)
            : response.body(); 

        String mimeType = getMimeType(response.headers());
        String charset = getCharset(response.headers());
        var status = toStatusConstructor(response.statusCode());

        Type respCons = store.lookupConstructors("response").iterator().next();
        IFunction bodyReceiver = createBodyReceiver(input, url, mimeType, charset);
        Type bodyConstructor = store.lookupConstructors("receive").iterator().next();
        IConstructor body = vf.constructor(bodyConstructor, bodyReceiver);

        return vf.constructor(respCons, status, vf.string(mimeType), headers, body);
    }

    private String getCharset(HttpHeaders headers) {
        String contentType = headers.firstValue("Content-Type").orElse("text/plain");
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

    private String getMimeType(HttpHeaders headers) {
        String contenType = headers.firstValue("Content-Type").orElse("text/plain");
        String[] parts = contenType.split(";");
        return parts[0].trim();
    }

    private IConstructor toStatusConstructor(int stCode) {
        var statusType = store.lookupAbstractDataType("Status");

        var status = Status.lookup(stCode);
        switch (status) {
            case OK:
                return vf.constructor(store.lookupConstructor(statusType, "ok", tf.tupleEmpty()));
            case NOT_FOUND: 
                return vf.constructor(store.lookupConstructor(statusType, "notFound", tf.tupleEmpty()));
            case ACCEPTED:
                return vf.constructor(store.lookupConstructor(statusType, "accepted", tf.tupleEmpty()));
            case BAD_REQUEST:
                return vf.constructor(store.lookupConstructor(statusType, "badRequest", tf.tupleEmpty()));
            case CONFLICT:
                return vf.constructor(store.lookupConstructor(statusType, "conflict", tf.tupleEmpty()));
            case CREATED:
                return vf.constructor(store.lookupConstructor(statusType, "create", tf.tupleEmpty()));
            case EXPECTATION_FAILED:
                return vf.constructor(store.lookupConstructor(statusType, "expectationFailed", tf.tupleEmpty()));
            case FORBIDDEN:
                return vf.constructor(store.lookupConstructor(statusType, "forbidden", tf.tupleEmpty()));
            case FOUND:
                return vf.constructor(store.lookupConstructor(statusType, "found", tf.tupleEmpty()));
            case GONE:
                return vf.constructor(store.lookupConstructor(statusType, "gone", tf.tupleEmpty()));
            case INTERNAL_ERROR:
                return vf.constructor(store.lookupConstructor(statusType, "internalError", tf.tupleEmpty()));
            case LENGTH_REQUIRED:
                return vf.constructor(store.lookupConstructor(statusType, "lengthRequired", tf.tupleEmpty()));
            case METHOD_NOT_ALLOWED:
                return vf.constructor(store.lookupConstructor(statusType, "methodNotAllowed", tf.tupleEmpty()));
            case MULTI_STATUS:
                return vf.constructor(store.lookupConstructor(statusType, "multiStatus", tf.tupleEmpty()));
            case NOT_ACCEPTABLE:
                return vf.constructor(store.lookupConstructor(statusType, "notAcceptible", tf.tupleEmpty()));
            case NOT_IMPLEMENTED:
                return vf.constructor(store.lookupConstructor(statusType, "notImplemented", tf.tupleEmpty()));
            case NOT_MODIFIED:
                return vf.constructor(store.lookupConstructor(statusType, "notModified", tf.tupleEmpty()));
            case NO_CONTENT:
                return vf.constructor(store.lookupConstructor(statusType, "noContent", tf.tupleEmpty()));
            case PARTIAL_CONTENT:
                return vf.constructor(store.lookupConstructor(statusType, "partialContent", tf.tupleEmpty()));
            case PAYLOAD_TOO_LARGE:
                return vf.constructor(store.lookupConstructor(statusType, "payloadTooLarge", tf.tupleEmpty()));
            case PRECONDITION_FAILED:
                return vf.constructor(store.lookupConstructor(statusType, "preconditionFailed", tf.tupleEmpty()));
            case RANGE_NOT_SATISFIABLE:
                return vf.constructor(store.lookupConstructor(statusType, "rangeNotSatisfieable", tf.tupleEmpty()));
            case REDIRECT:
                return vf.constructor(store.lookupConstructor(statusType, "redirect", tf.tupleEmpty()));
            case REDIRECT_SEE_OTHER:
                return vf.constructor(store.lookupConstructor(statusType, "redirectSeeOther", tf.tupleEmpty()));
            case REQUEST_TIMEOUT:
                return vf.constructor(store.lookupConstructor(statusType, "requestTimeout", tf.tupleEmpty()));
            case SERVICE_UNAVAILABLE:
                return vf.constructor(store.lookupConstructor(statusType, "serviceUnavailable", tf.tupleEmpty()));
            case SWITCH_PROTOCOL:
                return vf.constructor(store.lookupConstructor(statusType, "switchProtocol", tf.tupleEmpty()));
            case TEMPORARY_REDIRECT:
                return vf.constructor(store.lookupConstructor(statusType, "temporaryRedirect", tf.tupleEmpty()));
            case TOO_MANY_REQUESTS:
                return vf.constructor(store.lookupConstructor(statusType, "tooManyRequests", tf.tupleEmpty()));
            case UNAUTHORIZED:
                return vf.constructor(store.lookupConstructor(statusType, "unauthorized", tf.tupleEmpty()));
            case UNSUPPORTED_HTTP_VERSION:
                return vf.constructor(store.lookupConstructor(statusType, "unsupportedHTTPVersion", tf.tupleEmpty()));
            case UNSUPPORTED_MEDIA_TYPE:
                return vf.constructor(store.lookupConstructor(statusType, "unsupportedMediaType", tf.tupleEmpty()));
            default:
                // if we don't understand the error code; let's call it an internal error
                return vf.constructor(store.lookupConstructor(statusType, "internalError", tf.tupleEmpty()));
        }
    }

    private class MonitoredInputStream extends FilterInputStream {
        private final IRascalMonitor monitor;
        private final String jobName;

        private final long totalBytes;
        private long bytesRead = 0;
        private boolean started = false;
        private boolean done = false;

        public MonitoredInputStream(InputStream in, IRascalMonitor monitor, String jobName, long totalBytes) {
            super(in);
            this.totalBytes = totalBytes;
            this.monitor = monitor;
            this.jobName = jobName;
        }

        private void ensureStarted() {
            if (!started) {
                started = true;
                monitor.jobStart(jobName, Integer.MAX_VALUE);
            }
        }

        private void updateProgress(int bytesRead) throws InterruptedIOException {
            if (monitor.jobIsCanceled(jobName)) {
                throw new InterruptedIOException(jobName);
            }

            ensureStarted();
            long numberOfTheseSteps  = (int) (totalBytes / bytesRead);
            int stepSize = (int) (Integer.MAX_VALUE / numberOfTheseSteps);
            monitor.jobStep(jobName, "", java.lang.Math.max(stepSize, 1));
            checkDone();
        }

        private void checkDone() {
            if (!done && bytesRead >= totalBytes) {
                done = true;
                monitor.jobEnd(jobName, true);
            }
        }

        @Override
        public int read() throws IOException {
            int b = super.read();
            if (b != -1) {
                bytesRead += 1;
                updateProgress(1);
            }
            return b;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int n = super.read(b, off, len);
            if (n > 0) {
                bytesRead += n;
                updateProgress(n);
            }
            return n;
        }
    }
  }
