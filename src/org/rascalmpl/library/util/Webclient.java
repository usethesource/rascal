package org.rascalmpl.library.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.values.IRascalValueFactory;

import fi.iki.elonen.NanoHTTPD.Response.Status;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;
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

    public IConstructor fetch(IConstructor input) {
        try {
            var params = input.asWithKeywordParameters();

            switch (input.getName()) {
                case "get":
                    HttpRequest request = HttpRequest.newBuilder()
                        .uri(((ISourceLocation) params.getParameter("uri")).getURI())
                        .GET()
                        .build();
                
                    HttpResponse<String> response =
                    HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

                    return translateTextResponse(response);
                default: 
                    // not yet implemented
                    throw RuntimeExceptionFactory.illegalArgument(input);
            }
        }
        catch (IOException | InterruptedException e) {
            throw RuntimeExceptionFactory.io(e.getMessage());
        }
    }

    private IConstructor translateTextResponse(HttpResponse<String> response) {
        IMap headers = response
            .headers()
            .map()
            .entrySet()
            .stream()
            .map(e -> vf.tuple(
                vf.string(e.getKey()),
                vf.string(e.getValue().stream().collect(Collectors.joining(","))
            )))
            .collect(vf.mapWriter());

        IString body = vf.string(response.body());
        IConstructor status = toStatusConstructor(response.statusCode());

        Type respCons = store.lookupConstructors("response").iterator().next();

        var contentType = response.headers().firstValue("Content-Type");
        
        IString mimeType = vf.string(contentType.get().split(";")[0]);
        
        return vf.constructor(respCons, status, mimeType, headers, body);
    }

    IConstructor toStatusConstructor(int stCode) {
        Type statusType = store.lookupAbstractDataType("Status");

        Status status = Status.lookup(stCode);
        switch (status) {
            case OK:
                return vf.constructor(store.lookupConstructor(statusType, "ok", tf.tupleEmpty()));
            case NOT_FOUND: 
                return vf.constructor(store.lookupConstructor(statusType, "notFound", tf.tupleEmpty()));
            case ACCEPTED:
                return vf.constructor(store.lookupConstructor(statusType, "accepted", tf.tupleEmpty()));
            case BAD_REQUEST:
                return vf.constructor(store.lookupConstructor(statusType, "badRequest", tf.tupleEmpty()))
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
                break;
        }
    }
  }
