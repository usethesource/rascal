package org.rascalmpl.uri;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class URIResolverRegistry {
	private final static Map<String,IURIInputStreamResolver> inputResolvers = new HashMap<String, IURIInputStreamResolver>();
	private final static Map<String,IURIOutputStreamResolver> outputResolvers = new HashMap<String, IURIOutputStreamResolver>();
	
	private static class InstanceKeeper {
		public final static URIResolverRegistry sInstance = new URIResolverRegistry();
	} 
	
	private URIResolverRegistry() {
		super();
	}
	
	public static URIResolverRegistry getInstance() {
		return InstanceKeeper.sInstance;
	}
	
	public void registerInput(String scheme, IURIInputStreamResolver resolver) {
		inputResolvers.put(scheme, resolver);
	}
	
	public void registerOutput(String scheme, IURIOutputStreamResolver resolver) {
		outputResolvers.put(scheme, resolver);
	}
	
	public boolean exists(URI uri) {
		IURIInputStreamResolver resolver = inputResolvers.get(uri.getScheme());
		
		if (resolver == null) {
			return false;
		}
		
		return resolver.exists(uri);
	}
	
	public InputStream getInputStream(URI uri) throws IOException {
		IURIInputStreamResolver resolver = inputResolvers.get(uri.getScheme());
		
		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}
		
		return resolver.getInputStream(uri);
	}
	
	public OutputStream getOutputStream(URI uri, boolean append) throws IOException {
		IURIOutputStreamResolver resolver = outputResolvers.get(uri.getScheme());
		
		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}
		
		return resolver.getOutputStream(uri, append);
	}
}
