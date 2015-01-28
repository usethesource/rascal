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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.unicode.UnicodeInputStreamReader;
import org.rascalmpl.values.ValueFactoryFactory;

public class URIResolverRegistry {
	private static final IValueFactory vf = ValueFactoryFactory.getValueFactory();
	private final Map<String,ISourceLocationInput> inputResolvers = new HashMap<>();
	private final Map<String,ISourceLocationOutput> outputResolvers = new HashMap<>();
	private final Map<String, Map<String,ILogicalSourceLocationResolver>> logicalResolvers = new HashMap<>();
	private static class InstanceHolder {
		static URIResolverRegistry sInstance = new URIResolverRegistry();
	}
	
	private URIResolverRegistry() { }

	public static URIResolverRegistry getInstance() {
		return InstanceHolder.sInstance;
	}
	
	/**
	 * Translates a logical location (i.e. for a specific language
	 * scheme) to a physical location. For this mapping the registered
	 * {@link ILogicalSourceLocationResolver} collection is used. These
	 * are indexed first by scheme and then by authority. If the scheme
	 * is registered but the authority is not, then the same lookup is tried
	 * again without authority.
	 * 
	 * @param loc logical source location
	 * @return physical source location
	 * @throws IOException when there is no registered resolver for the logical scheme provided
	 */
	public ISourceLocation logicalToPhysical(ISourceLocation loc) throws IOException {
		ISourceLocation result = physicalLocation(loc);
		if (result == null) {
			throw new FileNotFoundException(loc.toString());
		}
		return result;
	}
	
	private ISourceLocation physicalLocation(ISourceLocation loc) {
		synchronized (logicalResolvers) {
			while (logicalResolvers.containsKey(loc.getScheme())) {
				Map<String, ILogicalSourceLocationResolver> map = logicalResolvers.get(loc.getScheme());
				String auth = loc.hasAuthority() ? loc.getAuthority() : "";
				ILogicalSourceLocationResolver resolver = map.get(auth);
				ILogicalSourceLocationResolver noAuth = map.get("");
				ISourceLocation prev = loc;
				boolean removedOffset = false;
				
				if (resolver != null) {
					loc = resolver.resolve(loc);
				}
				
				if (loc == null && prev.hasOffsetLength()) {
					loc = resolver.resolve(URIUtil.removeOffset(prev));
					removedOffset = true; 
				}
				
				if (noAuth != null && (loc == null || prev.equals(loc))) {
					loc = URIUtil.removeAuthority(prev);
				    loc = noAuth.resolve(loc);
				    
				    if (loc == null && prev.hasOffsetLength()) {
				    	loc = URIUtil.removeOffset(URIUtil.removeAuthority(prev));
				    	loc = noAuth.resolve(loc);
				    	removedOffset = true;
				    }
				}
				
				if (loc == null || prev.equals(loc)) {
					return null;
				}
				
				if (removedOffset || !loc.hasOffsetLength()) { // then copy the offset from the logical one
					if (prev.hasLineColumn()) {
						loc = vf.sourceLocation(loc, prev.getOffset(), prev.getLength(), prev.getBeginLine(), prev.getEndLine(), prev.getBeginColumn(), prev.getEndColumn());
					}
					else if (prev.hasOffsetLength()) {
						if (loc.hasOffsetLength()) {
							loc = vf.sourceLocation(loc, prev.getOffset() + loc.getOffset(), prev.getLength());
						}
						else {
							loc = vf.sourceLocation(loc, prev.getOffset(), prev.getLength());
						}
					}
				}
				else if (loc.hasLineColumn()) { // the logical location offsets relative to the physical offset, possibly including line numbers
					if (prev.hasLineColumn()) {
						loc = vf.sourceLocation(loc, loc.getOffset() + prev.getOffset(), loc.getLength(), loc.getBeginLine() + prev.getBeginLine() - 1, loc.getEndLine() + prev.getEndLine() - 1, loc.getBeginColumn(), loc.getEndColumn());
					}
					else if (prev.hasOffsetLength()) {
						loc = vf.sourceLocation(loc, loc.getOffset() + prev.getOffset(), loc.getLength());
					}
				}
				else if (loc.hasOffsetLength()) { // the logical location offsets relative to the physical one
					if (prev.hasOffsetLength()) {
						loc = vf.sourceLocation(loc, loc.getOffset() + prev.getOffset(), loc.getLength());
					}
				}
			}
		}
		
		return loc;
	}
	
