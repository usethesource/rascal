package org.meta_environment.locations;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

public class FileURIResolver implements IURIInputStreamResolver, IURIOutputStreamResolver {
	public InputStream getInputStream(URI uri) throws IOException {
		return new FileInputStream(uri.getPath());
	}
	
	public OutputStream getOutputStream(URI uri) throws IOException {
		return new FileOutputStream(uri.getPath());
	}
	
	public String scheme() {
		return "file";
	}
}
