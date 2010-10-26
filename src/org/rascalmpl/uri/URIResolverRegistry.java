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
	
	public void registerInput(IURIInputStreamResolver resolver) {
		inputResolvers.put(resolver.scheme(), resolver);
	}
	
	public void registerOutput(IURIOutputStreamResolver resolver) {
		outputResolvers.put(resolver.scheme(), resolver);
	}
	
	public void registerInputOutput(IURIInputOutputResolver resolver) {
		registerInput(resolver);
		registerOutput(resolver);
	}
	
	public boolean exists(URI uri) {
		IURIInputStreamResolver resolver = inputResolvers.get(uri.getScheme());
		
		if (resolver == null) {
			return false;
		}
		
		return resolver.exists(uri);
	}
	
	public String absolutePath(URI uri) throws IOException {
		IURIOutputStreamResolver oresolver = outputResolvers.get(uri.getScheme());
		if (oresolver != null) {
			return oresolver.absolutePath(uri);
		}
		throw new UnsupportedSchemeException(uri.getScheme());
	}
	
	public boolean isDirectory(URI uri) {
		IURIInputStreamResolver resolver = inputResolvers.get(uri.getScheme());
		
		if (resolver == null) {
			return false;
		}
		return resolver.isDirectory(uri);
	}
	
	public boolean mkDirectory(URI uri) throws IOException {
		IURIOutputStreamResolver resolver = outputResolvers.get(uri.getScheme());
		
		if (resolver == null) {
			return false;
		}
		return resolver.mkDirectory(uri);
	}

	public boolean isFile(URI uri) {
		IURIInputStreamResolver resolver = inputResolvers.get(uri.getScheme());
		
		if (resolver == null) {
			return false;
		}
		return resolver.isFile(uri);
	}

	public long lastModified(URI uri) throws IOException {
		IURIInputStreamResolver resolver = inputResolvers.get(uri.getScheme());
		
		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}
		return resolver.lastModified(uri);
	}

	public String[] listEntries(URI uri) throws IOException {
		IURIInputStreamResolver resolver = inputResolvers.get(uri.getScheme());
		
		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
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
