package org.rascalmpl.library.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.rascalmpl.ast.KeywordFormal;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.lang.json.io.JsonValueReader;
import org.rascalmpl.library.lang.json.io.JsonValueWriter;
import org.rascalmpl.uri.URIResolverRegistry;
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

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

public class Webserver {
  private final IValueFactory vf;
  private final Map<ISourceLocation, NanoHTTPD> servers;
  private final Map<IConstructor,Status> statusValues = new HashMap<>();
  private Type requestType;
  private Type post;
  private Type get;
  private Type head;
  private Type delete;
  private Type put;
  private Type functionType;
  
  
  public Webserver(IValueFactory vf) {
    this.vf = vf;
    this.servers = new HashMap<>();
  }

  public void serve(ISourceLocation url, final IValue callback, final IEvaluatorContext ctx) {
    URI uri = url.getURI();
    initMethodAndStatusValues(ctx);

    int port = uri.getPort() != -1 ? uri.getPort() : 80;
    String host = uri.getHost() != null ? uri.getHost() : "localhost";
    host = host.equals("localhost") ? "127.0.0.1" : host; // NanoHttp tries to resolve localhost, which isn't what we want!
    final ICallableValue callee = (ICallableValue) callback; 
    
    NanoHTTPD server = new NanoHTTPD(host, port) {
      
      @Override
      public Response serve(String uri, Method method, Map<String, String> headers, Map<String, String> parms,
          Map<String, String> files) {
        try {
          IConstructor request = makeRequest(uri, method, headers, parms, files);
          
          synchronized (callee.getEval()) {
            callee.getEval().__setInterrupt(false);
            return translateResponse(method, callee.call(new Type[] {requestType}, new IValue[] { request }, null).getValue());  
          }
        }
        catch (Throw rascalException) {
          ctx.getStdErr().println(rascalException.getMessage());
          return newFixedLengthResponse(Status.INTERNAL_ERROR, MIME_PLAINTEXT, rascalException.getMessage());
        }
        catch (Throwable unexpected) {
          ctx.getStdErr().println(unexpected.getMessage());
          unexpected.printStackTrace(ctx.getStdErr());
          return newFixedLengthResponse(Status.INTERNAL_ERROR, MIME_PLAINTEXT, unexpected.getMessage());
        }
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
            return vf.constructor(put, new IValue[]{vf.string(path), getContent(files, "content")}, kws);
          case POST:
            return vf.constructor(post, new IValue[]{vf.string(path), getContent(files, "postData")}, kws);
          default:
              throw new IOException("Unhandled request " + method);
        }
      }

      // TODO: this is highly interpreter dependent and must be reconsidered for the compiler version
      protected IValue getContent(Map<String, String> parms, String contentParamName) throws IOException {
          return new AbstractFunction(ctx.getCurrentAST(), ctx.getEvaluator(), (FunctionType) functionType, Collections.<KeywordFormal>emptyList(), false, ctx.getCurrentEnvt()) {
            
            @Override
            public boolean isStatic() {
              return false;
            }
            
            @Override
            public ICallableValue cloneInto(Environment env) {
              // this can not happen because the function is not present in an environment
              return null;
            }
            
            @Override
            public boolean isDefault() {
              return false;
            }
            
            public org.rascalmpl.interpreter.result.Result<IValue> call(Type[] argTypes, IValue[] argValues, java.util.Map<String,IValue> keyArgValues) {
              try {
                TypeStore store = new TypeStore();
                Type topType = new TypeReifier(vf).valueToType((IConstructor) argValues[0], store);
                
                if (topType.isString()) {
                  return ResultFactory.makeResult(getTypeFactory().stringType(), vf.string(parms.get(contentParamName)), ctx);
                }
                else {
                  IValue dtf = keyArgValues.get("dateTimeFormat");
                  IValue ics = keyArgValues.get("implicitConstructors");
                  IValue icn = keyArgValues.get("implicitNodes");
                  
                  return ResultFactory.makeResult(getTypeFactory().valueType(), new JsonValueReader(vf, store)
                      .setCalendarFormat((dtf != null) ? ((IString) dtf).getValue() : "yyyy-MM-dd\'T\'HH:mm:ss\'Z\'")
                      .setConstructorsAsObjects((ics != null) ? ((IBool) ics).getValue() : true)
                      .setNodesAsObjects((icn != null) ? ((IBool) icn).getValue() : true)
                      .read(new JsonReader(new StringReader(parms.get(contentParamName))), topType), ctx);
                }
              } catch (IOException e) {
                throw RuntimeExceptionFactory.io(vf.string(e.getMessage()), getAst(), getEval().getStackTrace());
              }
            };
          };
      }

      private Response translateResponse(Method method, IValue value) throws IOException {
        IConstructor cons = (IConstructor) value;
        initMethodAndStatusValues(ctx);
        
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
          e.printStackTrace(ctx.getStdErr());
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

  private void initMethodAndStatusValues(final IEvaluatorContext ctx) {
    if (statusValues.isEmpty() || requestType == null) {
      Environment env = ctx.getHeap().getModule("util::Webserver");
      TypeFactory tf = TypeFactory.getInstance();
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
      
      requestType = env.getAbstractDataType("Request");
      
      RascalTypeFactory rtf = RascalTypeFactory.getInstance();
      functionType = rtf.functionType(tf.valueType(), tf.tupleType(rtf.reifiedType(tf.valueType())), tf.voidType());
          
      get = env.getConstructor(requestType, "get", tf.tupleType(tf.stringType()));
      put = env.getConstructor(requestType, "put",  tf.tupleType(tf.stringType(), functionType));
      post = env.getConstructor(requestType, "post",  tf.tupleType(tf.stringType(), functionType));
      delete = env.getConstructor(requestType, "delete",  tf.tupleType(tf.stringType()));
      head = env.getConstructor(requestType, "head",  tf.tupleType(tf.stringType()));
    }
  }
}
