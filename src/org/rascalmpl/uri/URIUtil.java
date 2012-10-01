package org.rascalmpl.uri;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class URIUtil {
	public static URI create(String scheme, String authority, String path, String query, String fragment) throws URISyntaxException {
		return fixUnicode(new URI(scheme, authority, path, query, fragment));
	}
	public static URI create(String scheme, String authority, String path) throws URISyntaxException {
		return create(scheme, authority, path, null, null);
	}
	public static URI create(String scheme, String userInformation, String host, int port, String path, String query, String fragment) throws URISyntaxException {
		return fixUnicode(new URI(scheme, userInformation, host, port, path, query, fragment));
	}
	public static URI createFile(String path) throws URISyntaxException {
		return fixUnicode(new URI("file","", path, null));
	}
	
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
