package org.rascalmpl.uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class ClassResourceInputStreamResolver implements
		IURIInputStreamResolver {
	protected final Class<?> clazz;
	protected final String scheme;

	public ClassResourceInputStreamResolver(String scheme, Class<?> clazz) {
		this.clazz = clazz;
		this.scheme = scheme;
	}
	
	public boolean exists(URI uri) {
		return clazz.getResource(uri.getPath()) != null;
	}

	public InputStream getInputStream(URI uri) throws IOException {
		
		InputStream resourceAsStream = clazz.getResourceAsStream(uri.getPath());
		if(resourceAsStream != null)
			return resourceAsStream;
		throw new FileNotFoundException(uri.toString());
	}

	public String scheme() {
		return scheme;
	}

	public boolean isDirectory(URI uri) {
		URL res = clazz.getResource(uri.getPath());
		return (res == null) ? false : new File(res.getPath()).isDirectory();
	}

	public boolean isFile(URI uri) {
		URL res = clazz.getResource(uri.getPath());
		return (res == null) ? false : new File(res.getPath()).isFile();
	}

	public long lastModified(URI uri) {
		URL res = clazz.getResource(uri.getPath());
		return (res == null) ? 0L : new File(res.getPath()).lastModified();
	}

	public String[] listEntries(URI uri) {
		String[] ls = {};
		URL res = clazz.getResource(uri.getPath());
		return (res == null) ? ls : new File(res.getPath()).list();
	}

	public String absolutePath(URI uri) {
		return clazz.getResource(uri.getPath()).getPath();
	}

}
