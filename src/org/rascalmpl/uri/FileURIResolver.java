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

	public boolean isDirectory(URI uri) {
		return new File(uri.getPath()).isDirectory();
	}

	public boolean isFile(URI uri) {
		return new File(uri.getPath()).isFile();
	}

	public long lastModified(URI uri) {
		return new File(uri.getPath()).lastModified();
	}

	public String[] listEntries(URI uri) {
		return new File(uri.getPath()).list();
	}

	public boolean mkDirectory(URI uri) {
		return new File(uri.getPath()).mkdir();
	}

	public String absolutePath(URI uri) {
		return uri.getPath();
	}
}
