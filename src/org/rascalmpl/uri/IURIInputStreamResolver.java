package org.rascalmpl.uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public interface IURIInputStreamResolver {
	InputStream getInputStream(URI uri) throws IOException;  
	boolean exists(URI uri);
	long lastModified(URI uri)  throws IOException; 
	boolean isDirectory(URI uri);  
	boolean isFile(URI uri) ;
    String[] listEntries(URI uri)  throws IOException;
	String scheme();
	String absolutePath(URI uri) throws IOException;
}
