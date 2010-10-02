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

	public boolean isDirectory(URI uri) {
		return false;
	}

	public boolean isFile(URI uri) {
		return false;
	}

	public long lastModified(URI uri) {
		return 0L;
	}

	public String[] listEntries(URI uri) {
		String [] ls = {};
		return ls;
	}

	public String absolutePath(URI uri) {
		return "stdin";
	}
}
