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
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.uri;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
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
	
	public URI getResourceURI(URI uri) throws IOException {
		IURIOutputStreamResolver oresolver = outputResolvers.get(uri.getScheme());
		if (oresolver != null) {
			return oresolver.getResourceURI(uri);
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
	
	public void mkDirectory(URI uri) throws IOException {
		IURIOutputStreamResolver resolver = outputResolvers.get(uri.getScheme());
		
		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}
		
		mkParentDir(uri);
		
		resolver.mkDirectory(uri);
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
		
		if (uri.getPath() != null && uri.getPath().startsWith("/..")) {
			throw new IllegalArgumentException("Can not navigate beyond the root of a URI: " + uri);
		}
		
		mkParentDir(uri);
		
		return resolver.getOutputStream(uri, append);
	}

	/**
	 * @return a parent uri or null if there is none
	 */
	public static URI getParentURI(URI uri) {
		File file = new File(uri.getPath());
		File parent = file.getParentFile();
		
		if (parent != null && !parent.getName().isEmpty()) {
			try {
				return new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), parent.getAbsolutePath(), uri.getQuery(), uri.getFragment());
			} catch (URISyntaxException e) {
				// can not happen
			}
		}
		
		return null; // there is no parent;
	}
	
	public static URI getChildURI(URI uri, String child) {
		File file = new File(uri.getPath());
		File childFile = new File(file, child);
		
		try {
			return new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), childFile.getAbsolutePath(), uri.getQuery(), uri.getFragment());
		} catch (URISyntaxException e) {
			// can not happen
		}
		
		return null; // there is no child?;
	}
	
	public static String getURIName(URI uri) {
		File file = new File(uri.getPath());
		return file.getName();
	}
	
	private void mkParentDir(URI uri) throws IOException {
		URI parentURI = getParentURI(uri);
		
		if (parentURI != null && !exists(parentURI)) {
			mkDirectory(parentURI);
		}
	}
}
