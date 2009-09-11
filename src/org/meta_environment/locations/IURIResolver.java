package org.meta_environment.locations;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public interface IURIResolver {
	InputStream resolve(URI uri) throws IOException;  
}
