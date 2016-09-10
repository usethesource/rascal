package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.help;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Map;

import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.value.ISourceLocation;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

public class HelpServer extends NanoHTTPD {

	private final ISourceLocation root;
	private final HelpManager helpManager;

	public HelpServer(int port, HelpManager helpManager, ISourceLocation root) throws IOException {
		super(port);
		start();
		this.helpManager = helpManager;
		this.root = root;
	}

	@Override
	public Response serve(String uri, Method method, Map<String, String> headers, Map<String, String> parms, Map<String, String> files) {
	  Response response;
	
	  if(uri.startsWith("/Search")){
	    try {
	      String[] words = ("help " + URLDecoder.decode(parms.get("searchFor"), "UTF-8")).split(" ");
	      return new Response(Status.OK, "text/html", helpManager.giveHelp(words));
	    } catch (UnsupportedEncodingException e) {
	      return new Response(Status.OK, "text/plain", e.getStackTrace().toString());
	    }
	  }
	  if(uri.startsWith("/Validate")){
	    try {
	      if(parms.get("listing") == null || parms.get("question") == null || parms.get("hole0") == null){
	        new Response(Status.NOT_FOUND, "text/plain", "missing listing, question or hole0 parameter");
	      }
	      String listing = URLDecoder.decode(parms.get("listing"), "UTF-8");
	      String question = URLDecoder.decode(parms.get("question"), "UTF-8");

	      ArrayList<String> holes = new ArrayList<>();
	      for(int i = 0; parms.containsKey("hole" + i); i++){
	        holes.add(URLDecoder.decode(parms.get("hole" + i), "UTF-8"));
	      }

	      return new Response(Status.OK, "text/html", listing + "\n" + question + "\n" + holes);

	    } catch (UnsupportedEncodingException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
	  }
	  try {
	    ISourceLocation requestedItem = URIUtil.correctLocation(root.getScheme(), root.getAuthority(), root.getPath() + "/" + normalize(uri));
	    response = new Response(Status.OK, getMimeType(uri), URIResolverRegistry.getInstance().getInputStream(requestedItem));
	    addHeaders(response, uri, headers);
	    return response;
	  } catch (IOException e) {			
	    return new Response(Status.NOT_FOUND, "text/plain", uri + " not found.\n" + e);
	  }
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
