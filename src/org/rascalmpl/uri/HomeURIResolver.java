package org.rascalmpl.uri;

import java.net.URI;

public class HomeURIResolver extends FileURIResolver {
	@Override
	public String scheme() {
		return "home";
	}
	
	@Override
	protected String getPath(URI uri) {
		String path = super.getPath(uri);
		return System.getProperty("user.home") + (path.startsWith("/") ? path : ("/" + path));
	}
}
