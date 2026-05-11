package org.rascalmpl.library.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.stream.Collectors;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.types.TypeReifier;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.functions.IFunction;

import fi.iki.elonen.NanoHTTPD.Response.Status;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class Webclient {
    private final IRascalValueFactory vf;
    private final IRascalMonitor monitor;
    private final TypeStore store;
    private final TypeFactory tf;

    public Webclient(IRascalValueFactory vf, IRascalMonitor monitor, TypeStore store, TypeFactory tf) {
        this.vf = vf;
        this.monitor = monitor;
        this.store = store;
        this.tf = tf;
    }

    private HttpRequest makeGetRequest(IConstructor input) {
        var params = input.asWithKeywordParameters();
        return HttpRequest.newBuilder()
            .uri(URIUtil.getChildLocation(
                (ISourceLocation) params.getParameter("uri"), 
                ((IString) input.get("path")).getValue()).getURI())
            .GET()
            .build();
    }

    private HttpRequest makePutRequest(IConstructor input) {
        var params = input.asWithKeywordParameters();
        var postBody = (IFunction) input.get("body");
        var rt = new TypeReifier(vf).typeToValue(tf.stringType(), store, vf.map());

        return HttpRequest.newBuilder()
            .uri(((ISourceLocation) params.getParameter("uri")).getURI())
            .PUT(HttpRequest.BodyPublishers.ofString(((IString) postBody.call(rt)).getValue()))
            .build();
    }

    private HttpRequest makeDeleteRequest(IConstructor input) {
        var params = input.asWithKeywordParameters();

        return HttpRequest.newBuilder()
            .uri(((ISourceLocation) params.getParameter("uri")).getURI())
            .DELETE()
            .build();
    }

    private HttpRequest makeHeadRequest(IConstructor input) {
        var params = input.asWithKeywordParameters();

        return HttpRequest.newBuilder()
            .uri(((ISourceLocation) params.getParameter("uri")).getURI())
            .method("HEAD", BodyPublishers.noBody())
            .build();
    }

    private HttpRequest makePostRequest(IConstructor input) {
        var params = input.asWithKeywordParameters();
        var postBody = (IFunction) input.get("body");
        var rt = new TypeReifier(vf).typeToValue(tf.stringType(), store, vf.map());
                    
        return HttpRequest.newBuilder()
            .uri(((ISourceLocation) params.getParameter("uri")).getURI())
            .POST(HttpRequest.BodyPublishers.ofString(((IString) postBody.call(rt)).getValue()))
            .build();
    }

    private HttpRequest makeRequest(IConstructor input) {
        switch (input.getName()) {
            case "get":
                return makeGetRequest(input);
            case "post":
                return makePostRequest(input);
            case "put":
                return makePutRequest(input);
            case "delete":
                return makeDeleteRequest(input);
            case "head":
                return makeHeadRequest(input);

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
            var response = HttpClient
                .newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build()
                .send(request, HttpResponse.BodyHandlers.ofInputStream());

            return translateTextResponse(request.uri().toString(), response);
        }
        catch (IOException | InterruptedException e) {
            throw RuntimeExceptionFactory.io(e.getMessage());
        }
    }

    private IConstructor translateTextResponse(String url, HttpResponse<InputStream> response) throws IOException {
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

        var contentType = response.headers().firstValue("Content-Type");
        
        var mimeType = vf.string(contentType.get().split(";")[0]);

        // TODO: extract from contentType if present
        var charset = "utf-8";

        var body = vf.string(new String(Prelude.consumeInputStream(input), charset));
        var status = toStatusConstructor(response.statusCode());
        var respCons = store.lookupConstructors("response").iterator().next();

        return vf.constructor(respCons, status, mimeType, headers, body);
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

        private void updateProgress(int bytesRead) {
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