	private ISourceLocation safeResolve(ISourceLocation loc) {
		ISourceLocation resolved = null;
		
		try {
			resolved = physicalLocation(loc);
		}
		catch (Throwable e) {
			// robustness
		}
		
		return resolved != null ? resolved : loc;
	}
	
	public void registerInput(ISourceLocationInput resolver) {
		synchronized (inputResolvers) {
			inputResolvers.put(resolver.scheme(), resolver);
		}
	}

	public void registerOutput(ISourceLocationOutput resolver) {
		synchronized (outputResolvers) {
			outputResolvers.put(resolver.scheme(), resolver);
		}
	}

	public void registerInputOutput(ISourceLocationInputOutput resolver) {
		registerInput(resolver);
		registerOutput(resolver);
	}

	public void registerLogical(ILogicalSourceLocationResolver resolver) {
		synchronized (logicalResolvers) {
			Map<String, ILogicalSourceLocationResolver> map = logicalResolvers.get(resolver.scheme());
			if (map == null) {
				map = new HashMap<>();
				logicalResolvers.put(resolver.scheme(), map);
			}
			map.put(resolver.authority(), resolver);
		}
	}

	public void unregisterLogical(String scheme, String auth) {
		synchronized (logicalResolvers) {
			Map<String, ILogicalSourceLocationResolver> map = logicalResolvers.get(scheme);
			if (map != null) {
				map.remove(auth);
			}
		}
	}

	private static final Pattern splitScheme = Pattern.compile("^([^\\+]*)\\+");

	private ISourceLocationInput getInputResolver(String scheme) {
		synchronized (inputResolvers) {
			ISourceLocationInput result = inputResolvers.get(scheme);
			if (result == null) {
				Matcher m = splitScheme.matcher(scheme);
				if (m.find()) {
					String subScheme = m.group(1);
					return inputResolvers.get(subScheme);
				}
			}
			return result;
		}
	}

	private ISourceLocationOutput getOutputResolver(String scheme) {
		synchronized (outputResolvers) {
			ISourceLocationOutput result = outputResolvers.get(scheme);
			if (result == null) {
				Matcher m = splitScheme.matcher(scheme);
				if (m.find()) {
					String subScheme = m.group(1);
					return outputResolvers.get(subScheme);
				}
			}
			return result;
		}
	}

	public boolean supportsInputScheme(String scheme) {
		return getInputResolver(scheme) != null;
	}

	public boolean supportsOutputScheme(String scheme) {
		return getOutputResolver(scheme) != null;
	}

	public boolean supportsHost(ISourceLocation uri) {
		uri = safeResolve(uri);
		ISourceLocationInput resolver = getInputResolver(uri.getScheme());
		if (resolver == null) {
			ISourceLocationOutput resolverOther = getOutputResolver(uri.getScheme());
			if (resolverOther == null) {
				return false;
			}
			return resolverOther.supportsHost();
		}
		return resolver.supportsHost();
	}

	public boolean exists(ISourceLocation uri) {
		if (logicalResolvers.containsKey(uri.getScheme())) {
			uri = physicalLocation(uri);
			
			if (uri == null) {
				return false;
			}
		}
		
		ISourceLocationInput resolver = getInputResolver(uri.getScheme());

		if (resolver == null) {
			return false;
		}

		return resolver.exists(uri);
	}

