package org.meta_environment.locations;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public interface IURIOutputStreamResolver {
	OutputStream getOutputStream(URI uri) throws IOException;
	String scheme();
}
