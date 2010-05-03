package org.rascalmpl.uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class StandardInputURIResolver implements IURIInputStreamResolver {
	public boolean exists(URI uri) {
		return true;
	}

	public InputStream getInputStream(URI uri) throws IOException {
		return System.in;
	}

	public String scheme() {
		return "stdin";
	}
}
