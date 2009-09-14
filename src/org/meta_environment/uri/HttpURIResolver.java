package org.meta_environment.uri;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class HttpURIResolver implements IURIInputStreamResolver {

	public InputStream getInputStream(URI uri) throws IOException {
		return new BufferedInputStream(uri.toURL().openStream());
	}

	public String scheme() {
		return "http";
	}

}
