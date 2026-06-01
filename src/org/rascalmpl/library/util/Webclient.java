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
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.functions.IFunction;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class Webclient {
    private final IRascalValueFactory vf;
    private final IRascalMonitor monitor;
    private final TypeStore store;
    private final TypeFactory tf;
    private final HttpClient.Builder client;
    private final WebBody body;
    
    public Webclient(IRascalValueFactory vf, IRascalMonitor monitor, TypeStore store, TypeFactory tf, PrintWriter out, PrintWriter err) {
        this.vf = vf;
        this.monitor = monitor;
        this.store = store;
        this.tf = tf;
        this.client = HttpClient.newBuilder();
        this.body = new WebBody(tf, vf, monitor);
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

    /**
     * Creates a proper Content-Type header field from keyword parameters,
     * filling in defaults for missing ones.
     */
    private IMap contentType(IConstructor input, IMap headers) {
        IConstructor body = (IConstructor) input.get("content");
        var mimeType = (IString) body.asWithKeywordParameters().getParameter("mimeType");
        if (mimeType == null) {
            mimeType = vf.string(defaultContentType(input));
        }
        var charset = (IString) body.asWithKeywordParameters().getParameter("charset");
        if (charset == null) {
            charset = vf.string(StandardCharsets.UTF_8.name());
        }

        return headers.put(vf.string("content-type"), mimeType.concat(vf.string(";charset=").concat(charset)));
    }

    private HttpRequest makeGetRequest(IConstructor input, URI uri, IMap headers) {
        return HttpRequest.newBuilder(uri)
            .headers(makeHeaders(headers))
            .GET()
            .build();
    }

    private HttpRequest makePutRequest(IConstructor input, URI uri, IMap headers) {        
        return HttpRequest.newBuilder(uri)
            .headers(makeHeaders(contentType(input, headers)))
            .PUT(publishBody(input))
            .build();
    }

    private HttpRequest makeDeleteRequest(IConstructor input, URI uri, IMap headers) {
        return HttpRequest.newBuilder(uri)
            .headers(makeHeaders(headers))
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

        IMap params = (IMap) input.asWithKeywordParameters().getParameter("parameters");
        if (params == null) {
            params = vf.map();
        }

        var result =  URIUtil.getChildLocation(host, path.getValue());
        
        try {
            if (!params.isEmpty()) {
                var query = "?" + params.stream()
                    .map(ITuple.class::cast)
                    .map(t -> ((IString) t.get(0)).getValue() + "=" + ((IString) t.get(1)).getValue())
                    .collect(Collectors.joining("&"));
                result = URIUtil.changeQuery(host, query);
            }
        }
        catch (URISyntaxException e) {
            throw RuntimeExceptionFactory.malformedURI(e.getMessage());
        }

        return result.getURI();
    }

    private HttpRequest makeHeadRequest(IConstructor input, URI uri, IMap headers) {
        return HttpRequest.newBuilder(uri)
            .headers(makeHeaders(headers))
            .method("HEAD", BodyPublishers.noBody())
            .build();
    }

    /**
     * This is for PUT and POST requests that need to send data along with the request
     */
    private BodyPublisher publishBody(IConstructor input) {
        var postBody = (IConstructor) input.get("content");
       
        if (!postBody.getName().equals("send")) {
            throw RuntimeExceptionFactory.illegalArgument(postBody, "Client-side POST should send a Body, not receive one");
        }
    
        final var charset = (IString) postBody.asWithKeywordParameters().getParameter("charset");
        final var kind = (IConstructor) postBody.get("kind");
        final var cs = charset != null ? charset.getValue() : StandardCharsets.UTF_8.name();

        switch (kind.getName()) {
            case "json":
                return BodyPublishers.ofInputStream(() -> body.writeToInputStream(body.sendJsonBody(postBody, cs), cs));
            case "html":
                return BodyPublishers.ofInputStream(() -> body.writeToInputStream(body.sendHTMLBody(postBody, cs), cs));
            case "xml":
                return BodyPublishers.ofInputStream(() -> body.writeToInputStream(body.sendXMLBody(postBody, cs), cs));
            case "file":
                return BodyPublishers.ofInputStream(() -> body.sendFileBody(postBody));
            case "text":
                return BodyPublishers.ofInputStream(() -> body.sendTextBody(postBody, cs));
            default:
                return null;
        }
    }
    
    private HttpRequest makePostRequest(IConstructor input, URI uri, IMap headers) {
        return HttpRequest.newBuilder()
            .uri(uri)
            .headers(makeHeaders(contentType(input, headers)))
            .POST(publishBody(input))
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
                case "html":
                    return "text/html";
                case "xml":
                    return "text/xml";
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

        // need at least one header to avoid IllegalArgumentExceptions  by the HTTP builder
        headers = headers.put(vf.string("User-Agent"), vf.string("rascal"));

        switch (input.getName()) {
            case "get":
                return makeGetRequest(input, host, headers);
            case "post":
                return makePostRequest(input, host, headers);
            case "put":
                return makePutRequest(input, host, headers);
            case "delete":
                return makeDeleteRequest(input, host, headers);
            case "head":
                return makeHeadRequest(input, host, headers);

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
            var host = (ISourceLocation) input.asWithKeywordParameters().getParameter("host");
            if (host == null) {
                host = URIUtil.correctLocation("http", "localhost", "");
            }
            assert response != null;

            return translateResponse(host, response);
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(e.getClass().getName());    
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    private IConstructor translateResponse(ISourceLocation url, HttpResponse<InputStream> response) throws IOException {
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
        IFunction bodyReceiver = body.createBodyReceiver(input, url, mimeType, charset);
        Type bodyConstructor = store.lookupConstructors("receive").iterator().next();
        IConstructor body = vf.constructor(bodyConstructor, bodyReceiver);
        body = body.asWithKeywordParameters().setParameter("mimetype", vf.string(mimeType));
        
        return vf.constructor(respCons, status, body)
            .asWithKeywordParameters()
            .setParameter("headers", headers);
    }

    private String getCharset(HttpHeaders headers) {
        String contentType = headers.firstValue("content-type").orElse("text/plain");
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
        String contenType = headers.firstValue("content-type").orElse("text/plain");
        String[] parts = contenType.split(";");
        return parts[0].trim();
    }

    private IConstructor toStatusConstructor(int stCode) {
        Type statusType = store.lookupAbstractDataType("Status");
        String cons = HttpStatus.of(stCode).constructor();
        return vf.constructor(tf.constructor(store, statusType, cons, tf.tupleEmpty()));
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
            long numberOfTheseSteps  = (int) (totalBytes / bytesRead) + 1;
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
