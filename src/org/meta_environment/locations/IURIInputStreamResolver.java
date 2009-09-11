package org.meta_environment.locations;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public interface IURIInputStreamResolver {
	InputStream getInputStream(URI uri) throws IOException;  
	String scheme();
}
