/*******************************************************************************
 * Copyright (c) 2009-2012 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Davy Landman  - Davy.Landman@cwi.nl
*******************************************************************************/
package org.rascalmpl.uri;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class URIUtil {
	/**
	 * Create a new URI, non-encoded input is assumed.
	 * @throws URISyntaxException
	 */
	public static URI create(String scheme, String authority, String path, String query, String fragment) throws URISyntaxException {
		return fixUnicode(new URI(scheme, authority, path, query, fragment));
	}
	/**
	 * Create a new URI, non-encoded input is assumed.
	 * This is a shorthand for common cases were the query and fragment part are empty.
	 * @throws URISyntaxException
	 */
	public static URI create(String scheme, String authority, String path) throws URISyntaxException {
		return create(scheme, authority, path, null, null);
	}
	/**
	 * Create a new URI, non-encoded input is assumed.
	 * This is a version in case of a scheme which has a server-based authority part.
	 * And thus allows to set user information, host, and port.
	 * @throws URISyntaxException
	 */
	public static URI create(String scheme, String userInformation, String host, int port, String path, String query, String fragment) throws URISyntaxException {
		return fixUnicode(new URI(scheme, userInformation, host, port, path, query, fragment));
	}
	/**
	 * Shorthand for creating a file:// URI, non-encoded input is assumed.
	 * @throws URISyntaxException
	 */
	public static URI createFile(String path) throws URISyntaxException {
		return fixUnicode(new URI("file","", path, null));
	}

	/**
	 * Create a rascal module URI, moduleName is assumed to be correct.
	 */
	public static URI createRascalModule(String moduleName) {
		return assumeCorrect("rascal", moduleName, "");
	}
	/**
	 * Create a URI from a string which already contains an fully encoded URI
	 */
	public static URI createFromEncoded(String value) throws URISyntaxException {
		return fixUnicode(new URI(value));
	}	
	
	/**
	 * The non throwing variant of <a>createFromEncoded</a> 
	 */
	public static URI assumeCorrect(String value) {
		try {
			return createFromEncoded(value);
		} catch (URISyntaxException e) {
			IllegalArgumentException y = new IllegalArgumentException();
		    y.initCause(e);
		    throw y;
		}
	}	
	
	/**
	 * Non throwing variant of <a>create</a>, in case of scenarios where input can be trusted.
	 */
	public static URI assumeCorrect(String scheme, String authority, String path) {
		try {
			return create(scheme, authority, path);
		} catch (URISyntaxException e) {
			IllegalArgumentException y = new IllegalArgumentException();
		    y.initCause(e);
		    throw y;
		}
	}
	
	private static final URI invalidURI = URI.create("file://-");
	/**
	 * Returns an URI which cannot be read/write to.
	 * @return
	 */
	public static URI invalidURI() {
		return invalidURI;
	}
	
	/**
	 * Create a URI with only a scheme part set
	 * @param scheme
	 * @return
	 */
	public static URI rootScheme(String scheme) {
		return URI.create(scheme + ":///");
	}
	
	/**
	 * In case you want to use an external URI not created by this class, call this method to ensure RFC compliant unicode support.
	 * @throws URISyntaxException
	 */
	public static URI fixUnicode(URI uri) throws URISyntaxException {
		return new URI(uri.toASCIIString());
	}
	
	public static URI changeScheme(URI uri, String newScheme) throws URISyntaxException {
		return create(newScheme, uri.getAuthority(), uri.getPath(), uri.getQuery(), uri.getFragment());
	}
	public static URI changeAuthority(URI uri, String newAuthority) throws URISyntaxException {
		return create(uri.getScheme(), newAuthority, uri.getPath(), uri.getQuery(), uri.getFragment());
	}
	public static URI changePath(URI uri, String newPath) throws URISyntaxException {
		return create(uri.getScheme(), uri.getAuthority(), newPath, uri.getQuery(), uri.getFragment());
	}
	public static URI changeQuery(URI uri, String newQuery) throws URISyntaxException {
		return create(uri.getScheme(), uri.getAuthority(), uri.getPath(), newQuery, uri.getFragment());
	}
	public static URI changeFragment(URI uri, String newFragment) throws URISyntaxException {
		return create(uri.getScheme(), uri.getAuthority(), uri.getPath(), uri.getQuery(), newFragment);
	}
	
	/* special server-authority URI constructors */
	public static URI changeUserInformation(URI uri, String newUserInformation) throws URISyntaxException {
		return create(uri.getScheme(), newUserInformation, uri.getHost(), uri.getPort(), uri.getPath(), uri.getQuery(), uri.getFragment());
	}
	public static URI changeHost(URI uri, String newHost) throws URISyntaxException {
		return create(uri.getScheme(), uri.getUserInfo(), newHost, uri.getPort(), uri.getPath(), uri.getQuery(), uri.getFragment());
	}
	public static URI changePort(URI uri, int newPort) throws URISyntaxException {
		return create(uri.getScheme(), uri.getUserInfo(), uri.getHost(), newPort, uri.getPath(), uri.getQuery(), uri.getFragment());
	}
	
	/**
	 * @return a parent uri or null if there is none
	 */
	public static URI getParentURI(URI uri) {
		File file = new File(uri.getPath());
		File parent = file.getParentFile();
		
		if (parent != null && !parent.getName().isEmpty()) {
			try {
				return changePath(uri, parent.getAbsolutePath());
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
			return changePath(uri, childFile.getAbsolutePath());
		} catch (URISyntaxException e) {
			// can not happen
		}
		
		return null; // there is no child?;
	}
	public static String getURIName(URI uri) {
		File file = new File(uri.getPath());
		return file.getName();
	}
}
