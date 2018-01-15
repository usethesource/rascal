package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.help;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.BasicIDEServices;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.IFrameObserver;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.CommandExecutor;
import org.rascalmpl.library.experiments.tutor3.Feedback;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

public class HelpServer extends NanoHTTPD {

	private final ISourceLocation root;
	private final HelpManager helpManager;
	private CommandExecutor executor;
	IValueFactory vf = ValueFactoryFactory.getValueFactory();
	StringWriter outWriter;
    PrintWriter outPrintWriter;
    StringWriter errWriter;
    PrintWriter errPrintWriter;
    private final int port;
     
	public HelpServer(int port, HelpManager helpManager, ISourceLocation root) throws IOException {
		super(port);
		this.port = port;
		start();
		this.helpManager = helpManager;
		this.root = root;
	}
	
	
	public int getPort() {
        return port;
    }
	
	@Override
	public Response serve(String uri, Method method, Map<String, String> headers, Map<String, String> parms, Map<String, String> files) {
	  Response response;
	   //System.err.println("serve: " + uri);
	
	  if(uri.startsWith("/Search")){
	    try {
	      String[] words = ("help " + URLDecoder.decode(parms.get("searchFor"), "UTF-8")).split(" ");
	      return newFixedLengthResponse(Status.OK, "text/html", helpManager.giveHelp(words));
	    } catch (UnsupportedEncodingException e) {
	      return newFixedLengthResponse(Status.OK, "text/plain", Arrays.toString(e.getStackTrace()));
	    }
	  }
	  if(uri.startsWith("/ValidateCodeQuestion")){
	    try {
	      if(parms.get("listing") == null || parms.get("question") == null || parms.get("hole1") == null){
	        newFixedLengthResponse(Status.NOT_FOUND, "text/plain", "missing listing, question or hole1 parameter");
	      }
	      String listing = URLDecoder.decode(parms.get("listing"), "UTF-8");
	      String question = URLDecoder.decode(parms.get("question"), "UTF-8");

	      ArrayList<String> holes = new ArrayList<>();
	      for(int i = 1; parms.containsKey("hole" + i); i++){
	        holes.add(URLDecoder.decode(parms.get("hole" + i), "UTF-8"));
	      }
	      int k = 0;
	      while(listing.indexOf("_") >= 0 && k < holes.size()){
	        listing = listing.replaceFirst("_", holes.get(k++));
	      }
	      if(executor == null){
	        PathConfig pcfg = helpManager.getPathConfig();
	        outWriter = new StringWriter();
	        outPrintWriter = new PrintWriter(outWriter, true);
	        errWriter = new StringWriter();
            errPrintWriter = new PrintWriter(errWriter, true);
	        pcfg = pcfg.addSourceLoc(vf.sourceLocation("test-modules", "", ""));
	        executor = new CommandExecutor(pcfg, outPrintWriter, errPrintWriter, new BasicIDEServices(errPrintWriter), null, new IFrameObserver() {});
	      } else {
	        outWriter.getBuffer().setLength(0);
	        errWriter.getBuffer().setLength(0);
	      }
	      
	      writeModule(question, listing);
	      
	      try {
	        IConstructor tr = executor.executeTestsRaw(question);
	        System.err.println(tr);
	        outPrintWriter.flush();
	        errPrintWriter.flush();
	        return newFixedLengthResponse(Status.OK, "application/json", formatTestResults(tr));
	      } catch (ParseError e){
	        return newFixedLengthResponse(Status.OK, "application/json", "{ \"ok\": false, \"failed\": [], \"exceptions\": [], \"syntax\": " + makeLoc(e)
	                                                  + " }");
	      }

	    } catch (UnsupportedEncodingException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (NoSuchRascalFunction e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (URISyntaxException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
	  }
	  try {
	    ISourceLocation requestedItem = URIUtil.correctLocation(root.getScheme(), root.getAuthority(), root.getPath() + "/" + normalize(uri));
	    response = newChunkedResponse(Status.OK, getMimeType(uri), URIResolverRegistry.getInstance().getInputStream(requestedItem));
	    addHeaders(response, uri, headers);
	    return response;
	  } catch (IOException e) {			
	    return newFixedLengthResponse(Status.NOT_FOUND, "text/plain", uri + " not found.\n" + e);
	  }
	}
	
	String makeLoc(ParseError e){
	  return
	      "{" + "\"beginLine\": "   + e.getBeginLine() + ", "
	          + "\"beginColumn\": " + e.getBeginColumn()  + ", "
	          + "\"endLine\": "     + e.getEndLine()  + ", "
	          + "\"endColumn\": "   + e.getEndColumn()
	          + "}";
	}
	
	String makeLoc(ISourceLocation l){
	  return
	  "{" + "\"beginLine\": "   + l.getBeginLine() + ", "
	      + "\"beginColumn\": " + l.getBeginColumn()  + ", "
	      + "\"endLine\": "     + l.getEndLine()  + ", "
	      + "\"endColumn\": "   + l.getEndColumn()
	      + "}";
	}
	
	String makeResult(ISourceLocation l, IString msg){
	  return "{" + "\"src\": " + makeLoc(l) + ", "
	             + "\"msg\": " + msg + "}";
	}
	
	String formatTestResults(IConstructor tr){
	  IList results = (IList) tr.get("results");
	  IList exceptions = (IList) tr.get("exceptions");
	  boolean ok = true;
	  String failed = "[";

	  IInteger zero = vf.integer(0);
	  String sep = "";
	  for(IValue v : results){
	    ITuple tup = (ITuple) v;
	    if(tup.get(1).equals(zero)){
	      ok = false;
	      failed += sep + makeResult((ISourceLocation) tup.get(0), (IString) tup.get(2)) ;
	      sep = ", ";
	    }
	  }
	  failed += "]";

	  String sexceptions = "[";
	  sep = "";
	  for(IValue v : exceptions){
	    sexceptions += sep + ((IString) v).getValue();
	    sep = ", ";
	  }
	  sexceptions += "]";
	  
	  ok &= exceptions.length() == 0;
	  
	  return "{" + "\"ok\": " + ok + ", "
	             + "\"failed\": " + failed + "," 
	             + "\"exceptions\": " + sexceptions + ","
	             + "\"feedback\": " + Feedback.give(ok)
	             + "}";
	}
	
	private void writeModule(String question, String listing) throws IOException, URISyntaxException {
	  URIResolverRegistry reg = URIResolverRegistry.getInstance();
	  ISourceLocation sloc = vf.sourceLocation("test-modules", "", question + ".rsc");
	  OutputStream out = reg.getOutputStream(sloc, false);
	  listing = listing.replaceAll("\\\\n", "\n");
      out.write(listing.getBytes(), 0, listing.length());
      out.close();
      System.err.println("written to " + sloc + ":" + "\n" + listing);
  }

  String getExtension(String uri){
		int n = uri.lastIndexOf(".");
		if(n >= 0){
			return uri.substring(n + 1);
		}
		return "";
	}
	
	String getMimeType(String uri){
		switch(getExtension(uri)){
		case "css":		return "text/css";
		case "ico": 	return "image/x-icon";
		case "html": 	return "text/html";
		case "jpeg":	return "image/jpeg";
		case "png": 	return "image/png";
		case "txt":		return "text/plain";
		}
		return "text/html";
	}
	
	private String etag(String uri){
		String parts[] = uri.split("#");
		return String.valueOf(parts[0].hashCode());	
	}
	
	private String normalize(String uri){
		if(uri.startsWith("/")){
			uri = uri.substring(1, uri.length());
		}
		String[] parts = uri.split("#");
		if(parts.length >= 2){
			return parts[0] + "/index.html#" + parts[1];
		}
		if(!uri.contains(".")){
			return uri + "/index.html";
		}
		return uri;
	}

	private void addHeaders(Response response, String uri, Map<String, String> headers) {
		response.addHeader("Cache-Control", "max-age=8600, public");
		response.addHeader("ETag", etag(uri));

		for (String key : headers.keySet()) {
			response.addHeader(key, headers.get(key));
		}
	}
}