	public boolean isDirectory(ISourceLocation uri) {
		uri = safeResolve(uri);
		ISourceLocationInput resolver = getInputResolver(uri.getScheme());

		if (resolver == null) {
			return false;
		}
		return resolver.isDirectory(uri);
	}

	public void mkDirectory(ISourceLocation uri) throws IOException {
		uri = safeResolve(uri);
		ISourceLocationOutput resolver = getOutputResolver(uri.getScheme());

		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}

		mkParentDir(uri);

		resolver.mkDirectory(uri);
	}

	public void remove(ISourceLocation uri) throws IOException {
		uri = safeResolve(uri);
		ISourceLocationOutput out = getOutputResolver(uri.getScheme());

		if (out == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}

		if (isDirectory(uri)) { 
			for (ISourceLocation element : list(uri)) {
				remove(element);
			} 
		}

		out.remove(uri);
	}

	public boolean isFile(ISourceLocation uri) {
		uri = safeResolve(uri);
		ISourceLocationInput resolver = getInputResolver(uri.getScheme());

		if (resolver == null) {
			return false;
		}
		return resolver.isFile(uri);
	}

	public long lastModified(ISourceLocation uri) throws IOException {
		uri = safeResolve(uri);
		ISourceLocationInput resolver = getInputResolver(uri.getScheme());

		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}
		return resolver.lastModified(uri);
	}

	public String[] listEntries(ISourceLocation uri) throws IOException {
		uri = safeResolve(uri);
		ISourceLocationInput resolver = getInputResolver(uri.getScheme());

		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}
		return resolver.list(uri);
	}
	
	public ISourceLocation[] list(ISourceLocation uri) throws IOException {
		String[] entries = listEntries(uri);
		ISourceLocation[] list = new ISourceLocation[entries.length];
		int i = 0;
		for (String entry : entries) {
			list[i++] = URIUtil.getChildLocation(uri, entry);
		}
		return list;
	}


	public Reader getCharacterReader(ISourceLocation uri) throws IOException {
		return getCharacterReader(uri, getCharset(uri));
	}

	public Reader getCharacterReader(ISourceLocation uri, String encoding) throws IOException {
		return getCharacterReader(uri, Charset.forName(encoding));
	}

	public Reader getCharacterReader(ISourceLocation uri, Charset encoding) throws IOException {
		uri = safeResolve(uri);
		Reader res = new UnicodeInputStreamReader(getInputStream(uri), encoding);
		
		if (uri.hasOffsetLength()) {
			return new UnicodeOffsetLengthReader(res, uri.getOffset(), uri.getLength());
		}
		else {
			return res;
		}
	}
	
	public InputStream getInputStream(ISourceLocation uri) throws IOException {
		uri = safeResolve(uri);
		ISourceLocationInput resolver = getInputResolver(uri.getScheme());

		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}

		return resolver.getInputStream(uri);
	}

	public Charset getCharset(ISourceLocation uri) throws IOException {
		uri = safeResolve(uri);
		ISourceLocationInput resolver = getInputResolver(uri.getScheme());

		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}

		return resolver.getCharset(uri);
	}

	public OutputStream getOutputStream(ISourceLocation uri, boolean append) throws IOException {
		uri = safeResolve(uri);
		ISourceLocationOutput resolver = getOutputResolver(uri.getScheme());

		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}

		if (uri.getPath() != null && uri.getPath().startsWith("/..")) {
			throw new IllegalArgumentException("Can not navigate beyond the root of a URI: " + uri);
		}

		mkParentDir(uri);

		return resolver.getOutputStream(uri, append);
	}

	private void mkParentDir(ISourceLocation uri) throws IOException {
		uri = safeResolve(uri);
		ISourceLocation parentURI = URIUtil.getParentLocation(uri);

		if (parentURI != null && !exists(parentURI)) {
			mkDirectory(parentURI);
		}
	}
}
