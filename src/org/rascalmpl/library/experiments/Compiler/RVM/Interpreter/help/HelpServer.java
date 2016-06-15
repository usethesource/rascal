package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.help;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

public class HelpServer extends NanoHTTPD {

	private Path root;
	private HelpManager helpManager;

	public HelpServer(HelpManager helpManager, Path root) throws IOException {
		super(8000);
		start();
		this.helpManager = helpManager;
		this.root = root;
	}

	@Override
	public Response serve(String uri, Method method, Map<String, String> headers, Map<String, String> parms, Map<String, String> files) {

		Response response;
		if(uri.startsWith("/Search")){
			String[] words = ("help " + parms.get("searchFor")).split(" ");
			if(words.length > 1){
				String results = helpManager.giveHelp(words);
				return new Response(Status.OK, "text/html", results);
			} else {
				return new Response(Status.NO_CONTENT, "text/html", "<h1>Empty Search</h1>");
			}
		}
		try {
			String mime =  uri.endsWith(".css") ? "text/css" : (uri.endsWith(".ico") ? "image/x-icon": "text/html");
			response = new Response(Status.OK, mime, Files.newInputStream(root.resolve(massage(uri))));
			addHeaders(response, uri, headers);
			
			return response;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Response(Status.NOT_FOUND, "text/plain", uri + " not found.\n" + e);
		}
	}
	
	private String etag(String uri){
		String parts[] = uri.split("#");
		return String.valueOf(parts[0].hashCode());	
	}
	
	private String massage(String uri){
		if(uri.startsWith("/")){
			uri = uri.substring(1, uri.length());
		}
		if(uri.contains("#")){
			String[] parts = uri.split("#");
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
