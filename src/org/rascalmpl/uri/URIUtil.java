/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;

public class URIUtil {
	public static final String URI_PATH_SEPARATOR = "/";
	private static final IValueFactory vf = ValueFactoryFactory.getValueFactory();
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
		File file = new File(fixWindowsPath(path));
		try {
			file = file.getCanonicalFile();
		}
		catch (IOException e) {
			// swallow, let's keep the old file
		}
		return fixUnicode(file.toURI());
	}
	
	/**
	 * Some URLs exist which are not strictly in compliance with RFC 2396. Their toURI() method will then produce
	 * bad URI syntax leading to IO exceptions later. This method tries to circumvent the problem by decoding
	 * the content of the URL first back to basic UTF8 characters and then recoding the URI from scratch.
	 *  
	 * @param  url a possibly non-rfc-2396 compliant URL
	 * @return (hopefully) a correctly encoded URI
	 * @throws URISyntaxException when either the decoder of URLs or the encoder of URIs finds a bad input.
	 */
	public static URI fromURL(URL url) throws URISyntaxException {
	    try {
	        return create(
	            url.getProtocol(), 
	            decodeURLPart(url.getAuthority()),
	            decodeURLPart(url.getPath()),
	            url.getQuery() == null ? null : decodeURLPart(url.getQuery()),
	            url.getRef() == null ? null : decodeURLPart(url.getRef()));
	    }
        catch (UnsupportedEncodingException e) {
            throw new URISyntaxException(url.toString(), e.getMessage());
        }
	}
	
    private static String decodeURLPart(String part) throws UnsupportedEncodingException {
        return part == null || part.isEmpty() ? "" : URLDecoder.decode(part, StandardCharsets.UTF_8.name());
    }
	
	public static ISourceLocation createFileLocation(String path) throws URISyntaxException {
		return vf.sourceLocation(createFile(path));
	}
	
	private static String fixWindowsPath(String path) {
		if (!path.startsWith(URI_PATH_SEPARATOR)) {
			path = URI_PATH_SEPARATOR + path;
		}
		
		return path;
	}

	/**
	 * Create a rascal module URI, moduleName is assumed to be correct.
	 */
	@Deprecated
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
	
	/**
     * Non throwing variant of <a>create</a>, in case of scenarios where input can be trusted.
     */
    public static URI assumeCorrect(String scheme, String authority, String path, String query) {
        try {
            return create(scheme, authority, path, query, null);
        } catch (URISyntaxException e) {
            IllegalArgumentException y = new IllegalArgumentException();
            y.initCause(e);
            throw y;
        }
    }
	
	public static ISourceLocation correctLocation(String scheme, String authority, String path) {
		try {
			return createLocation(scheme, authority, path);
		} catch (URISyntaxException e) {
			IllegalArgumentException y = new IllegalArgumentException();
		    y.initCause(e);
		    throw y;
		}
	}
	
	private static ISourceLocation createLocation(String scheme, String authority,
			String path) throws URISyntaxException {
		return vf.sourceLocation(scheme, authority, path);
	}

	private static final URI invalidURI = URI.create("unknown:///");
	
	/**
	 * Returns an URI which cannot be read/write to.
	 * @return
	 */
	public static URI invalidURI() {
		return invalidURI;
	}
	
	private static final ISourceLocation invalidLocation = vf.sourceLocation(invalidURI);
	
	public static ISourceLocation invalidLocation() {
		return invalidLocation;
	}
	
	/**
	 * Create a URI with only a scheme part set
	 * @param scheme
	 * @return
	 */
	public static URI rootScheme(String scheme) {
		return URI.create(scheme + ":///");
	}
	
	public static ISourceLocation rootLocation(String scheme) {
		try {
			return vf.sourceLocation(scheme, "", URI_PATH_SEPARATOR);
		} catch (URISyntaxException e) {
			assert false;
			return null;
		} 
	}
	
	/**
	 * In case you want to use an external URI not created by this class, call this method to ensure RFC compliant unicode support.
	 * @throws URISyntaxException
	 */
	public static URI fixUnicode(URI uri) throws URISyntaxException {
		return new URI(uri.toASCIIString());
	}	
	
	private static String getCorrectAuthority(URI uri) {
		if (uri.getAuthority() == null) {
			return "";
		}
		return uri.getAuthority();
	}
	
	private static String getCorrectAuthority(ISourceLocation uri) {
		if (!uri.hasAuthority()) {
			return "";
		}
		return uri.getAuthority();
	}
	
	public static URI changeScheme(URI uri, String newScheme) throws URISyntaxException {
		return create(newScheme, getCorrectAuthority(uri), uri.getPath(), uri.getQuery(), uri.getFragment());
	}
	
	public static ISourceLocation changeScheme(ISourceLocation loc, String newScheme) throws URISyntaxException {
		ISourceLocation newLoc = vf.sourceLocation(newScheme, getCorrectAuthority(loc), loc.getPath(), loc.hasQuery() ? loc.getQuery() : null, loc.hasFragment() ? loc.getFragment() : null);
		
		if (loc.hasLineColumn()) {
			newLoc = vf.sourceLocation(newLoc, loc.getOffset(), loc.getLength(), loc.getBeginLine(), loc.getEndLine(), loc.getBeginColumn(), loc.getEndColumn());
		}
		else if (loc.hasOffsetLength()) {
			newLoc = vf.sourceLocation(newLoc, loc.getOffset(), loc.getLength());
		}
		
		return newLoc;
	}
	
	public static URI changeAuthority(URI uri, String newAuthority) throws URISyntaxException {
		return create(uri.getScheme(), newAuthority == null ? "" : newAuthority, uri.getPath(), uri.getQuery(), uri.getFragment());
	}
	
	public static URI changePath(URI uri, String newPath) throws URISyntaxException {
		return create(uri.getScheme(), getCorrectAuthority(uri), newPath, uri.getQuery(), uri.getFragment());
	}
	
	public static ISourceLocation changePath(ISourceLocation uri, String newPath) throws URISyntaxException {
		return  vf.sourceLocation(uri.getScheme(), getCorrectAuthority(uri), newPath, uri.getQuery(), uri.getFragment());
	}

	public static URI changeQuery(URI uri, String newQuery) throws URISyntaxException {
		return create(uri.getScheme(), getCorrectAuthority(uri), uri.getPath(), newQuery, uri.getFragment());
	}
	public static ISourceLocation changeQuery(ISourceLocation uri, String newQuery) throws URISyntaxException {
		return  vf.sourceLocation(uri.getScheme(), getCorrectAuthority(uri), uri.getPath(), newQuery, uri.getFragment());
	}
	public static URI changeFragment(URI uri, String newFragment) throws URISyntaxException {
		return create(uri.getScheme(), getCorrectAuthority(uri), uri.getPath(), uri.getQuery(), newFragment);
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
	
	public static ISourceLocation getParentLocation(ISourceLocation loc) {
	    String currentPath = loc.getPath();
	    assert currentPath.startsWith(URI_PATH_SEPARATOR);
	    if (currentPath.equals(URI_PATH_SEPARATOR)) {
	        return loc;
	    }
	    try {
	        int pathSep = currentPath.lastIndexOf('/');
            return changePath(loc, currentPath.substring(0, pathSep));
        }
        catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
	}
	
	public static ISourceLocation getChildLocation(ISourceLocation loc, String child) {
		String childPath = loc.getPath();
		if (childPath == null || childPath.isEmpty()) {
			childPath = URI_PATH_SEPARATOR;
		}
		else if (!childPath.endsWith(URI_PATH_SEPARATOR)) {
			childPath += URI_PATH_SEPARATOR;
		}
		
		if (!child.equals(URI_PATH_SEPARATOR)) {
		    childPath += child;
		}

		try {
			return vf.sourceLocation(loc.getScheme(), getCorrectAuthority(loc), childPath, loc.hasQuery() ? loc.getQuery() : null, loc.hasFragment() ? loc.getFragment() : null);
		} catch (URISyntaxException e) {
			assert false;
			return loc;
		}
	}
	
	public static String getURIName(URI uri) {
		File file = new File(uri.getPath());
		return file.getName();
	}

	public static String getLocationName(ISourceLocation uri) {
		File file = new File(uri.getPath());
		return file.getName();
	}

	public static boolean isParentOf(ISourceLocation parent, ISourceLocation child) {
		if (!parent.getScheme().equals(child.getScheme())) {
			return false;
		}

		if (!parent.getAuthority().equals(child.getAuthority())) {
			return false;
		}

		String parentPath = parent.getPath();
		if (!parentPath.endsWith(URI_PATH_SEPARATOR)) {
			parentPath += URI_PATH_SEPARATOR; 
		}

		String childPath = child.getPath();
		return childPath.length() > parentPath.length() && childPath.startsWith(parentPath);
	}

	public static ISourceLocation removeExtension(ISourceLocation file) {
		if (file.getPath().indexOf(".") != -1) {
			try {
				return changePath(file, file.getPath().substring(0, file.getPath().lastIndexOf(".")));
			}
			catch (URISyntaxException e) {
				// this can not happen
			}
		}

		return file;
	}

	public static ISourceLocation relativize(ISourceLocation outside, ISourceLocation inside) {
		// first we normalize trailing slashes, since "relativize" does not consider them meaningful
		// while ISourceLocation does.
		try {
			if (outside.getPath().endsWith(URI_PATH_SEPARATOR)) {
				outside = changePath(outside, outside.getPath().substring(0, outside.getPath().length() - 1));
			}
			if (inside.getPath().endsWith(URI_PATH_SEPARATOR)) {
				inside = changePath(inside, inside.getPath().substring(0, inside.getPath().length() - 1));
			}
		}
		catch (URISyntaxException e) {
			// can not happen by removing only a slash
		}

		if (outside.equals(inside)) {
			return URIUtil.correctLocation("relative", "", "/");
		}

		if (!isParentOf(outside, inside)) {
			return inside;
		}

		String[] outsidePath = outside.getPath().split(URI_PATH_SEPARATOR);
		String[] insidePath = inside.getPath().split(URI_PATH_SEPARATOR);

		// we already know that outsidePath is a parent of insidePath, so
		// all we need to do is skip over the prefix
		StringBuilder relativePath = new StringBuilder();
		for (int i = outsidePath.length; i < insidePath.length; i++) {
			relativePath.append(URI_PATH_SEPARATOR);
			relativePath.append(insidePath[i]);
		}
	
		return URIUtil.correctLocation("relative", "", relativePath.toString());
	}

	public static ISourceLocation removeAuthority(ISourceLocation loc) {
		try {
		 ISourceLocation res = vf.sourceLocation(loc.getScheme(),"", loc.getPath(), loc.hasQuery() ? loc.getQuery() : null, loc.hasFragment()? loc.getFragment() : null);
		 if (loc.hasLineColumn()) {
			 return vf.sourceLocation(res, loc.getOffset(), loc.getLength(), loc.getBeginLine(), loc.getEndLine(), loc.getBeginColumn(), loc.getEndColumn());
		 }
		 if (loc.hasOffsetLength()) {
			 return vf.sourceLocation(res, loc.getOffset(), loc.getLength(), loc.getBeginLine(), loc.getEndLine(), loc.getBeginColumn(), loc.getEndColumn());
		 }
		 
		 return res;
		} catch (UnsupportedOperationException | URISyntaxException e) {
			assert false; // can't happen
			return loc;
		} 
	}
	public static ISourceLocation removeOffset(ISourceLocation prev) {
		return prev.top();
	}
    public static ISourceLocation createFromURI(String value) throws URISyntaxException {
        return vf.sourceLocation(createFromEncoded(value));
    }

	/**
	 * Extracts the extension (the characters after the last . in the file name),
	 * if any, and otherwise returns the empty string.
	 * @param loc
	 * @return
	 */
	public static String getExtension(ISourceLocation loc) {
		String path = loc.getPath();
		boolean endsWithSlash = path.endsWith(URIUtil.URI_PATH_SEPARATOR);
		if (endsWithSlash) {
			path = path.substring(0, path.length() - 1);
		}

		if (path.length() > 1) {
			int slashIndex = path.lastIndexOf(URIUtil.URI_PATH_SEPARATOR);
			int index = path.substring(slashIndex).lastIndexOf('.');

			if (index != -1) {
				return path.substring(slashIndex + index + 1);
			}
		}

		return "";
	}

    public static ISourceLocation changeExtension(ISourceLocation location, String ext) throws URISyntaxException {
        String path = location.getPath();
		boolean endsWithSlash = path.endsWith(URIUtil.URI_PATH_SEPARATOR);
		if (endsWithSlash) {
			path = path.substring(0, path.length() - 1);
		}

		if (path.length() > 1) {
			int slashIndex = path.lastIndexOf(URIUtil.URI_PATH_SEPARATOR);
			int index = path.substring(slashIndex).lastIndexOf('.');

			if (index == -1 && !ext.isEmpty()) {
				path = path + (!ext.startsWith(".") ? "." : "") + ext;
			}
			else if (!ext.isEmpty()) {
				path = path.substring(0, slashIndex + index) + (!ext.startsWith(".") ? "." : "") + ext;
			}
			else if (index != -1) {
				path = path.substring(0, slashIndex + index);
			}

			if (endsWithSlash) {
				path = path + URIUtil.URI_PATH_SEPARATOR;
			}
		}

		return URIUtil.changePath(location, path);
    }
}
