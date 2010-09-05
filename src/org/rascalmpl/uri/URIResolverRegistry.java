package org.rascalmpl.uri;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class URIResolverRegistry {
	private final Map<String,IURIInputStreamResolver> inputResolvers;
	private final Map<String,IURIOutputStreamResolver> outputResolvers;
		
	public URIResolverRegistry() {
		this.inputResolvers = new HashMap<String, IURIInputStreamResolver>();
		this.outputResolvers = new HashMap<String, IURIOutputStreamResolver>();
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
	
	public boolean isDirectory(URI uri) {
		IURIInputStreamResolver resolver = inputResolvers.get(uri.getScheme());
		
		if (resolver == null) {
			return false;
		}
		return resolver.isDirectory(uri);
	}

	public boolean isFile(URI uri) {
		IURIInputStreamResolver resolver = inputResolvers.get(uri.getScheme());
		
		if (resolver == null) {
			return false;
		}
		return resolver.isFile(uri);
	}

	public long lastModified(URI uri) {
		IURIInputStreamResolver resolver = inputResolvers.get(uri.getScheme());
		
		if (resolver == null) {
			return 0L;
		}
		return resolver.lastModified(uri);
	}

	public String[] listEntries(URI uri) {
		IURIInputStreamResolver resolver = inputResolvers.get(uri.getScheme());
		String[] ls = {};
		
		if (resolver == null) {
			return ls;
		}
		return resolver.listEntries(uri);
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
