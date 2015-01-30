/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.values.ValueFactoryFactory;

/**
 * This class implements both input and output methods for files that reside in Java resources of a certain class.
 * Depending on where these resources are, i.e. on disk, or in a jar, (which depends on the classloader of the class)
 * some functionality may or may not work. Typically, the user will eventually get a "SchemeNotSupportedException" 
 * if an operation is not provided. 
 */
public class ClassResourceInput implements ISourceLocationInput {
	protected final Class<?> clazz;
	protected final String scheme;
	protected final URIResolverRegistry registry = URIResolverRegistry.getInstance();
	protected final String prefix;

	public ClassResourceInput(String scheme, Class<?> clazz, String prefix) {
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
	
	protected String getPath(ISourceLocation uri) {
		String path = uri.getPath();
		while (path.startsWith("/")) {
			path = path.substring(1);
		}
		if (path.contains("//")) {
			path = path.replaceAll("//","/");
		}
		return prefix + (prefix.endsWith("/") ? "" : "/") + path;
	}
	
	public boolean exists(ISourceLocation uri) {
		return clazz.getResource(getPath(uri)) != null;
	}

	public InputStream getInputStream(ISourceLocation uri) throws IOException {
		InputStream resourceAsStream = clazz.getResourceAsStream(getPath(uri));
		if (resourceAsStream != null) {
			return resourceAsStream;
		}
		throw new FileNotFoundException(uri.toString());
	}

	public String scheme() {
		return scheme;
	}

	public boolean isDirectory(ISourceLocation uri) {
		try {
			URL res = clazz.getResource(getPath(uri));
			if(res == null)
				return false;
			return registry.isDirectory(ValueFactoryFactory.getValueFactory().sourceLocation(res.toURI()));
		} catch (URISyntaxException e) {
			return false;
		}
	}

	public boolean isFile(ISourceLocation uri) {
		try {
			return registry.isFile(resolve(uri));
		} catch (IOException e) {
			return false;
		}
	}

	protected ISourceLocation resolve(ISourceLocation uri) throws IOException {
		try {
			URL res = clazz.getResource(getPath(uri));
			if(res == null) {
				throw new FileNotFoundException(getPath(uri));
			}
			return ValueFactoryFactory.getValueFactory().sourceLocation(res.toURI());
		} catch (URISyntaxException e) {
			assert false;
			throw new IOException(e);
		}
	}
	
	public long lastModified(ISourceLocation uri) throws IOException {
		return registry.lastModified(resolve(uri));
	}

	@Override
	public String[] list(ISourceLocation uri) throws IOException {
		return registry.listEntries(resolve(uri)); 
	}
	
	public boolean supportsHost() {
		return false;
	}

	@Override
	public Charset getCharset(ISourceLocation uri) throws IOException {
		return registry.getCharset(resolve(uri));
	}
}
