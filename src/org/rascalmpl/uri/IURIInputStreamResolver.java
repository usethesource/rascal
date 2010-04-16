package org.rascalmpl.uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public interface IURIInputStreamResolver {
	InputStream getInputStream(URI uri) throws IOException;  
	boolean exists(URI uri);
	String scheme();
}
