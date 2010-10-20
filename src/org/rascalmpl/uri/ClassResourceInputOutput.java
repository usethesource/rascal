package org.rascalmpl.uri;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * This class implements both input and output methods for files that reside in Java resources of a certain class.
 * Depending on where these resources are, i.e. on disk, or in a jar, (which depends on the classloader of the class)
 * some functionality may or may not work. Typically, the user will eventually get a "SchemeNotSupportedException" 
 * if an operation is not provided. 
 */
public class ClassResourceInputOutput implements
		IURIInputStreamResolver, IURIOutputStreamResolver {
	protected final Class<?> clazz;
	protected final String scheme;
	protected final URIResolverRegistry registry;
	protected final String prefix;

	public ClassResourceInputOutput(URIResolverRegistry registry, String scheme, Class<?> clazz, String prefix) {
		this.registry = registry;
		this.clazz = clazz;
		this.scheme = scheme;
		this.prefix = normalizePrefix(prefix);
	}

	private String normalizePrefix(String prefix) {
		if (!prefix.startsWith("/")) {
			prefix = "/" + prefix;
		}
		if (prefix.endsWith("/")) {
			prefix = prefix.substring(0, prefix.length() - 1);
		}
		return prefix;
	}
	
	private String getPath(URI uri) {
		String path = uri.getPath();
		return prefix + (path.startsWith("/") ? "" : "/") + path;
	}
	
	public boolean exists(URI uri) {
		return clazz.getResource(getPath(uri)) != null;
	}

	public InputStream getInputStream(URI uri) throws IOException {
		InputStream resourceAsStream = clazz.getResourceAsStream(getPath(uri));
		if (resourceAsStream != null) {
			return resourceAsStream;
		}
		throw new FileNotFoundException(uri.toString());
	}

	public String scheme() {
		return scheme;
	}

	public boolean isDirectory(URI uri) {
		try {
			URL res = clazz.getResource(getPath(uri));
			return registry.isDirectory(res.toURI());
		} catch (URISyntaxException e) {
			return false;
		}
	}

	public boolean isFile(URI uri) {
		try {
			URL res = clazz.getResource(getPath(uri));
			return registry.isFile(res.toURI());
		} catch (URISyntaxException e) {
			return false;
		}
	}

	public long lastModified(URI uri) throws IOException {
		try {
			URL res = clazz.getResource(getPath(uri));
			return registry.lastModified(res.toURI());
		} catch (URISyntaxException e) {
			throw new IOException(e.getMessage(), e);
		}
	}

	public String[] listEntries(URI uri) throws IOException {
		try {
			URL res = clazz.getResource(getPath(uri));
			return registry.listEntries(res.toURI());
		} catch (URISyntaxException e) {
			throw new IOException(e.getMessage(), e);
		}
	}

	public String absolutePath(URI uri) throws IOException {
		try {
			URL res = clazz.getResource(getPath(uri));
			return registry.absolutePath(res.toURI());
		} catch (URISyntaxException e) {
			throw new IOException(e.getMessage(), e);
		}
	}

	public OutputStream getOutputStream(URI uri, boolean append) throws IOException {
		try {
			URL res = clazz.getResource(getPath(uri));
			return registry.getOutputStream(res.toURI(), append);
		} catch (URISyntaxException e) {
			throw new IOException(e.getMessage(), e);
		}
	}

	public boolean mkDirectory(URI uri) throws IOException {
		try {
			URL res = clazz.getResource(getPath(uri));
			return registry.mkDirectory(res.toURI());
		} catch (URISyntaxException e) {
			throw new IOException(e.getMessage(), e);
		}
	}
}
