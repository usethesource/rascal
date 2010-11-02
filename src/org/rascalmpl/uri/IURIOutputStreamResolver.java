package org.rascalmpl.uri;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public interface IURIOutputStreamResolver {
	OutputStream getOutputStream(URI uri, boolean append) throws IOException;
	String scheme();
	boolean mkDirectory(URI uri) throws IOException;
	URI getResourceURI(URI uri) throws IOException;
}
