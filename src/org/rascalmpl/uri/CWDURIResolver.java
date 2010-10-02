package org.rascalmpl.uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * For reading and writing files relative to the current working directory.
 */
public class CWDURIResolver implements IURIInputStreamResolver,
		IURIOutputStreamResolver {

	public InputStream getInputStream(URI uri) throws IOException {
		return new FileInputStream(getAbsolutePath(uri));
	}

	public String absolutePath(URI uri) {
		return System.getProperty("user.dir") + uri.getPath();
	}
	
	private File getAbsolutePath(URI uri) {
		return new File(absolutePath(uri));
	}

	public String scheme() {
		return "cwd";
	}

	public OutputStream getOutputStream(URI uri, boolean append) throws IOException {
		return new FileOutputStream(getAbsolutePath(uri), append);
	}

	public boolean exists(URI uri) {
		return getAbsolutePath(uri).exists();
	}

	public boolean isDirectory(URI uri) {
		return getAbsolutePath(uri).isDirectory();
	}

	public boolean isFile(URI uri) {
		return getAbsolutePath(uri).isFile();
	}

	public long lastModified(URI uri) {
		return getAbsolutePath(uri).lastModified();
	}

	public String[] listEntries(URI uri) {
		return getAbsolutePath(uri).list();
	}

	public boolean mkDirectory(URI uri) {
		return getAbsolutePath(uri).mkdir();
	}

}
