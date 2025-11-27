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
package org.rascalmpl.uri.libraries;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

import org.rascalmpl.uri.ISourceLocationInput;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.classloaders.IClassloaderLocationResolver;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.ISourceLocation;

/**
 * This reusable class implements input methods for files that reside in Java resources of a certain class.
 * Extend it and bind the scheme, the clazz and the prefix by calling the
 * super() method in the sub-class' constructor with static values, e.g:
 * {@see StandardLibraryURIResolver}
 */
public abstract class ClassResourceInput implements ISourceLocationInput, IClassloaderLocationResolver {
	protected final Class<?> clazz;
	protected final String scheme;
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
	
	@Override
	public boolean exists(ISourceLocation uri) {
		return clazz.getResource(getPath(uri)) != null;
	}

	@Override
	public InputStream getInputStream(ISourceLocation uri) throws IOException {
		InputStream resourceAsStream = clazz.getResourceAsStream(getPath(uri));
		if (resourceAsStream != null) {
			return resourceAsStream;
		}
		throw new FileNotFoundException(uri.toString());
	}

	@Override
	public String scheme() {
		return scheme;
	}

	@Override
	public boolean isDirectory(ISourceLocation uri) {
	  try {
	    return URIResolverRegistry.getInstance().isDirectory(resolve(uri));
	  } catch (IOException e) {
	    return false;
	  }
	}

	@Override
	public boolean isFile(ISourceLocation uri) {
		try {
			return URIResolverRegistry.getInstance().isFile(resolve(uri));
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
			if (res.getProtocol().equals("jar") && res.getPath().startsWith("file:/")) {
			  return ValueFactoryFactory.getValueFactory().sourceLocation("jar", null, res.getPath().substring("file:".length()));
			}
			return ValueFactoryFactory.getValueFactory().sourceLocation(URIUtil.fromURL(res));
		} catch (URISyntaxException e) {
			assert false;
			throw new IOException(e);
		}
	}

	@Override
	public long lastModified(ISourceLocation uri) throws IOException {
		return URIResolverRegistry.getInstance().lastModified(resolve(uri));
	}

	@Override
	public String[] list(ISourceLocation uri) throws IOException {
		return URIResolverRegistry.getInstance().listEntries(resolve(uri)); 
	}
	
	@Override
	public boolean supportsHost() {
		return false;
	}

	@Override
	public Charset getCharset(ISourceLocation uri) throws IOException {
		return URIResolverRegistry.getInstance().getCharset(resolve(uri));
	}

	@Override
	public ClassLoader getClassLoader(ISourceLocation loc, ClassLoader parent) throws IOException {
		assert loc.getScheme().equals(scheme());
		return clazz.getClassLoader();
	}
}
