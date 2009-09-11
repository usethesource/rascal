package org.meta_environment.locations;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class URIResolverRegistry {
	private static Map<String,IURIInputStreamResolver> resolvers = new HashMap<String, IURIInputStreamResolver>();
	private static Map<String,IURIOutputStreamResolver> outputResolvers = new HashMap<String, IURIOutputStreamResolver>();
	
	private static class InstanceKeeper {
		public final static URIResolverRegistry sInstance = new URIResolverRegistry();
	}
	
	private URIResolverRegistry() { }
	
	public static URIResolverRegistry getInstance() {
		return InstanceKeeper.sInstance;
	}
	
	public void registerInput(String scheme, IURIInputStreamResolver resolver) {
		resolvers.put(scheme, resolver);
	}
	
	public void registerOutput(String scheme, IURIOutputStreamResolver resolver) {
		outputResolvers.put(scheme, resolver);
	}
	
	public InputStream getInputStream(URI uri) throws IOException {
		IURIInputStreamResolver resolver = resolvers.get(uri.getScheme());
		
		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}
		
		return resolver.getInputStream(uri);
	}
	
	public OutputStream getOutputStream(URI uri) throws IOException {
		IURIOutputStreamResolver resolver = outputResolvers.get(uri.getScheme());
		
		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}
		
		return resolver.getOutputStream(uri);
	}
}
