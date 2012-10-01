/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
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
public class ClassResourceInputOutput implements IURIInputOutputResolver {
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
		while (prefix.endsWith("/") && prefix.length() > 1) {
			prefix = prefix.substring(0, prefix.length() - 2);
		}
		return prefix;
	}
	
	private String getPath(URI uri) {
		String path = uri.getPath();
		while (path.startsWith("/")) {
			path = path.substring(1);
		}
		if (path.contains("//")) {
			path = path.replaceAll("//","/");
		}
		return prefix + "/" + path;
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
			if(res == null)
				return false;
			return registry.isDirectory(res.toURI());
		} catch (URISyntaxException e) {
			return false;
		}
	}

	public boolean isFile(URI uri) {
		try {
			URL res = clazz.getResource(getPath(uri));
			if(res == null)
				return false;
			return registry.isFile(res.toURI());
		} catch (URISyntaxException e) {
			return false;
		}
	}

	public long lastModified(URI uri) throws IOException {
		try {
			URL res = clazz.getResource(getPath(uri));
			if(res == null)
				throw new FileNotFoundException(getPath(uri));
			return registry.lastModified(res.toURI());
		} catch (URISyntaxException e) {
			throw new IOException(e.getMessage(), e);
		}
	}

	public String[] listEntries(URI uri) throws IOException {
		try {
			URL res = clazz.getResource(getPath(uri));
			if(res == null)
				throw new FileNotFoundException(getPath(uri));
			return registry.listEntries(res.toURI());
		} catch (URISyntaxException e) {
			throw new IOException(e.getMessage(), e);
		}
	}
	
	public URI getResourceURI(URI uri) throws IOException {
		try {
			URL res = clazz.getResource(getPath(uri));
			if(res == null)
				throw new FileNotFoundException(getPath(uri));
			return res.toURI();
		} catch (URISyntaxException e) {
			throw new IOException(e.getMessage(), e);
		}
	}
	
	private String getParent(URI uri){
		String path = getPath(uri);
		int n = path.lastIndexOf("/");
		return (n  < 0) ? "/" : path.substring(0, n);
	}
	
	private String getChild(URI uri){
		String path = getPath(uri);
		int n = path.lastIndexOf("/");
		return (n  < 0) ? path : path.substring(n);
	}

	public OutputStream getOutputStream(URI uri, boolean append) throws IOException {
		try {
			String parent = getParent(uri);
			String child = getChild(uri);
			
			URL res = clazz.getResource(parent);
			if(res == null)
				throw new FileNotFoundException(parent);
			URI parentUri = res.toURI();
			String path = parentUri.getPath();
			if (path == null) {
				path = "/";
			}
			URI childUri = URIUtil.changePath(parentUri, path + child);
			
			return registry.getOutputStream(childUri, append);
		} catch (URISyntaxException e) {
			throw new IOException(e.getMessage(), e);
		}
	}

	public void mkDirectory(URI uri) throws IOException {
		String parent = getParent(uri);
		String child = getChild(uri);

		try {
			URL res = clazz.getResource(parent);
			if(res == null)
				throw new FileNotFoundException(parent);
			URI parentUri = res.toURI();
			URI childUri = URIUtil.changePath(parentUri, parentUri.getPath() + child);

			registry.mkDirectory(childUri);
		} catch (URISyntaxException e) {
			throw new IOException(e.getMessage(), e);
		} 
	}
	
	public boolean supportsHost() {
		return false;
	}
}
