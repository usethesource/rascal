package org.rascalmpl.library.util;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Method;
import fi.iki.elonen.NanoHTTPD.Response.Status;

public class Webserver {
  private final IValueFactory vf;
  private final Map<ISourceLocation, NanoHTTPD> servers;
  private final Map<Method,IConstructor> methodValues = new HashMap<>();
  private final Map<IConstructor,Status> statusValues = new HashMap<>();
  private final TypeFactory tf = TypeFactory.getInstance();
  private final Type stringMap = tf.mapType(tf.stringType(), tf.stringType());
  private final Type[] argTypes = new Type[] { tf.sourceLocationType(), null, stringMap, stringMap, stringMap };
  
  public Webserver(IValueFactory vf) {
    this.vf = vf;
    this.servers = new HashMap<>();
  }

  public void serve(ISourceLocation url, final IValue callback, final IEvaluatorContext ctx) {
        URI uri = url.getURI();
    int port = uri.getPort() != -1 ? uri.getPort() : 80;
    String host = uri.getHost() != null ? uri.getHost() : "localhost";
    final ICallableValue callee = (ICallableValue) callback; 
    
    NanoHTTPD server = new NanoHTTPD(host, port) {
      

      @Override
      public Response serve(String uri, Method method, Map<String, String> headers, Map<String, String> parms,
          Map<String, String> files) {
        IConstructor methodVal = makeMethod(method);
        IMap headersVal = makeMap(headers);
        IMap paramsVal= makeMap(parms);
        IMap filesVal= makeMap(files);
        ISourceLocation loc = vf.sourceLocation(URIUtil.assumeCorrect("request", "", uri));
        try {
          synchronized (callee.getEval()) {
            callee.getEval().__setInterrupt(false);
            Result<IValue> response = callee.call(argTypes, new IValue[] { loc, methodVal, headersVal, paramsVal, filesVal }, null);
            return translateResponse(method, response.getValue());  
          }
        }
        catch (Throw rascalException) {
          ctx.getStdErr().println(rascalException.getMessage());
          return new Response(Status.INTERNAL_ERROR, "text/plain", rascalException.getMessage());
        }
        catch (Throwable unexpected) {
          ctx.getStdErr().println(unexpected.getMessage());
          unexpected.printStackTrace(ctx.getStdErr());
          return new Response(Status.INTERNAL_ERROR, "text/plain", unexpected.getMessage());
        }
      }

      private Response translateResponse(Method method, IValue value) {
        IConstructor cons = (IConstructor) value;
        initMethodAndStatusValues(ctx);
        
        if (cons.getName().equals("fileResponse")) {
          return translateFileResponse(method, cons);
        }
        else {
          return translateTextResponse(method, cons);
        }
      }
      
      private Response translateFileResponse(Method method, IConstructor cons) {
        ISourceLocation l = (ISourceLocation) cons.get("file");
        IString mimeType = (IString) cons.get("mimeType");
        IMap header = (IMap) cons.get("header");
        URI uri = l.getURI();
        
        Response response;
        try {
          response = new Response(Status.OK, mimeType.getValue(),URIResolverRegistry.getInstance().getInputStream(uri));
          addHeaders(response, header);
          return response;
        } catch (IOException e) {
          e.printStackTrace(ctx.getStdErr());
          return new Response(Status.NOT_FOUND, "text/plain", l + " not found.\n" + e);
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
        Response response = new Response(status, mimeType.getValue(), data.getValue());
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
        initMethodAndStatusValues(ctx);
        return statusValues.get(cons);
      }

      private IMap makeMap(Map<String, String> headers) {
        IMapWriter writer = vf.mapWriter();
        for (Entry<String, String> entry : headers.entrySet()) {
          writer.put(vf.string(entry.getKey()), vf.string(entry.getValue()));
        }
        return writer.done();
      }

      private IConstructor makeMethod(Method method) {
        initMethodAndStatusValues(ctx);
        return methodValues.get(method);
      }
    };
    servers.put(url, server);
    
    try {
      server.start();
    } catch (IOException e) {
      throw RuntimeExceptionFactory.io(vf.string(e.getMessage()), null, null);
    }
  }
  
  public void shutdown(ISourceLocation server) {
    NanoHTTPD nano = servers.get(server);
    if (nano != null) {
      if (nano.isAlive()) {
        nano.stop();
        servers.remove(server);
      }
    }
    else {
      throw RuntimeExceptionFactory.illegalArgument(server, null, null, "could not shutdown");
    }
  }
  
  @Override
  protected void finalize() throws Throwable {
    for (NanoHTTPD server : servers.values()) {
      if (server != null) {
        server.stop();
      }
    }
  }

  private void initMethodAndStatusValues(final IEvaluatorContext ctx) {
    if (methodValues.isEmpty() || statusValues.isEmpty()) {
      Environment env = ctx.getHeap().getModule("util::Webserver");
      Type methodType = env.getAbstractDataType("Method");
      TypeFactory tf = TypeFactory.getInstance();
      methodValues.put(Method.DELETE, vf.constructor(env.getConstructor(methodType, "delete", tf.voidType())));
      methodValues.put(Method.GET, vf.constructor(env.getConstructor(methodType, "get", tf.voidType())));
      methodValues.put(Method.HEAD, vf.constructor(env.getConstructor(methodType, "head", tf.voidType())));
      methodValues.put(Method.POST, vf.constructor(env.getConstructor(methodType, "post", tf.voidType())));
      methodValues.put(Method.PUT, vf.constructor(env.getConstructor(methodType, "put", tf.voidType())));
      
      Type statusType = env.getAbstractDataType("Status");
                        
      statusValues.put(vf.constructor(env.getConstructor(statusType, "ok", tf.voidType())), Status.OK);
      statusValues.put(vf.constructor(env.getConstructor(statusType, "created", tf.voidType())), Status.CREATED);
      statusValues.put(vf.constructor(env.getConstructor(statusType, "accepted", tf.voidType())), Status.ACCEPTED);
      statusValues.put(vf.constructor(env.getConstructor(statusType, "noContent", tf.voidType())), Status.NO_CONTENT);
      statusValues.put(vf.constructor(env.getConstructor(statusType, "partialContent", tf.voidType())), Status.PARTIAL_CONTENT);
      statusValues.put(vf.constructor(env.getConstructor(statusType, "redirect", tf.voidType())), Status.REDIRECT);
      statusValues.put(vf.constructor(env.getConstructor(statusType, "notModified", tf.voidType())), Status.NOT_MODIFIED);
      statusValues.put(vf.constructor(env.getConstructor(statusType, "badRequest", tf.voidType())), Status.BAD_REQUEST);
      statusValues.put(vf.constructor(env.getConstructor(statusType, "unauthorized", tf.voidType())), Status.UNAUTHORIZED);
      statusValues.put(vf.constructor(env.getConstructor(statusType, "forbidden", tf.voidType())), Status.FORBIDDEN);
      statusValues.put(vf.constructor(env.getConstructor(statusType, "notFound", tf.voidType())), Status.NOT_FOUND);
      statusValues.put(vf.constructor(env.getConstructor(statusType, "rangeNotSatisfiable", tf.voidType())), Status.RANGE_NOT_SATISFIABLE);
      statusValues.put(vf.constructor(env.getConstructor(statusType, "internalError", tf.voidType())), Status.INTERNAL_ERROR);
      // yes, we acknowledge our sins
      argTypes[1] = methodType;
    }
  }
}
