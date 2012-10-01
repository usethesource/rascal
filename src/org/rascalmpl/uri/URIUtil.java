package org.rascalmpl.uri;

import java.net.URI;
import java.net.URISyntaxException;

public class URIUtil {
	public static URI create(String scheme, String authority, String path, String query, String fragment) throws URISyntaxException {
		return fixUnicode(new URI(scheme, authority, path, query, fragment));
	}
	
	public static URI create(String scheme, String userInformation, String host, int port, String path, String query, String fragment) throws URISyntaxException {
		return fixUnicode(new URI(scheme, userInformation, host, port, path, query, fragment));
	}
	private static URI fixUnicode(URI uri) throws URISyntaxException {
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
}
