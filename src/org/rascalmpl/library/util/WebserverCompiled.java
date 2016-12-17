package org.rascalmpl.library.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ICallableCompiledValue;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.java2rascal.Java2Rascal;
import org.rascalmpl.library.lang.json.io.JsonValueReader;
import org.rascalmpl.library.lang.json.io.JsonValueWriter;
import org.rascalmpl.library.util.IWebserver.KWRequest;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.IWithKeywordParameters;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeStore;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

public class WebserverCompiled {
    private final IValueFactory vf;
    private final Map<ISourceLocation, NanoHTTPD> servers;
    private final Map<IConstructor,Status> statusValues = new HashMap<>();
    private Type requestType;

    private IWebserver webserver;

    public WebserverCompiled(IValueFactory vf) {
        this.vf = vf;
        this.servers = new HashMap<>();
    }

    public void serve(ISourceLocation url, final IValue callback, final RascalExecutionContext rex) throws IOException {
        URI uri = url.getURI();
        initMethodAndStatusValues(rex);

        int port = uri.getPort() != -1 ? uri.getPort() : 80;
        String host = uri.getHost() != null ? uri.getHost() : "localhost";
        host = host.equals("localhost") ? "127.0.0.1" : host; // NanoHttp tries to resolve localhost, which isn't what we want!
        final ICallableCompiledValue callee = (ICallableCompiledValue) callback; 

        NanoHTTPD server = new NanoHTTPD(host, port) {

            @Override
            public Response serve(String uri, Method method, 
                                  Map<String, String> headers, 
                                  Map<String, String> parms, 
                                  Map<String, String> files) {
                try {
                    IConstructor request = makeRequest(uri, method, headers, parms, files);

                    synchronized (callee) {
                        return translateResponse(method, callee.call(new Type[] {requestType}, new IValue[] { request }, null));  
                    }
                }
                catch (Throw rascalException) {
                    rex.getStdErr().println(rascalException.getMessage());
                    return newFixedLengthResponse(Status.INTERNAL_ERROR, MIME_PLAINTEXT, rascalException.getMessage());
                }
                catch (Throwable unexpected) {
                    rex.getStdErr().println(unexpected.getMessage());
                    unexpected.printStackTrace(rex.getStdErr());
                    return newFixedLengthResponse(Status.INTERNAL_ERROR, MIME_PLAINTEXT, unexpected.getMessage());
                }
            }

            private IConstructor makeRequest(String path, Method method, 
                                             Map<String, String> headers, 
                                             Map<String, String> parms, 
                                             Map<String, String> files) throws FactTypeUseException, IOException {

                KWRequest kws = webserver.kw_Request().parameters(makeMap(parms)).uploads(makeMap(files)).headers(makeMap(headers));

                switch (method) {
                    case HEAD:
                        return webserver.head(vf.string(path)); 
                    case DELETE:
                        return webserver.delete(vf.string(path));
                    case GET:
                        return webserver.get(vf.string(path), kws);
                    case PUT:
                        return webserver.put(vf.string(path), getContent(files, "content"), kws); 
                    case POST:
                        return webserver.put(vf.string(path), getContent(files, "postData"), kws);
                    default:
                        throw new IOException("Unhandled request " + method);
                }
            }

            protected IValue getContent(Map<String, String> parms, String contentParamName) throws IOException {
                class InlineCallableCompiledValue implements ICallableCompiledValue {

                    @Override
                    public IValue call(IRascalMonitor monitor, Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues) {
                        return call(argTypes, argValues, keyArgValues);
                    }

                    @Override
                    public IValue call(Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues) {
                        try {
                            TypeStore store = new TypeStore();
                            Type topType = new TypeReifier(vf).valueToType((IConstructor) argValues[0], store);

                            if (topType.isString()) {
                                return vf.string(parms.get(contentParamName));
                            }
                            else {
                                IValue dtf = keyArgValues.get("dateTimeFormat");
                                IValue ics = keyArgValues.get("implicitConstructors");
                                IValue icn = keyArgValues.get("implicitNodes");

                                return new JsonValueReader(vf, store)
                                    .setCalendarFormat((dtf != null) ? ((IString) dtf).getValue() : "yyyy-MM-dd\'T\'HH:mm:ss\'Z\'")
                                    .setConstructorsAsObjects((ics != null) ? ((IBool) ics).getValue() : true)
                                    .setNodesAsObjects((icn != null) ? ((IBool) icn).getValue() : true)
                                    .read(new JsonReader(new StringReader(parms.get(contentParamName))), topType);
                            }
                        } catch (IOException e) {
                            throw RuntimeExceptionFactory.io(vf.string(e.getMessage()), null, null);
                        }
                    }

                };
                return (IValue) new InlineCallableCompiledValue();
            }

            private Response translateResponse(Method method, IValue value) throws IOException {
                IConstructor cons = (IConstructor) value;
                initMethodAndStatusValues(rex);

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

            private Response translateJsonResponse(Method method, IConstructor cons) throws IOException {
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
                    e.printStackTrace(rex.getStdErr());
                    return newFixedLengthResponse(Status.NOT_FOUND, "text/plain", l + " not found.\n" + e);
                } 
            }

            private Response translateTextResponse(Method method, IConstructor cons) throws IOException {
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

            private Status translateStatus(IConstructor cons) throws IOException {
                initMethodAndStatusValues(rex);
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
            server.start();
            servers.put(url, server);
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
            throw RuntimeExceptionFactory.illegalArgument(server, null, null, "could not shutdown");
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

    private void initMethodAndStatusValues(final RascalExecutionContext rex) throws IOException {
        if (statusValues.isEmpty() || requestType == null) {
            webserver = Java2Rascal.Builder.bridge(vf, rex.getPathConfig(), IWebserver.class).build();

            statusValues.put(webserver.ok(), Status.OK);
            statusValues.put(webserver.created(), Status.CREATED);
            statusValues.put(webserver.accepted(), Status.ACCEPTED);
            statusValues.put(webserver.noContent(), Status.NO_CONTENT);
            statusValues.put(webserver.partialContent(), Status.PARTIAL_CONTENT);
            statusValues.put(webserver.redirect(), Status.REDIRECT);
            statusValues.put(webserver.notModified(), Status.NOT_MODIFIED);
            statusValues.put(webserver.badRequest(), Status.BAD_REQUEST);
            statusValues.put(webserver.unauthorized(), Status.UNAUTHORIZED);
            statusValues.put(webserver.forbidden(), Status.FORBIDDEN);
            statusValues.put(webserver.notFound(), Status.NOT_FOUND);
            statusValues.put(webserver.rangeNotSatisfiable(), Status.RANGE_NOT_SATISFIABLE);
            statusValues.put(webserver.internalError(), Status.INTERNAL_ERROR);
        }
    }
}
