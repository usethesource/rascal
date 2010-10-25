package org.rascalmpl.uri;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class StandardOutputURIResolver implements IURIOutputStreamResolver {

	public OutputStream getOutputStream(URI uri, boolean append)
			throws IOException {
		return System.out;
	}

	public String scheme() {
		return "stdout";
	}

	public boolean mkDirectory(URI uri) {
		return false;
	}

	public String absolutePath(URI uri) {
		return "stdout";
	}

}
