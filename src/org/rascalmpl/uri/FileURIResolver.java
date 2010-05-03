package org.rascalmpl.uri;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

public class FileURIResolver implements IURIInputStreamResolver, IURIOutputStreamResolver {
	
	public FileURIResolver(){
		super();
	}
	
	public InputStream getInputStream(URI uri) throws IOException {
		String path = uri.getPath();
		if (path != null) {
			return new FileInputStream(path);
		}
		throw new IOException("uri has no path: " + uri);
	}
	
	public OutputStream getOutputStream(URI uri, boolean append) throws IOException {
		String path = uri.getPath();
		if (path != null) {
			return new BufferedOutputStream(new FileOutputStream(uri.getPath(), append));
		}
		throw new IOException("uri has no path: " + uri);
	}
	
	public String scheme() {
		return "file";
	}

	public boolean exists(URI uri) {
		return new File(uri.getPath()).exists();
	}
}
