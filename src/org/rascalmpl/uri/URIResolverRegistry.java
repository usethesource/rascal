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
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.uri;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rascalmpl.unicode.UnicodeInputStreamReader;

public class URIResolverRegistry {
	private final Map<String,IURIInputStreamResolver> inputResolvers;
	private final Map<String,IURIOutputStreamResolver> outputResolvers;
		
	public URIResolverRegistry() {
		this.inputResolvers = new HashMap<String, IURIInputStreamResolver>();
		this.outputResolvers = new HashMap<String, IURIOutputStreamResolver>();
	}
	
	public void copyResolverRegistries(URIResolverRegistry source) {
		for (String key : source.inputResolvers.keySet()) {
			if (! inputResolvers.containsKey(key))
				inputResolvers.put(key, source.inputResolvers.get(key));
		}
		
		for (String key : source.outputResolvers.keySet()) {
			if (! outputResolvers.containsKey(key))
				outputResolvers.put(key, source.outputResolvers.get(key));
		}
	}
	
	public void registerInput(IURIInputStreamResolver resolver) {
		inputResolvers.put(resolver.scheme(), resolver);
	}
	
	public void registerOutput(IURIOutputStreamResolver resolver) {
		outputResolvers.put(resolver.scheme(), resolver);
	}
	
	private static final Pattern splitScheme = Pattern.compile("^([^\\+]*)\\+");
	
	private IURIInputStreamResolver getInputResolver(String scheme) {
		 IURIInputStreamResolver result = inputResolvers.get(scheme);
		 if (result == null) {
			 Matcher m = splitScheme.matcher(scheme);
			 if (m.find()) {
				 String subScheme = m.group(1);
				 return inputResolvers.get(subScheme);
			 }
		 }
		 return result;
	}
	private IURIOutputStreamResolver getOutputResolver(String scheme) {
		 IURIOutputStreamResolver result = outputResolvers.get(scheme);
		 if (result == null) {
			 Matcher m = splitScheme.matcher(scheme);
			 if (m.find()) {
				 String subScheme = m.group(1);
				 return outputResolvers.get(subScheme);
			 }
		 }
		 return result;
	}
	
	public boolean supportsInputScheme(String scheme) {
		return getInputResolver(scheme) != null;
	}
	
	public boolean supportsOutputScheme(String scheme) {
		return getOutputResolver(scheme) != null;
	}
	
	public void registerInputOutput(IURIInputOutputResolver resolver) {
		registerInput(resolver);
		registerOutput(resolver);
	}
	
	public boolean supportsHost(URI uri) {
		IURIInputStreamResolver resolver = getInputResolver(uri.getScheme());
		if (resolver == null) {
			IURIOutputStreamResolver resolverOther = getOutputResolver(uri.getScheme());
			if (resolverOther == null) {
				return false;
			}
			return resolverOther.supportsHost();
		}
		return resolver.supportsHost();
	}
	
	public boolean exists(URI uri) {
		IURIInputStreamResolver resolver = getInputResolver(uri.getScheme());
		
		if (resolver == null) {
			return false;
		}
		
		return resolver.exists(uri);
	}
	
	public boolean isDirectory(URI uri) {
		IURIInputStreamResolver resolver = getInputResolver(uri.getScheme());
		
		if (resolver == null) {
			return false;
		}
		return resolver.isDirectory(uri);
	}
	
	public void mkDirectory(URI uri) throws IOException {
		IURIOutputStreamResolver resolver = getOutputResolver(uri.getScheme());
		
		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}
		
		mkParentDir(uri);
		
		resolver.mkDirectory(uri);
	}
	
	public void remove(URI uri) throws IOException {
		IURIOutputStreamResolver out = getOutputResolver(uri.getScheme());
    
	  if (out == null) {
      throw new UnsupportedSchemeException(uri.getScheme());
    }
    
    try {
      if (isDirectory(uri)) { 
        for (String element : listEntries(uri)) {
          remove(URIUtil.changePath(uri, uri.getPath() + "/" + element));
        }
      }
      
      out.remove(uri);
    } 
    catch (URISyntaxException e) {
      throw new IOException("unexpected URI syntax error", e);
    }
	}

	public boolean isFile(URI uri) {
		IURIInputStreamResolver resolver = getInputResolver(uri.getScheme());
		
		if (resolver == null) {
			return false;
		}
		return resolver.isFile(uri);
	}

	public long lastModified(URI uri) throws IOException {
		IURIInputStreamResolver resolver = getInputResolver(uri.getScheme());
		
		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}
		return resolver.lastModified(uri);
	}

	public String[] listEntries(URI uri) throws IOException {
		IURIInputStreamResolver resolver = getInputResolver(uri.getScheme());
		
		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}
		return resolver.listEntries(uri);
	}
	
	
	public Reader getCharacterReader(URI uri) throws IOException {
		return getCharacterReader(uri, getCharset(uri));
	}
	
	public Reader getCharacterReader(URI uri, String encoding) throws IOException {
		return getCharacterReader(uri, Charset.forName(encoding));
	}
	
	public Reader getCharacterReader(URI uri, Charset encoding) throws IOException {
		return new UnicodeInputStreamReader(getInputStream(uri), encoding);
		
	}
	public InputStream getInputStream(URI uri) throws IOException {
		IURIInputStreamResolver resolver = getInputResolver(uri.getScheme());
		
		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}
		
		return resolver.getInputStream(uri);
	}
	
	public Charset getCharset(URI uri) throws IOException {
		IURIInputStreamResolver resolver = getInputResolver(uri.getScheme());
		
		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}
		
		return resolver.getCharset(uri);
	}
	
	public OutputStream getOutputStream(URI uri, boolean append) throws IOException {
		IURIOutputStreamResolver resolver = getOutputResolver(uri.getScheme());
		
		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}
		
		if (uri.getPath() != null && uri.getPath().startsWith("/..")) {
			throw new IllegalArgumentException("Can not navigate beyond the root of a URI: " + uri);
		}
		
		mkParentDir(uri);
		
		return resolver.getOutputStream(uri, append);
	}

	private void mkParentDir(URI uri) throws IOException {
		URI parentURI = URIUtil.getParentURI(uri);
		
		if (parentURI != null && !exists(parentURI)) {
			mkDirectory(parentURI);
		}
	}
}
